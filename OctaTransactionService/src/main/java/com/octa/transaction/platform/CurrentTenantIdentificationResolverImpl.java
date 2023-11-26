package com.octa.transaction.platform;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentificationResolverImpl implements CurrentTenantIdentifierResolver{

	@Override
	public  String resolveCurrentTenantIdentifier() {
		return TenantAwareRequestContext.getCurrentTenantScope().getCurrentSchemaName();
	}

	@Override
	public  boolean validateExistingCurrentSessions() {
		return false;
	}


}
