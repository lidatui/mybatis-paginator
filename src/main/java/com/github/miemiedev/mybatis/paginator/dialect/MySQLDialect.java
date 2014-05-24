package com.github.miemiedev.mybatis.paginator.dialect;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author badqiu
 */
public class MySQLDialect extends Dialect{

    public MySQLDialect(MappedStatement mappedStatement, Object parameterObject, PageBounds pageBounds) {
        super(mappedStatement, parameterObject, pageBounds);
    }

    protected boolean supportsLimitOffset(){
		return true;
	}
	
    public boolean supportsLimit() {
        return true;   
    }  
    
	public String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {

        if (offset > 0) {
        	sql =  sql + " limit ?, ?";
            setPageParameter(offsetName, offset, Integer.class);
            setPageParameter(limitName, limit, Integer.class);
        } else {   
            sql = sql + " limit ?";
            setPageParameter(limitName, limit, Integer.class);
        }
        return sql;
	}   
  
}
