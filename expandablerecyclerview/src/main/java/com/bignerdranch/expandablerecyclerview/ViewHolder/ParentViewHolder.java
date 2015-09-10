package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * ParentViewHolder that extends the Base RecyclerView ViewHolder. All expansion animation and click
 * handling is done here.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentItemClickListener mParentItemClickListener;
    private boolean mIsExpanded;

    /**
     * Public constructor that takes in an ItemView along with an implementation of
     * ParentItemClickListener to handle the clicks of either the Parent item or the custom defined
     * view.
     *
     * @param itemView
     */
    public ParentViewHolder(View itemView) {
        super(itemView);
        mIsExpanded = false;
    }

    /**
     * Sets the Parent only as the trigger to expand the item. Also disables rotation of the custom
     * clickable view.
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
        return mIsExpanded;
    }

    /**
     * Setter method for expanded state, used for initialization of expanded state.
     * changes to the state are given in {@link #expansionToggled(boolean)}
     *
     * @param isExpanded
     */
    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    /**
     * Called when expansion is changed, does not get called during the initial binding
     * Useful for implementing parent view animations for expansion
     * @param isExpanded
     */
    protected void expansionToggled(boolean isExpanded) {

    }

    /**
     * Getter for the ParentItemClickListener passed from the ExpandableRecyclerAdapter
     *
     * @return the ViewHolder's set ParentItemClickListner
     */
    public ParentItemClickListener getParentItemClickListener() {
        return mParentItemClickListener;
    }

    /**
     * Setter for the ParentItemClickListener implemented in ExpandableRecyclerAdapter
     *
     * @param mParentItemClickListener
     */
    public void setParentItemClickListener(ParentItemClickListener mParentItemClickListener) {
        this.mParentItemClickListener = mParentItemClickListener;
    }

    /**
     * Implementation of View.onClick to listen for the clicks on either the Parent item or the
     * custom clickable view if applicable. Triggers rotation if enabled on click.
     *
     * @param v the view that is the trigger for expansion
     */
    @Override
    public void onClick(View v) {
        toggleExpansion();
    }

    /**
     * Used to determine whether the entire row should trigger row expansion,
     * if you return false, call {@link #toggleExpansion()} to toggle an expansion in response to
     * a click in your custom view.
     * @return true to set a click listener on the item view that toggle expansion
     */
    public boolean shouldEntireRowExpand() {
        return true;
    }

    /**
     * Triggers expansion for the parent row
     */
    protected void toggleExpansion() {
        if (mParentItemClickListener != null) {
            setExpanded(!mIsExpanded);
            expansionToggled(mIsExpanded);
            mParentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }
}
