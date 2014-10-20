/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;


import org.visitor.appportal.domain.Label;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabelSearchForm extends SearchForm<Label> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Label label = new Label();

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    /**

     * The label instance used as an example.
     */

    //------------------------
    // support for date ranges
    //------------------------
    private DateRangeUtil sortOrderRange = new DateRangeUtil("sortOrder");
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");
    private DateRangeUtil modDateRange = new DateRangeUtil("modDate");

    /**
     * The {@link org.visitor.appportal.web.utils.DateRangeUtil} for the sortOrder attribute.
     */
    public DateRangeUtil getSortOrderRange() {
        return sortOrderRange;
    }

    /**
     * The {@link org.visitor.appportal.web.utils.DateRangeUtil} for the createDate attribute.
     */
    public DateRangeUtil getCreateDateRange() {
        return createDateRange;
    }

    /**
     * The {@link org.visitor.appportal.web.utils.DateRangeUtil} for the modDate attribute.
     */
    public DateRangeUtil getModDateRange() {
        return modDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getSortOrderRange());
        result.add(getCreateDateRange());
        result.add(getModDateRange());
        return result;
    }

	@Override
	public Specification<Label> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}