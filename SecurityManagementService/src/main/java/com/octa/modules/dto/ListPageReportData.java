package com.octa.modules.dto;

import lombok.Data;

@Data
public class ListPageReportData {
	
	private Long reportId;
    private String reportName;
    private String reportIcon = "viewNewOrderIcon";
    
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
