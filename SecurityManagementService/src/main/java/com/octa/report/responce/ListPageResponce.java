package com.octa.report.responce;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ListPageResponce {

	private List<ColumnDef> columnDefs;
	
	private List<Map<String,Object>> rowData;


}
