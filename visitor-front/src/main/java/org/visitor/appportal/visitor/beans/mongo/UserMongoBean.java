package org.visitor.appportal.visitor.beans.mongo;

import org.apache.commons.lang.StringUtils;

public class UserMongoBean extends BasicMongoBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1194598867125154413L;
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
	
	@Override
	public void convertToBSONObject() {
		if (StringUtils.isNotEmpty(user_email)) {
			this.put("user_email", user_email);
		}
		if (StringUtils.isNotEmpty(last_login_forward_ip)) {
			this.put("last_login_forward_ip", last_login_forward_ip);
		}
		if (StringUtils.isNotEmpty(user_description)) {
			this.put("user_description", user_description);
		}
	}
}
