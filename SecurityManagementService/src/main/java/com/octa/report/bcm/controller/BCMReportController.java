package com.octa.report.bcm.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.report.bcm.service.BCMReportService;
import com.octa.report.bcm.service.BCMReportType;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/v1/bcmreports")
public class BCMReportController {
	
	@Autowired
    private BCMReportService bcmReportService;
	
	
	@PostMapping("/pdf")
	public ResponseEntity<byte[]> generateAndDownloadPdfReport(@RequestBody Map<String, Object> parametersMap) {
		
		System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.PDF);
		byte[] pdfBytes = null;
		try {
			pdfBytes = bcmReportService.generateBcmReport(BCMReportType.PDF, "Prince Purchase Order", parametersMap);
			//HttpHeaders headers = new HttpHeaders();
	        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=example.zip");
	       // headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

           // return createResponse(pdfBytes, headers);
			return createResponse(pdfBytes, "report.pdf", MediaType.APPLICATION_PDF);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED); 

	}

	@PostMapping("/csv")
    public ResponseEntity<byte[]> generateAndDownloadCsvReport(@RequestBody Map<String, Object> parametersMap) {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.CSV);
        byte[] csvBytes = null;
		try {
			csvBytes = bcmReportService.generateBcmReport(BCMReportType.CSV, "Prince Purchase Order", parametersMap);
			return createResponse(csvBytes, "report.csv", MediaType.TEXT_PLAIN);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED); 
    }

    @PostMapping("/xls")
    public ResponseEntity<byte[]> generateAndDownloadExcelReport(@RequestBody Map<String, Object> parametersMap) {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.XLSX);
        byte[] excelBytes = null;
		try {
			excelBytes = bcmReportService.generateBcmReport(BCMReportType.XLS, "Prince Purchase Order", parametersMap);
			return createResponse(excelBytes, "report.xls", MediaType.valueOf("application/vnd.ms-excel"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED); 
    }

    @PostMapping("/xml")
    public ResponseEntity<byte[]> generateAndDownloadXmlReport(@RequestBody Map<String, Object> parametersMap) {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.XML);
        byte[] xmlBytes = null;
		try {
			xmlBytes = bcmReportService.generateBcmReport(BCMReportType.XML, "Prince Purchase Order", parametersMap);
			return createResponse(xmlBytes, "report.xml", MediaType.APPLICATION_XML);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED); 
    }
    
    @PostMapping("/html")
    public ResponseEntity<byte[]> generateAndDownloadHtmlReport(@RequestBody Map<String, Object> parametersMap) {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.HTML);
        byte[] htmlBytes = null;
		try {
			htmlBytes = bcmReportService.generateBcmReport(BCMReportType.HTML, "Prince Purchase Order",parametersMap);
			return createResponse(htmlBytes, "report.html", MediaType.TEXT_HTML);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED); 
    }

    private ResponseEntity<byte[]> createResponse(byte[] content, String filename, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", filename);
        System.out.println("BCM Download generateBcmReport Completed Filename :"+filename);
        return ResponseEntity.ok().headers(headers).body(content);
    }
    
    private ResponseEntity<ByteArrayResource> createResponse(byte[] content, HttpHeaders headers) {
        System.out.println("BCM Download generateBcmReport Completed ");
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}
