package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;

import java.io.Serializable;

/**
 * Custom child list item.
 *
 * This is for demo purposes, although it is recommended having a separate
 * child from your parent.
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
