package com.carson.pagination.tools;

/**
 * 会截取入参的类名，并与传入的order字符串拼接起来，作为mapper.xml文件中sql的id.
 * @author wsc
 * @Date 2018/9/9 14:35
 */
public class MySqlHelper {

    public static <T> String getSQLName(String order, T obj) {
        StringBuffer sql = new StringBuffer();
        sql.append(order);
        sql.append(obj.getClass().getSimpleName());
        //System.out.println(sql.toString());
        return sql.toString();
    }
}
