/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.RecommendRuleAcross;
import org.visitor.appportal.web.utils.SearchForm;

public class RecommendRuleAcrossSearchForm extends SearchForm<RecommendRuleAcross> implements Serializable {
    private static final long serialVersionUID = 1L;
    private RecommendRuleAcross recommendRuleAcross = new RecommendRuleAcross();

    /**
     * The RecommendRuleAcross instance used as an example.
     */
    public RecommendRuleAcross getRecommendRuleAcross() {
        return recommendRuleAcross;
    }

	@Override
	public Specification<RecommendRuleAcross> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}