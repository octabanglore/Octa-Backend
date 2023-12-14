package com.octa.report.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.octa.report.defination.ListColumn;
import com.octa.report.defination.ReportDefination;
import com.octa.report.query.NativeQueryService;
import com.octa.report.responce.ColumnDef;
import com.octa.report.responce.ListPageResponce;

@Service
public class ListPageReportDataService {
	
	@Autowired
	NativeQueryService nativeQueryService;
	

	public ListPageResponce getListPageReportData(Long listPageReportId, Map<String, String> parametersMap) throws JsonMappingException, JsonProcessingException {
		
		System.out.println("Get List Page Report Data Started for listPageReportId : "+listPageReportId);
		
		ListPageResponce listPageResponce = new ListPageResponce();
		
		Map<String, String> finalChildQueries = new LinkedHashMap<String, String>();
		
		ReportDefination reportDefination = nativeQueryService.getListReportDefination(listPageReportId);
		
		String reportQuery = reportDefination.getListQuery();
		
		Map<String, String> childQueries = reportDefination.getChildQueries();
		
		for (String key : childQueries.keySet()) {
			
			finalChildQueries.put(key, nativeQueryService.replaceValuesChildQuery(childQueries.get(key), parametersMap));
		}
		
		finalChildQueries.putAll(parametersMap);
		
		String finalReportQuery = nativeQueryService.replaceValuesMainQuery(reportQuery, finalChildQueries);
		
		List<String> applicableColumns = new ArrayList<String>();
		
		applicableColumns = getApplicableReportColumns(reportDefination.getListColumns());
		
		List<ColumnDef> columnDefList = prepareReportColumnDefination(reportDefination.getListColumns());
		
		List<Map<String, Object>> resultDataList = nativeQueryService.executeNativeQuery(finalReportQuery);
		
		listPageResponce.setRowData(resultDataList);
		listPageResponce.setColumnDefs(columnDefList);
		
		System.out.println("Get List Page Report Data Completed for listPageReportId : "+listPageReportId+"; Column Count :"+columnDefList.size()+"; Rows Count: "+resultDataList.size());
		
		return listPageResponce;
	}


	private List<ColumnDef> prepareReportColumnDefination(List<ListColumn> listColumns) {
		
		List<ColumnDef> columnDefList = new ArrayList<ColumnDef>();
		if(null!=listColumns && listColumns.size()>0) {
			for (ListColumn listColumn : listColumns) {
				ColumnDef columnDef = new ColumnDef();
				columnDef.setField(listColumn.getListColumnName());
				columnDef.setHeaderName(listColumn.getListColumnDescription());
				columnDef.setType(listColumn.getListColumnType());
				if(null!=listColumn.getListColumnType() && listColumn.getListColumnType().equalsIgnoreCase("checkbox")) {
					columnDef.setHeaderCheckboxSelection(true);
					columnDef.setCheckboxSelection(true);
				}else if(columnDef.getType().equalsIgnoreCase("radio")) {
					columnDef.setHeaderCheckboxSelection(false);
					columnDef.setCheckboxSelection(true);
				}
				columnDefList.add(columnDef);
			}
		}
		return columnDefList;
	}


	private List<String> getApplicableReportColumns(List<ListColumn> listColumns) {
		
		List<String> applicableColumns = new ArrayList<String>();
		if(null!=listColumns && listColumns.size()>0) {
			
			for (ListColumn listColumn : listColumns) {
				applicableColumns.add(listColumn.getListColumnName());
			}
		}
		return applicableColumns;
		
	}

}
