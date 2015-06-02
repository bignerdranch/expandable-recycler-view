package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentObject extends ExpandingObject {

    private boolean mExpanded;
    protected ChildObject mChildObject;
    protected int mStableID;

    public ParentObject (int stableID, ChildObject childObject) {
        mExpanded = false;
        mChildObject = childObject;
        mStableID = stableID;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
    }

    public ChildObject getChildObject() {
        return mChildObject;
    }

    public void setChildObject(ChildObject mChildObject) {
        this.mChildObject = mChildObject;
    }

    public int getStableID() {
        return mStableID;
    }

    public void setStableID(int stableID) {
        mStableID = stableID;
    }
}
