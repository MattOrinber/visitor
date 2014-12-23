package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "user_internal_mail")
public class UserInternalMail {
	private Long uimId;
	private String uimFromUserMail;
	private String uimToUserMail;
	private Long uimProductId;
	private String uimContent;
	private Integer uimStatus;
	private Date uimCreateDate;
	
	@Column(name = "uim_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getUimId() {
		return uimId;
	}
	public void setUimId(Long uimId) {
		this.uimId = uimId;
	}
	
	@Length(max = 64)
	@Column(name = "uim_from_user_mail", nullable = false)
	public String getUimFromUserMail() {
		return uimFromUserMail;
	}
	public void setUimFromUserMail(String uimFromUserMail) {
		this.uimFromUserMail = uimFromUserMail;
	}
	
	@Length(max = 64)
	@Column(name = "uim_to_user_mail", nullable = false)
	public String getUimToUserMail() {
		return uimToUserMail;
	}
	public void setUimToUserMail(String uimToUserMail) {
		this.uimToUserMail = uimToUserMail;
	}
	
	@Column(name = "uim_product_id", nullable = false, precision = 20)
	public Long getUimProductId() {
		return uimProductId;
	}
	public void setUimProductId(Long uimProductId) {
		this.uimProductId = uimProductId;
	}
	
	@Length(max = 1023)
	@Column(name = "uim_content", nullable = false)
	public String getUimContent() {
		return uimContent;
	}
	public void setUimContent(String uimContent) {
		this.uimContent = uimContent;
	}
	
	@Column(name = "uim_status", nullable = false, precision = 2)
	public Integer getUimStatus() {
		return uimStatus;
	}
	public void setUimStatus(Integer uimStatus) {
		this.uimStatus = uimStatus;
	}
	
	@Column(name = "uim_create_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getUimCreateDate() {
		return uimCreateDate;
	}
	public void setUimCreateDate(Date uimCreateDate) {
		this.uimCreateDate = uimCreateDate;
	}
}
