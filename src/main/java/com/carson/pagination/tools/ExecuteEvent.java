package com.carson.pagination.tools;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 增删改查传参与处理SQL的工具类
 * @author wsc
 * @Date 2018/9/15 12:40
 */
public class ExecuteEvent<T> {
    //要传入的对象
    private T obj;
    //meppper中SQL的ID   SQL
    private String statement;
    //入参
    private Map<String, Object> parameter = new HashMap<>();

    private String appendSql;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
    }

    public String getAppendSql() {
        return appendSql;
    }

    public void setAppendSql(String appendSql) {
        this.appendSql = appendSql;
    }

    /**
     * 对象转成如入参
     * @param obj
     */
    private void converObjectToParameter(Object obj){
        if (obj != null) {
            Class supers = obj.getClass().getSuperclass();
            Field[] fields;
            if (null != supers) {
                fields = supers.getDeclaredFields();
                if (fields.length > 0) {
                    this.setParamIntoMap(fields, obj);
                }

                supers = supers.getSuperclass();
                if (null != supers) {
                    fields = supers.getDeclaredFields();
                    if (fields.length > 0) {
                        this.setParamIntoMap(fields, obj);
                    }
                }
            }

            fields = obj.getClass().getDeclaredFields();
            if (fields.length > 0) {
                this.setParamIntoMap(fields, obj);
            }
        }
    }

    /**
     * object转map
     * @param fields
     * @param obj
     */
    private void setParamIntoMap(Field[] fields, Object obj){
        Field[] arrs = fields;
        int length = fields.length;

        for(int i = 0; i < length; ++i){
            Field field = arrs[i];

            field.setAccessible(true);
            try {
                this.parameter.put(field.getName(), field.get(obj));
            } catch (ExceptionInInitializerError var8) {
                this.parameter.put(field.getName(), (Object)null);
            } catch (NullPointerException var9) {
                this.parameter.put(field.getName(), (Object)null);
            } catch (IllegalArgumentException var10) {
                this.parameter.put(field.getName(), (Object)null);
            } catch (IllegalAccessException var11) {
                this.parameter.put(field.getName(), (Object)null);
            }
        }
    }

    /**
     * 初始化  Object转map
     */
    public void initParameter() {
        this.converObjectToParameter(this.obj);
        if (null != this.appendSql && !"".equals(this.appendSql)) {
            this.parameter.put("appendSql", this.appendSql);
        }
    }

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void putParameter(String key, String value){
        this.parameter.put(key, value);
    }

    /**
     * 移除参数
     * @param key
     * @param value
     */
    public void removeParameter(String key, String value){
        this.parameter.remove(key, value);
    }


}
