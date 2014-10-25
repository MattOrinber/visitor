package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "r_user_product")
public class RelationUserProduct {
	//raw attributes
	private Long rUpId;
	private Long rUpUserId;
	private Long rUpProductId;
	private Integer rUpStatus;
	private Integer rUpUserRole;
	private Date rUpPublishdate;
	private Date rUpLastUpdateDate;
	
	@Column(name = "r_up_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getrUpId() {
		return rUpId;
	}
	public void setrUpId(Long rUpId) {
		this.rUpId = rUpId;
	}
	
	@NotNull
	@Column(name = "r_up_user_id", nullable = false, precision = 20)
	public Long getrUpUserId() {
		return rUpUserId;
	}
	public void setrUpUserId(Long rUpUserId) {
		this.rUpUserId = rUpUserId;
	}
	
	@NotNull
	@Column(name = "r_up_product_id", nullable = false, precision = 20)
	public Long getrUpProductId() {
		return rUpProductId;
	}
	public void setrUpProductId(Long rUpProductId) {
		this.rUpProductId = rUpProductId;
	}
	
	@NotNull
	@Column(name = "r_up_status", nullable = false, precision = 2)
	public Integer getrUpStatus() {
		return rUpStatus;
	}
	public void setrUpStatus(Integer rUpStatus) {
		this.rUpStatus = rUpStatus;
	}
	
	@Column(name = "r_up_user_role", precision = 2)
	public Integer getrUpUserRole() {
		return rUpUserRole;
	}
	public void setrUpUserRole(Integer rUpUserRole) {
		this.rUpUserRole = rUpUserRole;
	}
	
	@Column(name = "r_up_publishdate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getrUpPublishdate() {
		return rUpPublishdate;
	}
	public void setrUpPublishdate(Date rUpPublishdate) {
		this.rUpPublishdate = rUpPublishdate;
	}
	
	@Column(name = "r_up_lastupdatedate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getrUpLastUpdateDate() {
		return rUpLastUpdateDate;
	}
	public void setrUpLastUpdateDate(Date rUpLastUpdateDate) {
		this.rUpLastUpdateDate = rUpLastUpdateDate;
	}
}
