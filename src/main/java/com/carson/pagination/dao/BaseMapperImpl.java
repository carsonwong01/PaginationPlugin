package com.carson.pagination.dao;

import com.carson.pagination.tools.ExecuteEvent;
import com.carson.pagination.tools.MapperConstant;
import com.carson.pagination.tools.MySqlHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BaseDao实现类  sqlsession
 * @author wsc
 * @Date 2018/9/15 14:29
 */
@Service
public class BaseMapperImpl<T, E> implements BaseMapper<T, E>{

    @Autowired
    private transient SqlSession sqlSession;
    @Autowired
    private transient SqlSessionFactory sqlSessionFactory;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @Override
    public int insert(T obj) {
        return this.getSqlSession().insert(MySqlHelper.getSQLName(MapperConstant.COMMON_INSERT, obj), obj);
    }

    @Override
    public int insert(ExecuteEvent<T> obj) {
        obj.initParameter();
        return this.getSqlSession().insert(obj.getStatement(), obj.getParameter());
    }

    @Override
    public int insert(String statement, T var1) {
        return this.getSqlSession().insert(statement, var1);
    }

    @Override
    public int delete(T obj) {
        return this.getSqlSession().delete(MySqlHelper.getSQLName(MapperConstant.COMMON_DELETE, obj), obj);
    }

    @Override
    public int delete(ExecuteEvent<T> var) {
        var.initParameter();
        return this.getSqlSession().delete(var.getStatement(), var.getParameter());
    }

    @Override
    public int delete(String statement, T var1) {
        return this.getSqlSession().delete(statement, var1);
    }

    @Override
    public int update(T obj) {
        return this.getSqlSession().update(MySqlHelper.getSQLName(MapperConstant.COMMON_UPDATE, obj), obj);
    }

    @Override
    public int update(ExecuteEvent<T> var1) {
        var1.initParameter();
        return this.getSqlSession().update(var1.getStatement(), var1.getParameter());
    }

    @Override
    public int update(String statement, T var1) {
        return this.getSqlSession().update(statement, var1);
    }

    @Override
    public int batchInsert(T obj, String statement, List<E> list) {
        if(null == statement || "".equals(statement)){
            if(null == obj){
                return 0;
            }
            statement = MySqlHelper.getSQLName(MapperConstant.COMMON_INSERT, obj);
        }
        SqlSession sqlSession = null;

        byte var6;
        try {
            sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                //E e = (E) iterator.next();
                sqlSession.insert(statement, iterator.next());
            }
            sqlSession.commit();
            return 1;
        } catch (Exception e) {
            var6 = 0;
        } finally {
            if(null != sqlSession){
                sqlSession.close();
            }
        }
        return var6;
    }

    @Override
    public int batchDelete(T obj, Map<String, List<E>> map) {
        return this.getSqlSession().delete(MySqlHelper.getSQLName(MapperConstant.COMMON_DELETE_BATCH, obj), map);
    }

    @Override
    public int batchDelete(T obj, String statement, List<E> idList) {
        if(null == statement || "".equals(statement)){
            if(null == obj){
                return 0;
            }
            statement = MySqlHelper.getSQLName(MapperConstant.COMMON_DELETE, obj);
        }
        SqlSession sqlSession = null;

        byte var6;
        try {
            sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            Iterator iterator = idList.iterator();
            while (iterator.hasNext()){
                sqlSession.insert(statement, iterator.next());
            }
            sqlSession.commit();
            return 1;
        } catch (Exception e) {
            var6 = 0;
        } finally {
            if(null != sqlSession){
                sqlSession.close();
            }
        }
        return var6;
    }

    @Override
    public int batchUpdate(T obj, Map<String, List<E>> map) {
        return this.getSqlSession().update(MySqlHelper.getSQLName(MapperConstant.COMMON_UPDATE_BATCH, obj), map);
    }

    @Override
    public int batchUpdate(T obj, String statement, List<E> list) {
        if(statement == null || "".equals(statement)){
            if(null == obj){
                return 0;
            }
            statement =  MySqlHelper.getSQLName(MapperConstant.COMMON_UPDATE, obj);
        }
        SqlSession sqlSession = null;
        byte var6;
        try {
            sqlSession = this.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                sqlSession.update(statement, iterator.next());
            }
            sqlSession.commit();
            return 1;
        } catch (Exception e) {
            var6 = 0;
        } finally {
            if(null != sqlSession){
                sqlSession.close();
            }
        }
        return var6;
    }

    @Override
    public T findById(T var1) {
        return this.getSqlSession().selectOne(MySqlHelper.getSQLName(MapperConstant.COMMON_FINDBYID ,var1), var1);
    }

    @Override
    public T findById(ExecuteEvent<T> var1) {
        var1.initParameter();
        return this.getSqlSession().selectOne(var1.getStatement(), var1.getParameter());
    }

    @Override
    public T findById(String statement, T var1) {
        return this.getSqlSession().selectOne(statement, var1);
    }

    @Override
    public List<E> findAll(T var1) {
        return this.getSqlSession().selectList(MySqlHelper.getSQLName(MapperConstant.COMMON_FINDALL, var1), var1);
    }

    @Override
    public List<E> findAll(ExecuteEvent<T> var1) {
        var1.initParameter();
        return this.getSqlSession().selectList(var1.getStatement(), var1.getParameter());
    }

    @Override
    public List<E> findAll(String statement, T var1) {
        return this.getSqlSession().selectList(statement, var1);
    }

    @Override
    public List<E> findByParams(ExecuteEvent<T> var1) {
        var1.initParameter();
        List list;
        if(null != var1.getStatement() && !"".equals(var1.getStatement())){
            list = this.getSqlSession().selectList(var1.getStatement(), var1.getParameter());
        }else {
            list = this.getSqlSession().selectList(MySqlHelper.getSQLName(MapperConstant.COMMON_FINDALL, var1.getObj()), var1.getParameter());
        }
        return list;
    }

    @Override
    public Object findOneByParams(ExecuteEvent<T> var1) {
        var1.initParameter();
        Object object = this.getSqlSession().selectOne(var1.getStatement(), var1.getParameter());
        return object;
    }

    @Override
    public Object executeSQL(String opType, String sqlName, Object param) {
        if(null != opType && !"".equals(opType) && !MapperConstant.COMMON_LIST.equals(opType)){
            if(MapperConstant.COMMON_ONE.equals(opType)){
                return this.getSqlSession().selectOne(sqlName, param);
            }else if (MapperConstant.COMMON_INSERT.equals(opType)){
                return this.getSqlSession().insert(sqlName, param);
            }else if (MapperConstant.COMMON_DELETE.equals(opType)){
                return this.getSqlSession().delete(sqlName,param);
            }else {
                return MapperConstant.COMMON_UPDATE.equals(opType) ? this.getSqlSession().update(sqlName, param) :
                        this.getSqlSession().selectList(sqlName, param);
            }
        }else {
            return this.getSqlSession().selectList(sqlName, param);
        }
    }

    @Override
    public Object count(ExecuteEvent<T> var1) {
        var1.initParameter();
        return this.getSqlSession().selectOne(var1.getStatement(), var1.getParameter());
    }

    @Override
    public Map<String, Object> executeProcedure(String statement, Map<String, Object> map) {
        this.getSqlSession().selectOne(statement, map);
        return map;
    }
}
