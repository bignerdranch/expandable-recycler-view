package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for a ChildView that extends the base RecyclerView ViewHolder. The user should extend
 * this and implement as they wish for their ChildObject.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
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