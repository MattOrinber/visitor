package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product_operation")
public class ProductOperation {
	//raw attributes
	private Long poOperationid;
	private Long poProductid;
	private Integer poType;
	private String poContent;
	private String poCreateby;
	
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
	@Column(name = "po_type", nullable = false, precision = 10)
	public Integer getPoType() {
		return poType;
	}
	public void setPoType(Integer poType) {
		this.poType = poType;
	}
	
	@NotNull
	@Column(name = "po_content", nullable = false)
	public String getPoContent() {
		return poContent;
	}
	public void setPoContent(String poContent) {
		this.poContent = poContent;
	}
	
	@Length(max = 60)
	@Column(name = "po_create_by")
	public String getPoCreateby() {
		return poCreateby;
	}
	public void setPoCreateby(String poCreateby) {
		this.poCreateby = poCreateby;
	}
}
