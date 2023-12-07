package com.octa.security.management.message.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="MST_LOCALE")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OctaLocale {

	@Id
	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="LANGUAGE_CODE")
	private String langCode;

	@Column(name="COUNTRY_CODE")
	private String countryCode;

	@Column(name="LOCALE_NAME")
	private String localeName;

	@Column(name="CREATED_USER_ID")
	private Long createdUserId;

	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="MODIFIED_USER_ID")
	private Long modifiedUserId;

	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name="ACTIVE")
	private Short active;
	
}
