package com.revature.models;

import java.util.Date;
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
import javax.persistence.OneToOne;
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
@Table(name="HAS_ROLES")
public class HasRole {
	@Id
	@Column(name="HAS_ROLE_ID")
	@GenericGenerator(name="HR_SEQ_GEN", strategy="uuid2")
	@GeneratedValue(generator="HR_SEQ_GEN", strategy=GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="USER_ID")
	private User fromUUID;
	
	@OneToOne
	@JoinColumn(name="ROLE_ID")
	private Role toUUID;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="START_EFFECTIVE")
	private boolean startEffective;
	
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="END_EFFECTIVE")
	private boolean ENDEffective;

	public HasRole() {
	}
	
	public HasRole(User fromUUID, Role toUUID, Date startDate, boolean startEffective, Date endDate,
			boolean eNDEffective) {
		this.fromUUID = fromUUID;
		this.toUUID = toUUID;
		this.startDate = startDate;
		this.startEffective = startEffective;
		this.endDate = endDate;
		ENDEffective = eNDEffective;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public User getFromUUID() {
		return fromUUID;
	}

	public void setFromUUID(User fromUUID) {
		this.fromUUID = fromUUID;
	}

	public Role getToUUID() {
		return toUUID;
	}

	public void setToUUID(Role toUUID) {
		this.toUUID = toUUID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isStartEffective() {
		return startEffective;
	}

	public void setStartEffective(boolean startEffective) {
		this.startEffective = startEffective;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isENDEffective() {
		return ENDEffective;
	}

	public void setENDEffective(boolean eNDEffective) {
		ENDEffective = eNDEffective;
	}
}
