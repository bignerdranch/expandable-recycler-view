package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Custom parent object that holds a string and an int for displaying data in the parent item. You
 * can use any Object here as long as it implements ParentObject and sets the list to a private
 * variable.
 */
public class HorizontalParent implements ParentListItem {

    // A List<Object> or subclass of List must be added for the object to work correctly
    private List<Object> mChildItemList;

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
     * Getter method for the list of children associated with this parent object
     *
     * @return list of all children associated with this specific parent object
     */
    @Override
    public List<Object> getChildItemList() {
        return mChildItemList;
    }

    /**
     * Setter method for the list of children associated with this parent object
     *
     * @param childItemList the list of all children associated with this parent object
     */
    public void setChildItemList(List<Object> childItemList) {
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
