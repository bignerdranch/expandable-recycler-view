package com.bignerdranch.expandablerecyclerview.Model;

import java.util.List;

/**
 * Interface for implementing required methods in a parent list item.
 */
public interface ParentListItem {

    /**
     * Getter for the list of this parent list item's child list items.
     * <p/>
     * If list is empty, the parent list item has no children.
     *
     * @return A {@link List} of the children of this {@link ParentListItem}
     */
    List<Object> getChildItemList();

    /**
     * Getter used to determine if this {@link ParentListItem}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return {@value true} if expanded, {@value false} if not
     */
    boolean isInitiallyExpanded();
}