package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class AbstractViewHolder extends RecyclerView.ViewHolder {

    protected int originalPosition = -1;

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }
}
