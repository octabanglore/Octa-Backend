package com.octa.transaction.platform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.octa.transaction.entity.Tenant;

public class TenantAwareRequestContext {
	
	private static final Logger logger = LoggerFactory.getLogger(TenantAwareRequestContext.class);
	
	public static TenantScopeInfo DEFAULT_MASTER_CONTEXT = null;
	
	private static Map<Long, Tenant> tenantContextStore = new HashMap<>();
	
	private static final ThreadLocal<TenantScopeInfo> tenantContextHolder = new ThreadLocal<>();
	
	private TenantAwareRequestContext() {
		
	}
	
	public static TenantScopeInfo getCurrentTenantScope() {
		TenantScopeInfo t = tenantContextHolder.get();
		return (t==null) ? DEFAULT_MASTER_CONTEXT : t;
	}
	
	public static void setTenantScope(Long tenantId) {
		TenantScopeInfo t = tenantContextStore.get(tenantId);
		if(t==null) {
			logger.info("Could not find current tenanat scope hence reload all tenant details");
			reload();
			t = tenantContextStore.get(tenantId);
			if(t==null) {
				throw new IllegalArgumentException("Could not find tenant info after reloading. ");
			}
		}
		tenantContextHolder.set(t);
		
	}
	public static TenantScopeInfo getTenantScope(Long tenantId) {
		return tenantContextStore.get(tenantId);
	}
	
	
	
	private static void reload() {
		//ApplicationContextHolder.context.getBean("tenantService");
	}




	public  static interface TenantScopeInfo extends Serializable {
		
		public Long getId();
		public String getCurrentSchemaName();
		public String getDatabaseURL();
		public String getTenantName();
		public boolean isOperable();
		public TenantScopeType getScopeType();
		public Integer minPool();
		public Integer maxPool();
		

	}
	
	public static synchronized void initTenants(List<Tenant> tenants) {
		tenantContextStore.clear();
		boolean defaultContext = false;
		for (Tenant tenant : tenants) {
			if(TenantScopeType.MASTER.equals(tenant.getScopeType())) {
				if(defaultContext) {
					throw new IllegalStateException("Multiple instances of the MASTER schema have been detected. Please ensure that only one MASTER schema is in use");
				}else {
					defaultContext = true;
					DEFAULT_MASTER_CONTEXT = tenant;

				}
			}

			tenantContextStore.put(tenant.getId(), tenant);
		}
		
		if(!defaultContext) {
			throw new IllegalStateException("The default MASTER context is not found. Please use 'MASTER' as the expected value.");
		}

	}
	
	
	public static TenantScopeInfo getCurrentFullyOperationalContext() throws IllegalAccessException {
		
		TenantScopeInfo  tsi = getCurrentTenantScope();
		final Long tenantId = tsi.getId();
		if(!tsi.isOperable()) {
			RetryTemplate retry = 	createRetryTemplate();
			retry.execute(new RetryCallback<Void, IllegalAccessException>() {

				@Override
				public Void doWithRetry(RetryContext context) throws IllegalAccessException {
					reload();
					TenantScopeInfo  tsi1 = getCurrentTenantScope();
					if(!tsi1.getId().equals(tenantId)) {
						throw new IllegalAccessException("After reloading tenants, the expected current context was not found." + tenantId +" Actual tenant" + tsi1.getId());
					}
					if(!tsi1.isOperable()) {
						throw new IllegalAccessException("Even after reloading the contexts from Master, TenantScopeInfo remains invalid. Tenant id" +tsi1.getId() );
					}
					return null;
				}

			});
			TenantScopeInfo  tsi1 = getCurrentTenantScope();
			if(!tsi1.getId().equals(tenantId)) {
				throw new IllegalAccessException("After reloading tenants, the expected current context was not found." + tenantId +" Actual tenant" + tsi1.getId());
			}
			if(!tsi1.isOperable()) {
				throw new IllegalAccessException("Even after reloading the contexts from Master, TenantScopeInfo remains invalid. Tenant id" +tsi1.getId() );
			}
			return tsi1;
		}
		
		return tsi;
	}
	
	
	private static RetryTemplate createRetryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		// Configure retry behavior
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1000L); // 1 second delay between retries
		retryTemplate.setBackOffPolicy(backOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3); // Retry a maximum of 3 times
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}

}
