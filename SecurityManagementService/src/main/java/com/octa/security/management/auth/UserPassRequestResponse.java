package com.octa.security.management.auth;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPassRequestResponse {
	
	private String statusCode;
	private HttpStatusCode status;
	private String userdetailtoken;
	private String errorMessage;
	private String errorCode;
	private String message;
	private boolean isEmailEnabled;
	

}
