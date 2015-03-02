package org.visitor.apportal.web.entity.order;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.User;

/**
 * 订单明细VO
 * 
 * @author yong.cao
 * 
 */
public class OrderDetailVO {
	private Long orderId;
	private Integer orderStatus;
	private ProductOrder order;
	private Product product;
	private User seller;
	private User customer;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public ProductOrder getOrder() {
		return order;
	}

	public void setOrder(ProductOrder order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
