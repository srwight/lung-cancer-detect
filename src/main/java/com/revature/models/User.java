package com.revature.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="USERS")
public class User implements UserDetails {
	
	@Id //necessary for Hibernate to identify objects uniquely
	@Column(name="USER_ID", updatable = false, nullable = false)
	//More on @SequenceGenerator at: https://docs.oracle.com/javaee/5/api/javax/persistence/SequenceGenerator.html
	//name(required), optional: allocationSize, initialValue, sequenceName
	@GenericGenerator(name="U_SEQ_GEN", strategy="uuid2")
	//more on @GeneratedValue at https://www.objectdb.com/java/jpa/entity/generated
	@GeneratedValue(generator="U_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;
	
	//change name of col and apply constraints
	//@Column attributes can be found at https://docs.jboss.org/hibernate/jpa/2.1/api/javax/persistence/Column.html
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(nullable=true)
	private long homePhone;
	
	@Column(nullable=true)
	private long cellPhone;
	
	@Column(nullable=true)
	private long workPhone;
	
	@Column(nullable=true)
	private String email;
	
	@Transient
	private List<? extends GrantedAuthority> grantedAuthorities;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "fromUUID")
	private List<HasRole> hasRole = new ArrayList<>();
	
	@Column(nullable=false)
	private boolean accountNonExpired;
	
	@Column(nullable=false)
	private boolean accountNonLocked;
	
	@Column(nullable=false)
	private boolean credentialsNonExpired;
	
	@Column(nullable=false)
	private boolean enabled;
	
	public User() {}
	
	public User(String username, String password, List<? extends GrantedAuthority> grantedAuthorities,
			boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
		this.username = username;
		this.password = password;
		this.firstName = "N/A";
		this.lastName = "N/A";
		this.grantedAuthorities = grantedAuthorities;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}

	public User(String username, String password, String firstName, String lastName, long homePhone,
			long cellPhone, long workPhone, String email, List<? extends GrantedAuthority> grantedAuthorities,
			boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.homePhone = homePhone;
		this.cellPhone = cellPhone;
		this.workPhone = workPhone;
		this.email = email;
		this.grantedAuthorities = grantedAuthorities;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ;
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		
		if (id != other.id)
			return false;
		
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(long homePhone) {
		this.homePhone = homePhone;
	}

	public long getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(long cellPhone) {
		this.cellPhone = cellPhone;
	}

	public long getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(long workPhone) {
		this.workPhone = workPhone;
	}

	public List<HasRole> getHasRole() {
		return hasRole;
	}

	public void setHasRole(List<HasRole> hasRole) {
		this.hasRole = hasRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
