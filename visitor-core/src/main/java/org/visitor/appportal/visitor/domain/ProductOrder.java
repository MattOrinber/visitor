package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_order")
public class ProductOrder {
	//raw attributes
	private Long orderId;
	private Long orderProductId;
	private String orderUserEmail;
	private Integer orderStatus;
	private Date orderStartDate;
	private Date orderEndDate;
	private Date orderCreateDate;
	private Date orderUpdateDate;
	private Double orderTotalAmount;
	private Long orderPayOrderId;
	private Double orderRemainAmount;
	private String orderCurrency;
	private String orderMultipriceIds;
	
	@Column(name = "order_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@NotNull
	@Column(name = "order_product_id", nullable = false, precision = 20)
	public Long getOrderProductId() {
		return orderProductId;
	}
	public void setOrderProductId(Long orderProductId) {
		this.orderProductId = orderProductId;
	}
	
	@Length(max = 64)
	@Column(name = "order_user_email", nullable = false)
	public String getOrderUserEmail() {
		return orderUserEmail;
	}
	public void setOrderUserEmail(String orderUserEmail) {
		this.orderUserEmail = orderUserEmail;
	}
	
	@Column(name = "order_status", nullable = false, precision = 2)
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	@Column(name = "order_start_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getOrderStartDate() {
		return orderStartDate;
	}
	public void setOrderStartDate(Date orderStartDate) {
		this.orderStartDate = orderStartDate;
	}
	
	@Column(name = "order_end_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getOrderEndDate() {
		return orderEndDate;
	}
	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}
	
	@Column(name = "order_create_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	
	@Column(name = "order_update_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getOrderUpdateDate() {
		return orderUpdateDate;
	}
	public void setOrderUpdateDate(Date orderUpdateDate) {
		this.orderUpdateDate = orderUpdateDate;
	}
	
	@Column(name="order_total_amount", precision = 22, scale=2)
	public Double getOrderTotalAmount() {
		return orderTotalAmount;
	}
	public void setOrderTotalAmount(Double orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}
	
	@Column(name = "order_pay_order_id", precision = 20)
	public Long getOrderPayOrderId() {
		return orderPayOrderId;
	}
	public void setOrderPayOrderId(Long orderPayOrderId) {
		this.orderPayOrderId = orderPayOrderId;
	}
	
	@Column(name="order_remain_amount", precision = 22, scale=2)
	public Double getOrderRemainAmount() {
		return orderRemainAmount;
	}
	public void setOrderRemainAmount(Double orderRemainAmount) {
		this.orderRemainAmount = orderRemainAmount;
	}
	
	@Length(max = 4)
	@Column(name="order_currency")
	public String getOrderCurrency() {
		return orderCurrency;
	}
	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}
	
	@Length(max = 511)
	@Column(name="order_multiprice_ids")
	public String getOrderMultipriceIds() {
		return orderMultipriceIds;
	}
	public void setOrderMultipriceIds(String orderMultipriceIds) {
		this.orderMultipriceIds = orderMultipriceIds;
	}
}
