package org.visitor.appportal.service.newsite.searchforms;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.web.utils.SearchForm;

public class ActivitySearchForm extends SearchForm<Product> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Specification<Product> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}
