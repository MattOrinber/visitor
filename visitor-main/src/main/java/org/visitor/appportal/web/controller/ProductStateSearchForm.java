/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductState;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductStateSearchForm extends SearchForm<ProductState> implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductState productState = new ProductState();

    /**
     * The ProductState instance used as an example.
     */
    public ProductState getProductState() {
        return productState;
    }

	@Override
	public Specification<ProductState> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}