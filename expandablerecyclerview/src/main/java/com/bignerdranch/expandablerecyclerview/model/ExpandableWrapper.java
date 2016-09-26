package com.bignerdranch.expandablerecyclerview.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper used to link metadata with a list item.
 *
 * @param <P> Parent list item
 * @param <C> Child list item
 */
public class ExpandableWrapper<P extends ParentListItem<C>, C> {

    private P mParentListItem;
    private C mChildListItem;
    private boolean mParent;
    private boolean mExpanded;

    private List<ExpandableWrapper<P, C>> mWrappedChildItemList;

    /**
     * Constructor to wrap a parent list item of type {@link P}.
     *
     * @param parentListItem The parent list item to wrap
     */
    public ExpandableWrapper(@NonNull P parentListItem) {
        mParentListItem = parentListItem;
        mParent = true;
        mExpanded = false;

        mWrappedChildItemList = generateChildItemList(parentListItem);
    }

    /**
     * Constructor to wrap a child list item of type {@link C}
     *
     * @param childListItem The child list item to wrap
     */
    public ExpandableWrapper(@NonNull C childListItem) {
        mChildListItem = childListItem;
        mParent = false;
        mExpanded = false;
    }

    public P getParentListItem() {
        return mParentListItem;
    }

    public void setParentListItem(@NonNull P parentListItem) {
        mParentListItem = parentListItem;
        mWrappedChildItemList = generateChildItemList(parentListItem);
    }

    public C getChildListItem() {
        return mChildListItem;
    }

    public void setChildListItem(@NonNull C childListItem) {
        mChildListItem = childListItem;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isParent() {
        return mParent;
    }

    public void setParent(boolean parent) {
        mParent = parent;
    }

    /**
     * @return The initial expanded state of a parent list item
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public boolean isParentInitiallyExpanded() {
        if (!mParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return mParentListItem.isInitiallyExpanded();
    }

    /**
     * @return The list of children of a parent list item
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public List<ExpandableWrapper<P, C>> getWrappedChildItemList() {
        if (!mParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return mWrappedChildItemList;
    }

    private List<ExpandableWrapper<P, C>> generateChildItemList(P parentListItem) {
        List<ExpandableWrapper<P, C>> childItemList = new ArrayList<>();

        for (C child : parentListItem.getChildItemList()) {
            childItemList.add(new ExpandableWrapper<P, C>(child));
        }

        return childItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ExpandableWrapper<?, ?> that = (ExpandableWrapper<?, ?>) o;

        if (mParentListItem != null ? !mParentListItem.equals(that.mParentListItem) : that.mParentListItem != null)
            return false;
        return mChildListItem != null ? mChildListItem.equals(that.mChildListItem) : that.mChildListItem == null;

    }

    @Override
    public int hashCode() {
        int result = mParentListItem != null ? mParentListItem.hashCode() : 0;
        result = 31 * result + (mChildListItem != null ? mChildListItem.hashCode() : 0);
        return result;
    }
}
