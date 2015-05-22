package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.Adapter.ExpandableRecyclerViewAdapter;
import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 5/21/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerViewAdapter implements ParentItemClickListener {

    public MyExpandableAdapter(Context context, ArrayList<? extends ExpandableItem> itemList) {
        super(context, itemList);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        CustomParentViewHolder itemHolder = new CustomParentViewHolder(view, this);
        return itemHolder;
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_layout_child, parent, false);
        CustomChildViewHolder itemHolder = new CustomChildViewHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder holder, int position) {
        CustomParentViewHolder customParentViewHolder = (CustomParentViewHolder) holder;
        TestDataModel testDataModel = (TestDataModel) itemList.get(position);
        customParentViewHolder.numberText.setText(testDataModel.getNumber() + "");
        customParentViewHolder.dataText.setText(testDataModel.getData());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int position) {
        CustomChildViewHolder customChildViewHolder = (CustomChildViewHolder) holder;
        TestDataModel testDataModel = (TestDataModel) itemList.get(position);
        ChildDataModel childDataModel = testDataModel.getChildObject();
        customChildViewHolder.dataText.setText(childDataModel.getData());
    }
}
