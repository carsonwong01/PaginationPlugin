package com.carson.pagination.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wsc
 * @Date 2018/9/15 15:44
 */
public class MySql5PageHepler {

    public static String getCountString(String querySelect){
        querySelect = getLineSql(querySelect);
        int orderIndex = getLastOrderInsertPoint(querySelect);
        int formIndex = getAfterFormInsertPoint(querySelect);
        String select = querySelect.substring(0, formIndex);
        return select.toLowerCase().indexOf("select distinct") == -1 && querySelect.toLowerCase().indexOf("group by") == -1
                ? (new StringBuffer(querySelect.length())).append("select count(1) count ")
                .append(querySelect.substring(formIndex, orderIndex)).toString()
                : (new StringBuffer(querySelect.length())).append("select count(1) count from (")
                .append(querySelect.substring(0, orderIndex)).append(" ) t").toString();
    }

    private static int getLastOrderInsertPoint(String querySelect){
        int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
        if(orderIndex != -1 && isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length()))){
            return orderIndex;
        }else {
            throw new RuntimeException("My SQL 分页必须要有Order by 语句!");
        }
    }


    public static String getLimitString(String querySelect, int offset, int limit){
        querySelect = getLineSql(querySelect);
        String sql = querySelect + " limit " + offset + " ," + limit;
        return sql;
    }


    private static String getLineSql(String sql){
        return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}"," ");
    }


    private static int getAfterFormInsertPoint(String querySelect){
        String regex = "\\s+FROM\\s+";
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(querySelect);

        int fromStartIndex;
        String text;
        do{
            if(!matcher.find()){
                return 0;
            }

            fromStartIndex = matcher.start(0);
            text = querySelect.substring(0, fromStartIndex);
        }while (!isBracketCanPartnership(text));
        return fromStartIndex;
    }


    public static boolean isBracketCanPartnership(String text){
        return text != null && getIndexOfCount(text, '(') == getIndexOfCount(text, '(');
    }


    public static int getIndexOfCount(String text, char ch){
        int count = 0;

        for(int i = 0; i < text.length(); ++i){
            count = text.charAt(i) == ch ? count + 1 : count;
        }
        return count;
    }
}
