package com.octa.modules.controller;

import java.sql.SQLException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.modules.bean.Module;
import com.octa.modules.service.ModuleService;
import com.octa.security.management.module.service.NativeQueryExecutorService;
import com.octa.security.management.service.TenantService;
import com.octa.security.management.util.URLExtractorUtil;
import com.octa.transaction.entity.Tenant;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {
	
	@Autowired
	ModuleService moduleService;
	
	private final URLExtractorUtil urlUtil;
	
	private final TenantService tenantService;
	
	private final NativeQueryExecutorService executorService;

	private static final Logger logger = LogManager.getLogger(ModuleController.class);
	
	@GetMapping(value = "/getModulesData")
	public ResponseEntity<Map<String, Module>> getModulesData() {
		logger.debug("modules : Get All Modules Data");
		System.out.println("Inside Get all orders");
		Tenant tenant = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		Map<String, Module> moduleData = moduleService.getModuleData();
		logger.debug("modules : Get All Modules Data Count :"+moduleData.keySet().size());
		return new ResponseEntity<>(moduleData, HttpStatus.OK);
	}
	
	@GetMapping(value = "/topbarData")
	public ResponseEntity<Map<String, String>> getTopbarData() {
		logger.debug("modules : Get Top Bar Data");
		System.out.println("Inside Get all orders");
		Tenant tenant = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		Map<String, String> topBarData = moduleService.getTopbarData();
		logger.debug("modules : Get Top Bar Data");
		return new ResponseEntity<>(topBarData, HttpStatus.OK);
	}
	
	@GetMapping(value = "/extrattest")
	public void extrattest() throws SQLException {
		logger.debug("modules : extrattest");
		executorService.getConnectionFromEntityManager(new Tenant(1L));
	}
	

}
