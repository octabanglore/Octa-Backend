package com.octa.report.responce;

import java.util.List;

import com.octa.report.defination.ReportFilter;

import lombok.Data;

@Data
public class ListPageReportFilters {
	
	List<ReportFilter> filters;

}
