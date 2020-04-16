package com.revature.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Entity
@Table(name="ROLES")
public class Role {
	
	@Id
	@Column(name="ROLE_ID")
	@GenericGenerator(name="R_SEQ_GEN", strategy="uuid2")
	@GeneratedValue(generator="R_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;
	
	@Column(name="ROLE", nullable=false, unique=true)
	private String role;
	
	public Role() {
	}
	
	public Role(String role) {
		this.role = role;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
