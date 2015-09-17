package com.bignerdranch.expandablerecyclerview.Listener;

/**
 * Interface to allow for handling clicks of the ParentObject.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public interface ParentListItemExpandCollapseListener {

    void onParentListItemExpanded(int position);
    void onParentListItemCollapsed(int position);

}