/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class SiteSearchForm extends SearchForm<Site> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Site site = new Site();
    private Integer templateType;

    /**
     * The Site instance used as an example.
     */
    public Site getSite() {
        return site;
    }

    //------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");
    private DateRangeUtil modDateRange = new DateRangeUtil("modDate");

    /**
     * The {@link DateRangeUtil} for the createDate attribute.
     */
    public DateRangeUtil getCreateDateRange() {
        return createDateRange;
    }

    /**
     * The {@link DateRangeUtil} for the modDate attribute.
     */
    public DateRangeUtil getModDateRange() {
        return modDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getCreateDateRange());
        result.add(getModDateRange());
        return result;
    }

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	@Override
	public Specification<Site> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}