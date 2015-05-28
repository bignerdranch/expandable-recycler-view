package com.ryanbrooks.expandablerecyclerviewsample;

import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;

/**
 * Created by Ryan Brooks on 5/28/15.
 */
public class CustomParentObject extends ParentObject {

    private String data;
    private int number;

    public CustomParentObject(ChildObject childObject) {
        super(childObject);
    }

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
}
