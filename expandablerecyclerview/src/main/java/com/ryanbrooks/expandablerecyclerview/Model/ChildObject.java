package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Interface for implementing required methods in a ChildObject
 * <p>
 * In the user's implementation of ChildObject, they should have a instance variable of type Object
 * (or casted to their custom ParentObject) for the ParentObject that corresponds with the ChildObject.
 * This is used in ExpandableRecyclerAdapter for association with the correct ParentObject.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public interface ChildObject {

    /**
     * Getter for a reference to the Child's ParentObject
     *
     * @return reference to the corresponding ParentObject
     */
    Object getParentObject();

    /**
     * Setter for a reference to the Child's ParentObject
     *
     * @param parentObject
     */
    void setParentObject(Object parentObject);
}