package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindParentViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int positon) {

    }
}
