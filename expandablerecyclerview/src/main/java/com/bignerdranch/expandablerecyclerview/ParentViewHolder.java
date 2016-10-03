package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.model.ParentWrapper;


/**
 * ViewHolder for a {@link com.bignerdranch.expandablerecyclerview.model.ParentListItem}
 * Keeps track of expanded state and holds callbacks which can be used to
 * trigger expansion-based events.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Nullable
    private ParentListItemExpandCollapseListener mParentListItemExpandCollapseListener;
    private boolean mExpanded;
    ParentWrapper mParentWrapper;
    ExpandableRecyclerAdapter mExpandableAdapter;

    /**
     * Empowers {@link com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter}
     * implementations to be notified of expand/collapse state change events.
     */
    public interface ParentListItemExpandCollapseListener {
        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        @UiThread
        void onParentListItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        @UiThread
        void onParentListItemCollapsed(int position);
    }

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    @UiThread
    public ParentViewHolder(@NonNull View itemView) {
        super(itemView);
        mExpanded = false;
    }

    /**
     * @return the ParentListItem associated with this ViewHolder
     */
    @Nullable
    @UiThread
    public ParentListItem getParentListItem() {
        if (mParentWrapper == null) {
            return null;
        }

        return mParentWrapper.getParentListItem();
    }

    /**
     * Returns the adapter position of the Parent associated with this ParentViewHolder
     *
     * @return The adapter position of the Parent if it still exists in the adapter.
     * RecyclerView.NO_POSITION if item has been removed from the adapter,
     * RecyclerView.Adapter.notifyDataSetChanged() has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    @UiThread
    public int getParentAdapterPosition() {
        int adapterPosition = getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return adapterPosition;
        }

        return mExpandableAdapter.getNearestParentPosition(adapterPosition);
    }

    /**
     * Sets a {@link android.view.View.OnClickListener} on the entire parent
     * view to trigger expansion.
     */
    @UiThread
    public void setMainItemClickToExpand() {
        itemView.setOnClickListener(this);
    }

    /**
     * Returns expanded state for the {@link com.bignerdranch.expandablerecyclerview.model.ParentListItem}
     * corresponding to this {@link ParentViewHolder}.
     *
     * @return true if expanded, false if not
     */
    @UiThread
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Setter method for expanded state, used for initialization of expanded state.
     * changes to the state are given in {@link #onExpansionToggled(boolean)}
     *
     * @param expanded true if expanded, false if not
     */
    @UiThread
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    /**
     * Callback triggered when expansion state is changed, but not during
     * initialization.
     * <p>
     * Useful for implementing animations on expansion.
     *
     * @param expanded true if view is expanded before expansion is toggled,
     *                 false if not
     */
    @UiThread
    public void onExpansionToggled(boolean expanded) {

    }

    /**
     * Getter for the {@link ParentListItemExpandCollapseListener} implemented in
     * {@link com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter}.
     *
     * @return The {@link ParentListItemExpandCollapseListener} set in the {@link ParentViewHolder}
     */
    @UiThread
    public ParentListItemExpandCollapseListener getParentListItemExpandCollapseListener() {
        return mParentListItemExpandCollapseListener;
    }

    /**
     * Setter for the {@link ParentListItemExpandCollapseListener} implemented in
     * {@link com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter}.
     *
     * @param parentListItemExpandCollapseListener The {@link ParentListItemExpandCollapseListener} to set on the {@link ParentViewHolder}
     */
    @UiThread
    public void setParentListItemExpandCollapseListener(@Nullable ParentListItemExpandCollapseListener parentListItemExpandCollapseListener) {
        mParentListItemExpandCollapseListener = parentListItemExpandCollapseListener;
    }

    /**
     * {@link android.view.View.OnClickListener} to listen for click events on
     * the entire parent {@link View}.
     * <p>
     * Only registered if {@link #shouldItemViewClickToggleExpansion()} is true.
     *
     * @param v The {@link View} that is the trigger for expansion
     */
    @Override
    @UiThread
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
     * <p>
     * If you return false, you can call {@link #expandView()} to trigger an
     * expansion in response to a another event or {@link #collapseView()} to
     * trigger a collapse.
     *
     * @return true to set an {@link android.view.View.OnClickListener} on the item view
     */
    @UiThread
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    /**
     * Triggers expansion of the parent.
     */
    @UiThread
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
    @UiThread
    protected void collapseView() {
        setExpanded(false);
        onExpansionToggled(true);

        if (mParentListItemExpandCollapseListener != null) {
            mParentListItemExpandCollapseListener.onParentListItemCollapsed(getAdapterPosition());
        }
    }
}
