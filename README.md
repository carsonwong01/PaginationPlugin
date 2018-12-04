# api
## Environment
MySQL 5.XX

Oracle

## 配置方式
打成jar包放到项目中即可

###springMVC 配置方式：
在properties文件中配置 mysql: 
<br>
db.dialect=com.carson.pagination.dialect.MySql5Dialect
<br>
或者 oracle: 
<br>
db.dialect=com.carson.pagination.dialect.OracleDialect

    <--物理分页拦截器-->
    <bean id="paginationInterceptor" class="com.carson.pagination.interceptor.PaginationInterceptor">
        <property name="myDialect"> <value>${db.dialect}</value></property>
    </bean>
    
    <--spring和MyBatis整合  省去了其他配置-->
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <property name="plugins">
                <array>
                    <ref bean="paginationInterceptor"/>
                </array>
            </property>
        </bean>
     
### springboot 配置方式  
yml数据库方言配置<br>
mysql： sqlDialect: com.carson.platform.utils.pageframework.dialect.MySql5Dialect
<br>
Oracle：   sql-dialect: com.carson.platform.utils.pageframework.dialect.OracleDialect
<br>
config类中（省去部分配置）:

    // myBatis分页插件  需要配置方言  mysql  OR oracle
    @Bean
    public PaginationInterceptor getInterceptor(){
        PaginationInterceptor pageHelper = new PaginationInterceptor();
        pageHelper.setMyDialect(sqlDialect);
        return pageHelper;
    }
    
    /**
     * SqlSessionFactory 
     */
    @Bean
    public SqlSessionFactory druidSqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource,
                                                    PaginationInterceptor pageHelper) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[] { pageHelper });
        // 添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
## 使用方式demo    
    
    public BaseDataResp findAllFrontLogs(FindFrontLogsReq frontLogsReq){
            BaseDataResp response = new BaseDataResp(CodeConstant.SUCCESS);
            PageContext pageContext = PageContext.getContext();
            pageContext.setPageSize(frontLogsReq.getPageSize());
            pageContext.setCurrentPage(frontLogsReq.getPageNum());
            pageContext.setPagination(StringUtils.isEmpty(frontLogsReq.getExportPath()));
            ExecuteEvent<FindFrontLogsReq> event = new ExecuteEvent<>();
            event.setObj(frontLogsReq);
            event.setStatement("findAllFrontLogs");
            List<SystemFrontLogs> list = baseDao.findByParams(event);
            Map<String, Object> data = new HashMap<>();
            data.put("allFrontLogs", new PageResult(pageContext, list));
            response.setData(data);
            return response;
        }
     

      