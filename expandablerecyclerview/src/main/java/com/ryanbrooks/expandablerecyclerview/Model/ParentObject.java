package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Interface for implementing required methods in a ParentObject
 *
 * In the user's specified ParentObject, they should set instance variables for the following:
 *
 * boolean mExpanded: for the Parent's current expanded state
 * Object (or the user's custom ChildObject type) mChildObject: the reference to the Parent's ChildObject
 * long mStableId: A unique long to properly identify the ParentObject from other ParentObjects
 */
public interface ParentObject {

    /**
     * Getter method for the current expansion state of this ParentObject
     *
     * @return true for currently expanded, false for currently collapsed
     */
    boolean isExpanded();

    /**
     * Setter method for setting the current expansion state.
     * For expanded, the value should be set to true.
     * For collapsed, the value should be set to false.
     *
     * @param expanded
     */
    void setExpanded(boolean expanded);

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

    /**
     * Getter method for the unique id associated with this ParentObject
     * The id is used by the Adapter to handle expansion and should be unique to only this
     * ParentObject
     *
     * @return the unique id associated only with this ParentObject
     */
    long getStableId();

    /**
     * Setter method for the unique id associated with this ParentObject
     * The user must handle generating this unique id
     *
     * @param stableId
     */
    void setStableId(long stableId);
}