package com.revature.models;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="PATIENT_SCANS")
public class PatientScan {
	
//	nodule coordinates
//	Patient ID
	
	@Id
	@Column(name="DIAGNOSIS_ID")
	@GenericGenerator(name="PS_SEQ_GEN", strategy="uuid2")
	@GeneratedValue(generator="PS_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;

	@Column(name="DIAGNOSIS_STATUS")
	private String diagnosis;
	
	@Column(name="CONFIDENCE_STATUS")
	private int confidence;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="TO_NODULE")
	private Nodule Nodule;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="PATIENT_ID")
	private Patient fromPatientUUID;
	
	public PatientScan() {
	}
	
	public PatientScan(String diagnosis, int confidence, com.revature.models.Nodule nodule,
			Patient fromPatientUUID) {
		this.diagnosis = diagnosis;
		this.confidence = confidence;
		Nodule = nodule;
		this.fromPatientUUID = fromPatientUUID;
	}
	
	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public Nodule getNodule() {
		return Nodule;
	}

	public void setNodule(Nodule nodule) {
		Nodule = nodule;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Patient getFromPatientUUID() {
		return fromPatientUUID;
	}

	public void setFromPatientUUID(Patient fromPatientUUID) {
		this.fromPatientUUID = fromPatientUUID;
	}
}
