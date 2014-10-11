/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class FolderSearchForm extends SearchForm<Folder> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Folder folder = new Folder();

    /**
     * The Folder instance used as an example.
     */
    public Folder getFolder() {
        return folder;
    }
    
    public void setFolder(Folder f){
    	this.folder = f;
    }

    //------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil sortOrderRange = new DateRangeUtil("sortOrder");
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");
    private DateRangeUtil modDateRange = new DateRangeUtil("modDate");

    /**
     * The {@link DateRangeUtil} for the sortOrder attribute.
     */
    public DateRangeUtil getSortOrderRange() {
        return sortOrderRange;
    }

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
        result.add(getSortOrderRange());
        result.add(getCreateDateRange());
        result.add(getModDateRange());
        return result;
    }

	@Override
	public Specification<Folder> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}