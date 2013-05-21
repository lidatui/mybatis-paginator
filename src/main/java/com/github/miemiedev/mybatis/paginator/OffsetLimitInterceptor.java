package com.github.miemiedev.mybatis.paginator;


import com.github.miemiedev.mybatis.paginator.dialect.Dialect;
import com.github.miemiedev.mybatis.paginator.support.PropertiesHelper;
import com.github.miemiedev.mybatis.paginator.support.SQLHelp;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


/**
 * 为ibatis3提供基于方言(Dialect)的分页查询的插件
 * 
 * 将拦截Executor.query()方法实现分页方言的插入.
 * @author badqiu
 * @author miemiedev
 *
 */

@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffsetLimitInterceptor implements Interceptor{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;
	static int ROWBOUNDS_INDEX = 2;
	static int RESULT_HANDLER_INDEX = 3;
	
	Dialect dialect;
	
	public Object intercept(Invocation invocation) throws Throwable {
        Paginator paginator = processIntercept(invocation.getArgs());
        if(paginator != null){
            List list = (List) invocation.proceed();
            return new PageList(list,paginator);
        }
		return invocation.proceed();
	}

    Paginator processIntercept(final Object[] queryArgs) throws SQLException {
		//queryArgs = query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)
		MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		final RowBounds rowBounds = (RowBounds)queryArgs[ROWBOUNDS_INDEX];

        PageQuery pageQuery = new PageQuery(rowBounds);

		int offset = pageQuery.getOffset();
		int limit = pageQuery.getLimit();
        int page = pageQuery.getPage();

        Paginator paginator = null;

        BoundSql boundSql = ms.getBoundSql(parameter);
        StringBuffer bufferSql = new StringBuffer(boundSql.getSql().trim());
        if(bufferSql.lastIndexOf(";") == bufferSql.length()-1){
            bufferSql.deleteCharAt(bufferSql.length()-1);
        }
        String sql = bufferSql.toString();

        sql = dialect.getSortString(sql, pageQuery.getSortInfoList());

        if(dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            if(pageQuery.isContainsTotalCount()){
                String count_sql = dialect.getCountString(sql);
                logger.debug("Total count SQL [{}] ", count_sql);
                int count = SQLHelp.getCount(sql, ms, parameter, boundSql);
                logger.debug("Total count: {}", count);
                paginator = new Paginator(page, limit, count);
            }

			if (dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql, offset, limit);
			} else {
				sql = dialect.getLimitString(sql, 0, limit);
			}
		}

        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        queryArgs[MAPPED_STATEMENT_INDEX] = newMs;

        return paginator;
	}

	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
			String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
		    String prop = mapping.getProperty();
		    if (boundSql.hasAdditionalParameter(prop)) {
		        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
		    }
		}
		return newBoundSql;
	}
	
	//see: MapperBuilderAssistant
	private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
		Builder builder = new Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
		
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if(ms.getKeyProperties() != null && ms.getKeyProperties().length !=0){
            StringBuffer keyProperties = new StringBuffer();
            for(String keyProperty : ms.getKeyProperties()){
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length()-1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}
		
		//setStatementTimeout()
		builder.timeout(ms.getTimeout());
		
		//setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());
		
		//setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
	    
		//setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());
		
		return builder.build();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		String dialectClass = new PropertiesHelper(properties).getRequiredString("dialectClass");
		try {
			dialect = (Dialect)Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot create dialect instance by dialectClass:"+dialectClass,e);
		} 
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
}
