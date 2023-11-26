package com.octa.transaction.platform;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import jakarta.transaction.Status;
import jakarta.transaction.TransactionManager;

public class OctaTransactionInterceptor implements MethodInterceptor {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(OctaTransactionInterceptor.class);
	
	private TransactionManager transactionManager;
	
	public OctaTransactionInterceptor() {
		super();
	}
	
	public void setTransactionManager(TransactionManager txManager) {
		this.transactionManager = txManager;
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("Intercepting method execution for tx advice");
		boolean begun = false;
		//begin only if no existing transaction (PROPAGATION_REQUIRED)
		if (transactionManager.getStatus() != Status.STATUS_ACTIVE) {
			transactionManager.begin();
			begun = true;
		}
		Object result = null;
		try {
			result = invocation.proceed();
		}
		catch (Throwable t) {
			logger.info("Rolling back the transaction");
			if (begun) {
				//rollback only if I started the transaction
				transactionManager.rollback();
			}
			t.printStackTrace();
			throw t;
		}
		logger.info("Committing the transaction");
		if (begun) {
			//commit only if I started the transaction
			transactionManager.commit();
		}
		return result;
	}
	
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

}
