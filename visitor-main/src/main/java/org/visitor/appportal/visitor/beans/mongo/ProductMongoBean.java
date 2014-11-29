package org.visitor.appportal.visitor.beans.mongo;

import org.apache.commons.lang.StringUtils;

public class ProductMongoBean extends BasicMongoBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4305059412724984466L;
	private String product_id;
	private String owner_email;
	
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
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	public String getOwner_email() {
		return owner_email;
	}
	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
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
		if (StringUtils.isNotEmpty(product_id))
		{
			this.put("product_id", product_id);
		}
		if (StringUtils.isNotEmpty(owner_email))
		{
			this.put("owner_email", owner_email);
		}
		if (StringUtils.isNotEmpty(product_overview_detail))
		{
			this.put("product_overview_detail", product_overview_detail);
		}
		if (StringUtils.isNotEmpty(productExtraInfoSpaceStr))
		{
			this.put("productExtraInfoSpaceStr", productExtraInfoSpaceStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoGuestAccessStr))
		{
			this.put("productExtraInfoGuestAccessStr", productExtraInfoGuestAccessStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoGuestInteractionStr))
		{
			this.put("productExtraInfoGuestInteractionStr", productExtraInfoGuestInteractionStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoNeighborhoodStr))
		{
			this.put("productExtraInfoNeighborhoodStr", productExtraInfoNeighborhoodStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoTransitStr))
		{
			this.put("productExtraInfoTransitStr", productExtraInfoTransitStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoOtherNoteStr))
		{
			this.put("productExtraInfoOtherNoteStr", productExtraInfoOtherNoteStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoHouseRuleStr))
		{
			this.put("productExtraInfoHouseRuleStr", productExtraInfoHouseRuleStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoHouseManualStr))
		{
			this.put("productExtraInfoHouseManualStr", productExtraInfoHouseManualStr);
		}
		if (StringUtils.isNotEmpty(productExtraInfoDirectionStr))
		{
			this.put("productExtraInfoDirectionStr", productExtraInfoDirectionStr);
		}
	}
}
