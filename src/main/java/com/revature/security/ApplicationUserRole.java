package com.revature.security;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.revature.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
	PATIENT(Sets.newHashSet(PATIENT_READ)),
	PRIMARY(Sets.newHashSet(PRIMARY_READ, PRIMARY_WRITE)),
	ONCOLOGIST(Sets.newHashSet(PATIENT_READ, ONCOLOGIST_READ, ONCOLOGIST_WRITE, ONCOLOGIST_UPLOAD)),
	RECEPTIONIST(Sets.newHashSet(RECEPTIONIST_READ, RECEPTIONIST_WRITE)),
	ADMIN(Sets.newHashSet(ADMIN_MANAGE));
	
	private final Set<ApplicationUserPermission> permissions;
	
	private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}
	
	public List<GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> permissions = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());
		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return permissions;
	}
}
