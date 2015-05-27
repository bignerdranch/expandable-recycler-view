package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/20/15.
 * Forces user to add one child object to the item they create
 *
 * User must override getChildObject() and setChildObject() to force setting.
 * User should never set Object to be null if no child exists

 * TODO: Add multiple children?
 */
public abstract class ExpandableItem<T> {

    private boolean expanded;
    protected T childObject;

    public ExpandableItem(T childObject) {
        this.expanded = false;
        this.childObject = childObject;
    }

    public T getChildObject() {
        return this.childObject;
    }

    public void setChildObject(T childObject) {
        this.childObject = childObject;
    }

    public boolean hasChildObject() {
        return childObject != null;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
