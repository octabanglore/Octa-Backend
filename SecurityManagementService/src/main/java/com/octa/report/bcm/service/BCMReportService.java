package com.octa.report.bcm.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.octa.report.bcm.util.BCMCustomReport;
import com.octa.report.bcm.util.ZipUtility;
import com.octa.security.management.module.service.NativeQueryExecutorService;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;
import net.sf.jasperreports.governors.MaxPagesGovernor;

@Service
public class BCMReportService {
	
	@Autowired
	NativeQueryExecutorService nativeQueryExecutorService;
	
	@Autowired
	ZipUtility zipUtility;
	
	
	public byte[] generateBcmReport(BCMReportType bcmReportType, String bcmReportFileName, Map<String, Object> parametersMap) throws SQLException, JRException {

		if(StringUtils.isBlank(bcmReportFileName)) {
			System.out.println("BCM Download generateBcmReport bcmReportFileName is Null or Empty bcmReportFileName:"+bcmReportFileName);
			return null;
		}

		if(null==bcmReportType) {
			System.out.println("BCM Download generateBcmReport bcmReportType is Null or Empty bcmReportType:"+bcmReportType);
			bcmReportType = BCMReportType.PDF;
		}

		BCMFileConfiguration bcmFileConfiguration = BCMFileConfiguration.getDefaultConfiguration();
		
		Date dateFld = new Date();
		//Random randomGenerator = new Random();
		Timestamp timeStamp = new Timestamp(dateFld.getTime());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
		String fileOutputPath = "";
		try {
			Random randomGenerator = new Random();
			String subPath = File.separator + "export" + File.separator+ simpleDateFormat.format(timeStamp).toString()+ randomGenerator.nextLong() + File.separator;
			fileOutputPath =  ResourceUtils.getFile("classpath:")+File.separator+"reports"+File.separator+"bcmexport"+subPath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JasperReport jasperReport = getBCMJasperReport(bcmReportFileName);
		
		List<Integer> selectedValues =   (List) parametersMap.get("selectedValues");
		System.out.println("selectedValues : "+selectedValues);
		for (Integer selectedValue : selectedValues) {
			parametersMap.put("processKey", selectedValue+"");
			return generateBcmReport(bcmReportType, jasperReport, parametersMap, bcmFileConfiguration, fileOutputPath, bcmReportFileName);
		}
		return null;
		//return zipUtility.downloadMultipleFiles(fileOutputPath);
	}
	
	@OctaTransaction
	public byte[] generateBcmReport(BCMReportType bcmReportType, JasperReport jasperReport, Map<String, Object> parametersMap, 
			BCMFileConfiguration bcmFileConfiguration, String fileOutputPath, String jasperFile) throws SQLException {
		Connection connection = null;

		try {

			/*List<Person> dataSource = new ArrayList<Person>();
            for (int i = 1; i < 10; i++) {
            	dataSource.add(new Person("A"+i, i, "C"+i));
			}

            JRDataSource jrDataSource = new JRBeanCollectionDataSource(dataSource);*/

			if(bcmReportType==BCMReportType.CSV || bcmReportType==BCMReportType.XML || bcmReportType==BCMReportType.XLS || bcmReportType==BCMReportType.XLSX ) {
				BCMCustomReport.unset();
				BCMCustomReport.loadData();
			}

			if(null!=bcmFileConfiguration && StringUtils.isNotBlank(bcmFileConfiguration.getMaxPages())){
				jasperReport.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES_ENABLED, Boolean.TRUE.toString());
				jasperReport.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES, String.valueOf(bcmFileConfiguration.getMaxPages()));
			}

			Tenant t = new Tenant();
			t.setId(1L);
			connection = nativeQueryExecutorService.getConnectionFromEntityManager(t);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, connection);
			
			
			File checkHiddenPath = new File(fileOutputPath);
			if (!checkHiddenPath.exists()) {
				checkHiddenPath.mkdirs();
			}
			
		    Random randomGenerator = new Random();
		    //String dateStr = "" +SecureRandomUtil.nextLong()+ (new Date().getTime());
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSSS");
		    String dateStr = "_" +simpleDateFormat.format(new Date())+"_"+(randomGenerator.nextLong()+"").substring(14);
		    String fileName = jasperFile+" "+ dateStr + "."+bcmReportType.name().toLowerCase();
		    fileName = fileName.replaceAll(" ", "_");
		    String filePath = fileOutputPath + fileName;
		    
		    /*downloadFile(jasperPrint, fileName, filePath);
		    
		    System.out.println("filePath : "+filePath);
		    if(null!=filePath) {
		    	return null;
		    }*/
		    
			switch (bcmReportType) {
			case PDF:
				return exportToPdf(jasperPrint, bcmFileConfiguration);
			case XLSX, XLS:
				/*if(!Boolean.parseBoolean(Mailer.getPropValue(jasper_excel_simple_content))) {
                        return exportExcelStream();
          	        }*/
				return BCMCustomReport.exportExcelStream().toByteArray();
			case HTML:
				return exportToHtml(jasperPrint);
			case CSV:
				return BCMCustomReport.exportCSVReportStream().toByteArray();
			case XML:
				return BCMCustomReport.exportXMLReportStream().toByteArray();
			default:
				throw new IllegalArgumentException("Unsupported report type: " + bcmReportType);
			}
		} catch (JRException e) {
			e.printStackTrace();
			throw new RuntimeException("Error generating report");
		}finally {
			if(null!=connection) {
				connection.close();
			}
			BCMCustomReport.unset();
		}
	}
	
	
	public static void downloadFile(JasperPrint jasperPrint, String fileType, String filePath) {
		try {
			if (jasperPrint != null) {
				if (filePath.contains(".html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint, filePath);
				} else if (filePath.contains(".xml")) {
					BCMCustomReport.exportXMLReport(filePath);
					BCMCustomReport.unset();
				} else if (filePath.contains(".pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
				} else if (filePath.contains(".xls")) {
					JRXlsxExporter exporter = new JRXlsxExporter();
					// render(exporter, jasperPrint, writer, filePath,session);
				} else if (filePath.contains(".xlsx")) {
					//render(new JRXlsxExporter(), jasperPrint, writer, filePath,session);
				} else if (filePath.contains(".csv")) {
					BCMCustomReport.exportCSVReport(filePath);
					BCMCustomReport.unset();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JasperReport getBCMJasperReport(String bcmReportFileName) throws JRException {
		
		if(StringUtils.isBlank(bcmReportFileName)) {
    		System.out.println("BCM Download getBCMJasperReport bcmReportFileName is Null or Empty bcmReportFileName:"+bcmReportFileName);
    		return null;
    	}
		
		System.out.println("BCM Download getBCMJasperReport Loading bcmReportFileName Location :"+"/reports/" + bcmReportFileName + ".jrxml");
		InputStream reportStream = getClass().getResourceAsStream("/reports/" + bcmReportFileName + ".jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		System.out.println("BCM Download getBCMJasperReport Loading Completed bcmReportFileName Location :"+"/reports/" + bcmReportFileName + ".jrxml");
		
		return jasperReport;
	}
    
	
    public static byte[] exportToPdf(JasperPrint jasperPrint, BCMFileConfiguration bcmFileConfiguration) throws JRException {
    	
		JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.encoding", bcmFileConfiguration.getEncoding());
		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, bos);
		
		return bos.toByteArray();

	}
    
    public static byte[] exportToCsv(JasperPrint jasperPrint) throws JRException {
    	
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleWriterExporterOutput(bos));
		exporter.exportReport();
		
		return bos.toByteArray();
	}

    public static byte[] exportToExcel(JasperPrint jasperPrint, BCMFileConfiguration bcmFileConfiguration) throws JRException {
    	
    	JRXlsxExporter exporter = new JRXlsxExporter();

		SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
		config.setOnePagePerSheet(bcmFileConfiguration.isOnePLoagePerSheet());
		config.setDetectCellType(Boolean.TRUE);
		config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);

		exporter.setConfiguration(config);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
		exporter.exportReport();
		
		return bos.toByteArray();
	}

    private byte[] exportToXml(JasperPrint jasperPrint) throws JRException {
    	
    	JRXmlExporter exporter = new JRXmlExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleXmlExporterOutput(new ByteArrayOutputStream()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.exportReport();
        return outputStream.toByteArray();
    }
    
    public static byte[] exportToHtml(JasperPrint jasperPrint) throws JRException {
		
    	HtmlExporter  exporter = new HtmlExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		exporter.setExporterOutput( new SimpleHtmlExporterOutput(bos));
		exporter.exportReport();
		return bos.toByteArray();
	}
    
    public ByteArrayOutputStream exportExcelStream() throws IOException {
    	if (BCMCustomReport.get() == null) {
    		System.out.println("No data found..");
    		System.out.println("BCM Download exportExcelStream No Data found to Export");
    		return null;
    	}
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	HSSFSheet sheet = workbook.createSheet("Sheet1");
    	int rowNum = 0;
    	int columnIndex = 0;
    	HSSFRow rowhead = sheet.createRow(rowNum++);

    	final List<List<Map>> mList = BCMCustomReport.get();
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
    }

    public void setCellValue(HSSFCell cell,Object v) {
    	if(v == null) {
    		cell.setCellValue("");	
    	} 
    	else if(v instanceof Integer) {
    		cell.setCellValue((Integer)v);
    	}
    	else if(v instanceof Double) {
    		cell.setCellValue((Double)v);
    	}
    	else {
    		cell.setCellValue(v+"");
    	}
    }	

}
