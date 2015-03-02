package org.visitor.apportal.web.entity.order;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.visitor.appportal.web.utils.OrderInfo;

/**
 * order展示VO
 * 
 * @author yong.cao
 *
 */
public class OrderVO {
	private Long orderId;
	private Long sellerId;
	private Long customerId;
	private String sellerName;
	private String customerName;
	private Date orderStartDate;
	private Date orderCreateDate;
	private Integer orderStatus;
	private String status;
	private String orderCurrency;
	private BigDecimal orderTotalAmount;
	private Integer orderTotalCount;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getOrderStartDate() {
		return orderStartDate;
	}
	public void setOrderStartDate(Date orderStartDate) {
		this.orderStartDate = orderStartDate;
	}
	public Date getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getStatus() {
		return OrderInfo.ProductOrderStatusEnum.getInstance(orderStatus).getDisplayName();
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderCurrency() {
		return orderCurrency;
	}
	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}
	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}
	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}
	public Integer getOrderTotalCount() {
		return orderTotalCount;
	}
	public void setOrderTotalCount(Integer orderTotalCount) {
		this.orderTotalCount = orderTotalCount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
