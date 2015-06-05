package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;

import java.io.Serializable;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomChildObject implements Serializable, ChildObject {
    private Object mParentObject;
    private String mChildText;

    public CustomChildObject() {}

    public String getChildText() {
        return mChildText;
    }

    public void setChildText(String childText) {
        mChildText = childText;
    }

    @Override
    public Object getParentObject() {
        return mParentObject;
    }

    @Override
    public void setParentObject(Object parentObject) {
        mParentObject = parentObject;
    }
}
