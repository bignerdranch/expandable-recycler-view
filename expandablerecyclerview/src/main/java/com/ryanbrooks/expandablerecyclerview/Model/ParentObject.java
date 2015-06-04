package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentObject extends ExpandingObject {

    private boolean mExpanded;
    protected ChildObject mChildObject;
    protected int mStableId;

    public ParentObject (int stableID, ChildObject childObject) {
        mExpanded = false;
        mChildObject = childObject;
        mStableId = stableID;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public ChildObject getChildObject() {
        return mChildObject;
    }

    public void setChildObject(ChildObject mChildObject) {
        this.mChildObject = mChildObject;
    }

    public int getStableID() {
        return mStableId;
    }

    public void setStableID(int stableId) {
        mStableId = stableId;
    }
}
