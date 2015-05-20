package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ryan Brooks on 5/19/15.
 * Abstract class to force user to create and acknowledge a separate ViewHolder from a child ViewHolder
 *
 */
public abstract class ParentViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    private View expansionView;

    public ParentViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View getExpansionView() {
        return expansionView;
    }

    public void setExpansionView(View expansionView) {
        this.expansionView = expansionView;
    }

    // TODO: Add flag to indicate where view is to be added
    // Currently hardcoded to be below
    public void addExpandView() {
        ViewGroup viewGroup = (ViewGroup) itemView;
        viewGroup.addView(expansionView);
    }

    public void removeExpandView(View viewToRemove) {
        ViewGroup viewGroup = (ViewGroup) itemView;
        viewGroup.removeView(expansionView);
    }
}
