package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentObject extends ExpandingObject {

    private boolean expanded;
    protected ChildObject childObject;

    public ParentObject(ChildObject childObject) {
        this.expanded = false;
        this.childObject = childObject;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ChildObject getChildObject() {
        return childObject;
    }

    public void setChildObject(ChildObject childObject) {
        this.childObject = childObject;
    }
}
