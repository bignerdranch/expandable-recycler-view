package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.Listener.ParentItemExpandCollapseListener;


/**
 * ParentViewHolder that extends the Base RecyclerView ViewHolder. Keeps track of expansion and
 * offers the ability to animate in response to a triggered expansion
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentItemExpandCollapseListener mParentItemExpandCollapseListener;
    private boolean mExpanded;

    /**
     * Default constructor
     * @param itemView view that will be shown for this ViewHolder
     */
    public ParentViewHolder(View itemView) {
        super(itemView);
        mExpanded = false;
    }

    /**
     * Sets the Parent only as the trigger to expand the item.
     */
    public void setMainItemClickToExpand() {
        itemView.setOnClickListener(this);
    }

    /**
     * Returns if the item is currently expanded.
     *
     * @return true if expanded, false if not
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Setter method for expanded state, used for initialization of expanded state.
     * changes to the state are given in {@link #onExpansionToggled(boolean)}
     *
     * @param expanded
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    /**
     * Called when expansion is changed, does not get called during the initial binding
     * Useful for implementing parent view animations for expansion
     * @param expanded
     */
    public void onExpansionToggled(boolean expanded) {

    }

    /**
     * Getter for the ParentItemClickListener passed from the ExpandableRecyclerAdapter
     *
     * @return the ViewHolder's set ParentItemClickListner
     */
    public ParentItemExpandCollapseListener getParentItemExpandCollapseListener() {
        return mParentItemExpandCollapseListener;
    }

    /**
     * Setter for the ParentItemClickListener implemented in ExpandableRecyclerAdapter
     *
     * @param mParentItemExpandCollapseListener
     */
    public void setParentItemExpandCollapseListener(ParentItemExpandCollapseListener mParentItemExpandCollapseListener) {
        this.mParentItemExpandCollapseListener = mParentItemExpandCollapseListener;
    }

    /**
     * Implementation of View.onClick to listen for the clicks on the entire row.
     * Only registered if {@link #shouldItemViewClickToggleExpansion()} is true
     *
     * @param v the view that is the trigger for expansion
     */
    @Override
    public void onClick(View v) {
        if (mExpanded) {
            collapseView();
        } else {
            expandView();
        }
    }

    /**
     * Used to determine whether the entire row should trigger row expansion,
     * if you return false, call {@link #expandView()} to toggle an expansion
     * in response to a click in your custom view or {@link #collapseView()} to
     * toggle a collapse.
     *
     * @return true to set a click listener on the item view that toggles expansion
     */
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    /**
     * Triggers expansion of the parent list item.
     */
    protected void expandView() {
        setExpanded(true);
        onExpansionToggled(false);

        if (mParentItemExpandCollapseListener != null) {
            mParentItemExpandCollapseListener.onParentItemExpanded(getAdapterPosition());
        }
    }

    /**
     * Triggers collapse of the parent list item.
     */
    protected void collapseView() {
        setExpanded(false);
        onExpansionToggled(true);

        if (mParentItemExpandCollapseListener != null) {
            mParentItemExpandCollapseListener.onParentItemCollapsed(getAdapterPosition());
        }
    }
}
