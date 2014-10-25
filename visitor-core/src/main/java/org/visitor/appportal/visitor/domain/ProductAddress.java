package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product_address")
public class ProductAddress {
	//raw attributes
	private Long paId;
	private Long paProductid;
	private Integer paCountry;
	private String paState;
	private String paCity;
	private String paZipcode;
	private String paStreetaddress;
	private String paDetail;
	
	@Column(name = "pa_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPaId() {
		return paId;
	}
	public void setPaId(Long paId) {
		this.paId = paId;
	}
	
	@NotNull
	@Column(name = "pa_product_id", nullable = false, precision = 20)
	public Long getPaProductid() {
		return paProductid;
	}
	public void setPaProductid(Long paProductid) {
		this.paProductid = paProductid;
	}
	
	@Column(name = "pa_country", precision = 4)
	public Integer getPaCountry() {
		return paCountry;
	}
	public void setPaCountry(Integer paCountry) {
		this.paCountry = paCountry;
	}
	
	@Length(max = 63)
	@Column(name = "pa_state")
	public String getPaState() {
		return paState;
	}
	public void setPaState(String paState) {
		this.paState = paState;
	}
	
	@Length(max = 31)
	@Column(name = "pa_city")
	public String getPaCity() {
		return paCity;
	}
	public void setPaCity(String paCity) {
		this.paCity = paCity;
	}
	
	@Length(max = 31)
	@Column(name = "pa_zipcode")
	public String getPaZipcode() {
		return paZipcode;
	}
	public void setPaZipcode(String paZipcode) {
		this.paZipcode = paZipcode;
	}
	
	@Length(max = 127)
	@Column(name = "pa_streetaddress")
	public String getPaStreetaddress() {
		return paStreetaddress;
	}
	public void setPaStreetaddress(String paStreetaddress) {
		this.paStreetaddress = paStreetaddress;
	}
	
	@Length(max = 255)
	@Column(name = "pa_detail")
	public String getPaDetail() {
		return paDetail;
	}
	public void setPaDetail(String paDetail) {
		this.paDetail = paDetail;
	}
}
