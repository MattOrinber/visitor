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
@Table(name = "r_product_order")
public class RelationProductOrder {
	//raw attributes
	private Long rPuId;
	private Long rPuUserProductId;
	private Long rPuUserId;
	private Integer rPuStatus;
	private Integer rPuTotalAmount;
	private String rPuBookdatelistKey;
	private Date rPuBookdate;
	private Date rPuLastbookupdatedate;
	
	@Column(name = "r_pu_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getrPuId() {
		return rPuId;
	}
	public void setrPuId(Long rPuId) {
		this.rPuId = rPuId;
	}
	
	@NotNull
	@Column(name = "r_pu_user_product_id", nullable = false, precision = 20)
	public Long getrPuUserProductId() {
		return rPuUserProductId;
	}
	public void setrPuUserProductId(Long rPuUserProductId) {
		this.rPuUserProductId = rPuUserProductId;
	}
	
	@NotNull
	@Column(name = "r_pu_user_id", nullable = false, precision = 20)
	public Long getrPuUserId() {
		return rPuUserId;
	}
	public void setrPuUserId(Long rPuUserId) {
		this.rPuUserId = rPuUserId;
	}
	
	@NotNull
	@Column(name = "r_pu_status", nullable = false, precision = 2)
	public Integer getrPuStatus() {
		return rPuStatus;
	}
	public void setrPuStatus(Integer rPuStatus) {
		this.rPuStatus = rPuStatus;
	}
	
	@Length(max = 63)
	@Column(name = "r_pu_bookdatelist_key")
	public String getrPuBookdatelistKey() {
		return rPuBookdatelistKey;
	}
	public void setrPuBookdatelistKey(String rPuBookdatelistKey) {
		this.rPuBookdatelistKey = rPuBookdatelistKey;
	}
	
	@Column(name = "r_pu_bookdate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getrPuBookdate() {
		return rPuBookdate;
	}
	public void setrPuBookdate(Date rPuBookdate) {
		this.rPuBookdate = rPuBookdate;
	}
	
	@Column(name = "r_pu_lastbookupdatedate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getrPuLastbookupdatedate() {
		return rPuLastbookupdatedate;
	}
	public void setrPuLastbookupdatedate(Date rPuLastbookupdatedate) {
		this.rPuLastbookupdatedate = rPuLastbookupdatedate;
	}
	
	@NotNull
	@Column(name = "r_pu_totalamount", nullable = false, precision = 10)
	public Integer getrPuTotalAmount() {
		return rPuTotalAmount;
	}
	public void setrPuTotalAmount(Integer rPuTotalAmount) {
		this.rPuTotalAmount = rPuTotalAmount;
	}
}
