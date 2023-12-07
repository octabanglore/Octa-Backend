package com.octa.security.management.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResetPasswordRequest {
	
	private String username;
	private String userdetailtoken;
	private String password;
	private String confirmPassword;
	

}
