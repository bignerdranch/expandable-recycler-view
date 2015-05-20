package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/20/15.
 * Forces user to add one child object to the item they create
 *
 * User must override getChildObject() and setChildObject() to force setting.
 * User should set Object to be null if no child exists

 * TODO: Add multiple children?
 */
public abstract class ExpandableItem {

    protected Object childObject;

    public abstract Object getChildObject();

    public abstract void setChildObject(Object childObject);

    public boolean hasChildObject() {
        return childObject != null;
    }
}
