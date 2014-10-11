/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.web.utils.SearchForm;

public class PictureSearchForm extends SearchForm<Picture> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Picture picture = new Picture();

    /**
     * The Picture instance used as an example.
     */
    public Picture getPicture() {
        return picture;
    }

	@Override
	public Specification<Picture> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}