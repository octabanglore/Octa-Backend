package com.octa.security.management.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octa.security.management.auth.UserAuthRequest;
import com.octa.security.management.auth.UserAuthResponse;
import com.octa.security.management.auth.UserPassRequestResponse;
import com.octa.security.management.auth.UserRegisterRequest;
import com.octa.security.management.auth.UserResetPasswordRequest;
import com.octa.security.management.entity.JwtAuthTkn;
import com.octa.security.management.entity.JwtTokenType;
import com.octa.security.management.entity.User;
import com.octa.security.management.repo.JwtTokenRepository;
import com.octa.security.management.repo.UserRepository;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthendicationService {
	private final UserRepository userRepository;
	private final JwtTokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtService;
	private final AuthenticationManager authenticationManager;
	

	@OctaTransaction
	public ResponseEntity<UserAuthResponse> register(Tenant t, UserRegisterRequest request) {
		try {
			User user = User.builder().firstName(request.getFirstname()).lastName(request.getLastname()).loginName(request.getLoginName())
					.emailAddress(request.getLoginName()).password(passwordEncoder.encode(request.getPassword()))
					// .roles(request.getRole())
					.build();
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK)
					.body(UserAuthResponse.builder().status(HttpStatus.OK).message("User created successfully").build());
		} catch(Exception ex){
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(UserAuthResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).errorMessage(ex.getMessage()).build());
		}
	}

	@OctaTransaction
	public ResponseEntity<UserAuthResponse> authendicateUser(Tenant t, UserAuthRequest request) {
		try {
			Map<String, Object> claim = new HashMap<>();
			claim.put("tenant", t.getId());
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			var user = userRepository.findByPrimaryEmailId(request.getEmail()).orElseThrow();
			revokeAllUserTokens(user);
			var jwtToken = jwtService.generateToken(claim,user);
			var refreshToken = jwtService.generateRefreshToken(user);
			saveUserToken(user, jwtToken);
			return ResponseEntity.status(HttpStatus.OK)
					.body(UserAuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmailAddress()).build());
		}  catch (LockedException ex) {
			return ResponseEntity.status(HttpStatus.LOCKED)
					.body(UserAuthResponse.builder().status(HttpStatus.LOCKED).errorCode(HttpStatus.LOCKED.toString()).errorMessage("User account is locked").build());
		} catch (DisabledException ex) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(UserAuthResponse.builder().status(HttpStatus.FORBIDDEN).errorCode(HttpStatus.FORBIDDEN.toString()).errorMessage("User account is disabled").build());
		} catch (AuthenticationException ex) {
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(UserAuthResponse.builder().status(HttpStatus.UNAUTHORIZED).errorCode(HttpStatus.UNAUTHORIZED.toString()).errorMessage(ex.getMessage()).build());
		}catch (Exception ex) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(UserAuthResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).errorMessage(ex.getMessage()).build());
		}
	}

	
	private void saveUserToken(User user, String jwtToken) {
		var token = JwtAuthTkn.builder().user(user).userToken(jwtToken).tokenType(JwtTokenType.BEARER).expired(false)
				.revoked(false).build();
		tokenRepository.save(token);
	}
	 

	private void revokeAllUserTokens(User user) {
		List<JwtAuthTkn> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.userRepository.findByPrimaryEmailId(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = UserAuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
	@OctaTransaction
	public ResponseEntity<UserPassRequestResponse> sendResetPasswordLink(Tenant t, UserResetPasswordRequest request) {
		try {
			LocalDateTime expirationDateTime =null;
			final User user = userRepository.findByUserName(request.getUsername());
			if(null == user) {
				return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserPassRequestResponse.builder().errorMessage("user not found").status(HttpStatus.NOT_FOUND).build());
			}
			expirationDateTime = LocalDateTime.now().plus(24, ChronoUnit.HOURS);
			final String passwordtoken = UUID.nameUUIDFromBytes(user.getUsername().getBytes()).toString();
			user.setResetPasswordToken(passwordtoken);
			user.setTokenExpiration(Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant()));
			userRepository.update(user);
			return  ResponseEntity.status(HttpStatus.OK).body(UserPassRequestResponse.builder().isEmailEnabled(false).userdetailtoken(passwordtoken).status(HttpStatus.OK).build());
		}catch (Exception ex) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(UserPassRequestResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).errorMessage(ex.getMessage()).build());
		}
	}
	@OctaTransaction
	public ResponseEntity<UserPassRequestResponse> updatePassword(Tenant t, UserResetPasswordRequest request) {
		try {
		String passwordToken = request.getUserdetailtoken();
		if(StringUtils.isEmpty(passwordToken)) {
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserPassRequestResponse.builder().errorMessage("Token not found from request").status(HttpStatus.NOT_FOUND).build());
		}
		User user = userRepository.findByPasswordToken(passwordToken);
		if(null == user) {
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserPassRequestResponse.builder().errorMessage("Token not found").status(HttpStatus.NOT_FOUND).build());
		}
		 LocalDateTime expiryDate = user.getTokenExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		 LocalDateTime currentDateTime = LocalDateTime.now();
		 
		 if (!currentDateTime.isBefore(expiryDate)) {
			 return  ResponseEntity.status(HttpStatus.GONE).body(UserPassRequestResponse.builder().errorMessage("Link has expired").status(HttpStatus.GONE).build());
		 }
		 
		 if(StringUtils.isEmpty(request.getPassword()) || StringUtils.isEmpty(request.getConfirmPassword())) {
			 return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPassRequestResponse.builder().errorMessage("Password and confirm password are required").status(HttpStatus.BAD_REQUEST).build());
		 }
		 
		 if(!request.getPassword().equals(request.getConfirmPassword())) {
			 return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPassRequestResponse.builder().errorMessage("Passwords do not match").status(HttpStatus.BAD_REQUEST).build()); 
		 }
		 user.setPassword(passwordEncoder.encode(request.getPassword()));
		 user.setResetPasswordToken(null);
		 user.setTokenExpiration(null);
		 userRepository.update(user);
		 return  ResponseEntity.status(HttpStatus.OK).body(UserPassRequestResponse.builder().status(HttpStatus.OK).message("Reset pasword updated successfully").build());
		} catch (Exception ex) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(UserPassRequestResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).errorMessage(ex.getMessage()).build());
		}
	}
	
}
