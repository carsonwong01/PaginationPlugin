package com.carson.pagination.page;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 反射工具类   todo  有待理解
 * @author wsc
 * @Date 2018/9/15 16:52
 */
public class ReflectHelper {

    /**
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object isPage(Object obj, String fieldName){
        if(obj instanceof Map){
            Map map = (Map)obj;
            return map.get(fieldName);
        }else {
            Class superClass = obj.getClass();
            while (superClass != Object.class){
                try {
                    return superClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    superClass = superClass.getSuperclass();
                }
            }
        }
        return null;
    }

    /**
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if(field != null){
            if(field.isAccessible()){
                value = field.get(obj);
            }else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName){
        Class superClass = obj.getClass();
        while (superClass != Object.class){
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        if(obj instanceof Map){
            Map map = (Map)obj;
            map.put(fieldName, value);
        }else {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if(field.isAccessible()){
                field.set(obj, value);
            }else {
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(false);
            }
        }
    }
}
