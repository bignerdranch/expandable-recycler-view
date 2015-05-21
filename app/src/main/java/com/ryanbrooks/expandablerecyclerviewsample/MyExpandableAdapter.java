package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.Adapter.ExpandableRecyclerViewAdapter;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;

import java.util.ArrayList;

/**
 * Created by ryanbrooks on 5/21/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerViewAdapter {

    public MyExpandableAdapter(Context context, ArrayList<? extends ExpandableItem> itemList) {
        super(context, itemList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        CustomParentViewHolder itemHolder = new CustomParentViewHolder(view);
        return itemHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        CustomChildViewHolder itemHolder = new CustomChildViewHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindParentViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomParentViewHolder customParentViewHolder = (CustomParentViewHolder) holder;
        TestDataModel testDataModel = (TestDataModel) itemList.get(position);
        customParentViewHolder.numberText.setText(testDataModel.getNumber() + "");
        customParentViewHolder.dataText.setText(testDataModel.getData());
    }

    @Override
    public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomChildViewHolder customChildViewHolder = (CustomChildViewHolder) holder;
        TestDataModel testDataModel = (TestDataModel) itemList.get(position);
        ChildDataModel childDataModel = testDataModel.getChildObject();
        customChildViewHolder.dataText.setText(childDataModel.getData());
    }
}
