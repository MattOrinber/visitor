package org.visitor.appportal.visitor.beans.mongo;

public class UserMongoBean {
	private String user_email;
	private String last_login_forward_ip;
	private String user_description;
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getLast_login_forward_ip() {
		return last_login_forward_ip;
	}
	public void setLast_login_forward_ip(String last_login_forward_ip) {
		this.last_login_forward_ip = last_login_forward_ip;
	}
	public String getUser_description() {
		return user_description;
	}
	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}
}
