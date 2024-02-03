package com.octa.modules.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.modules.dto.ListPageGroupsData;
import com.octa.modules.dto.ModulesData;
import com.octa.modules.service.ModuleService;
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
	
	private final TenantService tenantService; ;

	private static final Logger logger = LogManager.getLogger(ModuleController.class);
	
	@GetMapping(value = "/getModulesData")
	public ResponseEntity<List<ModulesData>> getModulesData() {
		logger.debug("modules : Get All Modules Data");
		System.out.println("Inside Get all orders");
		Tenant tenant = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		List<ModulesData> moduleDataList = moduleService.getModuleData(tenant);
		logger.debug("modules : Get All Modules Data Count :"+moduleDataList.size());
		return new ResponseEntity<>(moduleDataList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getModulesGroupData/{modileId}")
	public ResponseEntity<Map<String, List<ListPageGroupsData>>> getModulesGroupData(@PathVariable Long modileId) {
		logger.debug("modules : Get All Groupd and Listpage report Data");
		System.out.println("Inside Get All Groupd and Listpage report Data");
		Tenant tenant = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		List<ListPageGroupsData> listPageGroupsDataList = moduleService.getModulesGroupData(tenant, modileId);
		Map<String, List<ListPageGroupsData>> listPageGroupsDataMap = new HashMap<String, List<ListPageGroupsData>>();
		listPageGroupsDataMap.put("groups", listPageGroupsDataList);
		logger.debug("modules : Get All Groupd and Listpage report Data Count :"+listPageGroupsDataList.size());
		return new ResponseEntity<>(listPageGroupsDataMap, HttpStatus.OK);
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
	

}
