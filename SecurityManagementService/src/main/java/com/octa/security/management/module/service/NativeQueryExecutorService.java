package com.octa.security.management.module.service;

import java.util.List;
import java.util.Map;

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
	public void executeNativeQuery(Tenant t, String nativeQuery) {
		System.out.println("Native Query executeNativeQuery nativeQuery:"+nativeQuery);
		
	Query q = octaEntityManager.createNativeQuery(nativeQuery);
	NativeQueryImpl nativeQuery2 = (NativeQueryImpl) q;
	nativeQuery2.setTupleTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	List<Map<String,Object>> result2 = nativeQuery2.getResultList();
	
	
	}

}
