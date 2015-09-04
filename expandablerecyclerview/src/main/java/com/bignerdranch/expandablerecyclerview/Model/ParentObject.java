package com.bignerdranch.expandablerecyclerview.Model;

import java.util.List;

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
     * Getter method object to the reference to this ParentObject's child list. The list should
     * contain all children to be displayed. If list is empty, no children will be added.
     *
     * @return this Parent's child object
     */
    List<Object> getChildObjectList();

    /**
     * Used to determine whether parent view should show up initially as expanded.
     *
     * @return true if parent should be expanded
     */
    boolean isInitiallyExpanded();
}