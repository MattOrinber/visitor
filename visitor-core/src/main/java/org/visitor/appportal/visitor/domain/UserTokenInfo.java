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

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "user_facebook_info")
public class UserTokenInfo {
	private Long ufiId;
	private Long ufiUserId;
	private String ufiUserEmail;
	private String ufiAuthCode;
	private String ufiAccessToken;
	private Date ufiExpireDate;
	private String ufiDetailUrl;
	
	@Column(name = "ufi_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getUfiId() {
		return ufiId;
	}
	public void setUfiId(Long ufiId) {
		this.ufiId = ufiId;
	}
	
	@NotNull
	@Column(name = "ufi_user_id", nullable = false, precision = 20)
	public Long getUfiUserId() {
		return ufiUserId;
	}
	public void setUfiUserId(Long ufiUserId) {
		this.ufiUserId = ufiUserId;
	}
	
	@Length(max = 64)
	@Column(name = "ufi_user_email", nullable = false, unique = true)
	public String getUfiUserEmail() {
		return ufiUserEmail;
	}
	public void setUfiUserEmail(String ufiUserEmail) {
		this.ufiUserEmail = ufiUserEmail;
	}
	
	@Length(max = 511)
	@Column(name = "ufi_auth_code")
	public String getUfiAuthCode() {
		return ufiAuthCode;
	}
	public void setUfiAuthCode(String ufiAuthCode) {
		this.ufiAuthCode = ufiAuthCode;
	}
	
	@Length(max = 255)
	@Column(name = "ufi_access_token")
	public String getUfiAccessToken() {
		return ufiAccessToken;
	}
	public void setUfiAccessToken(String ufiAccessToken) {
		this.ufiAccessToken = ufiAccessToken;
	}
	
	@Column(name = "ufi_expire_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	public Date getUfiExpireDate() {
		return ufiExpireDate;
	}
	public void setUfiExpireDate(Date ufiExpireDate) {
		this.ufiExpireDate = ufiExpireDate;
	}
	
	@Length(max = 127)
	@Column(name = "ufi_detail_url")
	public String getUfiDetailUrl() {
		return ufiDetailUrl;
	}
	public void setUfiDetailUrl(String ufiDetailUrl) {
		this.ufiDetailUrl = ufiDetailUrl;
	}
}
