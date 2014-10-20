/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.web.utils.SearchForm;

public class SiteValueSearchForm extends SearchForm<SiteValue> implements Serializable {
    private static final long serialVersionUID = 1L;
    private SiteValue siteValue = new SiteValue();
    private Site site = new Site();
    private List<Long> site_values;

    /**
     * The SiteValue instance used as an example.
     */
    public SiteValue getSiteValue() {
        return siteValue;
    }

	public List<Long> getSite_values() {
		return site_values;
	}

	public void setSite_values(List<Long> siteValues) {
		site_values = siteValues;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Override
	public Specification<SiteValue> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}