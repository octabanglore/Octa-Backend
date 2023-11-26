package com.octa.transaction.platform;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionSystemException;

import com.octa.transaction.platform.TenantAwareRequestContext.TenantScopeInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Synchronization;
import jakarta.transaction.SystemException;



/**
 * This class has the same responsibility as the CurrentSessionContext
 * implementations, which is to manage the 'current' Hibernate
 * Session. But here in the JPA world we are dealing with
 * EntityManager and Hibernate's CurrentSessionContext is not applicable 
 * for that. This works in union with the EntityManager proxy 
 * ({@link DataTierConfiguration.entityManager()}) that gets injected into the DAOs. 
 */

public class EntityManagerContextHolder {
	
	public static EntityManagerFactory emf=null;
	private static final ThreadLocal<Map<String,EntityManager>> emfHolder = new ThreadLocal<>();
	
	public static EntityManager getEntityManager() throws IllegalAccessException {
		Map<String, EntityManager> emMap = emfHolder.get();
		if(emMap == null) {
			emMap = new HashMap<>();
			emfHolder.set(emMap);
		}
		TenantScopeInfo context = TenantAwareRequestContext.getCurrentFullyOperationalContext();
		final String dbKey = context.getDatabaseURL()+"-"+context.getCurrentSchemaName();
		EntityManager em = emMap.get(dbKey);
		if(em==null) {
			if(emf ==null) {
				throw new IllegalAccessException("EntityManagerContextHolder has not been set");
			}
			em = emf.createEntityManager();
		   if(emMap.isEmpty()) {
			  try {
				  AtomikosJtaPlatform.transactionManager.getTransaction().registerSynchronization(new Synchronization() {
					
					@Override
					public void beforeCompletion() {
						// Implement your logic for beforeCompletion if needed
					}
					
					@Override
					public void afterCompletion(int status) {
						Map<String, EntityManager> emMap = emfHolder.get();
						if(emMap != null) {
							for (EntityManager em : emMap.values()) {
								em.close();
							}
							emfHolder.remove();
						}
						
					}
				});
				  
			  }catch(IllegalStateException | SystemException |RollbackException se) {
				  throw new TransactionSystemException("Transaction system exception");
			  }catch(NullPointerException npe) {
				  throw new NoTransactionException("No transaction defined");
			  }
			   
		   }
		   emMap.put(dbKey, em);
		}
		return em;

	}
	
	

}
