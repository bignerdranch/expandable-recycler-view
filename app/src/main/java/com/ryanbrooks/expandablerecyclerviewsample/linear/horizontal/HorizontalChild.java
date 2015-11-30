package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;

import java.io.Serializable;

/**
 * Custom child list item.
 *
 * This is for demo purposes, although it is recommended having a separate
 * child from your parent.
 *
 * You do not need to implement Serializable in order to use ExpandableRecyclerView. We are doing so
 * here in order to store away our list of items (which you usually will have in a singleton or
 * database)
 */
public class HorizontalChild implements Serializable {

    private String mChildText;

    public String getChildText() {
        return mChildText;
    }

    public void setChildText(String childText) {
        mChildText = childText;
    }
}
