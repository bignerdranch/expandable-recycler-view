package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ryan Brooks on 5/19/15.
 * The child ViewHolder is an abstract class that will need to be extended to get proper functionality
 *  in a custom ViewHolder
 * While ChildViewHolder is extremely necessary for expanding/collapsing to work, this class is here
 *  just to force the user to create and acknowledge a ChildViewHolder that is separate from Parent's
 */
public abstract class ChildViewHolder extends RecyclerView.ViewHolder {

    public ChildViewHolder(View itemView) {
        super(itemView);
    }
}
