package com.octa.report.query;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.octa.security.management.module.service.NativeQueryExecutorService;
import com.octa.transaction.entity.Tenant;

@Component
public class NativeQueryRepository {
	
	@Autowired
	NativeQueryExecutorService nativeQueryExecutorService;
	
	
    public List<Map<String, Object>> executeNativeQuery(String nativeQuery) {
    	System.out.println("Native Query executeNativeQuery nativeQuery:"+nativeQuery);
    	Tenant t = new Tenant();
		t.setId(1L);
    	return nativeQueryExecutorService.executeNativeQuery(t, nativeQuery);
    }
}