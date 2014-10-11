/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Account;
import org.visitor.appportal.web.utils.SearchForm;

public class AccountSearchForm extends SearchForm<Account> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Account account = new Account();

    /**
     * The Account instance used as an example.
     */
    public Account getAccount() {
        return account;
    }

	@Override
	public Specification<Account> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}