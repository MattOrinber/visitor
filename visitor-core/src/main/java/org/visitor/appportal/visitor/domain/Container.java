package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "container")
public class Container {
	
	//raw attributes
	private Long containerId;
	private String containerName;
	private Date containerStartdate;
	private Integer containerLastdays;
	private String containerMoto;
	private String containerPicpaths;
	private String containerProductkey;
	
	@Column(name = "container_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getContainerId() {
		return containerId;
	}
	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}
	
	@Length(max = 63)
	@Column(name = "container_name")
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	@Column(name = "container_startdate", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getContainerStartdate() {
		return containerStartdate;
	}
	public void setContainerStartdate(Date containerStartdate) {
		this.containerStartdate = containerStartdate;
	}
	
	@Column(name = "container_lastdays", nullable = true, precision = 4)
	public Integer getContainerLastdays() {
		return containerLastdays;
	}
	public void setContainerLastdays(Integer containerLastdays) {
		this.containerLastdays = containerLastdays;
	}
	
	@Length(max = 63)
	@Column(name = "container_moto")
	public String getContainerMoto() {
		return containerMoto;
	}
	public void setContainerMoto(String containerMoto) {
		this.containerMoto = containerMoto;
	}
	
	@Length(max = 511)
	@Column(name = "container_picpaths")
	public String getContainerPicpaths() {
		return containerPicpaths;
	}
	public void setContainerPicpaths(String containerPicpaths) {
		this.containerPicpaths = containerPicpaths;
	}
	
	@Length(max = 31)
	@Column(name = "container_productkey")
	public String getContainerProductkey() {
		return containerProductkey;
	}
	public void setContainerProductkey(String containerProductkey) {
		this.containerProductkey = containerProductkey;
	}
}