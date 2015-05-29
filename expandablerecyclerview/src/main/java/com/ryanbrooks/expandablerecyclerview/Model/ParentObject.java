package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentObject extends ExpandingObject {

    private boolean mExpanded;
    protected ChildObject mChildObject;

    public ParentObject (ChildObject childObject) {
        this.mExpanded = false;
        this.mChildObject = childObject;
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
}
