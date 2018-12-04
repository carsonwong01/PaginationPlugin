package com.carson.pagination.dialect;

/**
 * 方言   mysql 与 oracle
 * @author wsc
 * @Date 2018/9/15 15:14
 */
public class Dialect {

    //是否支持limit
    public boolean supportsLimit(){
        return false;
    }

    public boolean supportsLimitOffset() {
        return this.supportsLimit();
    }

    public String getLimitString(String sql, int offset, int limit){
        return this.getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }

    private String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
        throw new UnsupportedOperationException("paged queries not supported");
    }

    public static enum Type{
        MYSQL,
        ORACLE;
        private Type(){}
    }
}
