package com.bignerdranch.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ParentWrapper {
    private boolean mIsExpanded;
    private long mStableId;
    private Object mParentObject;

    public ParentWrapper(Object parentObject, int stableId) {
        mParentObject = parentObject;
        mStableId = stableId;
        mIsExpanded = false;
    }

    public Object getParentObject() {
        return mParentObject;
    }

    public void setParentObject(Object parentObject) {
        mParentObject = parentObject;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public long getStableId() {
        return mStableId;
    }

    public void setStableId(long stableId) {
        mStableId = stableId;
    }
}
