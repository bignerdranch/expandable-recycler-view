package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentItemClickListener mParentItemClickListener;
    private View mClickableView;

    public ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.mParentItemClickListener = parentItemClickListener;
    }

    public void setCustomClickableView(View mClickableView) {
        this.mClickableView = mClickableView;
        itemView.setOnClickListener(null);
        mClickableView.setOnClickListener(this);
    }

    public ParentItemClickListener getParentItemClickListener() {
        return mParentItemClickListener;
    }

    public void setParentItemClickListener(ParentItemClickListener mParentItemClickListener) {
        this.mParentItemClickListener = mParentItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mParentItemClickListener != null) {
            mParentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }
}
