package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by Ryan Brooks on 5/20/15.
 * Interface containing methods to be implemented in ExpandableRecyclerAdapterInterface which will be extended
 * by user later
 */
public interface ExpandableRecyclerAdapterInterface<parentViewHolder extends RecyclerView.ViewHolder, childViewHolder extends RecyclerView.ViewHolder> {

    public parentViewHolder onCreateParentViewHolder(ViewGroup parentView);

    public void onBindParentViewHolder(parentViewHolder holder, int position);

    public childViewHolder onCreateChildViewHolder(ViewGroup parentView);

    public void onBindChildViewHolder(childViewHolder holder, int position);
}