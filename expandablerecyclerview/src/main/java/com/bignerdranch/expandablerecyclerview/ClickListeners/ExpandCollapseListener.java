package com.bignerdranch.expandablerecyclerview.ClickListeners;

/**
 * Interface callback allowing objects to register themselves as expand/collapse listeners to be
 * notified of change events.
 *
 * @author Evan Baker
 * @version 1.0
 * @since 7/10/2015
 */
public interface ExpandCollapseListener {

    /**
     * Method called when an item in the ExpandableRecycleView is expanded
     */
    void onRecyclerViewItemExpanded(int position);

    /**
     * Method called when an item in the ExpandableRecyclerView is collapsed
     */
    void onRecyclerViewItemCollapsed(int position);
}
