package com.octa.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.octa.report.defination.ReportDefination;
import com.octa.report.defination.ReportFilter;
import com.octa.report.query.NativeQueryService;
import com.octa.report.responce.ListPageReportFilters;
import com.octa.report.responce.ListPageReportFiltersData;

@Service
public class ListPageFiltersService {
	
	@Autowired
	NativeQueryService nativeQueryService;

	public ListPageReportFilters getListPageReportFilters(Long listPageReportId) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("Get List Page Report Filters Started for listPageReportId : "+listPageReportId);
		
		ReportDefination reportDefination = nativeQueryService.getListReportDefination(listPageReportId);
		
		List<ReportFilter> filters = reportDefination.getFilters();
		
		ListPageReportFilters listPageReportFilters = new ListPageReportFilters();
		
		listPageReportFilters.setFilters(filters);
		
		System.out.println("Get List Page Report Filters Completed for listPageReportId : "+listPageReportId+"; filters Size : "+filters);
		
		return listPageReportFilters;
	}

	public ListPageReportFiltersData getListPageReportFiltersData(Long listPageReportId, String filterParamName, Map<String, String> parametersMap) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("Get List Page Report Filters Data Started for listPageReportId : "+listPageReportId+"; filterParamName:"+filterParamName+"; parametersMap:"+parametersMap);
		
		List<Map<String, String>> resultFiltersDataList = new ArrayList<Map<String, String>>();
		
		ListPageReportFiltersData listPageReportFiltersData = new ListPageReportFiltersData();
		
		ReportDefination reportDefination = nativeQueryService.getListReportDefination(listPageReportId);
		
		List<ReportFilter> filters = reportDefination.getFilters();
		
		ReportFilter selectedFilter = null;
		
		for (ReportFilter reportFilter : filters) {
			if(reportFilter.getFilterParameter().equalsIgnoreCase(filterParamName)) {
				selectedFilter = reportFilter;
				break;
			}
		}
		
		if(selectedFilter!=null && StringUtils.isNoneBlank(selectedFilter.getFilterQuery())) {
			
			String filterQuery = selectedFilter.getFilterQuery();
			
			String finalReportQuery = nativeQueryService.replaceValuesMainQuery(filterQuery, parametersMap);
			
			resultFiltersDataList = nativeQueryService.executeFiltersNativeQuery(finalReportQuery);
			
			listPageReportFiltersData.setFiltersData(resultFiltersDataList);
		}
		
		System.out.println("Get List Page Report Filters Data Completed for listPageReportId : "+listPageReportId+"; filterParamName:"+filterParamName+"; parametersMap:"+parametersMap+"; Filter Values Count:"+listPageReportFiltersData.getFiltersData().size());
		
		return listPageReportFiltersData;
	}

}
