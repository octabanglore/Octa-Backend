package com.octa.transaction.platform;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ExceptionLoggingInterceptor implements MethodInterceptor{

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(ExceptionLoggingInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		}
		catch (Throwable t) {
			logger.error("Exception in service layer", t);
			throw t;
		}
	}

}
