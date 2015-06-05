package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public interface ParentObject {

    boolean isExpanded();

    void setExpanded(boolean expanded);

    Object getChildObject();

    void setChildObject(Object mChildObject);
}
