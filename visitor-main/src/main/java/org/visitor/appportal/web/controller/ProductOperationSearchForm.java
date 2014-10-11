/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductOperationSearchForm extends SearchForm<ProductOperation> implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductOperation productOperation = new ProductOperation();

    /**
     * The ProductOperation instance used as an example.
     */
    public ProductOperation getProductOperation() {
        return productOperation;
    }

    //------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");

    /**
     * The {@link DateRangeUtil} for the createDate attribute.
     */
    public DateRangeUtil getCreateDateRange() {
        return createDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getCreateDateRange());
        return result;
    }

	@Override
	public Specification<ProductOperation> toSpecification() {
		return new Specification<ProductOperation>(){

			@Override
			public Predicate toPredicate(Root<ProductOperation> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(null != createDateRange.getFromDate()) {
					Path<Date> from = root.get("createDate");
					predicate = cb.and(predicate, cb.greaterThanOrEqualTo(from, createDateRange.getFromDate()));
				}
				if(null != productOperation.getType()) {
					ProductOperation.OperationTypeEnum type = ProductOperation.OperationTypeEnum.getInstance(productOperation.getType());
					if(type != null) {
						predicate = cb.and(predicate, cb.equal(root.get("type"), type.ordinal()));
					} else {
						type = ProductOperation.OperationTypeEnum.Create;
						predicate = cb.and(predicate, cb.notEqual(root.get("type"), type.ordinal()));
					}
				}
				return predicate;
			}
		};
	}
}