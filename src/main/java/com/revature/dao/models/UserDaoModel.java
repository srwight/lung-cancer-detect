package com.revature.dao.models;

import javax.persistence.Column;

import org.springframework.stereotype.Component;

@Component
public class UserDaoModel {
	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private long homePhone;
	
	private long cellPhone;
	
	private long workPhone;
	
	private String email;
	
	private String[] role;

	public UserDaoModel() {}
	
	public UserDaoModel(String username, String password, String firstName, String lastName, long homePhone,
			long cellPhone, long workPhone, String email, String ... role) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.homePhone = homePhone;
		this.cellPhone = cellPhone;
		this.workPhone = workPhone;
		this.email = email;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getRole() {
		return role;
	}

	public void setRole(String ... role) {
		this.role = role;
	}
}
