package com.octa.security.management.service;

import org.springframework.stereotype.Service;

import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;
import com.octa.transaction.platform.TenantAwareRequestContext;
import com.octa.transaction.repo.TenantDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {
	
	private final TenantDAO tenantRepository;
	
	@OctaTransaction
	public Tenant getbyLoginurl(String loginUrl) {
		Long currentTenant = TenantAwareRequestContext.getCurrentTenantScope().getId();
		TenantAwareRequestContext.setTenantScope(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getId());
		var tenant = tenantRepository.findLoginUrl(loginUrl).orElseThrow();
		TenantAwareRequestContext.setTenantScope(currentTenant);
		return tenant;
	}

}
