package com.octa.report.responce;

import lombok.Data;

@Data
public class ColumnDef {
	
	private String field;
	
    private String headerName;
    
    private String type;
    
    private boolean headerCheckboxSelection = false;
    
    private boolean headerCheckboxSelectionFilteredOnly = false;
    
    private boolean checkboxSelection = false;
    
    private boolean rowDrag = false;
    
    private boolean hide = false;

}
