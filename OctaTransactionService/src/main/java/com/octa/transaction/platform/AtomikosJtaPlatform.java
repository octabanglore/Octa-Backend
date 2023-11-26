package com.octa.transaction.platform;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import com.atomikos.icatch.jta.UserTransactionManager;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

public class AtomikosJtaPlatform extends AbstractJtaPlatform{
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(AtomikosJtaPlatform.class);
	
	private static final long serialVersionUID = 8621522227255652544L;
	
	public static UserTransactionManager transactionManager;
	public static UserTransaction transaction;
	
	@Override
	protected TransactionManager locateTransactionManager() {
		logger.debug("Returning transactionManager");
		return transactionManager;
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		logger.debug("Returning transaction");
		return transaction;
	}

}
