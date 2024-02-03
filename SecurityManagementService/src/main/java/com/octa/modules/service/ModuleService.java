package com.octa.modules.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octa.modules.dto.ListPageGroupsData;
import com.octa.modules.dto.ListPageReportData;
import com.octa.modules.dto.ModulesData;
import com.octa.security.management.entity.AdminListpage;
import com.octa.security.management.entity.AdminListpageGroup;
import com.octa.security.management.entity.AdminModule;
import com.octa.security.management.module.service.AdminListPageService;
import com.octa.security.management.repo.AdminListPageGroupDao;
import com.octa.security.management.repo.AdminModuleDao;
import com.octa.transaction.entity.Tenant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuleService {
	
	@Autowired
	AdminListPageService adminListPageService;
	
	private final AdminModuleDao moduleRepository;
	
	private final AdminListPageGroupDao listPageGroupRepository;
	
	public List<ModulesData> getModuleData(Tenant tenant) {

		List<ModulesData> moduleDataList = new ArrayList<ModulesData>();
		
		List<AdminModule> modulesDataList = adminListPageService.getAllModules(tenant);

		for (AdminModule adminModule : modulesDataList) {
			moduleDataList.add(new ModulesData(adminModule.getModuleId(),adminModule.getModuleName(), adminModule.getModuleDescription(), "Inspect"));
		}
		return moduleDataList;
	}

	public Map<String, String> getTopbarData() {
		Map<String, String> topBarData = new HashMap<String, String>();

		topBarData.put("bulletins", "Bulletins");
		topBarData.put("help", "Help");
		topBarData.put("modules", "Modules");
		topBarData.put("tasks", "Tasks");
		topBarData.put("dashboard", "Dashboards");

		return topBarData;
	}

	public List<ListPageGroupsData> getModulesGroupData(Tenant tenant, Long modileId) {
		List<ListPageGroupsData> listPageGroupsDataList = new ArrayList<ListPageGroupsData>();
		List<AdminListpageGroup> adminListpageGroupList = adminListPageService.getAllModuleGroups(tenant, modileId);
		
		for (AdminListpageGroup adminListpageGroup : adminListpageGroupList) {
			ListPageGroupsData ListPageGroupsData = new ListPageGroupsData();
			ListPageGroupsData.setGroupId(adminListpageGroup.getListpageGroupId());
			ListPageGroupsData.setGroupName(adminListpageGroup.getListpageGroupName());
			//ListPageGroupsData.setGroupIcon(adminListpageGroup.ge);
			ListPageGroupsData.setReports(prepareListPageDataForGroupId(tenant, adminListpageGroup.getListpageGroupId()));
			listPageGroupsDataList.add(ListPageGroupsData);
		}

		return listPageGroupsDataList;
	}

	private List<ListPageReportData> prepareListPageDataForGroupId(Tenant tenant, Long listpageGroupId) {
		
		List<ListPageReportData> listPageReportDataList = new ArrayList<ListPageReportData>();
		
		if(null!=listpageGroupId) {
			List<AdminListpage> adminListpageList = adminListPageService.getAllListpageReportsByGroupId(tenant, listpageGroupId);
			for (AdminListpage adminListpage : adminListpageList) {
				ListPageReportData ListPageReportData = new ListPageReportData();
				ListPageReportData.setReportId(adminListpage.getListpageId());
				ListPageReportData.setReportName(adminListpage.getListpageName());
				listPageReportDataList.add(ListPageReportData);
			}
		}
		return listPageReportDataList;
		
	}
	
	
	
	
	
	

}
