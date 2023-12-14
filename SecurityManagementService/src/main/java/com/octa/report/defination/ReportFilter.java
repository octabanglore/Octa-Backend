package com.octa.report.defination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportFilter {
	
	private String filterName;
	
	private String filterType;
	
	private String filterParameter;
	
	private String filterQuery;
	
	private String filterSelect; // single/multiple;
	
	private String filterDefault;
	
	
}
