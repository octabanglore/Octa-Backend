package com.octa.security.management.message.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="I18_Labels")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Labels {
	
	@Id
	@Column(name="LABEL_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="ENTITY_NAME")
	private String entityName;

	@Column(name="LABEL_CODE")
	private String labelCode;

	@Column(name="LABEL_VALUE")
	private String labelValue;
	
	@ManyToOne
	@JoinColumn(name="LOCALE_CODE")
	private OctaLocale locale;

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
