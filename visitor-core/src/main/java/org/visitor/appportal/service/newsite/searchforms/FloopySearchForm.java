package org.visitor.appportal.service.newsite.searchforms;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;
import org.visitor.appportal.visitor.domain.FloopyThing;
import org.visitor.appportal.web.utils.SearchForm;

public class FloopySearchForm extends SearchForm<FloopyThing> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Specification<FloopyThing> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}
