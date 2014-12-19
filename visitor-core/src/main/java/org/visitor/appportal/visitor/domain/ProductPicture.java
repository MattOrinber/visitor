package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product_picture")
public class ProductPicture {
	private Long productPicId;
	private Long productPicProductId;
	private String productPicProductUrl;
	private Integer productPicStatus;
	
	@Column(name = "product_pic_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductPicId() {
		return productPicId;
	}
	public void setProductPicId(Long productPicId) {
		this.productPicId = productPicId;
	}
	
	@Column(name = "product_pic_product_id", nullable = false, precision = 20)
	public Long getProductPicProductId() {
		return productPicProductId;
	}
	public void setProductPicProductId(Long productPicProductId) {
		this.productPicProductId = productPicProductId;
	}
	
	@Length(max = 255)
	@Column(name = "product_pic_product_url", nullable = false)
	public String getProductPicProductUrl() {
		return productPicProductUrl;
	}
	public void setProductPicProductUrl(String productPicProductUrl) {
		this.productPicProductUrl = productPicProductUrl;
	}
	
	@Column(name = "product_pic_status", nullable = false, precision = 2)
	public Integer getProductPicStatus() {
		return productPicStatus;
	}
	public void setProductPicStatus(Integer productPicStatus) {
		this.productPicStatus = productPicStatus;
	}
}
