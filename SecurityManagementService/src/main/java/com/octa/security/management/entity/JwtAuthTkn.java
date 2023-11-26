package com.octa.security.management.entity;


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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="JWT_TOKEN")
public class JwtAuthTkn {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name="TOKEN_ID")
	  private Long id;

	  @Column(name="USER_TOKEN",unique = true)
	  private String userToken;

	  @Enumerated(EnumType.STRING)
	  @Column(name="TOKEN_TYPE")
	  private JwtTokenType tokenType;
	  
	  @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	  @Column(name="TOKEN_REVOKED")
	  private boolean revoked;

	  @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	  @Column(name="TOKEN_EXPIRED")
	  private boolean expired;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name ="USER_ID",referencedColumnName = "USER_ID")
	  private User user;

}
