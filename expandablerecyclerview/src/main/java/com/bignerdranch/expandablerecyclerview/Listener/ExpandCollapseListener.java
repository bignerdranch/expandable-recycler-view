package com.bignerdranch.expandablerecyclerview.Listener;

/**
 * Allows objects to register themselves as expand/collapse listeners to be
 * notified of change events.
 * <p/>
 * Implement this in your {@link android.app.Activity} or {@link android.app.Fragment}
 * to receive these callbacks.
 *
 * @author Evan Baker
 * @version 1.0
 * @since 7/10/2015
 */
public interface ExpandCollapseListener {

    /**
     * Called when a list item is expanded.
     *
     * @param position The index of the item in the list being expanded
     */
    void onListItemExpanded(int position);

    /**
     * Called when a list item is collapsed.
     *
     * @param position The index of the item in the list being collapsed
     */
    void onListItemCollapsed(int position);
}
