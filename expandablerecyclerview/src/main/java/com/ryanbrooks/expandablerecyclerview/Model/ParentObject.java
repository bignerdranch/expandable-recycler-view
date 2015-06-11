package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Interface for implementing required methods in a ParentObject
 * <p/>
 * In the user's specified ParentObject, they should set instance variables for the following:
 * <p/>
 * boolean mExpanded: for the Parent's current expanded state
 * Object (or the user's custom ChildObject type) mChildObject: the reference to the Parent's ChildObject
 * long mStableId: A unique long to properly identify the ParentObject from other ParentObjects
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public interface ParentObject {

    /**
     * Getter method object to the reference to this ParentObject's child
     *
     * @return this Parent's child object
     */
    Object getChildObject();

    /**
     * Setter method for this parent's child object
     *
     * @param childObject
     */
    void setChildObject(Object childObject);
}