package com.octa.security.management.entity;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADMIN_USER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
	
	
	private static final long serialVersionUID = 5185278137163764608L;
	
	@Id
	@Column(name="USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="LOGIN_NAME")
	private String loginName;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="EMAIL_ADDRESS")
	private String emailAddress;
	
	@Column(name="RESET_PASSWORD_TOKEN")
	private String resetPasswordToken;
	
	@Column(name="TOKEN_EXPIRATION")
	private Date tokenExpiration;
	
	/*
	 * @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) private
	 * Set<AdminMapUserRole> userRoles;
	 * 
	 * @Transient private Set<Role> roles;
	 */
	
	@Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "CREATED_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;

    @Column(name = "active", nullable = false)
    private int active = 1;

    @Column(name = "deleted", nullable = false)
    private int deleted = 0;

    @Column(name = "locked", nullable = false)
    private int locked = 0;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return loginName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	

}
