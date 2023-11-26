package com.octa.security.management.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "ADMIN_MAP_USER_ROLE")
@Data
public class AdminMapUserRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7812389692212572145L;
	
	
	@EmbeddedId
    private AdminMapUserRoleKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Column(name = "CREATED_DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;

}
