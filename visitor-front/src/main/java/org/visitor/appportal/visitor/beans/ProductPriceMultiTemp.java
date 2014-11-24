package org.visitor.appportal.visitor.beans;

public class ProductPriceMultiTemp {
	private String productIdStr;
	private String additionalPriceKeyStr;
	private String additionalPriceValue;
	public String getProductIdStr() {
		return productIdStr;
	}
	public void setProductIdStr(String productIdStr) {
		this.productIdStr = productIdStr;
	}
	public String getAdditionalPriceKeyStr() {
		return additionalPriceKeyStr;
	}
	public void setAdditionalPriceKeyStr(String additionalPriceKeyStr) {
		this.additionalPriceKeyStr = additionalPriceKeyStr;
	}
	public String getAdditionalPriceValue() {
		return additionalPriceValue;
	}
	public void setAdditionalPriceValue(String additionalPriceValue) {
		this.additionalPriceValue = additionalPriceValue;
	}
}
