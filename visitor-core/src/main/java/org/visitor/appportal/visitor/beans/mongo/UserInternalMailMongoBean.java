package org.visitor.appportal.visitor.beans.mongo;

public class UserInternalMailMongoBean {
	private Long target_product_id;
	private Integer status;
	private String from_user_email;
	private String to_user_email;
	private String content;
	public Long getTarget_product_id() {
		return target_product_id;
	}
	public void setTarget_product_id(Long target_product_id) {
		this.target_product_id = target_product_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
