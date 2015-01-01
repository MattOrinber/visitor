package org.visitor.appportal.visitor.beans.view;

import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOrder;

public class OrderProduct {
	private Product product;
	private ProductOrder order;
	private String productPicUrlFirst;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public ProductOrder getOrder() {
		return order;
	}
	public void setOrder(ProductOrder order) {
		this.order = order;
	}
	public String getProductPicUrlFirst() {
		return productPicUrlFirst;
	}
	public void setProductPicUrlFirst(String productPicUrlFirst) {
		this.productPicUrlFirst = productPicUrlFirst;
	}
}
