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
@Table(name="TENSORFLOW_NODULES")
public class Nodule {
	
	@Id
	@Column(name="ID")
	@GenericGenerator(name="N_SEQ_GEN", strategy="uuid2")
	@GeneratedValue(generator="N_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;
	
	@Column(name="NODULE_X", nullable=false)
	private int x;
	
	@Column(name="NODULE_Y", nullable=false)
	private int y;
	
	@Column(name="NODULE_Z", nullable=false)
	private int z;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
