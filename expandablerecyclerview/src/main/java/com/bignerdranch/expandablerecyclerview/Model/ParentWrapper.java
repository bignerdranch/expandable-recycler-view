package com.bignerdranch.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ParentWrapper {

    private boolean mExpanded;
    private ParentListItem mParentListItem;

    public ParentWrapper(ParentListItem parentListItem) {
        mParentListItem = parentListItem;
        mExpanded = false;
    }

    public ParentListItem getParentListItem() {
        return mParentListItem;
    }

    public void setParentListItem(ParentListItem parentListItem) {
        mParentListItem = parentListItem;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }
}
