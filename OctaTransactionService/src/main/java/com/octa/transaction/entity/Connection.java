package com.octa.transaction.entity;

import java.io.Serializable;

import com.octa.transaction.platform.TenantScopeType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="admin_connection")
@Data
public class Connection implements Serializable{
	
	private static final long serialVersionUID = 4661108381464862116L;
	
	@Id
	@Column(name="CONNECTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="CONNECTION_NAME")
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name="CONNECTION_TYPE")
	private TenantScopeType connectionType;
	
	@Column(name="CONNECTION_URL")
	private String databaseUrl;
	
	@Column(name="SCHEMA_NAME")
	private String schemaName;
	
	@Column(name="MIN_POOL")
	private Integer minPoll;
	
	@Column(name="MAX_POOL")
	private Integer maxPool;
	
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	@Column(name="ACTIVE")
	private Boolean active;
	
	
	

}
