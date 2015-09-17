package com.bignerdranch.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ParentWrapper {

    private boolean mExpanded;
    private ParentObject mParentObject;

    public ParentWrapper(ParentObject parentObject) {
        mParentObject = parentObject;
        mExpanded = false;
    }

    public ParentObject getParentObject() {
        return mParentObject;
    }

    public void setParentObject(ParentObject parentObject) {
        mParentObject = parentObject;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }
}
