package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for a ChildView that extends the base RecyclerView ViewHolder. The user should extend
 * this and implement as they wish for their ChildObject.
 *
 * @author Ryan Brooks
 * @since 5/27/2015
 * @version 1.0
 */
public class ChildViewHolder extends RecyclerView.ViewHolder {

    /**
     * Default constructor.
     *
     * @param itemView
     */
    public ChildViewHolder(View itemView) {
        super(itemView);
    }
}