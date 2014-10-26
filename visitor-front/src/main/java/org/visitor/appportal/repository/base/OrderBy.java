/*

 * Template pack-backend:src/main/java/project/dao/support/OrderBy.p.vm.java
 */
package org.visitor.appportal.repository.base;

import static org.visitor.appportal.repository.base.OrderByDirection.ASC;
import static org.visitor.appportal.repository.base.OrderByDirection.DESC;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * Holder class for search ordering used by the {@link SearchTemplate}  
 */
public class OrderBy implements Serializable {
    private static final long serialVersionUID = 1L;
    private String column;
    private OrderByDirection direction;

    public OrderBy(String column, OrderByDirection direction) {
        Validate.notNull(column);
        Validate.notNull(direction);
        this.column = column;
        this.direction = direction;
    }

    public OrderBy(String column) {
        Validate.notNull(column);
        this.column = column;
        this.direction = ASC;
    }

    public String getColumn() {
        return column;
    }

    public OrderByDirection getDirection() {
        return direction;
    }

    public boolean isOrderDesc() {
        return DESC == direction;
    }
}