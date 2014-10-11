/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductContainerTaskSearchForm extends SearchForm<ProductContainerTask> implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductContainerTask productContainerTask = new ProductContainerTask();

    /**
     * The ProductContainerTask instance used as an example.
     */
    public ProductContainerTask getProductContainerTask() {
        return productContainerTask;
    }

    //------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil startDateRange = new DateRangeUtil("startDate");
    private DateRangeUtil endDateRange = new DateRangeUtil("endDate");

    /**
     * The {@link DateRangeUtil} for the startDate attribute.
     */
    public DateRangeUtil getStartDateRange() {
        return startDateRange;
    }

    /**
     * The {@link DateRangeUtil} for the endDate attribute.
     */
    public DateRangeUtil getEndDateRange() {
        return endDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getStartDateRange());
        result.add(getEndDateRange());
        return result;
    }

	@Override
	public Specification<ProductContainerTask> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

}