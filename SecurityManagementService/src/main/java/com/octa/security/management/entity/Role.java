package com.octa.security.management.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "ADMIN_ROLE")
@Data
public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4656121508609859933L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;
	
	@Column(name = "role_name")
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;

    @Column(name = "active")
    private Integer active;

    @Column(name = "deleted")
    private Integer deleted;

}
