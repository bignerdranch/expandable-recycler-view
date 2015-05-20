package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by Ryan Brooks on 5/20/15.
 * This is mainly here as a placeholder so the user will know to use a ParentViewHolder rather than
 */
public abstract class ExpandableRecyclerAdapterSimple extends RecyclerView.Adapter<ParentViewHolder> {
    @Override
    public abstract ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(ParentViewHolder holder, int position);

    @Override
    public abstract int getItemCount();
}
