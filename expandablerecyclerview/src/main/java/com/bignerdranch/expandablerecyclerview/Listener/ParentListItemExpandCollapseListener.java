package com.bignerdranch.expandablerecyclerview.Listener;

/**
 * Empowers {@link com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter}
 * implementations to be notified of expand/collapse state change events.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public interface ParentListItemExpandCollapseListener {

    /**
     * Called when a list item is expanded.
     *
     * @param position The index of the item in the list being expanded
     */
    void onParentListItemExpanded(int position);

    /**
     * Called when a list item is collapsed.
     *
     * @param position The index of the item in the list being collapsed
     */
    void onParentListItemCollapsed(int position);

}