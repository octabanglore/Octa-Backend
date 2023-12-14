package com.octa.report.defination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportLink {
	
	private String linktype;
	
	private String linkpath;
	
	private String linkColumn;
	

}
