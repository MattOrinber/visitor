package org.visitor.appportal.web.utils;

import static org.visitor.appportal.web.utils.NullRestriction.NullRestrictionKind.IS_NOT_NULL;
import static org.visitor.appportal.web.utils.NullRestriction.NullRestrictionKind.IS_NULL;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.isNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.repository.base.SearchTemplate;


public abstract class SearchForm<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private SearchParameters searchParameters = new SearchParameters();

    /**
     * @return the {@link SearchParameters} controlling search meta attributes (order, pagination, etc.)
     */
    public SearchParameters getSp() {
        return searchParameters;
    }

    /**
     * Override it in subclass in order to provide specific {@link DateRange} criteria to search.
     */
    protected List<DateRange> getDateRanges() {
        return new ArrayList<DateRange>();
    }
    
    /**
     * Override it in subclass in order to provide specific {@link NullRestriction} criteria in search.
     */
    protected List<NullRestriction> getNullRestrictions() {
        return new ArrayList<NullRestriction>();
    }
    

	public Pageable getPageable() {
		if(this.searchParameters.hasSortColumnKey()) {
			Sort sort = null;
			if(StringUtils.equalsIgnoreCase(searchParameters.getSortOrder(), SearchParameters.ASCENDING)) {
				sort = new Sort(Sort.Direction.ASC, searchParameters.getSortColumnKey());
			} else if(StringUtils.equalsIgnoreCase(searchParameters.getSortOrder(), SearchParameters.DESCENDING)) {
				sort = new Sort(Sort.Direction.DESC, searchParameters.getSortColumnKey());
			}
			return this.getPageable(sort);
		} else {
			return new PageRequest(getSp().getPageNumber() - 1, getSp().getPageSize()); 
		}
	}
	
	public Pageable getPageable(Sort sort) {
		return new PageRequest(getSp().getPageNumber() - 1, getSp().getPageSize(), sort);
	}
	
	public abstract Specification<T> toSpecification();

    /**
     * Copy this search form information to a new {@link SearchTemplate} and returns it.
     */
    public SearchTemplate toSearchTemplate() {
        // search meta parameters
        SearchTemplate searchTemplate = searchParameters.toSearchTemplate();
        
        searchTemplate.setFirstResult(this.getPageable().getOffset());
        searchTemplate.setMaxResults(this.getPageable().getPageSize());

        // date ranges
        searchTemplate.setDateRanges(getDateRanges());

        // null/not null criterion
        for (NullRestriction nr : getNullRestrictions()) {
            if (nr.getRestriction() == IS_NULL) {
                searchTemplate.addCriterion(isNull(nr.getProperty()));
            } else if (nr.getRestriction() == IS_NOT_NULL) {
                searchTemplate.addCriterion(isNotNull(nr.getProperty()));
            }
        }
        return searchTemplate;
    }
}