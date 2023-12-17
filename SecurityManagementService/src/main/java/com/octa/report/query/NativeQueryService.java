package com.octa.report.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octa.report.defination.ReportDefination;
import com.octa.security.management.entity.AdminListpage;
import com.octa.security.management.module.service.AdminListPageService;
import com.octa.transaction.entity.Tenant;

@Service
public class NativeQueryService {
	
	@Autowired
	NativeQueryRepository nativeQueryRepository;
	
	@Autowired
	AdminListPageService adminListPageService;
	
	public static List<String> getNamesBetweenDelimiters(String query) {
        
		List<String> replaceParamList = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(.*?)#");
        Matcher matcher = pattern.matcher(query);

        while (matcher.find()) {
            String match = matcher.group(1); 
            replaceParamList.add(match);
        }

        return replaceParamList;
    }
	
	public String replaceValuesChildQuery(String query, Map<String, String> replacementParams) {
      
		Pattern pattern = Pattern.compile("#(.*?)#");

        Matcher matcher = pattern.matcher(query);

        StringBuffer finalQuery = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1); 
            
            if(null!=replacementParams && replacementParams.containsKey(key)) {
            	String replacement = replacementParams.get(key);
            	matcher.appendReplacement(finalQuery, replacement != null ? Matcher.quoteReplacement(replacement) : "");
            }else {
            	return "";
            }
            
        }
        matcher.appendTail(finalQuery);

        return finalQuery.toString();
    }
	
	public String replaceValuesMainQuery(String query, Map<String, String> replacementParams) {
	      
		Pattern pattern = Pattern.compile("#(.*?)#");

        Matcher matcher = pattern.matcher(query);

        StringBuffer finalQuery = new StringBuffer();

        while (matcher.find()) {
        	
        	String key = matcher.group(1); 
        	String replacement = replacementParams.get(key);
        	matcher.appendReplacement(finalQuery, replacement != null ? Matcher.quoteReplacement(replacement) : "");

        }
        matcher.appendTail(finalQuery);
        return finalQuery.toString();
    }
	
	public List<Map<String, Object>> executeNativeQuery(String query) {
		
		List<Map<String, Object>> resultDataList = new ArrayList<>();

		if(StringUtils.isNoneBlank(query)) {
			resultDataList = nativeQueryRepository.executeNativeQuery(query);
		}else {
			System.out.println("executeNativeQuery Query is Empty or Null");
		}
		return resultDataList;
	}
	
	public ReportDefination getListReportDefination(Long listPageId) throws JsonMappingException, JsonProcessingException {
		Tenant t = new Tenant();
		t.setId(1L);
		AdminListpage adminListpage = adminListPageService.getListPageById(t, listPageId);
		String listpageDefinition = adminListpage.getListpageDefinition();
		
		ObjectMapper objectMapper = new ObjectMapper();
		ReportDefination reportDefination = objectMapper.readValue(listpageDefinition, ReportDefination.class);

		return reportDefination;


	}

}
