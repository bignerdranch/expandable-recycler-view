package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.io.Serializable;
import java.util.List;

/**
 * Custom parent list item that holds a string and an int for displaying data in the parent item. You
 * can use any Object here as long as it implements ParentListItem and sets the list to a private
 * variable.
 */
public class HorizontalParent implements ParentListItem, Serializable {

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
}
