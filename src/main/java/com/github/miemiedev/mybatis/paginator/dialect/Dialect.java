package com.github.miemiedev.mybatis.paginator.dialect;

import com.github.miemiedev.mybatis.paginator.domain.Order;

import java.util.List;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 * @author miemiedev
 */
public class Dialect {
	
    public boolean supportsLimit(){
    	return false;
    }

    public boolean supportsLimitOffset() {
    	return supportsLimit();
    }
    
    /**
     * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符.</br>
     * 源代码为: getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit))
     */
    public String getLimitString(String sql, int offset, int limit) {
    	return getLimitString(sql,offset,Integer.toString(offset),limit,Integer.toString(limit));
    }
    
    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit,String limitPlaceholder) {
    	throw new UnsupportedOperationException("paged queries not supported");
    }

    /**
     * 将sql转换为总记录数SQL
     * @param sql SQL语句
     * @return 总记录数的sql
     */
    public String getCountString(String sql){
        return "select count(1) from (" + sql + ") tmp_count";
    }
    /**
     * 将sql转换为总记录数SQL 尽量避免嵌套查询，提升查询速度 
     * @param sql SQL语句
     * @return 总记录数的sql 
     */
     public String getCountString2(String sql){
    	int select_position=-1;
    	if(sql.startsWith("select")){
    		select_position=sql.indexOf("select");
    	}
    	else if(sql.startsWith("SELECT")){
    		select_position=sql.indexOf("SELECT");
    	}
    	
    	int from_position =0;
    	if(sql.contains("from")){
    		from_position=sql.indexOf("from");
    	}
    	else if(sql.contains("FROM")){
    		from_position=sql.indexOf("FROM");
    	}
    	
    	if(select_position>=0&&from_position>0)
    	{
    		return sql.substring(select_position,6)+" count(1) "+sql.substring(from_position);
    	}
    	else{
    		 return "select count(1) from (" + sql + ") tmp_count";
    	}
       
    }
    /**
     * 将sql转换为带排序的SQL
     * @param sql SQL语句
     * @return 总记录数的sql
     */
    public String getSortString(String sql, List<Order> orders){
        if(orders == null || orders.isEmpty()){
            return sql;
        }

        StringBuffer buffer = new StringBuffer("select * from (").append(sql).append(") temp_order order by ");
        for(Order order : orders){
            if(order != null){
                buffer.append(order.toString())
                        .append(", ");
            }

        }
        buffer.delete(buffer.length()-2, buffer.length());
        return buffer.toString();
    }
    
}
