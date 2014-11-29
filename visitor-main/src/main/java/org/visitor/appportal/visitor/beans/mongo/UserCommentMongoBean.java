package org.visitor.appportal.visitor.beans.mongo;

import java.util.Date;

public class UserCommentMongoBean {
	private String from_user_email;
	private String to_user_email;
	private Date date;
	private String content;
	public String getFrom_user_email() {
		return from_user_email;
	}
	public void setFrom_user_email(String from_user_email) {
		this.from_user_email = from_user_email;
	}
	public String getTo_user_email() {
		return to_user_email;
	}
	public void setTo_user_email(String to_user_email) {
		this.to_user_email = to_user_email;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
