package com.revature.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name="PATIENT")
public class Patient {
	
//	For the proof of concept, maybe we start our database with a Patient table that has:
//		Patient ID
//		Patient name
//		Health of lungs
//
//		Kyle Storm Cloud  27 minutes ago
//		And another table for if a patient has nodules that contains:
//		nodule coordinates
//		Patient ID
//		This second table would be for the doctors.
	
	@Id
	@Column(name="PATIENT_ID")
	@GenericGenerator(name="P_SEQ_GEN", strategy="uuid2")
	@GeneratedValue(generator="P_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;
	
	@Column(name="PATIENT_FIRST_NAME", nullable=false)
	private String patientFirstname;
	
	@Column(name="PATIENT_LAST_NAME", nullable=false)
	private String patientLastname;
	
	@Column(name="PATIENT_ADDRESS", nullable=false)
	private String patientAddress;
	
	@Column(name="PATIENT_ADDRESS2", nullable=true)
	private String patientAddress2;
	
	@Column(name="PATIENT_STATE", nullable=false)
	private String patientState;
	
	@Column(name="PATIENT_POSTAL", nullable=false)
	private int patientPostal;
	
	@Column(name="HEALTH_OF_LUNGS", nullable=true)
	private int healthOfLungs;	
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "fromPatientUUID")
	private List<PatientScan> hasScan = new ArrayList<>();
	
	public Patient() {
	}

	public Patient(String patientFirstname, String patientLastname, String patientAddress, String patientAddress2,
			String patientState, int patientPostal) {
		this.patientFirstname = patientFirstname;
		this.patientLastname = patientLastname;
		this.patientAddress = patientAddress;
		this.patientAddress2 = patientAddress2;
		this.patientState = patientState;
		this.patientPostal = patientPostal;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPatientFirstname() {
		return patientFirstname;
	}

	public void setPatientFirstname(String patientFirstname) {
		this.patientFirstname = patientFirstname;
	}

	public String getPatientLastname() {
		return patientLastname;
	}

	public void setPatientLastname(String patientLastname) {
		this.patientLastname = patientLastname;
	}
	
	public int getHealthOfLungs() {
		return healthOfLungs;
	}

	public void setHealthOfLungs(int healthOfLungs) {
		this.healthOfLungs = healthOfLungs;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getPatientAddress2() {
		return patientAddress2;
	}

	public void setPatientAddress2(String patientAddress2) {
		this.patientAddress2 = patientAddress2;
	}

	public String getPatientState() {
		return patientState;
	}

	public void setPatientState(String patientState) {
		this.patientState = patientState;
	}

	public int getPatientPostal() {
		return patientPostal;
	}

	public void setPatientPostal(int patientPostal) {
		this.patientPostal = patientPostal;
	}

	public List<PatientScan> getHasScan() {
		return hasScan;
	}

	public void setHasScan(List<PatientScan> hasScan) {
		this.hasScan = hasScan;
	}
}
