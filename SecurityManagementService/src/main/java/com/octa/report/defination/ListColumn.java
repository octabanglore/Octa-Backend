package com.octa.report.defination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListColumn {
	
	private String listColumnName;
	
	private String listColumnDescription;
	
	private String listColumnType;  //text, numericColumn, dateColumn, checkbox, currencyColumn, percentColumn
	
	private String listColumnFormat;
	
	private String listColumnVisible;
	
	private String localizationKey;
	

}
