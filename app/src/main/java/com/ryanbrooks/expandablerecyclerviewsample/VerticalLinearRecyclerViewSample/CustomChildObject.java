package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomChildObject {
    private Object mParentObject;
    private String mChildText;

    public CustomChildObject() {}

    public String getChildText() {
        return mChildText;
    }

    public void setChildText(String childText) {
        mChildText = childText;
    }
}
