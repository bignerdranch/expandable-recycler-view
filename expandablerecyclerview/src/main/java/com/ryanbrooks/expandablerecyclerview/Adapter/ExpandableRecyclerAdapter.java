package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public abstract class ExpandableRecyclerAdapter<parentViewHolder extends ParentViewHolder, childViewHolder extends ChildViewHolder>
extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExpandableRecyclerAdapterInterface<parentViewHolder, childViewHolder> {

    /**
     * DO NOT CALL THIS METHOD!!! Implement both onCreateParentViewHolder and onCreateChildViewHolder
     * @param parent
     * @param viewType
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * DO NOT CALL THIS METHOD!!! Implement both onBindChildViewHolder and onBindChildViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * DO NOT CALL THIS METHOD
     * @return 0 as a placeholder
     */
    @Override
    public int getItemCount() {
        return 0;
    }
}
