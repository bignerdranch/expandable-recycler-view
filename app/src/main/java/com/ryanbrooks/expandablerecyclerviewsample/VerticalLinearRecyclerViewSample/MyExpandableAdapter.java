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

import java.util.List;

/**
 * Created by Ryan Brooks on 5/21/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerAdapter<CustomParentViewHolder, CustomChildViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mInflater;

    public MyExpandableAdapter(Context context, List<Object> itemList) {
        super(context, itemList);
        mInflater = LayoutInflater.from(context);
    }

    public MyExpandableAdapter(Context context, List<Object> itemList,
                               int customClickableViewId) {
        super(context, itemList, customClickableViewId);
        mInflater = LayoutInflater.from(context);
    }

    public MyExpandableAdapter(Context context, List<Object> itemList,
                               int customClickableViewId, long animationDuration) {
        super(context, itemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CustomParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        return new CustomParentViewHolder(view, this);
    }

    @Override
    public CustomChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_child, parent, false);
        return new CustomChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(CustomParentViewHolder parentViewHolder, int position, Object parentObject) {
        CustomParentObject customParentObject = (CustomParentObject) parentObject;
        parentViewHolder.numberText.setText(Integer.toString(customParentObject.getParentNumber()));
        parentViewHolder.dataText.setText(customParentObject.getParentText());
    }

    @Override
    public void onBindChildViewHolder(CustomChildViewHolder childViewHolder, int position, Object childObject) {
        CustomChildObject customChildObject = (CustomChildObject) childObject;
        childViewHolder.dataText.setText(customChildObject.getChildText());
    }
}
