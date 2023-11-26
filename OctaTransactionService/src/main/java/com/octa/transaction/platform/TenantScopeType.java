package com.octa.transaction.platform;

public enum TenantScopeType {
	MASTER("Master"),CUSTOMER("Customer");
	
	private final String name;
	
	private TenantScopeType(final String name) {
		this.name= name;
	}
	public String getName() {
		return name;
	}

}
