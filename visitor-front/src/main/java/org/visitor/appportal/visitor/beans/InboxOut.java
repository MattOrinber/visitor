package org.visitor.appportal.visitor.beans;

import org.visitor.appportal.visitor.domain.User;

public class InboxOut {
	private User userFrom;
	private Long daysFromNow;
	private String dateAndAccomodates;
	private Long productId; 
	private Double totalBasicPrice;
	
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
}
