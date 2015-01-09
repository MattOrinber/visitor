package org.visitor.appportal.visitor.beans.mongo;

import java.util.Date;

public class ProductCommentMongoBean {
	private String from_user_email;
	private Long target_product_id;
	private Date date;
	private String content;
	public String getFrom_user_email() {
		return from_user_email;
	}
	public void setFrom_user_email(String from_user_email) {
		this.from_user_email = from_user_email;
	}
	public Long getTarget_product_id() {
		return target_product_id;
	}
	public void setTarget_product_id(Long target_product_id) {
		this.target_product_id = target_product_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
