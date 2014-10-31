package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
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
import org.visitor.appportal.domain.Copyable;

@Entity
@Table(name = "user")
public class User implements Serializable, Copyable<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//raw attributes
	private Long userId;
	private String userEmail;
	private String userFacebookId;
	private Integer userType;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private Integer userGender; //0----male, 1----female
	private Date userBirthdate;
	private String userPhonenum;
	private String userAddress;
	private String userSchool;
	private String userWork;
	private Integer userTimeZone;
	private String userLanguage;
	private String userEmergency;
	private String userPhotourl;
	private Date userRegisterDate;
	private Date userLastLoginTime;
	private Integer userStatus;
	
	@Column(name = "user_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Length(max = 64)
	@Column(name = "user_email", nullable = false, unique = true)
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	@Length(max = 64)
	@Column(name = "user_facebookid")
	public String getUserFacebookId() {
		return userFacebookId;
	}
	public void setUserFacebookId(String userFacebookId) {
		this.userFacebookId = userFacebookId;
	}
	
	@Column(name = "user_type", nullable = false, precision = 4)
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	@Length(max = 1000)
	@Column(name = "user_password", nullable = false)
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	@Length(max = 32)
	@Column(name = "user_firstname")
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	
	@Length(max = 32)
	@Column(name = "user_lastname")
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	
	@Column(name = "user_gender", nullable = true, precision = 1)
	public Integer getUserGender() {
		return userGender;
	}
	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}
	
	@Column(name = "user_birthdate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getUserBirthdate() {
		return userBirthdate;
	}
	public void setUserBirthdate(Date userBirthdate) {
		this.userBirthdate = userBirthdate;
	}
	
	@Length(max = 32)
	@Column(name = "user_phonenum")
	public String getUserPhonenum() {
		return userPhonenum;
	}
	public void setUserPhonenum(String userPhonenum) {
		this.userPhonenum = userPhonenum;
	}
	
	@Length(max = 512)
	@Column(name = "user_address")
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
	@Length(max = 64)
	@Column(name = "user_school")
	public String getUserSchool() {
		return userSchool;
	}
	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}
	
	@Length(max = 64)
	@Column(name = "user_work")
	public String getUserWork() {
		return userWork;
	}
	public void setUserWork(String userWork) {
		this.userWork = userWork;
	}
	
	@Column(name = "user_timezone", nullable = true, precision = 2)
	public Integer getUserTimeZone() {
		return userTimeZone;
	}
	public void setUserTimeZone(Integer userTimeZone) {
		this.userTimeZone = userTimeZone;
	}
	
	@Length(max = 64)
	@Column(name = "user_language")
	public String getUserLanguage() {
		return userLanguage;
	}
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	
	@Length(max = 256)
	@Column(name = "user_emergency")
	public String getUserEmergency() {
		return userEmergency;
	}
	public void setUserEmergency(String userEmergency) {
		this.userEmergency = userEmergency;
	}
	
	@Length(max = 256)
	@Column(name = "user_photourl")
	public String getUserPhotourl() {
		return userPhotourl;
	}
	public void setUserPhotourl(String userPhotourl) {
		this.userPhotourl = userPhotourl;
	}
	
	@Column(name = "user_registerdate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	public Date getUserRegisterDate() {
		return userRegisterDate;
	}
	public void setUserRegisterDate(Date userRegisterDate) {
		this.userRegisterDate = userRegisterDate;
	}
	
	@Column(name = "user_lastlogintime", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	public Date getUserLastLoginTime() {
		return userLastLoginTime;
	}
	public void setUserLastLoginTime(Date userLastLoginTime) {
		this.userLastLoginTime = userLastLoginTime;
	}
	
	@Override
	public User copy() {
		// TODO Auto-generated method stub
		User userT = new User();
		copyTo(userT);
		return userT;
	}
	
	
	@Override
	public void copyTo(User t) {
		// TODO Auto-generated method stub
		t.setUserId(this.getUserId());
		t.setUserEmail(this.getUserEmail());
		t.setUserFacebookId(this.getUserFacebookId());
		t.setUserAddress(this.getUserAddress());
		t.setUserBirthdate(this.getUserBirthdate());
		t.setUserEmergency(this.getUserEmergency());
		t.setUserFirstName(this.getUserFirstName());
		t.setUserGender(this.getUserGender());
		t.setUserLanguage(this.getUserLanguage());
		t.setUserLastLoginTime(this.getUserLastLoginTime());
		t.setUserLastName(this.getUserLastName());
		t.setUserPassword(this.getUserPassword());
		t.setUserPhonenum(this.getUserPhonenum());
		t.setUserPhotourl(this.getUserPhotourl());
		t.setUserRegisterDate(this.getUserRegisterDate());
		t.setUserSchool(this.getUserSchool());
		t.setUserTimeZone(this.getUserTimeZone());
		t.setUserType(this.getUserType());
		t.setUserWork(this.getUserWork());
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("user.userId=[").append(this.getUserId()).append("],");
		result.append("user.userEmail=[").append(this.getUserEmail()).append("],");
		result.append("user.userFacebookId=[").append(this.getUserFacebookId()).append("],");
		result.append("user.userAddress=[").append(this.getUserAddress()).append("],");
		result.append("user.userBirthdate=[").append(this.getUserBirthdate()).append("],");
		result.append("user.userEmergency=[").append(this.getUserEmergency()).append("],");
		result.append("user.userFirstName=[").append(this.getUserFirstName()).append("],");
		result.append("user.userGender=[").append(this.getUserGender()).append("],");
		result.append("user.userLanguage=[").append(this.getUserLanguage()).append("],");
		result.append("user.userLastLoginTime=[").append(this.getUserLastLoginTime()).append("],");
		result.append("user.userLastName=[").append(this.getUserLastName()).append("],");
		result.append("user.userPassword=[").append(this.getUserPassword()).append("],");
		result.append("user.userPhonenum=[").append(this.getUserPhonenum()).append("],");
		result.append("user.userPhotourl=[").append(this.getUserPhotourl()).append("],");
		result.append("user.userRegisterDate=[").append(this.getUserRegisterDate()).append("],");
		result.append("user.userSchool=[").append(this.getUserSchool()).append("],");
		result.append("user.userTimeZone=[").append(this.getUserTimeZone()).append("],");
		result.append("user.userType=[").append(this.getUserType()).append("],");
		result.append("user.userWork=[").append(this.getUserWork()).append("],");
		return result.toString();
	}
	
	@Column(name = "user_status", nullable = false, precision = 2)
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
}
