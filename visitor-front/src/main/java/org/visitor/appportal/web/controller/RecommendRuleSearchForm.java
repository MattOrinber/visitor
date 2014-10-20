/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.web.utils.SearchForm;

public class RecommendRuleSearchForm extends SearchForm<RecommendRule> implements Serializable {
    private static final long serialVersionUID = 1L;
    private RecommendRule recommendRule = new RecommendRule();

    /**
     * The RecommendRule instance used as an example.
     */
    public RecommendRule getRecommendRule() {
        return recommendRule;
    }

	@Override
	public Specification<RecommendRule> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}