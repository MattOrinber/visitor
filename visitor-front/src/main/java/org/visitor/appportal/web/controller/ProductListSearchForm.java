/**
 * 
 */
package org.visitor.appportal.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Example;
import org.hibernate.type.Type;
import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.NullRestriction;
import org.visitor.appportal.web.utils.SearchForm;

/**
 * @author mengw
 *
 */
public class ProductListSearchForm extends SearchForm<ProductList> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3293029190246415372L;
    private ProductList productList = new ProductList();
    private ProductSiteFolder productSiteFolder = new ProductSiteFolder();
    private Integer bind;

    /**
     * The ProductList instance used as an example.
     */
    public ProductList getProductList() {
        return productList;
    }
    
    public ProductSiteFolder getProductSiteFolder(){
    	return productSiteFolder;
    }
    

    /**
	 * @return the bind
	 */
	public Integer getBind() {
		return bind;
	}

	/**
	 * @param bind the bind to set
	 */
	public void setBind(Integer bind) {
		this.bind = bind;
	}



	//------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil publishDateRange = new DateRangeUtil("publishDate");
    private DateRangeUtil modDateRange = new DateRangeUtil("modDate");
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");

    /**
     * The {@link DateRangeUtil} for the publishDate attribute.
     */
    public DateRangeUtil getPublishDateRange() {
        return publishDateRange;
    }

    /**
     * The {@link DateRangeUtil} for the modDate attribute.
     */
    public DateRangeUtil getModDateRange() {
        return modDateRange;
    }

    /**
     * The {@link DateRangeUtil} for the createDate attribute.
     */
    public DateRangeUtil getCreateDateRange() {
        return createDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getPublishDateRange());
        result.add(getModDateRange());
        result.add(getCreateDateRange());
        return result;
    }
    
    @Override
    public List<NullRestriction> getNullRestrictions() {
        List<NullRestriction> result = new ArrayList<NullRestriction>();
        return result;
    }

	@Override
	public Specification<ProductList> toSpecification() {
		return new Specification<ProductList>(){

			@Override
			public Predicate toPredicate(Root<ProductList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Predicate criteria = cb.conjunction();
								
				if(StringUtils.isNotBlank(productList.getName())) {
					ParameterExpression<String> p = cb.parameter(String.class, "name");
					criteria = cb.and(criteria, cb.like(p, productList.getName()));
				}
				if(null != createDateRange) {
					ParameterExpression<Date> p = cb.parameter(Date.class, createDateRange.getField());
					criteria = cb.and(criteria, cb.between(p, createDateRange.getFromDate(), createDateRange.getToDate()));
				}
				if(null != productList.getBillingTypeId()) {
					ParameterExpression<Long> p = cb.parameter(Long.class, "billingTypeId");
					criteria = cb.and(criteria, cb.equal(p, productList.getBillingTypeId()));
				}
				
				//productList.get
				return criteria;
			}
			
		};
	}
	
    static final class NotNullOrEmptyPropertySelector implements Example.PropertySelector {
        private static final long serialVersionUID = 1L;

        public boolean include(Object object, String propertyName, Type type) {
            if (object != null) {
                if ((object instanceof String) && "".equals((String) object)) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}
