package com.octa.security.management.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.octa.security.management.entity.User;
import com.octa.security.management.repo.UserRepository;
import com.octa.transaction.entity.Tenant;
import com.octa.transaction.platform.OctaTransaction;
import com.octa.transaction.platform.TenantAwareRequestContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TenantAwareRequestContext.setTenantScope(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getId());
		return userRepo.findOneEntityByAttribute("loginName", username);
	}
	
	@OctaTransaction
	public User saveUserDetail(Tenant t, User user) {
		return userRepo.save(user);
	}
	
	@OctaTransaction
	public UserDetails loadUserByUsername(Tenant t, String username) {
		return userRepo.findOneEntityByAttribute("loginName", username);
	}

}
