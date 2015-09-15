package com.bignerdranch.expandablerecyclerview.Model;

import java.util.List;

/**
 * Interface for implementing required methods in a ParentObject
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