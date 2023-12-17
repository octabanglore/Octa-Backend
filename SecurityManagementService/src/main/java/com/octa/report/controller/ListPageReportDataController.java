package com.octa.report.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.octa.report.responce.ListPageResponce;
import com.octa.report.service.ListPageReportDataService;

@RestController
@RequestMapping("/api/v1/reportData")
public class ListPageReportDataController {
	
	@Autowired
	ListPageReportDataService listPageReportDataService;

	private static final Logger logger = LogManager.getLogger(ListPageReportDataController.class);

	 @GetMapping("/{listPageReportId}")
	public ResponseEntity<ListPageResponce> getListPageReportData(@PathVariable Long listPageReportId, @RequestBody Map<String, String> parametersMap) {
		 
		 if(null==listPageReportId) {
			 System.out.println("Data Grid Get List Page Report Data listPageReportId is null listPageReportId: "+listPageReportId);
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 }
		 
		 ListPageResponce ListPageResponce;
		try {
			ListPageResponce = listPageReportDataService.getListPageReportData(listPageReportId, parametersMap);
			 return new ResponseEntity<ListPageResponce>(ListPageResponce, HttpStatus.OK);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<ListPageResponce>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
