package com.ryanbrooks.expandablerecyclerview.Model;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public interface ChildObject {

    Object getParentObject();

    void setParentObject(Object parentObject);
}