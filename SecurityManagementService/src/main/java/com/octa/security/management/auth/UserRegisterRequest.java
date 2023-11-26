package com.octa.security.management.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

	private String firstname;
	private String lastname;
	private String loginName;
	private String password;
	private String loginurl;
	//private Role role;

}
