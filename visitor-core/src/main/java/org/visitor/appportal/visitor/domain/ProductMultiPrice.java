package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product_multi_price")
public class ProductMultiPrice {
	private Long pmpId;
	private Long pmpProductId;
	private String pmpProductPriceKey;
	private Double pmpProductPriceValue;
	private Integer pmpStatus;
	
	@Column(name = "pmp_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPmpId() {
		return pmpId;
	}
	public void setPmpId(Long pmpId) {
		this.pmpId = pmpId;
	}
	
	@NotNull
	@Column(name = "pmp_product_id", nullable = false, precision = 20)
	public Long getPmpProductId() {
		return pmpProductId;
	}
	public void setPmpProductId(Long pmpProductId) {
		this.pmpProductId = pmpProductId;
	}
	
	@Length(max = 127)
	@Column(name = "pmp_product_price_key", nullable = false)
	public String getPmpProductPriceKey() {
		return pmpProductPriceKey;
	}
	public void setPmpProductPriceKey(String pmpProductPriceKey) {
		this.pmpProductPriceKey = pmpProductPriceKey;
	}
	
	@Column(name = "pmp_product_price_value", precision = 22, scale=2)
	public Double getPmpProductPriceValue() {
		return pmpProductPriceValue;
	}
	public void setPmpProductPriceValue(Double pmpProductPriceValue) {
		this.pmpProductPriceValue = pmpProductPriceValue;
	}
	
	@Column(name = "pmp_status", precision = 2)
	public Integer getPmpStatus() {
		return pmpStatus;
	}
	public void setPmpStatus(Integer pmpStatus) {
		this.pmpStatus = pmpStatus;
	}
}
