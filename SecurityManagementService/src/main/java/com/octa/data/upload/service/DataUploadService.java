package com.octa.data.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.octa.security.management.module.service.NativeQueryExecutorService;
import com.octa.transaction.platform.OctaTransaction;

@Service
public class DataUploadService {
	
	@Autowired
	NativeQueryExecutorService nativeQueryExecutorService;

	
	public Workbook getWorkbookInstance(MultipartFile file) throws IOException {
		InputStream inputStream = file.getInputStream();
        return WorkbookFactory.create(inputStream);
    }
    
	@SuppressWarnings("unchecked")
	public Map readDataFromXLS(MultipartFile file) throws IOException {
		
		java.util.Map<String,Object> map = new HashMap<String,Object>();

		try{

			Workbook wb =getWorkbookInstance(file);
			Sheet sheet = wb.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			boolean isRowBalnk=true;

			int noOfRows = 0;
			java.util.Map<Integer,Map<String,String>> dataListMap = new HashMap<Integer,Map<String,String>>();
			for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){

				row = sheet.getRow(rowIndex);
				@SuppressWarnings("rawtypes")
				Map dataMap=new HashMap();
				isRowBalnk=true;
				if(row!=null){
					noOfRows++;
					if(row.getLastCellNum()<=0){
						continue;
					}
					String[] columnsData = new String[row.getLastCellNum()];
					for(int cellIndex=0;cellIndex<row.getLastCellNum();cellIndex++){
						if(noOfRows!=1){
							columnsData=(String[])map.get("columns");
							cell  = row.getCell(cellIndex);
							if(cellIndex<columnsData.length){
								if(cell!=null){
									String cellVal = getCellValue(wb.getCreationHelper().createFormulaEvaluator(), cell);
									dataMap.put(columnsData[cellIndex], cellVal);
									if(cellVal.length()>0){
										isRowBalnk = false;
									}
								}else{
									dataMap.put(columnsData[cellIndex], "");
								}
							}
						}else{
							cell  = row.getCell(cellIndex);
							if(cell!=null){
								if (cell.getCellType() == CellType.STRING && cell.getStringCellValue()!=null) {
									columnsData[cellIndex] = (String)cell.getStringCellValue().trim().toUpperCase();
									if(columnsData[cellIndex].trim().endsWith("(*)")){
										columnsData[cellIndex] =columnsData[cellIndex].substring(0,columnsData[cellIndex].length()-3);
									}
								}
							}
						}
					}
					if(noOfRows ==1 && columnsData!=null && columnsData.length!=0){
						map.put("columns", columnsData);
					}
					if(dataMap!=null && dataMap.size()!=0){
						if(!isRowBalnk){ 
							dataListMap.put(noOfRows, dataMap);
						}else{
							noOfRows = noOfRows-1;
						}
					}
				}
			}
			map.put("noOfRows", noOfRows);
			map.put("dataList", dataListMap);

			cell = null;
			row = null;
			sheet = null;
			wb = null;

		}catch(Exception ioe){
			ioe.printStackTrace();
		}
		return map;
	}
    
    private String getCellValue(FormulaEvaluator evaluator, Cell cell) {
    	String cellValue = "";
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("");
    	
    	if (cell != null) {
    		if (cell.getCellType() == CellType.NUMERIC) {
    			if (HSSFDateUtil.isCellDateFormatted(cell)) {
    				Date dt = cell.getDateCellValue();
    				cellValue = sdf.format(dt);
    			} else {
    				cellValue = String.valueOf(new Double(cell.getNumericCellValue()).doubleValue());
    				if (cellValue.indexOf('E')>0) {
    					try{
    						cellValue = String.valueOf(NumberToTextConverter.toText(cell.getNumericCellValue()));
    					}catch(Exception e){
    						cellValue = String.valueOf(new Double(cell.getNumericCellValue()).longValue());	
    					}
    				}
    				if (cellValue.endsWith(".0")) {
    					cellValue = cellValue.substring(0, cellValue.length() - 2);
    				}
    			}
    		} else if (cell.getCellType() == CellType.STRING) {
    			cellValue = cell.getStringCellValue();
    		} else if (cell.getCellType() == CellType.FORMULA) {
    			CellValue cellValueObj = evaluator.evaluate(cell);
    			if (cellValueObj.getCellType() == CellType.NUMERIC) {
    				cellValue = String.valueOf(new Double(cellValueObj.getNumberValue()).floatValue());
    				if (cellValue.indexOf('E')>0) {
    					cellValue = String.valueOf(new Double(cellValueObj.getNumberValue()).longValue());
    				}
    				if (cellValue.endsWith(".0")) {
    					cellValue = cellValue.substring(0, cellValue.length() - 2);
    				}
    			} else if (cellValueObj.getCellType() == CellType.STRING) {
    				cellValue = cellValueObj.getStringValue();
    			} else if (cellValueObj.getCellType() == CellType.BOOLEAN) {
    				cellValue = cellValueObj.getBooleanValue() + "";
    			} else {
    				cellValue = "";
    			}
    		} else if (cell.getCellType() == CellType.BOOLEAN) {
    			cellValue = cell.getBooleanCellValue() + "";
    		} else {
    			cellValue = "";
    		}
    	}
    	return cellValue;
    }
    
    @OctaTransaction
    public void insertData(String tableName, List<Map<String, Object>> data) {
    	
        String columns = String.join(", ", data.get(0).keySet());
        String values = String.join(", ", data.get(0).keySet().stream().map(key -> "?").toArray(String[]::new));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, values);

        try (Connection connection = nativeQueryExecutorService.getConnectionFromEntityManager();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int batchSize = 100;
            int count = 0;

            for (Map<String, Object> item : data) {
                int parameterIndex = 1;
                for (Object value : item.values()) {
                    preparedStatement.setObject(parameterIndex++, value);
                }
                preparedStatement.addBatch();

                if (++count % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL batch insert", e);
        }
    }

}
