package com.octa.transaction.entity;


import java.util.Date;

import com.octa.transaction.platform.TenantScopeType;
import com.octa.transaction.platform.TenantAwareRequestContext.TenantScopeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name="ADMIN_TENANT")
@Data
public class Tenant implements TenantScopeInfo {
	
	private static final long serialVersionUID = 6540121493039396706L;
	@Id
	@Column(name="TENANT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="TENANT_NAME")
	private String name;
	
	@Column(name="TENANT_DESCRIPTION")
	private String description;
	
	@Column(name="LOGIN_RUL")
	private String loginUrl;
	
	@ManyToOne(fetch=FetchType.EAGER, optional = true)
	@JoinColumn(name="CONNECTION_ID", referencedColumnName = "CONNECTION_ID")
	private Connection connection;
	
	@Enumerated(EnumType.STRING)
	@Column(name= "TENANT_SCOPE")
	private TenantScopeType scopeType;
	
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	@Column(name="ACTIVE")
	private boolean active;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	public Tenant() {}
	
	public Tenant(Long id) {
		this.id= id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getCurrentSchemaName() {
		return connection.getSchemaName();
	}

	@Override
	public String getDatabaseURL() {
		return connection.getDatabaseUrl();
	}

	@Override
	public String getTenantName() {
		return name;
	}

	@Override
	public boolean isOperable() {
		return connection != null;
	}

	@Override
	public TenantScopeType getScopeType() {
		return scopeType;
	}

	@Override
	public Integer minPool() {
		return connection.getMinPoll();
	}

	@Override
	public Integer maxPool() {
		return connection.getMaxPool();
	}

}
