package com.octa.security.management.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class URLExtractorUtil {
	
	
	public String getCurrentRequestUrl() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return extractDomain(requestAttributes.getRequest().getRequestURL().toString());
	}
	
	public  String extractDomain(String fullUrl) {
		try {
			URL url = new URL(fullUrl);
			return url.getProtocol() + "://" + url.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace(); // Handle the exception according to your requirements
			return null;
		}
	}

}
