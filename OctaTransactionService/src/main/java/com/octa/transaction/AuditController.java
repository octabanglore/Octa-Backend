package com.octa.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.transaction.entity.Audit;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.service.AuditService;

@RestController
@RequestMapping("/audit")
public class AuditController {
	@Autowired
	private AuditService auditService;
	
	@GetMapping(value = "/save")
	public void saveAudit() {
		Tenant t = new Tenant();
		t.setId(1L);
		Audit a = new Audit();
		a.setName("tcdddd");
		
		auditService.saveAudit(t, a);
	}

}
