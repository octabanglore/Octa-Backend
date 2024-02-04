package com.octa.security.management.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.octa.security.management.entity.JwtAuthTkn;
import com.octa.transaction.entity.Tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	private final JwtTokenService jwtTokenService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		jwt = authHeader.substring(7);
		Long tenantId = jwtTokenService.extractTenant(jwt);
		Tenant tenant = new Tenant(tenantId);
		
		
		JwtAuthTkn userToken = jwtTokenService.findByToken(tenant,jwt).orElse(null);
		if (userToken != null) {
			userToken.setExpired(true);
			userToken.setRevoked(true);
			jwtTokenService.updateToken(tenant,userToken);
			SecurityContextHolder.clearContext();
		}
	}
}
