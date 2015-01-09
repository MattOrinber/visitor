package org.visitor.appportal.visitor.beans.view;

import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.User;

public class CityProduct {
	private Product product;
	private User owner;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
}
