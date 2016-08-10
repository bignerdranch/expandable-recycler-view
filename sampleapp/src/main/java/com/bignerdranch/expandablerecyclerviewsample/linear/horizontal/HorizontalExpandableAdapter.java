package com.bignerdranch.expandablerecyclerviewsample.linear.horizontal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.bignerdranch.expandablerecyclerviewsample.R;

import java.util.List;

/**
 * An example custom implementation of the ExpandableRecyclerAdapter.
 */
public class HorizontalExpandableAdapter extends ExpandableRecyclerAdapter<HorizontalParentViewHolder, HorizontalChildViewHolder> {

    private LayoutInflater mInflater;

    /**
     * Public primary constructor.
     *
     * @param parentItemList the list of parent items to be displayed in the RecyclerView
     */
    public HorizontalExpandableAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * OnCreateViewHolder implementation for parent items. The desired ParentViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public HorizontalParentViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_parent_horizontal, parent, false);
        return new HorizontalParentViewHolder(view);
    }

    /**
     * OnCreateViewHolder implementation for child items. The desired ChildViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public HorizontalChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_child_horizontal, parent, false);
        return new HorizontalChildViewHolder(view);
    }

    /**
     * OnBindViewHolder implementation for parent items. Any data or view modifications of the
     * parent view should be performed here.
     *
     * @param parentViewHolder the ViewHolder of the parent item created in OnCreateParentViewHolder
     * @param parentPosition the position in the RecyclerView of the item
     */
    @Override
    public void onBindParentViewHolder(HorizontalParentViewHolder parentViewHolder, int parentPosition, ParentListItem parentListItem) {
        HorizontalParent horizontalParent = (HorizontalParent) parentListItem;
        parentViewHolder.bind(horizontalParent.getParentNumber(), horizontalParent.getParentText());
    }

    /**
     * OnBindViewHolder implementation for child items. Any data or view modifications of the
     * child view should be performed here.
     *
     * @param childViewHolder the ViewHolder of the child item created in OnCreateChildViewHolder
     * @param childPosition the position in the RecyclerView of the item
     */
    @Override
    public void onBindChildViewHolder(HorizontalChildViewHolder childViewHolder, int parentPosition, int childPosition, Object childListItem) {
        HorizontalChild horizontalChild = (HorizontalChild) childListItem;
        childViewHolder.bind(horizontalChild.getChildText());
    }
}