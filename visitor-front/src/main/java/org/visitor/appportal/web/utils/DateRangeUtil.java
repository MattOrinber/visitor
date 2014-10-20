/*

 * Template pack-mvc-3:src/main/java/web/util/DateRangeUtil.p.vm.java
 */
package org.visitor.appportal.web.utils;

import java.io.Serializable;
import java.util.Date;

import org.visitor.appportal.repository.base.DateRange;

/**
 * DateRange for {@link Date} that are used in {@link SearchForm} objects
 */
public class DateRangeUtil extends DateRange implements Serializable {
    static final private long serialVersionUID = 1L;

    /**
     * Constructs a new {@link DateRangeUtil} with no boundaries and no restrictions on date's nullability.
     * @param field the property's name of an existing entity.
     */
    public DateRangeUtil(String field) {
        super(field);
    }

    public Date getFromDate() {
        return (Date) getFrom();
    }

    public void setFromDate(Date from) {
        setFrom(from);
    }

    public Date getToDate() {
        return (Date) getTo();
    }

    public void setToDate(Date to) {
        setTo(to);
    }
}