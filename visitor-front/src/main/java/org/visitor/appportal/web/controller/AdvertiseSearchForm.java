/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.web.utils.SearchForm;

public class AdvertiseSearchForm extends SearchForm<Advertise> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Advertise advertise = new Advertise();

    /**
     * The Picture instance used as an example.
     */
    public Advertise getAdvertise() {
        return advertise;
    }

	@Override
	public Specification<Advertise> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}