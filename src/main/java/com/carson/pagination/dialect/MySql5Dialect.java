package com.carson.pagination.dialect;

/**
 * mysql 5.X
 * @author wsc
 * @Date 2018/9/15 15:21
 */
public class MySql5Dialect extends Dialect {

    protected static final String SQL_END_DELIMITER = ";";

    public String getLimitString(String sql, boolean hasOffset) {
        return MySql5PageHepler.getLimitString(sql, -1, -1);
    }

    public String getLimitString(String sql, int offset, int limit) {
        return MySql5PageHepler.getLimitString(sql, offset, limit);
    }

    //支持mysql
    public boolean supportsLimit(){
        return true;
    }
}
