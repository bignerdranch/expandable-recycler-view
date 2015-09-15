package com.bignerdranch.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ParentWrapper {
    private boolean mIsExpanded;
    private ParentObject mParentObject;

    public ParentWrapper(ParentObject parentObject) {
        mParentObject = parentObject;
        mIsExpanded = false;
    }

    public ParentObject getParentObject() {
        return mParentObject;
    }

    public void setParentObject(ParentObject parentObject) {
        mParentObject = parentObject;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }
}
