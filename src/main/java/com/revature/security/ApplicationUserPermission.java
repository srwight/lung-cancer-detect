package com.revature.security;

public enum ApplicationUserPermission {
	PATIENT_READ("patient:read"),
	RECEPTIONIST_READ("receptionist:read"),
	RECEPTIONIST_WRITE("receptionist:write"),
	PRIMARY_READ("primary:read"),
	PRIMARY_WRITE("primary:write"),
	ONCOLOGIST_READ("oncologist:read"),
	ONCOLOGIST_WRITE("oncologist:write"),
	ONCOLOGIST_UPLOAD("oncologist:upload"),
	ADMIN_MANAGE("admin:manage");
	
	private final String permission;
	
	public String getPermission() {
		return permission;
	}

	ApplicationUserPermission(String permission) {
		this.permission = permission;
	}
}
