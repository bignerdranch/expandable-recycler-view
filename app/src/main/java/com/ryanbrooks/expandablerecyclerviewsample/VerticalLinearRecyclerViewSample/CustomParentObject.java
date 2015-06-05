package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;

import java.io.Serializable;

/**
 * Created by Ryan Brooks on 5/28/15.
 */
public class CustomParentObject implements ParentObject {

    private boolean mExpanded = false;
    private long mStableId;
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
    public long getStableId() {
        return mStableId;
    }

    @Override
    public void setStableId(long stableId) {
        mStableId = stableId;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
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
