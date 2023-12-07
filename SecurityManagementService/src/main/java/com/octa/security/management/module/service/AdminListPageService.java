package com.octa.security.management.module.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.octa.security.management.entity.AdminListpage;
import com.octa.security.management.entity.AdminListpageGroup;
import com.octa.security.management.entity.AdminModule;
import com.octa.security.management.repo.AdminListPageDao;
import com.octa.security.management.repo.AdminListPageGroupDao;
import com.octa.security.management.repo.AdminModuleDao;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminListPageService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminListPageService.class);
	
	private final AdminListPageDao listPageRepository;
	
	private final AdminListPageGroupDao listPageGroupRepository;
	
	private final AdminModuleDao moduleRepository;
	
	@OctaTransaction
	public AdminListpage getListPageById(Tenant t, Long id) {
		return listPageRepository.get(id);
	}
	
	@OctaTransaction
	public AdminListpageGroup getListPageGroupById(Tenant t, Long id) {
		return listPageGroupRepository.get(id);
	}
	
	@OctaTransaction
	public AdminModule getModuleById(Tenant t, Long id) {
		return moduleRepository.get(id);
	}

}
