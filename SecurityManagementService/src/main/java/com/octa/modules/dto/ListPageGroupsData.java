package com.octa.modules.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListPageGroupsData {
	
	private Long groupId;
    private String groupName;
    private String groupIcon = "openOrdersIcon";
    private List<ListPageReportData> reports;
}
