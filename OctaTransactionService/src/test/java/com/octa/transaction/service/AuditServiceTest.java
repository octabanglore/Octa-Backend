package com.octa.transaction.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.octa.transaction.entity.Audit;
import com.octa.transaction.entity.Tenant;

@SpringBootTest
public class AuditServiceTest {
	
	@Autowired
	private AuditService auditService;
	
	@Test
	public void saveAuditTest() {
		Tenant t = new Tenant();
		t.setId(1L);
		Audit a = new Audit();
		a.setName("testuser");
		
		auditService.saveAudit(t, a);
		
	}

}
