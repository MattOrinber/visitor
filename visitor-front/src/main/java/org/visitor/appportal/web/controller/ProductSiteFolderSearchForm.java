/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductSiteFolderSearchForm extends SearchForm<ProductSiteFolder> implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductSiteFolder productSiteFolder = new ProductSiteFolder();

    /**
     * The ProductSiteFolder instance used as an example.
     */
    public ProductSiteFolder getProductSiteFolder() {
        return productSiteFolder;
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
	public Specification<ProductSiteFolder> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}