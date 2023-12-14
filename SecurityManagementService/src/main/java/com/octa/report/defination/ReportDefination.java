package com.octa.report.defination;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDefination {
	
	private String listQuery;
	
	private String selectionType = "none"; //none, radio, checkbox
	
	private Map<String, String> childQueries;
	
	private List<ListColumn> listColumns;
	
	private List<ReportFilter> filters;
	
	private List<ReportLink> links;

}
