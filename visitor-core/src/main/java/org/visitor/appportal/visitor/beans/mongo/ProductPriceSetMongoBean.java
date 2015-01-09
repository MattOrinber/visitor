package org.visitor.appportal.visitor.beans.mongo;

import org.apache.commons.lang.StringUtils;

public class ProductPriceSetMongoBean extends BasicMongoBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9066247757238869449L;
	
	private String productIdStr;
	private String keyStr;
	private String valueStr;

	public String getProductIdStr() {
		return productIdStr;
	}

	public void setProductIdStr(String productIdStr) {
		this.productIdStr = productIdStr;
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}
	
	@Override
	public void convertToBSONObject() {
		// TODO Auto-generated method stub
		if (StringUtils.isNotEmpty(productIdStr)) {
			this.put("productIdStr", productIdStr);
		}
		if (StringUtils.isNotEmpty(keyStr)) {
			this.put("keyStr", keyStr);
		}
		if (StringUtils.isNotEmpty(valueStr)) {
			this.put("valueStr", valueStr);
		}
	}

}
