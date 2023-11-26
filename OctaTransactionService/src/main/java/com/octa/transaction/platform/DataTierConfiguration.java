package com.octa.transaction.platform;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.xa.PGXADataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.ClassUtils;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.TenantAwareRequestContext.TenantScopeInfo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.MappedSuperclass;

@Configuration
@DependsOn("transactionManager")
public class DataTierConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(DataTierConfiguration.class);

	private String[] packagestoScanEntityClass = new String[] {"com.octa.transaction.entity","com.octa.security.management.entity"};
	private List<String> scannedEntityClasses = new ArrayList<>();

	@Autowired
	private UserTransactionManager atomikosTransactionManager;

	@Value("${datasource.master.url}")
	private String dataSourceMasterUrl;
	@Value("${datasource.master.driver}")
	private String dataSourceMasterDriver;
	@Value("${datasource.master.user}")
	private String dataSourceMasterUser;
	@Value("${datasource.master.password}")
	private String dataSourceMasterPassword;
	@Value("${datasource.master.schema}")
	private String dataSourceMasterSchema;
	@Value("${datasource.master.maxpool}")
	private String dataSourceMaxPool;
	@Value("${datasource.master.minpool}")
	private String dataSourceMinPool;


    @Bean(name = "emf")
    EntityManagerFactory entityManagerFactory() throws SQLException {
		Map<String, EntityManagerFactory> emfMap = new HashMap<>();
		prepareEmf(emfMap, true);
		
		EntityManagerFactory factory = (EntityManagerFactory) Proxy.newProxyInstance(getClass().getClassLoader(),new Class<?>[] {
			EntityManagerFactory.class,SessionFactory.class},new InvocationHandler() {

				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					EntityManagerFactory delegate = emfMap.get(TenantAwareRequestContext.getCurrentTenantScope().getDatabaseURL());
					if(delegate ==null) {
						prepareEmfs(emfMap);
						delegate = emfMap.get(TenantAwareRequestContext.getCurrentTenantScope().getDatabaseURL());
						if(delegate ==null) {
							throw new IllegalStateException("Got a database url: "+ TenantAwareRequestContext.getCurrentTenantScope().getDatabaseURL()+" that does not exists");
						}
					}
					return method.invoke(delegate, args);
				}

			});
		EntityManagerContextHolder.emf = factory;
		return factory;

	}
	private void prepareEmfs(Map<String, EntityManagerFactory> emfMap) throws SQLException {
		TenantScopeInfo tc = TenantAwareRequestContext.getCurrentTenantScope();
		if(StringUtils.isNotEmpty(tc.getDatabaseURL()) ) {
			DataSource ds = createXADatasource(tc.getDatabaseURL(),dataSourceMasterUser, dataSourceMasterPassword, tc.getDatabaseURL().substring(tc.getDatabaseURL().lastIndexOf("/")+1),Integer.valueOf(dataSourceMaxPool),Integer.valueOf(dataSourceMinPool) );
			emfMap.put(tc.getDatabaseURL(), createEmbededEntityManagerFactory(ds));
		}


	}
	
	
	private EntityManagerFactory createEmbededEntityManagerFactory(DataSource ds){

		LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
		lemfb.setJtaDataSource(ds);
		lemfb.setPersistenceUnitName("octa_s_db1");
		lemfb.setPackagesToScan(packagestoScanEntityClass);
		lemfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lemfb.setJpaProperties(hibernateJTAProperties()); 
		// Set excludeUnlistedClasses to true 
		lemfb.setPackagesToScan();
		lemfb.setPackagesToScan(packagestoScanEntityClass);
		lemfb.afterPropertiesSet();

		return lemfb.getObject();
	}
	
	
	/*

	private EntityManagerFactory createEmbededEntityManagerFactory(DataSource ds) throws SQLException{


		List<String> managedClazz = new ArrayList<>();
		if(scannedEntityClasses.isEmpty()) {
			populateEntityClassNames();
		}
		scannedEntityClasses.stream().map(managedClazz::add);


		PersistenceProvider pp = new HibernatePersistenceProvider();
		HibernatePersistenceUnitInfo hpui = new HibernatePersistenceUnitInfo("octa_s_db1", managedClazz, hibernateJTAProperties());
		hpui.setJtaDataSource(ds);

		MutablePersistenceUnitInfo mpui = new MutablePersistenceUnitInfo();
		mpui.setJtaDataSource(ds);
		mpui.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName());
		mpui.setPersistenceProviderPackageName(HibernatePersistenceProvider.class.getPackage().getName());
		mpui.setPersistenceUnitName("octa_s_db1");
		mpui.setTransactionType(PersistenceUnitTransactionType.JTA);
		mpui.setExcludeUnlistedClasses(true);
		mpui.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);


		EntityManagerFactory emf = pp.createContainerEntityManagerFactory(hpui, hibernateJTAProperties());
		SessionFactoryImpl hiberNateEmf = emf.unwrap(SessionFactoryImpl.class);
		logger.info("Using Hibernate Interceptor: {}", hiberNateEmf.getInterceptor());
		if (logger.isDebugEnabled()) {
			Set<EntityType<?>> entities = emf.getMetamodel().getEntities();
			for (EntityType<?> entityType : entities) {
				logger.debug("Manage entity {}", entityType.getName());
			}
		}


		return emf;
	}
	*/
	
	private Properties hibernateJTAProperties() {
		Properties props = new Properties();
		//props.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
		props.setProperty(AvailableSettings.ISOLATION, "3");
		props.setProperty(AvailableSettings.SHOW_SQL, "false");
		props.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "jta");
		props.setProperty(AvailableSettings.JTA_PLATFORM, AtomikosJtaPlatform.class.getName());
		props.setProperty(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, "com.octa.transaction.platform.MultiTenantConnectionProviderImpl");
		props.setProperty(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, "com.octa.transaction.platform.CurrentTenantIdentificationResolverImpl");
		props.setProperty(AvailableSettings.GENERATE_STATISTICS,"false");
		props.setProperty(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE,"50");
		if(Boolean.valueOf(false)) {
			props.setProperty(AvailableSettings.DEFAULT_CACHE_CONCURRENCY_STRATEGY,"read-only");
			props.setProperty(AvailableSettings.CACHE_REGION_FACTORY,"org.hibernate.cache.");
			props.setProperty("net.sf.ehcache.configurationResourceName", "");
			props.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE,"true");
			props.setProperty(AvailableSettings.USE_QUERY_CACHE,"true");
		}
		return props;
	}
	private void populateEntityClassNames() {
		try {
			Set<TypeFilter> entityTypeFilter = new LinkedHashSet<>(4);
			entityTypeFilter.add(new AnnotationTypeFilter(Entity.class,false));
			entityTypeFilter.add(new AnnotationTypeFilter(Embeddable.class,false));
			entityTypeFilter.add(new AnnotationTypeFilter(MappedSuperclass.class,false));
			for (String packageName : packagestoScanEntityClass) {
				ResourcePatternResolver patternReolver = new PathMatchingResourcePatternResolver();
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+ClassUtils.convertClassNameToResourcePath(packageName)+"/**/*.class";
				Resource[] resouces = patternReolver.getResources(pattern);
				MetadataReaderFactory readorFactory = new SimpleMetadataReaderFactory(patternReolver);
				for (Resource resource : resouces) {
					if(resource.isReadable()) {
						MetadataReader reader = readorFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						for (TypeFilter typeFilter : entityTypeFilter) {
							if(typeFilter.match(reader, readorFactory)) {
								scannedEntityClasses.add(className);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid specification in  Package to Scan");
		}	

	}

	private DataSource createXADatasource(String dbURL, String user,
		String pass, String resourceName, int maxPoolSize, int minPoolSize) {
		PGXADataSource pgxaDataSource  = new PGXADataSource();
		pgxaDataSource.setUrl(dbURL);
		pgxaDataSource.setDatabaseName(resourceName);
		pgxaDataSource.setUser(user);
		pgxaDataSource.setPassword(pass);
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(pgxaDataSource);
		xaDataSource.setUniqueResourceName(resourceName);
		xaDataSource.setMinPoolSize(minPoolSize);
		xaDataSource.setMaxPoolSize(maxPoolSize);
		xaDataSource.setMaxLifetime(2*60*60);
		MultiTenantConnectionProviderImpl.dataSources.put(dbURL, xaDataSource);
		return xaDataSource;
	}

	private void prepareEmf(Map<String, EntityManagerFactory> emfMap, boolean initialize) throws SQLException {
		List<Tenant> tenants = getTenantFromDataBase();
		if(initialize) {
			TenantAwareRequestContext.initTenants(tenants);
		}
		List<String> uniqueDatabases = filterDistinctDatabases(tenants);
		emfMap.clear();
		for (String dbURL : uniqueDatabases) {
			if(StringUtils.isNotEmpty(dbURL)) {
				DataSource ds = createXADatasource(dbURL, dataSourceMasterUser, dataSourceMasterPassword, dbURL.substring(dbURL.lastIndexOf("/")+1), Integer.valueOf(dataSourceMaxPool), Integer.valueOf(dataSourceMinPool));
				emfMap.put(dbURL, createEmbededEntityManagerFactory(ds));
			}

		}
	}
	private List<String> filterDistinctDatabases(List<Tenant> tenants) {
		Set<String> dataBaseNames = new HashSet<>();
		String masterDbURL = null;
		for(Tenant t : tenants) {
			if( TenantScopeType.MASTER.equals(t.getScopeType())){
				masterDbURL = t.getDatabaseURL();
			} else {
				dataBaseNames.add(t.getDatabaseURL());
			}
		}
		dataBaseNames.remove(masterDbURL);
		List<String> dbUrls = new ArrayList<>(dataBaseNames);
		dbUrls.add(0, masterDbURL);
		return dbUrls;
	}
	private List<Tenant> getTenantFromDataBase() {
		List<Tenant> tenants = new ArrayList<>();
		try {
			Class.forName(dataSourceMasterDriver);
			try(Connection con = DriverManager.getConnection(dataSourceMasterUrl, dataSourceMasterUser, dataSourceMasterPassword)){
				Statement stmt = con.createStatement();
				stmt.execute("SET search_path TO \"" + dataSourceMasterSchema + "\"");
				ResultSet rs = stmt.executeQuery("SELECT T.TENANT_ID, C.CONNECTION_URL, C.SCHEMA_NAME, T.TENANT_SCOPE, T.TENANT_NAME FROM ADMIN_TENANT T INNER JOIN ADMIN_CONNECTION C ON(T.CONNECTION_ID =C.CONNECTION_ID )");
				while(rs.next()) {
					Tenant t = new Tenant();
					t.setId(rs.getLong("TENANT_ID"));
					 com.octa.transaction.entity.Connection c = new com.octa.transaction.entity.Connection();
					c.setDatabaseUrl(rs.getString("CONNECTION_URL"));
					c.setSchemaName(rs.getString("SCHEMA_NAME"));
					if(StringUtils.isEmpty(rs.getString("TENANT_SCOPE"))){
						logger.error("Tenant Type is empty for one of the tenant correct the data then proceed {}", rs.getString("TENANT_ID"));
						continue;
					}
					t.setScopeType(TenantScopeType.valueOf(rs.getString("TENANT_SCOPE")));
					t.setName(rs.getString("TENANT_NAME"));
					t.setConnection(c);
					tenants.add(t);
				}
				rs.close();
				stmt.close();
			}

		}catch(ClassNotFoundException e) {
			throw new IllegalStateException("data source driver : "+ dataSourceMasterDriver + " is not in classpath" + e);

		}catch(SQLException e) {
			throw new IllegalStateException("Could not connect or retive information from master schema"+e);
		}
		return tenants;
	}


	/**
	 * Returns a proxy EntityManager that can be injected into the DAOs.
	 * This proxy delegates to an actual EntityManager for which it depends
	 * on {@link HibernateEntityManagerHolder}
	 * @return
	 * @see EntityManagerContextHolder
	 */
	@Bean(name="octaEntityManager")
	EntityManager entityManager() {
		return (EntityManager) Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {EntityManager.class},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if (method.getName().equals("equals")) {
							return (proxy == args[0]);
						}
						else if (method.getName().equals("hashCode")) {
							return System.identityHashCode(proxy);
						}
						else if(method.getName().equals("close")) {
							return null;
						}
						
						return method.invoke(EntityManagerContextHolder.getEntityManager(), args);
					}
				});
	}

	/**
	 * Enabling auto proxying for all point cuts advised by
	 * the advisors.* advisors.
	 * @return
	 */
	//@Bean(name="autoProxy")
	DefaultAdvisorAutoProxyCreator setupAutoProxy() {
		DefaultAdvisorAutoProxyCreator daapc = new DefaultAdvisorAutoProxyCreator();
		daapc.setProxyTargetClass(true);
		daapc.setAdvisorBeanNamePrefix("advisors.");
		return daapc;
	}

    /**
     * Transactional advisor that applies transaction interceptor advice on
     * methods annotated with @OctaTransaction
     * 
     * The bean name used here is deliberately prefixed with 'advisors.'
     * This is because that is the prefix the auto proxying is 
     * looking for. Check out setupAutoProxy() above.
     * 
     * @return
     * @see OctaTransaction
     */
    @Bean(name = "advisors.vlcTxAdvisor")
    DefaultPointcutAdvisor transactionAdvisor() {
		DefaultPointcutAdvisor dpa = new DefaultPointcutAdvisor();
		dpa.setPointcut(new StaticMethodMatcherPointcut() {
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return method.isAnnotationPresent(OctaTransaction.class) && Modifier.isPublic(method.getModifiers());
			}
		});
		dpa.setAdvice(transactionInterceptor());
		dpa.setOrder(1);
		return dpa;
	}
	
	/**
	 * Registers the transaction advice implementation with Spring
	 * @return
	 */
	@Bean(name="octaTransactionInterceptor")
	Advice transactionInterceptor() {
		OctaTransactionInterceptor interceptor = new OctaTransactionInterceptor();
		interceptor.setTransactionManager(atomikosTransactionManager);
		return interceptor;
	}
	
	/**
	 * Registers the tenant context setting advice implementation with Spring
	 * @return
	 */
	@Bean(name="tenantContextSettingInterceptor")
	Advice tenantContextSettingInterceptor() {
		return new TenantContextSettingInterceptor();
	}
	
	/**
	 * Registers the tenant context setting advice implementation with Spring
	 * @return
	 */
	@Bean(name="exceptionLoggingInterceptor")
	Advice exceptionLoggingInterceptor() {
		return new ExceptionLoggingInterceptor();
	}

}
