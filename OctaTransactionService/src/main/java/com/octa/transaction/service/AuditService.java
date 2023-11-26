package com.octa.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octa.transaction.entity.Audit;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;
import com.octa.transaction.repo.AuditDao;

@Service
public class AuditService {
	
	@Autowired
	private AuditDao auditDao;
	
	@OctaTransaction
	public void saveAudit(Tenant t, Audit a) {
		System.out.println("getting Call here");
		auditDao.save(a);
	}

}
