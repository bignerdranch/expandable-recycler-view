package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomChildObject extends ChildObject {

    private String data;

    public CustomChildObject() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
