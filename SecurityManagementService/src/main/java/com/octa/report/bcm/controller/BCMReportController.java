package com.octa.report.bcm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.report.bcm.service.BCMReportService;
import com.octa.report.bcm.service.BCMReportType;

@RestController
@RequestMapping("/api/v1/bcmreports")
public class BCMReportController {
	
	@Autowired
    private BCMReportService bcmReportService;
	
	
	@GetMapping("/pdf")
    public ResponseEntity<byte[]> generateAndDownloadPdfReport() {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.PDF);
        byte[] pdfBytes = bcmReportService.generateBcmReport(BCMReportType.PDF, "report_template");
        return createResponse(pdfBytes, "report.pdf", MediaType.APPLICATION_PDF);
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> generateAndDownloadCsvReport() {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.CSV);
        byte[] csvBytes = bcmReportService.generateBcmReport(BCMReportType.CSV, "report_template");
        return createResponse(csvBytes, "report.csv", MediaType.TEXT_PLAIN);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> generateAndDownloadExcelReport() {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.XLSX);
        byte[] excelBytes = bcmReportService.generateBcmReport(BCMReportType.XLSX, "report_template");
        return createResponse(excelBytes, "report.xlsx", MediaType.APPLICATION_OCTET_STREAM);
    }

    @GetMapping("/xml")
    public ResponseEntity<byte[]> generateAndDownloadXmlReport() {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.XML);
        byte[] xmlBytes = bcmReportService.generateBcmReport(BCMReportType.XML, "report_template");
        return createResponse(xmlBytes, "report.xml", MediaType.APPLICATION_XML);
    }
    
    @GetMapping("/html")
    public ResponseEntity<byte[]> generateAndDownloadHtmlReport() {
    	System.out.println("BCM Download generateBcmReport Started Doctype :"+BCMReportType.HTML);
        byte[] xmlBytes = bcmReportService.generateBcmReport(BCMReportType.HTML, "report_template");
        return createResponse(xmlBytes, "report.xml", MediaType.TEXT_HTML);
    }

    private ResponseEntity<byte[]> createResponse(byte[] content, String filename, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", filename);
        System.out.println("BCM Download generateBcmReport Completed Filename :"+filename);
        return ResponseEntity.ok().headers(headers).body(content);
    }
	

}
