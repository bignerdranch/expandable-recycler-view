package com.bignerdranch.expandablerecyclerviewsample.linear.horizontal;


import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import java.io.Serializable;
import java.util.List;

/**
 * Custom parent list item that holds a string and an int for displaying data in the parent item. You
 * can use any Object here as long as it implements ParentListItem and sets the list to a private
 * variable.
 *
 * You do not need to implement Serializable in order to use ExpandableRecyclerView. We are doing so
 * here in order to store away our list of items (which you usually will have in a singleton or
 * database)
 */
public class HorizontalParent implements ParentListItem<HorizontalChild>, Serializable {

    private List<HorizontalChild> mChildItemList;
    private String mParentText;
    private int mParentNumber;
    private boolean mInitiallyExpanded;

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public int getParentNumber() {
        return mParentNumber;
    }

    public void setParentNumber(int parentNumber) {
        mParentNumber = parentNumber;
    }

    /**
     * Getter method for the list of children associated with this parent list item
     *
     * @return list of all children associated with this specific parent list item
     */
    @Override
    public List<HorizontalChild> getChildItemList() {
        return mChildItemList;
    }

    /**
     * Setter method for the list of children associated with this parent list item
     *
     * @param childItemList the list of all children associated with this parent list item
     */
    public void setChildItemList(List<HorizontalChild> childItemList) {
        mChildItemList = childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        mInitiallyExpanded = initiallyExpanded;
    }

    @Override
    public String toString() {
        return mParentText;
    }
}
