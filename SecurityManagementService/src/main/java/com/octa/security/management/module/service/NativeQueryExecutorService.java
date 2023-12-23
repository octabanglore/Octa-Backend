package com.octa.security.management.module.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class NativeQueryExecutorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(NativeQueryExecutorService.class);
	
	@Autowired
	EntityManager octaEntityManager;
	
	@OctaTransaction
	public List<Map<String,Object>> executeNativeQuery(Tenant t, String nativeQuery) {
		System.out.println("Native Query executeNativeQuery nativeQuery:"+nativeQuery);
		
	Query q = octaEntityManager.createNativeQuery(nativeQuery);
	NativeQueryImpl nativeQuery2 = (NativeQueryImpl) q;
	nativeQuery2.setTupleTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	
	return nativeQuery2.getResultList();
	}
	
	@OctaTransaction
	public Connection getConnectionFromEntityManager(Tenant t) throws SQLException {
		Session session = (Session) octaEntityManager.getDelegate();
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
		JdbcConnectionAccess connectionAccess = ((SessionImplementor)sfi.getCurrentSession()).getJdbcConnectionAccess();
		Connection conn = connectionAccess.obtainConnection();
		/*
		Map<String, Object> properties = octaEntityManager.getEntityManagerFactory().getProperties();
		AtomikosDataSourceBean dataSource = (AtomikosDataSourceBean) properties.get("javax.persistence.jtaDataSource");
		dataSource.getConnection();
		*/
		
		return conn;
	}
	

}
