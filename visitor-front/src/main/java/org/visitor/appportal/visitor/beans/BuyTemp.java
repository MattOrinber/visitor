package org.visitor.appportal.visitor.beans;

public class BuyTemp {
	private String startDate;
	private String endDate;
	private String productIdStr;
	private String totalCountStr;
	
	private String orderIdStr;
	private String priceIdStr;
	private String priceAmount;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getProductIdStr() {
		return productIdStr;
	}
	public void setProductIdStr(String productIdStr) {
		this.productIdStr = productIdStr;
	}
	public String getOrderIdStr() {
		return orderIdStr;
	}
	public void setOrderIdStr(String orderIdStr) {
		this.orderIdStr = orderIdStr;
	}
	public String getPriceIdStr() {
		return priceIdStr;
	}
	public void setPriceIdStr(String priceIdStr) {
		this.priceIdStr = priceIdStr;
	}
	public String getPriceAmount() {
		return priceAmount;
	}
	public void setPriceAmount(String priceAmount) {
		this.priceAmount = priceAmount;
	}
	public String getTotalCountStr() {
		return totalCountStr;
	}
	public void setTotalCountStr(String totalCountStr) {
		this.totalCountStr = totalCountStr;
	}
}
