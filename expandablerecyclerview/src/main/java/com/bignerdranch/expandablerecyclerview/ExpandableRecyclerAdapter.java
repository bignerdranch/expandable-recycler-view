package com.bignerdranch.expandablerecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ExpandableWrapper;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView.Adapter implementation that
 * adds the ability to expand and collapse list items.
 *
 * Changes should be notified through:
 * {@link #notifyParentItemInserted(int)}
 * {@link #notifyParentItemRemoved(int)}
 * {@link #notifyParentItemChanged(int)}
 * {@link #notifyParentItemRangeInserted(int, int)}
 * {@link #notifyChildItemInserted(int, int)}
 * {@link #notifyChildItemRemoved(int, int)}
 * {@link #notifyChildItemChanged(int, int)}
 * methods and not the notify methods of RecyclerView.Adapter.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter<P extends ParentListItem<C>, C, PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ParentViewHolder.ParentListItemExpandCollapseListener {

    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";

    /** Default ViewType for parent rows */
    public static final int TYPE_PARENT = 0;
    /** Default ViewType for children rows */
    public static final int TYPE_CHILD = 1;
    /** Start of user-defined view types */
    public static final int TYPE_FIRST_USER = 2;

    /**
     * A {@link List} of all currently expanded parents and their children, in order.
     * Changes to this list should be made through the add/remove methods
     * available in {@link ExpandableRecyclerAdapter}.
     */
    protected List<ExpandableWrapper<P, C>> mItemList;

    private List<P> mParentItemList;
    private ExpandCollapseListener mExpandCollapseListener;
    private List<RecyclerView> mAttachedRecyclerViewPool;

    /**
     * Allows objects to register themselves as expand/collapse listeners to be
     * notified of change events.
     * <p>
     * Implement this in your {@link android.app.Activity} or {@link android.app.Fragment}
     * to receive these callbacks.
     */
    public interface ExpandCollapseListener {

        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        void onListItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        void onListItemCollapsed(int position);
    }

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     *
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all parents to be displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public ExpandableRecyclerAdapter(@NonNull List<P> parentItemList) {
        super();
        mParentItemList = parentItemList;
        mItemList = generateParentChildItemList(parentItemList);
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either {@link #onCreateParentViewHolder(ViewGroup, int)}
     * or {@link #onCreateChildViewHolder(ViewGroup, int)}.
     *
     * @param viewGroup The {@link ViewGroup} into which the new {@link android.view.View}
     *                  will be added after it is bound to an adapter position.
     * @param viewType The view type of the new {@code android.view.View}.
     * @return A new RecyclerView.ViewHolder
     *         that holds a {@code android.view.View} of the given view type.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (isParentViewType(viewType)) {
            PVH pvh = onCreateParentViewHolder(viewGroup, viewType);
            pvh.setParentListItemExpandCollapseListener(this);
            pvh.mExpandableAdapter = this;
            return pvh;
        } else {
            CVH cvh = onCreateChildViewHolder(viewGroup, viewType);
            cvh.mExpandableAdapter = this;
            return cvh;
        }
    }

    /**
     * Implementation of Adapter.onBindViewHolder(RecyclerView.ViewHolder, int)
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either
     * {@link #onBindParentViewHolder(ParentViewHolder, int, P)} or
     * {@link #onBindChildViewHolder(ChildViewHolder, int, int, C)}.
     *
     * @param holder The RecyclerView.ViewHolder to bind data to
     * @param position The index in the list at which to bind
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableWrapper<P, C> listItem = mItemList.get(position);
        if (listItem.isParent()) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            parentViewHolder.setExpanded(listItem.isExpanded());
            parentViewHolder.mParentListItem = listItem.getParentListItem();
            onBindParentViewHolder(parentViewHolder, getNearestParentPosition(position), listItem.getParentListItem());
        } else {
            CVH childViewHolder = (CVH) holder;
            childViewHolder.mChildListItem = listItem.getChildListItem();
            onBindChildViewHolder(childViewHolder, getNearestParentPosition(position), getChildPosition(position), listItem.getChildListItem());
        }
    }

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a parent.
     *
     * @param parentViewGroup The {@link ViewGroup} in the list for which a {@link PVH} is being
     *                        created
     * @return A {@code PVH} corresponding to the parent with the {@code ViewGroup} parentViewGroup
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType);

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a child.
     *
     * @param childViewGroup The {@link ViewGroup} in the list for which a {@link CVH}
     *                       is being created
     * @return A {@code CVH} corresponding to the child list item with the
     *         {@code ViewGroup} childViewGroup
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup childViewGroup, int viewType);

    /**
     * Callback called from onBindViewHolder(RecyclerView.ViewHolder, int)
     * when the list item bound to is a parent.
     * <p>
     * Bind data to the {@link PVH} here.
     *
     * @param parentViewHolder The {@code PVH} to bind data to
     * @param parentPosition The position of the parent to bind
     * @param parentListItem The parent which holds the data to be bound to the {@code PVH}
     */
    public abstract void onBindParentViewHolder(PVH parentViewHolder, int parentPosition, P parentListItem);

    /**
     * Callback called from onBindViewHolder(RecyclerView.ViewHolder, int)
     * when the list item bound to is a child.
     * <p>
     * Bind data to the {@link CVH} here.
     *
     * @param childViewHolder The {@code CVH} to bind data to
     * @param parentPosition The position of the parent that contains the child to bind
     * @param childPosition The position of the child to bind
     * @param childListItem The child which holds that data to be bound to the {@code CVH}
     */
    public abstract void onBindChildViewHolder(CVH childViewHolder, int parentPosition, int childPosition, C childListItem);

    /**
     * Gets the number of parents and children currently expanded.
     *
     * @return The size of {@link #mItemList}
     */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * For multiple view type support look at overriding {@link #getParentItemViewType(int)} and
     * {@link #getChildItemViewType(int, int)}. Almost all cases should override those instead
     * of this method.
     *
     * @param position The index in the list to get the view type of
     * @return Gets the view type of the item at the given position.
     */
    @Override
    public int getItemViewType(int position) {
        ExpandableWrapper<P, C> listItem = mItemList.get(position);
        if (listItem.isParent()) {
            return getParentItemViewType(getNearestParentPosition(position));
        } else {
            return getChildItemViewType(getNearestParentPosition(position), getChildPosition(position));
        }
    }

    /**
     * Return the view type of the parent item at {@code parentPosition} for the purposes
     * of view recycling.
     * <p>
     * The default implementation of this method returns {@link #TYPE_PARENT}, making the assumption of
     * a single view type for the parent items in this adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     * <p>
     * If you are overriding this method make sure to override {@link #isParentViewType(int)} as well.
     * <p>
     * Start your defined viewtypes at {@link #TYPE_FIRST_USER}
     *
     * @param parentPosition The index of the parent to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 {@code parentPosition}. Type codes need not be contiguous.
     */
    public int getParentItemViewType(int parentPosition) {
        return TYPE_PARENT;
    }


    /**
     * Return the view type of the child item {@code parentPosition} contained within the parent
     * at {@code parentPosition} for the purposes of view recycling.
     * <p>
     * The default implementation of this method returns {@link #TYPE_CHILD}, making the assumption of
     * a single view type for the children in this adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     * <p>
     * Start your defined viewtypes at {@link #TYPE_FIRST_USER}
     *
     * @param parentPosition The index of the parent continaing the child to query
     * @param childPosition The index of the child within the parent to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 {@code parentPosition}. Type codes need not be contiguous.
     */
    public int getChildItemViewType(int parentPosition, int childPosition) {
        return TYPE_CHILD;
    }

    /**
     * Used to determine whether a viewType is that of a parent or not, for ViewHolder creation purposes.
     * <p>
     * Only override if {@link #getParentItemViewType(int)} is being overriden
     *
     * @param viewType the viewType identifier in question
     * @return whether the given viewType belongs to a parent view
     */
    public boolean isParentViewType(int viewType) {
        return viewType == TYPE_PARENT;
    }

    /**
     * Gets the list of parents that is backing this adapter.
     * Changes can be made to the list and the adapter notified via the
     * {@link #notifyParentItemInserted(int)}
     * {@link #notifyParentItemRemoved(int)}
     * {@link #notifyParentItemChanged(int)}
     * {@link #notifyParentItemRangeInserted(int, int)}
     * {@link #notifyChildItemInserted(int, int)}
     * {@link #notifyChildItemRemoved(int, int)}
     * {@link #notifyChildItemChanged(int, int)}
     * methods.
     *
     *
     * @return The list of parents that this adapter represents
     */
    public List<P> getParentItemList() {
        return mParentItemList;
    }

    /**
     * Implementation of {@link com.bignerdranch.expandablerecyclerview.ParentViewHolder.ParentListItemExpandCollapseListener#onParentListItemExpanded(int)}.
     * <p>
     * Called when a {@link P} is triggered to expand.
     * TODO: Clear up confusion about what index this refers to
     *
     * @param position The index of the item in the list being expanded
     */
    @Override
    public void onParentListItemExpanded(int position) {
        ExpandableWrapper<P, C> listItem = mItemList.get(position);
        if (listItem.isParent()) {
            expandParentListItem(listItem, position, true);
        }
    }

    /**
     * Implementation of {@link com.bignerdranch.expandablerecyclerview.ParentViewHolder.ParentListItemExpandCollapseListener#onParentListItemCollapsed(int)}.
     * <p>
     * Called when a {@link P} is triggered to collapse.
     * TODO: Clear up confusion about what index this refers to
     *
     * @param position The index of the item in the list being collapsed
     */
    @Override
    public void onParentListItemCollapsed(int position) {
        ExpandableWrapper<P, C> listItem = mItemList.get(position);
        if (listItem.isParent()) {
            collapseParentListItem(listItem, position, true);
        }
    }

    /**
     * Implementation of Adapter#onAttachedToRecyclerView(RecyclerView).
     * <p>
     * Called when this {@link ExpandableRecyclerAdapter} is attached to a RecyclerView.
     *
     * @param recyclerView The {@code RecyclerView} this {@code ExpandableRecyclerAdapter}
     *                     is being attached to
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.add(recyclerView);
    }

    /**
     * Implementation of Adapter.onDetachedFromRecyclerView(RecyclerView)
     * <p>
     * Called when this ExpandableRecyclerAdapter is detached from a RecyclerView.
     *
     * @param recyclerView The {@code RecyclerView} this {@code ExpandableRecyclerAdapter}
     *                     is being detached from
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.remove(recyclerView);
    }

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    // region Programmatic Expansion/Collapsing

    /**
     * Expands the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to expand
     */
    public void expandParent(int parentIndex) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentIndex);

        ExpandableWrapper<P, C> listItem = mItemList.get(parentWrapperIndex);
        if (!listItem.isParent()) {
            return;
        }

        expandViews(listItem, parentWrapperIndex);
    }

    /**
     * Expands the parent associated with a specified {@link P} in the list of parents.
     *
     * @param parentListItem The {@code P} of the parent to expand
     */
    public void expandParent(P parentListItem) {
        ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        expandViews(mItemList.get(parentWrapperIndex), parentWrapperIndex);
    }

    /**
     * Expands all parents in a range of indices in the list of parents.
     *
     * @param startParentIndex The index at which to to start expanding parents
     * @param parentCount The number of parents to expand
     */
    public void expandParentRange(int startParentIndex, int parentCount) {
        int endParentIndex = startParentIndex + parentCount;
        for (int i = startParentIndex; i < endParentIndex; i++) {
            expandParent(i);
        }
    }

    /**
     * Expands all parents in the list.
     */
    public void expandAllParents() {
        for (P parentListItem : mParentItemList) {
            expandParent(parentListItem);
        }
    }

    /**
     * Collapses the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to collapse
     */
    public void collapseParent(int parentIndex) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentIndex);

        ExpandableWrapper<P, C> listItem = mItemList.get(parentWrapperIndex);
        if (!listItem.isParent()) {
            return;
        }

        collapseViews(listItem, parentWrapperIndex);
    }

    /**
     * Collapses the parent associated with a specified {@link P} in the list of parents.
     *
     * @param parentListItem The {@code P} of the parent to collapse
     */
    public void collapseParent(P parentListItem) {
        ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        collapseViews(mItemList.get(parentWrapperIndex), parentWrapperIndex);
    }

    /**
     * Collapses all parents in a range of indices in the list of parents.
     *
     * @param startParentIndex The index at which to to start collapsing parents
     * @param parentCount The number of parents to collapse
     */
    public void collapseParentRange(int startParentIndex, int parentCount) {
        int endParentIndex = startParentIndex + parentCount;
        for (int i = startParentIndex; i < endParentIndex; i++) {
            collapseParent(i);
        }
    }

    /**
     * Collapses all parents in the list.
     */
    public void collapseAllParents() {
        for (P parentListItem : mParentItemList) {
            collapseParent(parentListItem);
        }
    }

    /**
     * Stores the expanded state map across state loss.
     * <p>
     * Should be called from {@link Activity#onSaveInstanceState(Bundle)} in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p>
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in {@link #onRestoreInstanceState(Bundle)}.
     *
     * @param savedInstanceState The {@code Bundle} into which to store the
     *                           expanded state map
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap());
    }

    /**
     * Fetches the expandable state map from the saved instance state {@link Bundle}
     * and restores the expanded states of all of the list items.
     * <p>
     * Should be called from {@link Activity#onRestoreInstanceState(Bundle)} in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p>
     * Assumes that the list of parent list items is the same as when the saved
     * instance state was stored.
     *
     * @param savedInstanceState The {@code Bundle} from which the expanded
     *                           state map is loaded
     */
    @SuppressWarnings("unchecked")
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceState.getSerializable(EXPANDED_STATE_MAP);
        if (expandedStateMap == null) {
            return;
        }

        List<ExpandableWrapper<P, C>> itemList = new ArrayList<>();
        int parentListItemCount = mParentItemList.size();
        for (int i = 0; i < parentListItemCount; i++) {
            ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(mParentItemList.get(i));
            itemList.add(parentWrapper);

            if (expandedStateMap.containsKey(i)) {
                boolean expanded = expandedStateMap.get(i);
                if (expanded) {
                    parentWrapper.setExpanded(true);

                    List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
                    int childListItemCount = wrappedChildItemList.size();
                    for (int j = 0; j < childListItemCount; j++) {
                        ExpandableWrapper<P, C> childWrapper = wrappedChildItemList.get(j);
                        itemList.add(childWrapper);
                    }
                }
            }
        }

        mItemList = itemList;

        notifyDataSetChanged();
    }

    /**
     * Gets the list item held at the specified adapter position.
     *
     * @param position The index of the list item to return
     * @return The list item at the specified position
     */
    protected Object getListItem(int position) {
        boolean indexInRange = position >= 0 && position < mItemList.size();
        if (indexInRange) {
            ExpandableWrapper<P, C> listItem = mItemList.get(position);
            if (listItem.isParent()) {
                return listItem.getParentListItem();
            } else {
                return listItem.getChildListItem();
            }
        } else {
            return null;
        }
    }

    /**
     * Calls through to the ParentViewHolder to expand views for each
     * RecyclerView the specified parent is a child of.
     *
     * These calls to the ParentViewHolder are made so that animations can be
     * triggered at the ViewHolder level.
     *
     * @param parentIndex The index of the parent to expand
     */
    @SuppressWarnings("unchecked")
    private void expandViews(ExpandableWrapper<P, C> parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && !viewHolder.isExpanded()) {
                viewHolder.setExpanded(true);
                viewHolder.onExpansionToggled(false);
            }
        }

        expandParentListItem(parentWrapper, parentIndex, false);
    }

    /**
     * Calls through to the ParentViewHolder to collapse views for each
     * RecyclerView a specified parent is a child of.
     *
     * These calls to the ParentViewHolder are made so that animations can be
     * triggered at the ViewHolder level.
     *
     * @param parentIndex The index of the parent to collapse
     */
    @SuppressWarnings("unchecked")
    private void collapseViews(ExpandableWrapper<P, C> parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && viewHolder.isExpanded()) {
                viewHolder.setExpanded(false);
                viewHolder.onExpansionToggled(true);
            }
        }

        collapseParentListItem(parentWrapper, parentIndex, false);
    }

    /**
     * Expands a specified parent. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper The ExpandableWrapper of the parent to expand
     * @param parentIndex The index of the parent to expand
     * @param expansionTriggeredByListItemClick true if expansion was triggered
     *                                          by a click event, false otherwise.
     */
    private void expandParentListItem(ExpandableWrapper<P, C> parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);

            List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
            if (wrappedChildItemList != null) {
                int childListItemCount = wrappedChildItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    mItemList.add(parentIndex + i + 1, wrappedChildItemList.get(i));
                }

                notifyItemRangeInserted(parentIndex + 1, childListItemCount);
            }

            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                mExpandCollapseListener.onListItemExpanded(getNearestParentPosition(parentIndex));
            }
        }
    }

    /**
     * Collapses a specified parent item. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper The ExpandableWrapper of the parent to collapse
     * @param parentIndex The index of the parent to collapse
     * @param collapseTriggeredByListItemClick true if expansion was triggered
     *                                         by a click event, false otherwise.
     */
    private void collapseParentListItem(ExpandableWrapper<P, C> parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
            if (wrappedChildItemList != null) {
                int childListItemCount = wrappedChildItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mItemList.remove(parentIndex + i + 1);
                }

                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
            }

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                mExpandCollapseListener.onListItemCollapsed(getNearestParentPosition(parentIndex));
            }
        }
    }

    /**
     * Given the index relative to the entire RecyclerView, returns the nearest
     * ParentPosition without going past the given index.
     *
     * If it is the index of a parent, will return the corresponding parent position.
     * If it is the index of a child within the RV, will return the position of that child's parent.
     */
    int getNearestParentPosition(int fullPosition) {
        if (fullPosition == 0) {
            return 0;
        }

        int parentCount = -1;
        for (int i = 0; i <= fullPosition; i++) {
            ExpandableWrapper<P, C> listItem = mItemList.get(i);
            if (listItem.isParent()) {
                parentCount++;
            }
        }
        return parentCount;
    }

    /**
     * Given the index relative to the entire RecyclerView for a child item,
     * returns the child position within the child list of the parent.
     */
    int getChildPosition(int fullPosition) {
        if (fullPosition == 0) {
            return 0;
        }

        int childCount = 0;
        for (int i = 0; i < fullPosition; i++) {
            ExpandableWrapper<P, C> listItem = mItemList.get(i);
            if (listItem.isParent()) {
                childCount = 0;
            } else {
                childCount++;
            }
        }
        return childCount;
    }

    // endregion

    // region Data Manipulation

    /**
     * Notify any registered observers that the parent reflected at {@code parentPosition}
     * has been newly inserted. The parent previously at {@code parentPosition} is now at
     * position {@code parentPosition + 1}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.
     *
     * @param parentPosition Position of the newly inserted parent in the data set, relative
     *                       to the list of parents only.
     *
     * @see #notifyParentItemRangeInserted(int, int)
     */
    public void notifyParentItemInserted(int parentPosition) {
        P parentListItem = mParentItemList.get(parentPosition);

        int wrapperIndex;
        if (parentPosition < mParentItemList.size() - 1) {
            wrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        } else {
            wrapperIndex = mItemList.size();
        }

        int sizeChanged = addParentWrapper(wrapperIndex, parentListItem);
        notifyItemRangeInserted(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the currently reflected {@code itemCount}
     * parents starting at {@code parentPositionStart} have been newly inserted.
     * The parents previously located at {@code parentPositionStart} and beyond
     * can now be found starting at position {@code parentPositionStart + itemCount}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPositionStart Position of the first parent that was inserted, relative
     *                            to the list of parents only.
     * @param itemCount Number of items inserted
     *
     * @see #notifyParentItemInserted(int)
     */
    public void notifyParentItemRangeInserted(int parentPositionStart, int itemCount) {
        int initialWrapperIndex;
        if (parentPositionStart < mParentItemList.size() - itemCount) {
            initialWrapperIndex = getParentExpandableWrapperIndex(parentPositionStart);
        } else {
            initialWrapperIndex = mItemList.size();
        }

        int sizeChanged = 0;
        int wrapperIndex = initialWrapperIndex;
        int changed;
        int parentPositionEnd = parentPositionStart + itemCount;
        for (int i = parentPositionStart; i < parentPositionEnd; i++) {
            P parentListItem = mParentItemList.get(i);
            changed = addParentWrapper(wrapperIndex, parentListItem);
            wrapperIndex += changed;
            sizeChanged += changed;
        }

        notifyItemRangeInserted(initialWrapperIndex, sizeChanged);
    }

    private int addParentWrapper(int wrapperIndex, P parentListItem) {
        int sizeChanged = 1;
        ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parentListItem);
        mItemList.add(wrapperIndex, parentWrapper);
        if (parentWrapper.isParentInitiallyExpanded()) {
            parentWrapper.setExpanded(true);
            List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
            mItemList.addAll(wrapperIndex + sizeChanged, wrappedChildItemList);
            sizeChanged += wrappedChildItemList.size();
        }
        return sizeChanged;
    }

    /**
     * Notify any registered observers that the parents previously located at {@code parentPosition}
     * has been removed from the data set. The parents previously located at and after
     * {@code parentPosition} may now be found at {@code oldPosition - 1}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPosition Position of the parent that has now been removed, relative
     *                       to the list of parents only.
     */
    public void notifyParentItemRemoved(int parentPosition) {
        int wrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        int sizeChanged = removeParentWrapper(wrapperIndex);

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the {@code itemCount} parents previously
     * located at {@code parentPositionStart} have been removed from the data set. The parents
     * previously located at and after {@code parentPositionStart + itemCount} may now be
     * found at {@code oldPosition - itemCount}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPositionStart The previous position of the first parent that was
     *                            removed, relative to list of parents only.
     * @param itemCount Number of parents removed from the data set
     */
    public void notifyParentItemRangeRemoved(int parentPositionStart, int itemCount) {
        int sizeChanged = 0;
        int wrapperIndex = getParentExpandableWrapperIndex(parentPositionStart);
        for (int i = 0; i < itemCount; i++) {
            sizeChanged += removeParentWrapper(wrapperIndex);
        }

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    private int removeParentWrapper(int parentWrapperIndex) {
        int sizeChanged = 1;
        ExpandableWrapper<P, C> parentWrapper = mItemList.remove(parentWrapperIndex);
        if (parentWrapper.isExpanded()) {
            int childListSize = parentWrapper.getWrappedChildItemList().size();
            for (int i = 0; i < childListSize; i++) {
                mItemList.remove(parentWrapperIndex);
                sizeChanged++;
            }
        }
        return sizeChanged;
    }

    /**
     * Notify any registered observers that the parent at {@code parentPosition} has changed.
     * This will also trigger an item changed for children of the parent list specified.
     * <p>
     * This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at {@code parentPosition} is out of date and should be updated.
     * The parent at {@code parentPosition} retains the same identity. This means
     * the number of children must stay the same.
     *
     * @param parentPosition Position of the item that has changed
     */
    public void notifyParentItemChanged(int parentPosition) {
        P parentListItem = mParentItemList.get(parentPosition);
        int wrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        int sizeChanged = changeParentWrapper(wrapperIndex, parentListItem);

        notifyItemRangeChanged(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the {@code itemCount} parents starting
     * at {@code parentPositionStart} have changed. This will also trigger an item changed
     * for children of the parent list specified.
     * <p>
     * This is an item change event, not a structural change event. It indicates that any
     * reflection of the data in the given position range is out of date and should be updated.
     * The parents in the given range retain the same identity. This means that the number of
     * children must stay the same.
     *
     * @param parentPositionStart Position of the item that has changed
     * @param itemCount Number of parents changed in the data set
     */
    public void notifyParentItemRangeChanged(int parentPositionStart, int itemCount) {
        int initialWrapperIndex = getParentExpandableWrapperIndex(parentPositionStart);

        int wrapperIndex = initialWrapperIndex;
        int sizeChanged = 0;
        int changed;
        P parentListItem;
        for (int j = 0; j < itemCount; j++) {
            parentListItem = mParentItemList.get(parentPositionStart);
            changed = changeParentWrapper(wrapperIndex, parentListItem);
            sizeChanged += changed;
            wrapperIndex += changed;
            parentPositionStart++;
        }
        notifyItemRangeChanged(initialWrapperIndex, sizeChanged);
    }

    private int changeParentWrapper(int wrapperIndex, P parentListItem) {
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(wrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        int sizeChanged = 1;
        if (parentWrapper.isExpanded()) {
            List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
            int childListSize = wrappedChildItemList.size();
            for (int i = 0; i < childListSize; i++) {
                mItemList.set(wrapperIndex + i + 1, wrappedChildItemList.get(i));
                sizeChanged++;
            }
        }

        return sizeChanged;
    }

    /**
     * Notify any registered observers that the parent and its children reflected at
     * {@code fromParentPosition} has been moved to {@code toParentPosition}.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.</p>
     *
     * @param fromParentPosition Previous position of the parent, relative to the list of
     *                           parents only.
     * @param toParentPosition New position of the parent, relative to the list of parents only.
     */
    public void notifyParentItemMoved(int fromParentPosition, int toParentPosition) {
        int fromWrapperIndex = getParentExpandableWrapperIndex(fromParentPosition);
        ExpandableWrapper<P, C> fromParentWrapper = mItemList.get(fromWrapperIndex);

        // If the parent is collapsed we can take advantage of notifyItemMoved otherwise
        // we are forced to do a "manual" move by removing and then adding the parent + children
        // (no notifyItemRangeMovedAvailable)
        boolean isCollapsed = !fromParentWrapper.isExpanded();
        boolean isExpandedNoChildren = !isCollapsed && (fromParentWrapper.getWrappedChildItemList().size() == 0);
        if (isCollapsed || isExpandedNoChildren) {
            int toWrapperIndex = getParentExpandableWrapperIndex(toParentPosition);
            ExpandableWrapper<P, C> toParentWrapper = mItemList.get(toWrapperIndex);
            mItemList.remove(fromWrapperIndex);
            int childOffset = 0;
            if (toParentWrapper.isExpanded()) {
                childOffset = toParentWrapper.getWrappedChildItemList().size();
            }
            mItemList.add(toWrapperIndex + childOffset, fromParentWrapper);

            notifyItemMoved(fromWrapperIndex, toWrapperIndex + childOffset);
        } else {
            // Remove the parent and children
            int sizeChanged = 0;
            int childListSize = fromParentWrapper.getWrappedChildItemList().size();
            for (int i = 0; i < childListSize + 1; i++) {
                mItemList.remove(fromWrapperIndex);
                sizeChanged++;
            }
            notifyItemRangeRemoved(fromWrapperIndex, sizeChanged);


            // Add the parent and children at new position
            int toWrapperIndex = getParentExpandableWrapperIndex(toParentPosition);
            int childOffset = 0;
            if (toWrapperIndex != -1) {
                ExpandableWrapper<P, C> toParentWrapper = mItemList.get(toWrapperIndex);
                if (toParentWrapper.isExpanded()) {
                    childOffset = toParentWrapper.getWrappedChildItemList().size();
                }
            } else {
                toWrapperIndex = mItemList.size();
            }

            mItemList.add(toWrapperIndex + childOffset, fromParentWrapper);
            List<ExpandableWrapper<P, C>> childItemList = fromParentWrapper.getWrappedChildItemList();
            sizeChanged = childItemList.size() + 1;

            mItemList.addAll(toWrapperIndex + childOffset + 1, childItemList);

            notifyItemRangeInserted(toWrapperIndex + childOffset, sizeChanged);
        }
    }

    /**
     * Notify any registered observers that the parent reflected at {@code parentPosition}
     * has a child list item that has been newly inserted at {@code childPosition}.
     * The child list item previously at {@code childPosition} is now at
     * position {@code childPosition + 1}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.
     *
     * @param parentPosition Position of the parent which has been added a child, relative
     *                       to the list of parents only.
     * @param childPosition Position of the child that has been inserted, relative to children
     *                      of the parent specified by {@code parentPosition} only.
     *
     */
    public void notifyChildItemInserted(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);

        parentWrapper.setParentListItem(mParentItemList.get(parentPosition));
        if (parentWrapper.isExpanded()) {
            ExpandableWrapper<P, C> child = parentWrapper.getWrappedChildItemList().get(childPosition);
            mItemList.add(parentWrapperIndex + childPosition + 1, child);
            notifyItemInserted(parentWrapperIndex + childPosition + 1);
        }
    }

    /**
     * Notify any registered observers that the parent reflected at {@code parentPosition}
     * has {@code itemCount} child list items that have been newly inserted at {@code childPositionStart}.
     * The child list item previously at {@code childPositionStart} and beyond are now at
     * position {@code childPositionStart + itemCount}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.
     *
     * @param parentPosition Position of the parent which has been added a child, relative
     *                       to the list of parents only.
     * @param childPositionStart Position of the first child that has been inserted,
     *                           relative to children of the parent specified by
     *                           {@code parentPosition} only.
     * @param itemCount number of children inserted
     *
     */
    public void notifyChildItemRangeInserted(int parentPosition, int childPositionStart, int itemCount) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);

        parentWrapper.setParentListItem(mParentItemList.get(parentPosition));
        if (parentWrapper.isExpanded()) {
            List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
            for (int i = 0; i < itemCount; i++) {
                ExpandableWrapper<P, C> child = wrappedChildItemList.get(childPositionStart + i);
                mItemList.add(parentWrapperIndex + childPositionStart + i + 1, child);
            }
            notifyItemRangeInserted(parentWrapperIndex + childPositionStart + 1, itemCount);
        }
    }

    /**
     * Notify any registered observers that the parent located at {@code parentPosition}
     * has a child that has been removed from the data set, previously located at {@code childPosition}.
     * The child list item previously located at and after {@code childPosition} may
     * now be found at {@code childPosition - 1}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPosition Position of the parent which has a child removed from, relative
     *                       to the list of parents only.
     * @param childPosition Position of the child that has been removed, relative to children
     *                      of the parent specified by {@code parentPosition} only.
     */
    public void notifyChildItemRemoved(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(mParentItemList.get(parentPosition));

        if (parentWrapper.isExpanded()) {
            mItemList.remove(parentWrapperIndex + childPosition + 1);
            notifyItemRemoved(parentWrapperIndex + childPosition + 1);
        }
    }

    /**
     * Notify any registered observers that the parent located at {@code parentPosition}
     * has {@code itemCount} children that have been removed from the data set, previously
     * located at {@code childPositionStart} onwards. The child previously located at and
     * after {@code childPositionStart} may now be found at {@code childPositionStart - itemCount}.
     * <p>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPosition Position of the parent which has a child removed from, relative
     *                       to the list of parents only.
     * @param childPositionStart Position of the first child that has been removed, relative
     *                           to children of the parent specified by {@code parentPosition} only.
     * @param itemCount number of children removed
     */
    public void notifyChildItemRangeRemoved(int parentPosition, int childPositionStart, int itemCount) {
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(mParentItemList.get(parentPosition));

        if (parentWrapper.isExpanded()) {
            for (int i = 0; i < itemCount; i++) {
                mItemList.remove(parentWrapperIndex + childPositionStart + 1);
            }
            notifyItemRangeRemoved(parentWrapperIndex + childPositionStart + 1, itemCount);
        }
    }

    /**
     * Notify any registered observers that the parent at {@code parentPosition} has
     * a child located at {@code childPosition} that has changed.
     * <p>
     * This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at {@code childPosition} is out of date and should be updated.
     * The parent at {@code childPosition} retains the same identity.
     *
     * @param parentPosition Position of the parent which has a child that has changed
     * @param childPosition Position of the child that has changed
     */
    public void notifyChildItemChanged(int parentPosition, int childPosition) {
        P parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        if (parentWrapper.isExpanded()) {
            int listChildPosition = parentWrapperIndex + childPosition + 1;
            ExpandableWrapper<P, C> child = parentWrapper.getWrappedChildItemList().get(childPosition);
            mItemList.set(listChildPosition, child);
            notifyItemChanged(listChildPosition);
        }
    }

    /**
     * Notify any registered observers that the parent at {@code parentPosition} has
     * {@code itemCount} children starting at {@code childPositionStart} that have changed.
     * <p>
     * This is an item change event, not a structural change event. It indicates that any
     * The parent at {@code childPositionStart} retains the same identity.
     * reflection of the set of {@code itemCount} children starting at {@code childPositionStart}
     * are out of date and should be updated.
     *
     * @param parentPosition Position of the parent who has a child that has changed
     * @param childPositionStart Position of the first child that has changed
     * @param itemCount number of children changed
     */
    public void notifyChildItemRangeChanged(int parentPosition, int childPositionStart, int itemCount) {
        P parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);
        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        if (parentWrapper.isExpanded()) {
            int listChildPosition = parentWrapperIndex + childPositionStart + 1;
            for (int i = 0; i < itemCount; i++) {
                ExpandableWrapper<P, C> child
                        = parentWrapper.getWrappedChildItemList().get(childPositionStart + i);
                mItemList.set(listChildPosition + i, child);
            }
            notifyItemRangeChanged(listChildPosition, itemCount);
        }
    }

    /**
     * Notify any registered observers that the child list item contained within the parent
     * at {@code parentPosition} has moved from {@code fromChildPosition} to {@code toChildPosition}.
     *
     * <p>This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.</p>
     *
     * @param parentPosition Position of the parent which has a child that has moved
     * @param fromChildPosition Previous position of the child
     * @param toChildPosition New position of the child
     */
    public void notifyChildItemMoved(int parentPosition, int fromChildPosition, int toChildPosition) {
        P parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentExpandableWrapperIndex(parentPosition);

        ExpandableWrapper<P, C> parentWrapper = mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        if (parentWrapper.isExpanded()) {
            ExpandableWrapper<P, C> fromChild = mItemList.remove(parentWrapperIndex + 1 + fromChildPosition);
            mItemList.add(parentWrapperIndex + 1 + toChildPosition, fromChild);
            notifyItemMoved(parentWrapperIndex + 1 + fromChildPosition, parentWrapperIndex + 1 + toChildPosition);
        }
    }

    // endregion

    /**
     * Generates a full list of all parents and their children, in order.
     *
     * @param parentItemList A list of the parents from
     *                       the {@link ExpandableRecyclerAdapter}
     * @return A list of all parents and their children, expanded
     */
    private List<ExpandableWrapper<P, C>> generateParentChildItemList(List<P> parentItemList) {
        List<ExpandableWrapper<P, C>> itemList = new ArrayList<>();

        int parentListItemCount = parentItemList.size();
        for (int i = 0; i < parentListItemCount; i++) {
            ExpandableWrapper<P, C> parentWrapper = new ExpandableWrapper<>(parentItemList.get(i));
            itemList.add(parentWrapper);

            if (parentWrapper.isParentInitiallyExpanded()) {
                parentWrapper.setExpanded(true);

                List<ExpandableWrapper<P, C>> wrappedChildItemList = parentWrapper.getWrappedChildItemList();
                int childListItemCount = wrappedChildItemList.size();
                for (int j = 0; j < childListItemCount; j++) {
                    ExpandableWrapper<P, C> childWrapper = wrappedChildItemList.get(j);
                    itemList.add(childWrapper);
                }
            }
        }

        return itemList;
    }

    /**
     * Generates a HashMap used to store expanded state for items in the list
     * on configuration change or whenever onResume is called.
     *
     * @return A HashMap containing the expanded state of all parents
     */
    private HashMap<Integer, Boolean> generateExpandedStateMap() {
        HashMap<Integer, Boolean> parentListItemHashMap = new HashMap<>();
        int childCount = 0;

        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) != null) {
                ExpandableWrapper<P, C> listItem = mItemList.get(i);
                if (listItem.isParent()) {
                    parentListItemHashMap.put(i - childCount, listItem.isExpanded());
                } else {
                    childCount++;
                }
            }
        }

        return parentListItemHashMap;
    }

    /**
     * Gets the index of a ExpandableWrapper within the helper item list based on
     * the index of the ExpandableWrapper.
     *
     * @param parentIndex The index of the parent in the list of parents
     * @return The index of the parent in the list of all views in the adapter
     */
    private int getParentExpandableWrapperIndex(int parentIndex) {
        int parentCount = 0;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i).isParent()) {
                parentCount++;

                if (parentCount > parentIndex) {
                    return i;
                }
            }
        }

        return -1;
    }
}
