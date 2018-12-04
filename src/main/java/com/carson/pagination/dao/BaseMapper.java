package com.carson.pagination.dao;



import com.carson.pagination.tools.ExecuteEvent;

import java.util.List;
import java.util.Map;

/**
 * 所有增删改查操作接口
 * @author wsc
 * @Date 2018/9/15 12:25
 */
public interface BaseMapper<T, E> {

    /**
     * 这种写法要求mapper.xml的ID要写为 “insert+入参类名”的形式，局限大
     * 比如入参类型  SystemFrontLogs， 则mapper.xml中的ID必须要写为insertSystemFrontLogs，
     * 否则就找不到要执行的语句 以下此种类型的delete(T var1)  update(T var1)也一样。
     * 单条记录插入
     * @param var1 要插入的数据
     * @return
     */
    int insert(T var1);
    /**
     * 单条记录插入
     * @param obj
     * @return
     */
    int insert(ExecuteEvent<T> obj);

    /**
     * 单条记录插入
     * @param statement  mapper.xml中的ID
     * @param var1 入参
     * @return
     */
    int insert(String statement, T var1);

    /**
     * 删除
     * @param var
     * @return
     */
    int delete(T var);

    /**
     * 删除
     * @param var
     * @return
     */
    int delete(ExecuteEvent<T> var);

    /**
     * 删除
     * @param statement  mapper.xml中的ID
     * @param var1 入参
     * @return
     */
    int delete(String statement, T var1);

    /**
     * 更新
     * @param var1
     * @return
     */
    int update(T var1);

    /**
     * 更新
     * @param var1
     * @return
     */
    int update(ExecuteEvent<T> var1);

    /**
     * 更新
     * @param statement  mapper.xml中的ID
     * @param var1  入参
     * @return
     */
    int update(String statement, T var1);


    /**
     * 批量插入
     * @param var1  要插入的数据的参数以及类型
     * @param var2  statement SQL即  mapper文件中SQL的ID
     * @param var3  要插入的数据list
     * @return
     */
    int batchInsert(T var1, String var2, List<E> var3);

    /**
     * 批量删除
     * @param var1
     * @param var2
     * @return
     */
    int batchDelete(T var1, Map<String, List<E>> var2);

    /**
     * 批量删除
     * @param var2 statement SQL即  mapper文件中SQL的ID
     * @param idList 要插入的数据list
     * @return
     */
    int batchDelete(T obj, String var2, List<E> idList);

    /**
     * 批量更新
     * @param var1
     * @param var2
     * @return
     */
    int batchUpdate(T var1, Map<String, List<E>> var2);

    /**
     * 批量更新
     * @param var2  statement SQL即  mapper文件中SQL的ID
     * @param var3  要插入的数据list
     * @return
     */
    int batchUpdate(T obj, String var2, List<E> var3);

    /**
     * 根据ID查询
     * @param var1 ID
     * @return
     */
    T findById(T var1);

    /**
     * 根据ID查询
     * @param var1
     * @return
     */
    T findById(ExecuteEvent<T> var1);

    /**
     * 根据ID查询
     * @param statement   mapper文件中SQL的ID
     * @param var1
     * @return
     */
    T findById(String statement, T var1);

    /**
     * 查询所有记录
     * @return
     */
    List<E> findAll(T var1);

    /**
     * 查询所有记录
     * @param var1
     * @return
     */
    List<E> findAll(ExecuteEvent<T> var1);

    /**
     * 查询所有记录
     * @param statement  sql id
     * @param var1
     * @return
     */
    List<E> findAll(String statement, T var1);

    /**
     * 条件查询
     * @param var1
     * @return
     */
    List<E> findByParams(ExecuteEvent<T> var1);

    /**
     * 查询满足条件的单条记录
     * @param var1
     * @return
     */
    Object findOneByParams(ExecuteEvent<T> var1);

    /**
     * 执行  批量执行
     * @param var1  执行方式  insert update delete select
     * @param var2  sqlname 即 mapper文件中的id
     * @param var3  传入参数
     * @return
     */
    Object executeSQL(String var1, String var2, Object var3);

    /**
     * 查询满足条件的记录条数
     * @param var1
     * @return
     */
    Object count(ExecuteEvent<T> var1);

    /**
     * 执行存储过程？？
     * @param var1  statement  mapper中的ID  即SQL
     * @param var2 入参
     * @return
     */
    Map<String, Object> executeProcedure(String var1, Map<String, Object> var2);

}
