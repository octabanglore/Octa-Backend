package com.octa.security.management.auth;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octa.security.management.service.AuthendicationService;
import com.octa.security.management.service.TenantService;
import com.octa.security.management.util.URLExtractorUtil;
import com.octa.transaction.entity.Tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
public class AuthendicationController {

	private final AuthendicationService authService;
	private final URLExtractorUtil urlUtil;
	private final TenantService tenantService; ;

	@PostMapping("/registeruser")
	public ResponseEntity<UserAuthResponse> register(@RequestBody UserRegisterRequest request) {
		Tenant t = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		return authService.register(t, request);
	}

	@PostMapping("/authenticateuser")
	public ResponseEntity<UserAuthResponse> authenticate(@RequestBody UserAuthRequest request) {
		Tenant t = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		return authService.authenticate(t,request);
	}

	@PostMapping("/refreshtoken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		authService.refreshToken(request, response);
	}
	
	@PostMapping("/userresetpasswordrequest")
	public ResponseEntity<UserPassRequestResponse> resetPasswordRequest(@RequestBody UserResetPasswordRequest request) {
		Tenant t = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		return authService.sendResetPasswordLink(t, request);
	}
	
	@PostMapping("/userresetpassword")
	public ResponseEntity<UserPassRequestResponse> updatePassword(@RequestBody UserResetPasswordRequest request) {
		Tenant t = tenantService.getbyLoginurl(urlUtil.getCurrentRequestUrl());
		return authService.updatePassword(t,request);
	}
	
	

}
