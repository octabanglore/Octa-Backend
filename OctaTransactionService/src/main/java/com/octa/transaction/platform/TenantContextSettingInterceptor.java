package com.octa.transaction.platform;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.octa.transaction.platform.TenantAwareRequestContext.TenantScopeInfo;

public class TenantContextSettingInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		for (Object o : invocation.getArguments()) {
			if (TenantScopeInfo.class.isInstance(o)) {
				Long tenantId = ((TenantScopeInfo)o).getId();
				//TODO: Ensure that a tenant specific context is chosen for all tenant specific services
				//if (TenantRequestContext.DEFAULT_MASTER_CONTEXT.getId().equals(tenantId)) {
				//	invocation.getClass().isInstance(CentralService.class);
				//}
				TenantAwareRequestContext.setTenantScope(tenantId);
				break;
			}
		}
		Object result = invocation.proceed();
		//Guard against accidental inserts into some random tenant's schema
		//We might as well have the wrong inserts go into the central schema
		//away from the eyes of the tenants.
		TenantAwareRequestContext.setTenantScope(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getId());
		return result;
	}

}
