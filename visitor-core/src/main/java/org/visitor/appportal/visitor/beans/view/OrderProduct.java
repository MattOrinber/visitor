package org.visitor.appportal.visitor.beans.view;

import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOrder;

public class OrderProduct {
	private Product product;
	private ProductOrder order;
	private String productPicUrlFirst;
	private Integer remainSteps;
	
	private String orderBookDateStr;
	private String orderCreatedateStr;
	private String orderUserNameStr;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public ProductOrder getOrder() {
		return order;
	}
	public void setOrder(ProductOrder order) {
		this.order = order;
	}
	public String getProductPicUrlFirst() {
		return productPicUrlFirst;
	}
	public void setProductPicUrlFirst(String productPicUrlFirst) {
		this.productPicUrlFirst = productPicUrlFirst;
	}
	public Integer getRemainSteps() {
		return remainSteps;
	}
	public void setRemainSteps(Integer remainSteps) {
		this.remainSteps = remainSteps;
	}
	public String getOrderBookDateStr() {
		return orderBookDateStr;
	}
	public void setOrderBookDateStr(String orderBookDateStr) {
		this.orderBookDateStr = orderBookDateStr;
	}
	public String getOrderCreatedateStr() {
		return orderCreatedateStr;
	}
	public void setOrderCreatedateStr(String orderCreatedateStr) {
		this.orderCreatedateStr = orderCreatedateStr;
	}
	public String getOrderUserNameStr() {
		return orderUserNameStr;
	}
	public void setOrderUserNameStr(String orderUserNameStr) {
		this.orderUserNameStr = orderUserNameStr;
	}
}
