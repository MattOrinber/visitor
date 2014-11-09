package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "timezone")
public class TimeZone {
	private Integer timeZoneId;
	private String timeZoneCity;
	private String timeZoneName;
	
	@Column(name = "timezone_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Integer getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(Integer timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	
	@Length(max = 31)
	@Column(name = "timezone_city", unique = true, nullable = false)
	public String getTimeZoneCity() {
		return timeZoneCity;
	}
	public void setTimeZoneCity(String timeZoneCity) {
		this.timeZoneCity = timeZoneCity;
	}
	
	@Length(max = 63)
	@Column(name = "timezone_name", nullable = false)
	public String getTimeZoneName() {
		return timeZoneName;
	}
	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
	}
}
