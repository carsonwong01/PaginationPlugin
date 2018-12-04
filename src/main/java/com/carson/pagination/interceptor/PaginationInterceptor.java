package com.carson.pagination.interceptor;

import com.carson.pagination.dialect.Dialect;
import com.carson.pagination.page.Page;
import com.carson.pagination.page.PageContext;
import com.carson.pagination.page.ReflectHelper;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 *  物理分页拦截器  mybatis
 * @author wsc
 * @Date 2018/9/15 16:42
 */
@Intercepts(@Signature(type = Executor.class, method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class,
                    ResultHandler.class}))
public class PaginationInterceptor implements Interceptor {

    public PaginationInterceptor() {
    }

    private Dialect dialect;

    private String myDialect;

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public String getMyDialect() {
        return myDialect;
    }

    public void setMyDialect(String myDialect) {
        if(null != myDialect && !"".equals(myDialect)){
            try {
                this.dialect = (Dialect)Class.forName(myDialect).newInstance();
                this.setDialect(this.dialect);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        String originalSql = null;
        if(null == boundSql){
            return null;
        }else {
            originalSql = boundSql.getSql().trim();
            Object parameterObject = boundSql.getParameterObject();
            if(boundSql != null && boundSql.getSql() != null && !"".equals(boundSql.getSql())){
                Page page = null;
                PageContext context = PageContext.getContext();
                if(parameterObject != null){
                    page = (Page) ReflectHelper.isPage(parameterObject, "page");
                }

                if(page == null && context.isPagination()){
                    page = context;
                }
                if(page !=null && page.isPagination()){
                    page.setPagination(false);
                    int totalPage = 0;
                    StringBuffer countSql = new StringBuffer(originalSql.length() + 100);
                    countSql.append("select count(1) from (").append(originalSql).append(") t");
                    Connection connection = mappedStatement.getConfiguration().getEnvironment()
                            .getDataSource().getConnection();
                    PreparedStatement countStmt = connection.prepareStatement(countSql.toString());
                    BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql.toString(),
                                boundSql.getParameterMappings(), parameterObject);
                    Field metaParamsField = ReflectHelper.getFieldByFieldName(boundSql, "metaParameters");
                    MetaObject mo = (MetaObject) ReflectHelper.getValueByFieldName(boundSql, "metaParameters");
                    if(metaParamsField != null){
                        ReflectHelper.setValueByFieldName(countBS, "metaParameters", mo);
                    }
                    this.setParameters(countStmt, mappedStatement, countBS, parameterObject);
                    ResultSet rs = countStmt.executeQuery();
                    if(rs.next()){
                        totalPage = rs.getInt(1);
                    }

                    rs.close();
                    countStmt.close();
                    connection.close();
                    RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
                    page.init(totalPage, page.getPageSize(), page.getCurrentPage());
                    if(rowBounds == null || rowBounds == RowBounds.DEFAULT){
                        rowBounds = new RowBounds(page.getPageSize() * (page.getCurrentPage() -1), page.getPageSize());
                    }
                    invocation.getArgs()[2] = new RowBounds(0, Integer.MAX_VALUE);
                    countBS = new BoundSql(mappedStatement.getConfiguration(), this.dialect.getLimitString(originalSql,
                            rowBounds.getOffset(), rowBounds.getLimit()), boundSql.getParameterMappings(), parameterObject);
                    if(metaParamsField != null){
                        ReflectHelper.setValueByFieldName(countBS, "metaParameters", mo);
                    }
                    invocation.getArgs()[0] = this.copyFromMappedStatement(mappedStatement,
                            new PaginationInterceptor.BoundSqlSqlSource(countBS));
                }
                return invocation.proceed();
            }else {
                return null;
            }
        }
    }


    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }


    public void setProperties(Properties arg0) {
    }


    /**
     *
     * @param preparedStatement
     * @param mappedStatement
     * @param boundSql
     * @param paramObj
     * @throws SQLException
     */
    public void setParameters(PreparedStatement preparedStatement, MappedStatement mappedStatement,
                              BoundSql boundSql, Object paramObj) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if(parameterMappings != null){
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = paramObj == null ? null : configuration.newMetaObject(paramObj);

            for(int i = 0; i < parameterMappings.size(); ++i){
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if(parameterMapping.getMode() != ParameterMode.OUT){
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    Object value;
                    if(paramObj == null){
                        value = null;
                    }else if(typeHandlerRegistry.hasTypeHandler(paramObj.getClass())){
                        value = paramObj;
                    }else if(boundSql.hasAdditionalParameter(propertyName)){
                        value = boundSql.getAdditionalParameter(propertyName);
                    }else if(propertyName.startsWith("__frch_") && boundSql.hasAdditionalParameter(prop.getName())){
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if(value != null){
                            value = configuration.newMetaObject(value)
                                    .getValue(propertyName.substring(prop.getName().length()));
                        }
                    }else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if(typeHandler == null){
                        throw new ExecutorException("There was no TypeHandler found for parameter "
                                + propertyName + " of statement " + mappedStatement.getId());
                    }

                    typeHandler.setParameter(preparedStatement, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }


    /**
     *
     * @param ms
     * @param sqlSource
     * @return
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource sqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),
                sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] s = ms.getKeyProperties();
        StringBuffer pro = new StringBuffer();
        if (null != s) {
            String[] arrs = s;
            int length = s.length;
            for (int i = 0; i < length; ++i) {
                String str = arrs[i];
                pro.append(str);
                pro.append(',');
            }
        }
        builder.keyProperty(pro.toString());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }

    /**
     *
     */
    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql){
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return this.boundSql;
        }
    }
}
