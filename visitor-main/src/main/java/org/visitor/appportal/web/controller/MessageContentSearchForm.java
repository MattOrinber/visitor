/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class MessageContentSearchForm extends SearchForm<MessageContent> implements Serializable {
    private static final long serialVersionUID = 1L;
    private MessageContent messageContent = new MessageContent();

    /**
     * The MessageContent instance used as an example.
     */
    public MessageContent getMessageContent() {
        return messageContent;
    }

    //------------------------
    // support for date ranges
    //------------------------    
    private DateRangeUtil createDateRange = new DateRangeUtil("createDate");
    private DateRangeUtil replyDateRange = new DateRangeUtil("replyDate");

    /**
     * The {@link DateRangeUtil} for the createDate attribute.
     */
    public DateRangeUtil getCreateDateRange() {
        return createDateRange;
    }

    /**
     * The {@link DateRangeUtil} for the replyDate attribute.
     */
    public DateRangeUtil getReplyDateRange() {
        return replyDateRange;
    }

    @Override
    protected List<DateRange> getDateRanges() {
        List<DateRange> result = new ArrayList<DateRange>();
        result.add(getCreateDateRange());
        result.add(getReplyDateRange());
        return result;
    }

	@Override
	public Specification<MessageContent> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}