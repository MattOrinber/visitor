package org.visitor.appportal.domain;

import java.io.Serializable;

/**
 * By making entities implement this interface we can easily retrieve from the
 * generic DAO the identifier property of the entity.
 */
public interface Identifiable<PK extends Serializable> {

    /**
     * @return the primary key
     */
    PK getPrimaryKey();

    /**
     * Sets the primary key
     */
    void setPrimaryKey(PK id);

    /**
     * Helper method to know whether the primary key is set or not.
     * @return true if the primary key is set, false otherwise
     */
    boolean isPrimaryKeySet();
}