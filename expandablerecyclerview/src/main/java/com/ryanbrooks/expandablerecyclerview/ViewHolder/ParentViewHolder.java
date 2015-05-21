package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ryan Brooks on 5/21/15.
 * Doesn't do anything other than allow user to differentiate between Parent and Child VH
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private boolean expanded;

    public ParentViewHolder(View itemView) {
        super(itemView);
        expanded = false;
    }

    @Override
    public void onClick(View v) {
        // Expand and collapse

    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
