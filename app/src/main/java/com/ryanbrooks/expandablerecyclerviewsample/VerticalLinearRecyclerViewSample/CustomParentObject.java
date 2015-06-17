package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;


import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by Ryan Brooks on 5/28/15.
 */
public class CustomParentObject implements ParentObject {
    private List<Object> mChildObjectList;

    private String mParentText;
    private int mParentNumber;

    public CustomParentObject() {
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public int getParentNumber() {
        return mParentNumber;
    }

    public void setParentNumber(int parentNumber) {
        mParentNumber = parentNumber;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildObjectList;
    }

    @Override
    public void setChildObjectList(List<Object> childObjectList) {
        mChildObjectList = childObjectList;
    }
}
