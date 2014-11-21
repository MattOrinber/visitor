package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "floopy_thing")
public class FloopyThing {
	private Long floopyId;
	private String floopyKey;
	private String floopyValue;
	private Integer floopyStatus;
	
	@Column(name = "floopy_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getFloopyId() {
		return floopyId;
	}
	public void setFloopyId(Long floopyId) {
		this.floopyId = floopyId;
	}
	
	@Length(max = 63)
	@Column(name = "floopy_key", nullable = false, unique = true)
	public String getFloopyKey() {
		return floopyKey;
	}
	public void setFloopyKey(String floopyKey) {
		this.floopyKey = floopyKey;
	}
	
	@Length(max = 1023)
	@Column(name = "floopy_value", nullable = false, unique = false)
	public String getFloopyValue() {
		return floopyValue;
	}
	public void setFloopyValue(String floopyValue) {
		this.floopyValue = floopyValue;
	}
	
	@Column(name = "floopy_status", nullable = false, precision = 2)
	public Integer getFloopyStatus() {
		return floopyStatus;
	}
	public void setFloopyStatus(Integer floopyStatus) {
		this.floopyStatus = floopyStatus;
	}
	
	
}
