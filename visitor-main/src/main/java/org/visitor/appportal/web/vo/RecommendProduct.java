package org.visitor.appportal.web.vo;

import java.util.List;

import org.visitor.appportal.domain.ProductList;

public class RecommendProduct {

	public String name;
	public List<ProductList> productLists;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the productLists
	 */
	public List<ProductList> getProductLists() {
		return productLists;
	}
	/**
	 * @param productLists the productLists to set
	 */
	public void setProductLists(List<ProductList> productLists) {
		this.productLists = productLists;
	}
	
}
