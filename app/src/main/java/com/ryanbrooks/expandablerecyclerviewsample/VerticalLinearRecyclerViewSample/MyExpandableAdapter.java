package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 5/21/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerAdapter implements ParentItemClickListener {
    private final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mInflater;

    public MyExpandableAdapter(Context context, ArrayList<Object> itemList) {
        super(context, itemList);
        mInflater = LayoutInflater.from(context);
    }

    public MyExpandableAdapter(Context context, ArrayList<Object> itemList,
                               int customClickableViewId) {
        super(context, itemList, customClickableViewId);
        mInflater = LayoutInflater.from(context);
    }

    public MyExpandableAdapter(Context context, ArrayList<Object> itemList,
                               int customClickableViewId, long animationDuration) {
        super(context, itemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        return new CustomParentViewHolder(view, this);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_child, parent, false);
        return new CustomChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position) {
        CustomParentViewHolder customParentViewHolder = (CustomParentViewHolder) parentViewHolder;
        CustomParentObject parentObject = (CustomParentObject) mItemList.get(position);
        customParentViewHolder.numberText.setText(Integer.toString(parentObject.getParentNumber()));
        customParentViewHolder.dataText.setText(parentObject.getParentText());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int position) {
        CustomChildViewHolder customChildViewHolder = (CustomChildViewHolder) childViewHolder;
        CustomChildObject childObject = (CustomChildObject) mItemList.get(position);
        customChildViewHolder.dataText.setText(childObject.getChildText());
    }
}
