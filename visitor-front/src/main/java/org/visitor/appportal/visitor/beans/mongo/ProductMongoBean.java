package org.visitor.appportal.visitor.beans.mongo;

public class ProductMongoBean extends BasicMongoBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4305059412724984466L;
	private Long product_id;
	private String owner_email;
	private String product_extrapriceset;
	
	private String product_overview_detail;
	
	private String productExtraInfoSpaceStr;
	private String productExtraInfoGuestAccessStr;

	private String productExtraInfoGuestInteractionStr;
	private String productExtraInfoNeighborhoodStr;
	private String productExtraInfoTransitStr;
	private String productExtraInfoOtherNoteStr;
	private String productExtraInfoHouseRuleStr;
	private String productExtraInfoHouseManualStr;
	private String productExtraInfoDirectionStr;
	
	public Long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}
	
	public String getOwner_email() {
		return owner_email;
	}
	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}
	
	public String getProduct_extrapriceset() {
		return product_extrapriceset;
	}
	public void setProduct_extrapriceset(String product_extrapriceset) {
		this.product_extrapriceset = product_extrapriceset;
	}
	
	public String getProduct_overview_detail() {
		return product_overview_detail;
	}
	public void setProduct_overview_detail(String product_overview_detail) {
		this.product_overview_detail = product_overview_detail;
	}
	
	public String getProductExtraInfoSpaceStr() {
		return productExtraInfoSpaceStr;
	}
	public void setProductExtraInfoSpaceStr(String productExtraInfoSpaceStr) {
		this.productExtraInfoSpaceStr = productExtraInfoSpaceStr;
	}
	public String getProductExtraInfoGuestAccessStr() {
		return productExtraInfoGuestAccessStr;
	}
	public void setProductExtraInfoGuestAccessStr(
			String productExtraInfoGuestAccessStr) {
		this.productExtraInfoGuestAccessStr = productExtraInfoGuestAccessStr;
	}
	public String getProductExtraInfoGuestInteractionStr() {
		return productExtraInfoGuestInteractionStr;
	}
	public void setProductExtraInfoGuestInteractionStr(
			String productExtraInfoGuestInteractionStr) {
		this.productExtraInfoGuestInteractionStr = productExtraInfoGuestInteractionStr;
	}
	public String getProductExtraInfoNeighborhoodStr() {
		return productExtraInfoNeighborhoodStr;
	}
	public void setProductExtraInfoNeighborhoodStr(
			String productExtraInfoNeighborhoodStr) {
		this.productExtraInfoNeighborhoodStr = productExtraInfoNeighborhoodStr;
	}
	public String getProductExtraInfoTransitStr() {
		return productExtraInfoTransitStr;
	}
	public void setProductExtraInfoTransitStr(String productExtraInfoTransitStr) {
		this.productExtraInfoTransitStr = productExtraInfoTransitStr;
	}
	public String getProductExtraInfoOtherNoteStr() {
		return productExtraInfoOtherNoteStr;
	}
	public void setProductExtraInfoOtherNoteStr(String productExtraInfoOtherNoteStr) {
		this.productExtraInfoOtherNoteStr = productExtraInfoOtherNoteStr;
	}
	public String getProductExtraInfoHouseRuleStr() {
		return productExtraInfoHouseRuleStr;
	}
	public void setProductExtraInfoHouseRuleStr(String productExtraInfoHouseRuleStr) {
		this.productExtraInfoHouseRuleStr = productExtraInfoHouseRuleStr;
	}
	public String getProductExtraInfoHouseManualStr() {
		return productExtraInfoHouseManualStr;
	}
	public void setProductExtraInfoHouseManualStr(
			String productExtraInfoHouseManualStr) {
		this.productExtraInfoHouseManualStr = productExtraInfoHouseManualStr;
	}
	public String getProductExtraInfoDirectionStr() {
		return productExtraInfoDirectionStr;
	}
	public void setProductExtraInfoDirectionStr(String productExtraInfoDirectionStr) {
		this.productExtraInfoDirectionStr = productExtraInfoDirectionStr;
	}
	
	@Override
	public void convertToBSONObject() {
		// TODO Auto-generated method stub
		super.convertToBSONObject();
	}
}
