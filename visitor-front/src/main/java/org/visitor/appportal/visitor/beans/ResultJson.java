package org.visitor.appportal.visitor.beans;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ResultJson {
	private Integer result;
	private String resultDesc;
	private String token;
	private String userEmail;
	private Date userLoginTime;
	private String userPicUrl;
	private String userName;
	private String imageUrl;
	
	private Long productId;
	private Integer productCan;
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	@JsonIgnore
	public Date getUserLoginTime() {
		return userLoginTime;
	}
	public void setUserLoginTime(Date userLoginTime) {
		this.userLoginTime = userLoginTime;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getUserPicUrl() {
		return userPicUrl;
	}
	public void setUserPicUrl(String userPicUrl) {
		this.userPicUrl = userPicUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getProductCan() {
		return productCan;
	}
	public void setProductCan(Integer productCan) {
		this.productCan = productCan;
	}
}
