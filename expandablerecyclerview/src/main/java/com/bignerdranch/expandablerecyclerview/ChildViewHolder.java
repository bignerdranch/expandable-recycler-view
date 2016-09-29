package com.bignerdranch.expandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder for a child list item.
 * <p>
 * The user should extend this class and implement as they wish for their
 * child list item.
 */
public class ChildViewHolder<C> extends RecyclerView.ViewHolder {

    C mChildListItem;
    ExpandableRecyclerAdapter mExpandableAdapter;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public ChildViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * @return the childListItem associated with this view holder
     */
    public C getChildListItem() {
        return mChildListItem;
    }

    /**
     *
     * Returns the adapter position of the Parent associated with this ChildViewHolder
     *
     * @return The adapter position of the Parent if it still exists in the adapter.
     * RecyclerView.NO_POSITION if item has been removed from the adapter,
     * RecyclerView.Adapter.notifyDataSetChanged() has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    public int getParentAdapterPosition() {
        int flatPosition = getAdapterPosition();
        if (flatPosition == RecyclerView.NO_POSITION) {
            return flatPosition;
        }

        return mExpandableAdapter.getNearestParentPosition(flatPosition);
    }

    /**
     *
     * Returns the adapter position of the Child associated with this ChildViewHolder
     *
     * @return The adapter position of the Child if it still exists in the adapter.
     * RecyclerView.NO_POSITION if item has been removed from the adapter,
     * RecyclerView.Adapter.notifyDataSetChanged() has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    public int getChildAdapterPosition() {
        int flatPosition = getAdapterPosition();
        if (flatPosition == RecyclerView.NO_POSITION) {
            return flatPosition;
        }

        return mExpandableAdapter.getChildPosition(flatPosition);
    }
}