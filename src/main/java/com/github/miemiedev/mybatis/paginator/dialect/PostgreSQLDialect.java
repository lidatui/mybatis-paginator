package com.github.miemiedev.mybatis.paginator.dialect;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author badqiu
 */
public class PostgreSQLDialect extends Dialect{

    public PostgreSQLDialect(MappedStatement mappedStatement, Object parameterObject, PageBounds pageBounds) {
        super(mappedStatement, parameterObject, pageBounds);
    }
	
	public String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {
		return new StringBuffer( sql.length()+20 )
		.append(sql)
		.append(offset > 0 ? " limit "+String.valueOf(limit)+" offset "+String.valueOf(offset) : " limit "+String.valueOf(limit))
		.toString();
	}
}
