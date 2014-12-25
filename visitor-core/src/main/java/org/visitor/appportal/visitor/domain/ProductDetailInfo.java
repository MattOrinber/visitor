package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product_detail_info")
public class ProductDetailInfo {
	private Long pdiId;
	private Long pdiProductId;
	private String pdiOwnerEmail;
	private String pdiProductOverviewDetail;
	private String pdiProductExtraDirection;
	private String pdiProductExtraGuestAccess;
	private String pdiProductExtraGuestInteraction;
	private String pdiProductExtraHouseManual;
	private String pdiProductExtraHouseRule;
	private String pdiProductExtraNeighborhood;
	private String pdiProductExtraOtherNote;
	private String pdiProductExtraSpace;
	private String pdiProductExtraTransit;
	
	@Column(name = "pdi_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPdiId() {
		return pdiId;
	}
	public void setPdiId(Long pdiId) {
		this.pdiId = pdiId;
	}
	
	@NotNull
	@Column(name = "pdi_product_id", nullable = false, precision = 20)
	public Long getPriProductId() {
		return pdiProductId;
	}
	public void setPriProductId(Long pdiProductId) {
		this.pdiProductId = pdiProductId;
	}
	
	@Length(max = 64)
	@Column(name = "pdi_owner_email", nullable = false)
	public String getPdiOwnerEmail() {
		return pdiOwnerEmail;
	}
	public void setPdiOwnerEmail(String pdiOwnerEmail) {
		this.pdiOwnerEmail = pdiOwnerEmail;
	}
	
	@Lob
	@Column(name = "pdi_product_overview_detail",columnDefinition="TEXT")
	public String getPdiProductOverviewDetail() {
		return pdiProductOverviewDetail;
	}
	public void setPdiProductOverviewDetail(String pdiProductOverviewDetail) {
		this.pdiProductOverviewDetail = pdiProductOverviewDetail;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_direction")
	public String getPdiProductExtraDirection() {
		return pdiProductExtraDirection;
	}
	public void setPdiProductExtraDirection(String pdiProductExtraDirection) {
		this.pdiProductExtraDirection = pdiProductExtraDirection;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_guest_access")
	public String getPdiProductExtraGuestAccess() {
		return pdiProductExtraGuestAccess;
	}
	public void setPdiProductExtraGuestAccess(String pdiProductExtraGuestAccess) {
		this.pdiProductExtraGuestAccess = pdiProductExtraGuestAccess;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_guest_interaction")
	public String getPdiProductExtraGuestInteraction() {
		return pdiProductExtraGuestInteraction;
	}
	public void setPdiProductExtraGuestInteraction(
			String pdiProductExtraGuestInteraction) {
		this.pdiProductExtraGuestInteraction = pdiProductExtraGuestInteraction;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_house_manual")
	public String getPdiProductExtraHouseManual() {
		return pdiProductExtraHouseManual;
	}
	public void setPdiProductExtraHouseManual(String pdiProductExtraHouseManual) {
		this.pdiProductExtraHouseManual = pdiProductExtraHouseManual;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_house_rule")
	public String getPdiProductExtraHouseRule() {
		return pdiProductExtraHouseRule;
	}
	public void setPdiProductExtraHouseRule(String pdiProductExtraHouseRule) {
		this.pdiProductExtraHouseRule = pdiProductExtraHouseRule;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_neighborhood")
	public String getPdiProductExtraNeighborhood() {
		return pdiProductExtraNeighborhood;
	}
	public void setPdiProductExtraNeighborhood(String pdiProductExtraNeighborhood) {
		this.pdiProductExtraNeighborhood = pdiProductExtraNeighborhood;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_other_note")
	public String getPdiProductExtraOtherNote() {
		return pdiProductExtraOtherNote;
	}
	public void setPdiProductExtraOtherNote(String pdiProductExtraOtherNote) {
		this.pdiProductExtraOtherNote = pdiProductExtraOtherNote;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_space")
	public String getPdiProductExtraSpace() {
		return pdiProductExtraSpace;
	}
	public void setPdiProductExtraSpace(String pdiProductExtraSpace) {
		this.pdiProductExtraSpace = pdiProductExtraSpace;
	}
	
	@Length(max = 1023)
	@Column(name = "pdi_product_extra_transit")
	public String getPdiProductExtraTransit() {
		return pdiProductExtraTransit;
	}
	public void setPdiProductExtraTransit(String pdiProductExtraTransit) {
		this.pdiProductExtraTransit = pdiProductExtraTransit;
	}
}
