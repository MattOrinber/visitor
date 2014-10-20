/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Role;
import org.visitor.appportal.web.utils.SearchForm;

public class RoleSearchForm extends SearchForm<Role> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Role role = new Role();

    /**
     * The Role instance used as an example.
     */
    public Role getRole() {
        return role;
    }

	@Override
	public Specification<Role> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}