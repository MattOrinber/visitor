package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "city_info")
public class City {
	private Long cityId;
	private String cityName;
	private Integer cityStatus;
	
	@Column(name = "city_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	@Length(max = 255)
	@Column(name = "city_name", unique = true, nullable = false)
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@Column(name = "city_status", nullable = false, precision = 2)
	public Integer getCityStatus() {
		return cityStatus;
	}
	public void setCityStatus(Integer cityStatus) {
		this.cityStatus = cityStatus;
	}
}
