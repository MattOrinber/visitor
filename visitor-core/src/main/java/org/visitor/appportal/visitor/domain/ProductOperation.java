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
@Table(name = "product_operation")
public class ProductOperation {
	//raw attributes
	private Long poOperationid;
	private Long poProductid;
	private Integer poType;
	private Date poStartDate;
	private Date poEndDate;
	private String poCreateby;
	private String poCurrency;
	private Double poPricePerNight;
	private String poNotice;
	private Integer poStatus;
	
	@Column(name = "po_operation_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPoOperationid() {
		return poOperationid;
	}
	public void setPoOperationid(Long poOperationid) {
		this.poOperationid = poOperationid;
	}
	
	@NotNull
	@Column(name = "po_product_id", nullable = false, precision = 20)
	public Long getPoProductid() {
		return poProductid;
	}
	public void setPoProductid(Long poProductid) {
		this.poProductid = poProductid;
	}
	
	@NotNull
	@Column(name = "po_type", nullable = false, precision = 2)
	public Integer getPoType() {
		return poType;
	}
	public void setPoType(Integer poType) {
		this.poType = poType;
	}
	
	@Column(name = "po_start_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getPoStartDate() {
		return poStartDate;
	}
	public void setPoStartDate(Date poStartDate) {
		this.poStartDate = poStartDate;
	}
	
	@Column(name = "po_end_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getPoEndDate() {
		return poEndDate;
	}
	public void setPoEndDate(Date poEndDate) {
		this.poEndDate = poEndDate;
	}
	
	@Length(max = 64)
	@Column(name = "po_create_by", nullable = false)
	public String getPoCreateby() {
		return poCreateby;
	}
	public void setPoCreateby(String poCreateby) {
		this.poCreateby = poCreateby;
	}
	
	@Length(max = 7)
	@Column(name = "po_currency", nullable = false)
	public String getPoCurrency() {
		return poCurrency;
	}
	public void setPoCurrency(String poCurrency) {
		this.poCurrency = poCurrency;
	}
	
	@Column(name = "po_price_per_night", nullable = false, precision = 22)
	public Double getPoPricePerNight() {
		return poPricePerNight;
	}
	public void setPoPricePerNight(Double poPricePerNight) {
		this.poPricePerNight = poPricePerNight;
	}
	
	@Length(max = 127)
	@Column(name = "po_notice")
	public String getPoNotice() {
		return poNotice;
	}
	public void setPoNotice(String poNotice) {
		this.poNotice = poNotice;
	}
	
	@NotNull
	@Column(name = "po_status", nullable = false, precision = 2)
	public Integer getPoStatus() {
		return poStatus;
	}
	public void setPoStatus(Integer poStatus) {
		this.poStatus = poStatus;
	}
}
