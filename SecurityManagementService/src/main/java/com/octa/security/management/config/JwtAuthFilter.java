package com.octa.security.management.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.octa.security.management.repo.JwtTokenRepository;
import com.octa.security.management.service.JwtTokenService;
import com.octa.security.management.service.UserService;
import com.octa.transaction.entity.Tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtTokenService jwtService;
	private final UserService userDetailsService;
	private final JwtTokenRepository tokenRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		if (request.getServletPath().contains("/api/v1/auth")|| request.getServletPath().contains("/api/v1/i18") ) {
			filterChain.doFilter(request, response);
			return;
		}/*else if (request.getServletPath().contains("/api/v1/purchaseOrders/")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}else if (request.getServletPath().contains("/api/v1/modules/")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}else if (request.getServletPath().contains("/api/v1/bcmreports")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}else if (request.getServletPath().contains("/api/v1/reportData")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}else if (request.getServletPath().contains("/api/v1/reportfilters")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}else if (request.getServletPath().contains("/api/v1/uploadData")) {
			System.out.println("asas:"+request.getServletPath());
			filterChain.doFilter(request, response);
			return;
		}*/
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwt);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Long tenantId = jwtService.extractTenant(jwt);
			Tenant tenant = new Tenant(tenantId);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(tenant,userEmail);
			boolean isTokenValid = jwtService.findByToken(tenant,jwt).map(t -> !t.isExpired() && !t.isRevoked())
					.orElse(false);

			if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
