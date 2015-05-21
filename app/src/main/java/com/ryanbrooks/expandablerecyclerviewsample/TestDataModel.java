package com.ryanbrooks.expandablerecyclerviewsample;

import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;

import java.util.Objects;

/**
 * Created by Ryan Brooks on 5/19/15.
 */
public class TestDataModel extends ExpandableItem<ChildDataModel>{

    private String data;
    private int number;
    private ChildDataModel childObject;

    public TestDataModel() {}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public ChildDataModel getChildObject() {
        return childObject;
    }

    @Override
    public void setChildObject(ChildDataModel childObject) {
        this.childObject = childObject;
    }
}
