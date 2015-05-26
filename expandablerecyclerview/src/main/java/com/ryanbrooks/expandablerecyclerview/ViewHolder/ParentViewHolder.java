package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;

/**
 * Created by Ryan Brooks on 5/21/15.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final int TYPE_PARENT = 0;

    private boolean expanded;
    private int childPosition;
    private ParentItemClickListener parentItemClickListener;
    private int originalPosition = -1;

    protected ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.expanded = false;
        this.parentItemClickListener = parentItemClickListener;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    public ParentItemClickListener getParentItemClickListener() {
        return parentItemClickListener;
    }

    public void setParentItemClickListener(ParentItemClickListener parentItemClickListener) {
        this.parentItemClickListener = parentItemClickListener;
    }

    @Override
    public void onClick(View v) {
        // Expand and collapse
        if (parentItemClickListener != null) {
            parentItemClickListener.onParentItemClickListener(getLayoutPosition(), TYPE_PARENT, originalPosition);
        }
    }
}
