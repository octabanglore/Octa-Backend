package com.octa.report.bcm.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class BCMCustomReport {
	
	public static final ThreadLocal<List<List<Map>>> userThreadLocal = new ThreadLocal<>();
	  
	  public static final ThreadLocal<Boolean> isInitialized = new ThreadLocal<>();
	  
	  public static void exportXMLReport(String filePath) {
	    if (get() != null)
	      try {
	        List<List<Map>> mList = get();
	        Object key = null;
	        Document xmlDoc = null;
	        xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	        Element root = xmlDoc.createElement("rows");
	        xmlDoc.appendChild(root);
	        Element row = null;
	        Element col = null;
	        for (List<Map> element : mList) {
	          if (element == null)
	            continue; 
	          row = xmlDoc.createElement("row");
	          for (Map map : element) {
	            if (map == null)
	              continue; 
	            try {
	              key = map.keySet().iterator().next();
	              col = xmlDoc.createElement(key.toString());
	              col.appendChild(xmlDoc.createTextNode((map.get(key) != null) ? map.get(key).toString() : ""));
	              row.appendChild(col);
	            } catch (Exception e2) {
	              System.out.println("error while creating node : " + key);
	            } 
	          } 
	          root.appendChild(row);
	        } 
	        FileWriter fstream = new FileWriter(filePath);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(serializeXml(xmlDoc));
	        out.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }  
	  }
	  
	  public static ByteArrayOutputStream exportXMLReportStream() {
	    if (get() != null)
	      try {
	        List<List<Map>> mList = get();
	        Object key = null;
	        Document xmlDoc = null;
	        xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	        Element root = xmlDoc.createElement("rows");
	        xmlDoc.appendChild(root);
	        Element row = null;
	        Element col = null;
	        for (List<Map> element : mList) {
	          if (element == null)
	            continue; 
	          row = xmlDoc.createElement("row");
	          for (Map map : element) {
	            if (map == null)
	              continue; 
	            try {
	              key = map.keySet().iterator().next();
	              col = xmlDoc.createElement(key.toString());
	              col.appendChild(xmlDoc.createTextNode((map.get(key) != null) ? map.get(key).toString().trim() : ""));
	              row.appendChild(col);
	            } catch (Exception e2) {
	              System.out.println("error while creating node : " + key);
	            } 
	          } 
	          root.appendChild(row);
	        } 
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        bos.write(serializeXml(xmlDoc).getBytes());
	        return bos;
	      } catch (Exception e) {
	        e.printStackTrace();
	      }  
	    return null;
	  }
	  
	  public static void exportCSVReport(String filePath) {
	    if (get() != null)
	      try {
	        List<List<Map>> mList = get();
	        Object key = null;
	        FileWriter writer = new FileWriter(filePath);
	        boolean flag = true;
	        if (mList == null)
	          return; 
	        List<Map> hElement = mList.get(0);
	        if (hElement != null)
	          for (Map map : hElement) {
	            if (map == null)
	              continue; 
	            try {
	              key = map.keySet().iterator().next();
	              writer.append(String.valueOf(String.valueOf(key.toString())) + ",");
	            } catch (Exception e2) {
	              System.out.println("error while creating node : " + key);
	            } 
	          }  
	        writer.append('\n');
	        for (List<Map> element : mList) {
	          if (element == null)
	            continue; 
	          for (Map map2 : element) {
	            if (map2 == null)
	              continue; 
	            try {
	              key = map2.keySet().iterator().next();
	              writer.append(String.valueOf(String.valueOf((map2.get(key) != null) ? map2.get(key).toString().replaceAll(",", " ") : "")) + ",");
	            } catch (Exception e3) {
	              System.out.println("error while creating node : " + key);
	            } 
	          } 
	          writer.append('\n');
	        } 
	        writer.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }  
	  }
	  
	  public static ByteArrayOutputStream exportCSVReportStream() {
	    if (get() != null)
	      try {
	        ByteArrayOutputStream writer = new ByteArrayOutputStream();
	        List<List<Map>> mList = get();
	        Object key = null;
	        boolean flag = true;
	        if (mList == null)
	          return null; 
	        List<Map> hElement = mList.get(0);
	        if (hElement != null)
	          for (Map map : hElement) {
	            if (map == null)
	              continue; 
	            try {
	              key = map.keySet().iterator().next();
	              writer.write((String.valueOf(String.valueOf(key.toString())) + ",").getBytes());
	            } catch (Exception e2) {
	              System.out.println("error while creating node : " + key);
	            } 
	          }  
	        writer.write("\n".getBytes());
	        for (List<Map> element : mList) {
	          if (element == null)
	            continue; 
	          for (Map map2 : element) {
	            if (map2 == null)
	              continue; 
	            try {
	              key = map2.keySet().iterator().next();
	              String s = String.valueOf(String.valueOf((map2.get(key) != null) ? map2.get(key).toString().replaceAll(",", " ") : "")) + ",";
	              writer.write(s.getBytes());
	            } catch (Exception e3) {
	              System.out.println("error while creating node : " + key);
	            } 
	          } 
	          writer.write("\n".getBytes());
	        } 
	        return writer;
	      } catch (Exception e) {
	        e.printStackTrace();
	      }  
	    return null;
	  }
	  
	  public static ByteArrayOutputStream exportExcelStream() {
		  if (get() != null) {
			  try {
				  HSSFWorkbook workbook = new HSSFWorkbook();
				  HSSFSheet sheet = workbook.createSheet("Sheet1");
				  int rowNum = 0;
				  int columnIndex = 0;
				  HSSFRow rowhead = sheet.createRow(rowNum++);

				  final List<List<Map>> mList = get();
				  Object key = null;
				  // final FileWriter writer = new FileWriter(filePath);
				  final boolean flag = true;
				  if (mList == null) {
					  return null;
				  }
				  final List<Map> hElement = mList.get(0);
				  if (hElement != null) {
					  for (final Map map : hElement) {
						  key = map.keySet().iterator().next();
						  rowhead.createCell(columnIndex++).setCellValue(key + "");
					  }
				  }

				  for (final List<Map> element : mList) {
					  if (element == null) {
						  continue;
					  }
					  HSSFRow row = sheet.createRow(rowNum++);
					  columnIndex = 0;
					  for (final Map map2 : element) {

						  if (map2 == null) {
							  continue;
						  }

						  key = map2.keySet().iterator().next();
						  Object v = map2.get(key);
						  HSSFCell cell = row.createCell(columnIndex++);
						  setCellValue(cell, v);

					  }
				  }

				  ByteArrayOutputStream bos = new ByteArrayOutputStream();
				  workbook.write(bos);
				  return bos;
			  }catch (Exception e) {
				  e.printStackTrace();
			}
		  }
			return null;
		}
	  
	  public static void setCellValue(HSSFCell cell,Object v) {
		  if(v == null) {
			  cell.setCellValue("");	
		  } else if(v instanceof Integer) {
			  cell.setCellValue((Integer)v);
		  }else if(v instanceof Double) {
			  cell.setCellValue((Double)v);
		  }else {
			  cell.setCellValue(v+"");
		  }

	  }	
	  
	  /*private static String serializeXml(Document xml) {
	    StringWriter out = new StringWriter();
	    OutputFormat format = new OutputFormat(xml);
	    format.setIndenting(true);
	    XMLSerializer serializer = new XMLSerializer(out, format);
	    try {
	      serializer.asDOMSerializer();
	      serializer.serialize(xml);
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
	    return out.toString();
	  }*/
	  
	  private static String serializeXml(Document xml) {
	        try {
	            XmlMapper xmlMapper = new XmlMapper();
	            return xmlMapper.writeValueAsString(xml);
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error serializing XML", e);
	        }
	    }
	  
	  public static void loadData() {
	    isInitialized.set(Boolean.TRUE);
	  }
	  
	  public static Boolean isInitialized() {
		  if(null!=isInitialized && null!=isInitialized.get()) {
			  return (Boolean)isInitialized.get().booleanValue();
		  }else {
			  return false;
		  }
	  }
	  
	  public static void set(List<List<Map>> list) {
	    if (isInitialized.get() != null && ((Boolean)isInitialized.get()).booleanValue()) {
	    	 userThreadLocal.set(list);
	    }
	      
	  }
	  
	  public static void unset() {
	    userThreadLocal.remove();
	    isInitialized.remove();
	  }
	  
	  public static List<List<Map>> get() {
	    return userThreadLocal.get();
	  }

}
