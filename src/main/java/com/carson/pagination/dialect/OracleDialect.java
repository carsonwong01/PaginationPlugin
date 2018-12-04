package com.carson.pagination.dialect;

/**
 * oracle 方言支持
 * @author wsc
 * @Date 2018/9/15 15:24
 */
public class OracleDialect extends Dialect {

    public String getLimitString(String sql, int offset, int limit){
        sql = sql.trim();
        boolean isForUpdate = false;
        if(sql.toLowerCase().endsWith(" for update")){
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        pagingSelect.append(sql);
        pagingSelect.append(" ) row_ ) where rownum_ > " + offset + " and rownum_ <= " + (offset + limit));
        if(isForUpdate){
            pagingSelect.append(" for update");
        }
        return pagingSelect.toString();
    }
}
