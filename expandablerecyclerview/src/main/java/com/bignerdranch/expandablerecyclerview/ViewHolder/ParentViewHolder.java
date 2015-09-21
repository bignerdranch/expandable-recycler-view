package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.Listener.ParentListItemExpandCollapseListener;


/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder} for a
 * {@link com.bignerdranch.expandablerecyclerview.Model.ParentListItem}.
 * Keeps track of expanded state and holds callbacks which can be used to
 * trigger expansion-based events.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentListItemExpandCollapseListener mParentListItemExpandCollapseListener;
    private boolean mExpanded;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this {@link android.support.v7.widget.RecyclerView.ViewHolder}
     */
    public ParentViewHolder(View itemView) {
        super(itemView);
        mExpanded = false;
    }

    /**
     * Sets a {@link android.view.View.OnClickListener} on the entire parent
     * view to trigger expansion.
     */
    public void setMainItemClickToExpand() {
        itemView.setOnClickListener(this);
    }

    /**
     * Returns expanded state for the {@link com.bignerdranch.expandablerecyclerview.Model.ParentListItem}
     * corresponding to this {@link ParentViewHolder}.
     *
     * @return {@value true} if expanded, {@value false} if not
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Setter method for expanded state, used for initialization of expanded state.
     * changes to the state are given in {@link #onExpansionToggled(boolean)}
     *
     * @param expanded {@value true} if expanded, {@value false} if not
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    /**
     * Callback triggered when expansion state is changed, but not during
     * initialization.
     * <p/>
     * Useful for implementing animations on expansion.
     *
     * @param expanded {@value true} if expanded, {@value false} if not
     */
    public void onExpansionToggled(boolean expanded) {

    }

    /**
     * Getter for the {@link ParentListItemExpandCollapseListener} implemented in
     * {@link com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter}.
     *
     * @return The {@link ParentListItemExpandCollapseListener} set in the {@link ParentViewHolder}
     */
    public ParentListItemExpandCollapseListener getParentListItemExpandCollapseListener() {
        return mParentListItemExpandCollapseListener;
    }

    /**
     * Setter for the {@link ParentListItemExpandCollapseListener} implemented in
     * {@link com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter}.
     *
     * @param parentListItemExpandCollapseListener The {@link ParentListItemExpandCollapseListener} to set on the {@link ParentViewHolder}
     */
    public void setParentListItemExpandCollapseListener(ParentListItemExpandCollapseListener parentListItemExpandCollapseListener) {
        mParentListItemExpandCollapseListener = parentListItemExpandCollapseListener;
    }

    /**
     * {@link android.view.View.OnClickListener} to listen for click events on
     * the entire parent {@link View}.
     * <p/>
     * Only registered if {@link #shouldItemViewClickToggleExpansion()} is true.
     *
     * @param v The {@link View} that is the trigger for expansion
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
     * Used to determine whether a click in the entire parent {@link View}
     * should trigger row expansion.
     * <p/>
     * If you return {@value false}, you can call {@link #expandView()} to
     * trigger an expansion in response to a another event or
     * {@link #collapseView()} to trigger a collapse.
     *
     * @return {@value true} to set an {@link android.view.View.OnClickListener} on the item view
     */
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    /**
     * Triggers expansion of the parent.
     */
    protected void expandView() {
        setExpanded(true);
        onExpansionToggled(false);

        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemExpanded(getAdapterPosition());
        }
    }

    /**
     * Triggers collapse of the parent.
     */
    protected void collapseView() {
        setExpanded(false);
        onExpansionToggled(true);

        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemCollapsed(getAdapterPosition());
        }
    }
}
