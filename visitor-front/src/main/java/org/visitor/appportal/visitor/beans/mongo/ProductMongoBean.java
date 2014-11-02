package org.visitor.appportal.visitor.beans.mongo;

public class ProductMongoBean {
	private Long product_id;
	private String owner_email;
	private String product_extrapriceset;
	private String product_overview_detail;
	private String product_more_detail;
	public Long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}
	public String getOwner_email() {
		return owner_email;
	}
	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}
	public String getProduct_extrapriceset() {
		return product_extrapriceset;
	}
	public void setProduct_extrapriceset(String product_extrapriceset) {
		this.product_extrapriceset = product_extrapriceset;
	}
	public String getProduct_overview_detail() {
		return product_overview_detail;
	}
	public void setProduct_overview_detail(String product_overview_detail) {
		this.product_overview_detail = product_overview_detail;
	}
	public String getProduct_more_detail() {
		return product_more_detail;
	}
	public void setProduct_more_detail(String product_more_detail) {
		this.product_more_detail = product_more_detail;
	}
}
