package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

/**
 * Created by Ryan Brooks on 5/21/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerAdapter implements ParentItemClickListener {
    private final String TAG = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    public MyExpandableAdapter(Context context, List<ExpandingObject> itemList) {
        super(context, itemList);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        return new CustomParentViewHolder(view, this);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.recycler_item_layout_child, parent, false);
        return new CustomChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position) {
        CustomParentViewHolder customParentViewHolder = (CustomParentViewHolder) parentViewHolder;
        CustomParentObject parentObject = (CustomParentObject) itemList.get(position);
        customParentViewHolder.numberText.setText(parentObject.getNumber() + "");
        customParentViewHolder.dataText.setText(parentObject.getData());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int position) {
        CustomChildViewHolder customChildViewHolder = (CustomChildViewHolder) childViewHolder;
        CustomChildObject childObject = (CustomChildObject) itemList.get(position);
        customChildViewHolder.dataText.setText(childObject.getData());
    }
}
