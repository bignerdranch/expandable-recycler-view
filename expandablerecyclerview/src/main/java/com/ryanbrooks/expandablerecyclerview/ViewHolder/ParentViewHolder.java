package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;

/**
 * Created by Ryan Brooks on 5/21/15.
 * Doesn't do anything other than allow user to differentiate between Parent and Child VH
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final int TYPE_PARENT = 0;

    private boolean expanded;
    private ParentItemClickListener parentItemClickListener;

    protected ParentViewHolder(View itemView) {
        super(itemView);
        expanded = false;
    }

    protected ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);
        this.expanded = false;
        this.parentItemClickListener = parentItemClickListener;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onClick(View v) {
        // Expand and collapse
        Log.d(TAG, "Click Registered");
        if (parentItemClickListener != null) {
            Log.d(TAG, "Got inside click listener check");
            parentItemClickListener.onParentItemClickListener(getPosition(), TYPE_PARENT);
        }
    }
}
