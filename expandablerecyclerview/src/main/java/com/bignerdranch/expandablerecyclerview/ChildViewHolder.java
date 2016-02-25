package com.bignerdranch.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for a child list item.
 * <p>
 * The user should extend this class and implement as they wish for their
 * child list item.
 */
public class ChildViewHolder extends RecyclerView.ViewHolder {

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public ChildViewHolder(View itemView) {
        super(itemView);
    }
}