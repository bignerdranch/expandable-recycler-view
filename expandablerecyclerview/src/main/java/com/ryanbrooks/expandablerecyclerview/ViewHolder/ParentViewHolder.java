package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentItemClickListener parentItemClickListener;

    public ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.parentItemClickListener = parentItemClickListener;
    }

    public ParentItemClickListener getParentItemClickListener() {
        return parentItemClickListener;
    }

    public void setParentItemClickListener(ParentItemClickListener parentItemClickListener) {
        this.parentItemClickListener = parentItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (parentItemClickListener != null) {
            parentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }
}
