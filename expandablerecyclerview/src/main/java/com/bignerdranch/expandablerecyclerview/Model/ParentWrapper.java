package com.bignerdranch.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ParentWrapper {
    private boolean mIsExpanded;
    private long mStableId;
    private ParentObject mParentObject;

    public ParentWrapper(ParentObject parentObject, int stableId) {
        mParentObject = parentObject;
        mStableId = stableId;
        mIsExpanded = false;
    }

    public ParentObject getParentObject() {
        return mParentObject;
    }

    public void setParentObject(ParentObject parentObject) {
        mParentObject = parentObject;
    }

    public boolean isExpanded() {
        return mIsExpanded != mParentObject.isInitiallyExpanded();
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded != mParentObject.isInitiallyExpanded();
    }

    public long getStableId() {
        return mStableId;
    }

    public void setStableId(long stableId) {
        mStableId = stableId;
    }
}
