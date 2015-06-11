package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;

/**
 * Created by Ryan Brooks on 5/28/15.
 */
public class CustomParentObject implements ParentObject {
    private Object mChildObject;

    private String mParentText;
    private int mParentNumber;

    public CustomParentObject() {
    }

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

    @Override
    public Object getChildObject() {
        return mChildObject;
    }

    @Override
    public void setChildObject(Object childObject) {
        mChildObject = childObject;
    }
}
