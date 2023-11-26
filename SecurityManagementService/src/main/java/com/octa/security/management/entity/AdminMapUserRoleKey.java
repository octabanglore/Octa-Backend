package com.octa.security.management.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AdminMapUserRoleKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4328573542595890019L;
	
	
	@Column(name = "USER_ID")
    private int userId;

    @Column(name = "ROLE_ID")
    private int roleId;

}
