package com.bignerdranch.expandablerecyclerview.model;

import java.util.List;

/**
 * Simple implementation of the ParentListItem interface,
 * by default all items are not initially expanded.
 *
 * @param <C> Type of the Child Items held by the Parent.
 */
public class SimpleParentListItem<C> implements ParentListItem<C> {

    private List<C> mChildItemList;

    protected SimpleParentListItem(List<C> childItemList) {
        mChildItemList = childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public List<C> getChildItemList() {
        return mChildItemList;
    }

    public void setChildItemList(List<C> childItemList) {
        mChildItemList = childItemList;
    }
}
