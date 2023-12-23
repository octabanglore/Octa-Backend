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
import com.octa.report.responce.ListPageReportFilters;
import com.octa.report.responce.ListPageReportFiltersData;
import com.octa.report.service.ListPageFiltersService;

@RestController
@RequestMapping("/api/v1/reportfilters")
public class ListPageFiltersController {
	
	@Autowired
	ListPageFiltersService listPageFiltersService;

	private static final Logger logger = LogManager.getLogger(ListPageFiltersController.class);

	 @GetMapping("/{listPageReportId}")
	public ResponseEntity<ListPageReportFilters> getListPageReportFilters(@PathVariable Long listPageReportId, @RequestBody Map<String, String> parametersMap) {
		 
		 if(null==listPageReportId) {
			 System.out.println("Data Grid Get List Page Report Filters listPageReportId is null listPageReportId: "+listPageReportId);
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 }
		 
		 ListPageReportFilters listPageReportFilters;
		try {
			listPageReportFilters = listPageFiltersService.getListPageReportFilters(listPageReportId);
			 return new ResponseEntity<ListPageReportFilters>(listPageReportFilters, HttpStatus.OK);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<ListPageReportFilters>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	 
	 @GetMapping("/{listPageReportId}/data/{filterParamName}")
	public ResponseEntity<ListPageReportFiltersData> getListPageReportFiltersData(@PathVariable Long listPageReportId, @PathVariable String filterParamName, @RequestBody Map<String, String> parametersMap) {
		 
		 if(null==listPageReportId) {
			 System.out.println("Data Grid Get List Page Report Filters Data listPageReportId is null listPageReportId: "+listPageReportId);
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 }
		 
		 ListPageReportFiltersData listPageReportFiltersData;
		 
		try {
			listPageReportFiltersData = listPageFiltersService.getListPageReportFiltersData(listPageReportId, filterParamName, parametersMap);
			 return new ResponseEntity<ListPageReportFiltersData>(listPageReportFiltersData, HttpStatus.OK);
			 
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<ListPageReportFiltersData>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
