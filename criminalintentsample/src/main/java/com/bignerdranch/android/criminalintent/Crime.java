package com.bignerdranch.android.criminalintent;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Crime implements ParentListItem {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private List<Object> mChildItemList;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public List<Object> getChildItemList() {
        return mChildItemList;
    }

    public void setChildItemList(List<Object> list) {
        mChildItemList = list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
