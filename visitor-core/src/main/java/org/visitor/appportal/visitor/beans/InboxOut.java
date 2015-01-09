package org.visitor.appportal.visitor.beans;

import org.visitor.appportal.visitor.domain.User;

public class InboxOut {
	private User userFrom;
	private Long daysFromNow;
	private String dateAndAccomodates;
	private Long productId; 
	private Double totalBasicPrice;
	private String contentStr;
	private Long uimId;
	
	public User getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}
	public Long getDaysFromNow() {
		return daysFromNow;
	}
	public void setDaysFromNow(Long daysFromNow) {
		this.daysFromNow = daysFromNow;
	}
	public String getDateAndAccomodates() {
		return dateAndAccomodates;
	}
	public void setDateAndAccomodates(String dateAndAccomodates) {
		this.dateAndAccomodates = dateAndAccomodates;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Double getTotalBasicPrice() {
		return totalBasicPrice;
	}
	public void setTotalBasicPrice(Double totalBasicPrice) {
		this.totalBasicPrice = totalBasicPrice;
	}
	public String getContentStr() {
		return contentStr;
	}
	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}
	public Long getUimId() {
		return uimId;
	}
	public void setUimId(Long uimId) {
		this.uimId = uimId;
	}
}
