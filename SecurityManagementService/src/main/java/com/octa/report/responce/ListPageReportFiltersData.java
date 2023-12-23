package com.octa.report.responce;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ListPageReportFiltersData {
	
	String selectType; 
	
	List<Map<String, String>> filtersData;

}
