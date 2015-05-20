package com.ryanbrooks.expandablerecyclerviewsample;

import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;

/**
 * Created by Ryan Brooks on 5/19/15.
 */
public class TestDataModel extends ExpandableItem{

    private String data;
    private int number;

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
    public Object getChildObject() {
        return null;
    }

    @Override
    public void setChildObject(Object childObject) {

    }
}
