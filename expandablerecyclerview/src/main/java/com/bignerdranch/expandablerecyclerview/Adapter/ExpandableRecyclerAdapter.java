package com.bignerdranch.expandablerecyclerview.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} implementation that
 * adds the ability to expand and collapse list items.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentViewHolder.ParentListItemExpandCollapseListener {

    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    /**
     * A {@link List} of all currently expanded {@link ParentListItem} objects
     * and their children, in order. Changes to this list should be made through the add/remove methods
     * available in {@link ExpandableRecyclerAdapter}
     */
    protected List<Object> mItemList;

    /**
     * A {@link List} of all {@link ParentListItem} objects.
     */
    protected List<? extends ParentListItem> mParentItemList;

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
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the {@link RecyclerView} that this
     *                       adapter is linked to
     */
    public ExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
        super();
        mParentItemList = parentItemList;
        mItemList = ExpandableRecyclerAdapterHelper.generateParentChildItemList(parentItemList);
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    /**
     * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either {@link #onCreateParentViewHolder(ViewGroup)}
     * or {@link #onCreateChildViewHolder(ViewGroup)}.
     *
     * @param viewGroup The {@link ViewGroup} into which the new {@link android.view.View}
     *                  will be added after it is bound to an adapter position.
     * @param viewType The view type of the new {@code android.view.View}.
     * @return A new {@link android.support.v7.widget.RecyclerView.ViewHolder}
     *         that holds a {@code android.view.View} of the given view type.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARENT) {
            PVH pvh = onCreateParentViewHolder(viewGroup);
            pvh.setParentListItemExpandCollapseListener(this);
            return pvh;
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    /**
     * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either {@link #onBindParentViewHolder(ParentViewHolder, int, ParentListItem)}
     * or {@link #onBindChildViewHolder(ChildViewHolder, int, Object)}.
     *
     * @param holder The {@link android.support.v7.widget.RecyclerView.ViewHolder}
     *               to bind data to
     * @param position The index in the list at which to bind
     * @throws IllegalStateException if the item in the list is either null or
     *         not of type {@link ParentListItem}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            ParentWrapper parentWrapper = (ParentWrapper) listItem;
            parentViewHolder.setExpanded(parentWrapper.isExpanded());
            onBindParentViewHolder(parentViewHolder, position, parentWrapper.getParentListItem());
        } else if (listItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((CVH) holder, position, listItem);
        }
    }

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a parent.
     *
     * @param parentViewGroup The {@link ViewGroup} in the list for which a {@link PVH}
     *                        is being created
     * @return A {@code PVH} corresponding to the {@link ParentListItem} with
     *         the {@code ViewGroup} parentViewGroup
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parentViewGroup);

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a child.
     *
     * @param childViewGroup The {@link ViewGroup} in the list for which a {@link CVH}
     *                       is being created
     * @return A {@code CVH} corresponding to the child list item with the
     *         {@code ViewGroup} childViewGroup
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup childViewGroup);

    /**
     * Callback called from {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}
     * when the list item bound to is a parent.
     * <p>
     * Bind data to the {@link PVH} here.
     *
     * @param parentViewHolder The {@code PVH} to bind data to
     * @param position The index in the list at which to bind
     * @param parentListItem The {@link ParentListItem} which holds the data to
     *                       be bound to the {@code PVH}
     */
    public abstract void onBindParentViewHolder(PVH parentViewHolder, int position, ParentListItem parentListItem);

    /**
     * Callback called from {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}
     * when the list item bound to is a child.
     * <p>
     * Bind data to the {@link CVH} here.
     *
     * @param childViewHolder The {@code CVH} to bind data to
     * @param position The index in the list at which to bind
     * @param childListItem The child list item which holds that data to be
     *                      bound to the {@code CVH}
     */
    public abstract void onBindChildViewHolder(CVH childViewHolder, int position, Object childListItem);

    /**
     * Gets the number of parent and child objects currently expanded.
     *
     * @return The size of {@link #mItemList}
     */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The index in the list to get the view type of
     * @return {@value #TYPE_PARENT} for {@link ParentListItem} and {@value #TYPE_CHILD}
     *         for child list items
     * @throws IllegalStateException if the item at the given position in the list is null
     */
    @Override
    public int getItemViewType(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            return TYPE_PARENT;
        } else if (listItem == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_CHILD;
        }
    }

    /**
     * Implementation of {@link com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder.ParentListItemExpandCollapseListener#onParentListItemExpanded(int)}.
     * <p>
     * Called when a {@link ParentListItem} is triggered to expand.
     *
     * @param position The index of the item in the list being expanded
     */
    @Override
    public void onParentListItemExpanded(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            expandParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    /**
     * Implementation of {@link com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder.ParentListItemExpandCollapseListener#onParentListItemCollapsed(int)}.
     * <p>
     * Called when a {@link ParentListItem} is triggered to collapse.
     *
     * @param position The index of the item in the list being collapsed
     */
    @Override
    public void onParentListItemCollapsed(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            collapseParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    /**
     * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter#onAttachedToRecyclerView(RecyclerView)}.
     * <p>
     * Called when this {@link ExpandableRecyclerAdapter} is attached to a {@link RecyclerView}.
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
     * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter#onDetachedFromRecyclerView(RecyclerView)}.
     * <p>
     * Called when this {@link ExpandableRecyclerAdapter} is detached from a {@link RecyclerView}.
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
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
             parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands the parent associated with a specified {@link ParentListItem} in
     * the list of parents.
     *
     * @param parentListItem The {@code ParentListItem} of the parent to expand
     */
    public void expandParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands all parents in the list.
     */
    public void expandAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            expandParent(parentListItem);
        }
    }

    /**
     * Collapses the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to collapse
     */
    public void collapseParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses the parent associated with a specified {@link ParentListItem} in
     * the list of parents.
     *
     * @param parentListItem The {@code ParentListItem} of the parent to collapse
     */
    public void collapseParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses all parents in the list.
     */
    public void collapseAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            collapseParent(parentListItem);
        }
    }

    /**
     * Stores the expanded state map across state loss.
     * <p>
     * Should be called from {@link Activity#onSaveInstanceState(Bundle)} in
     * the {@link Activity} that hosts the {@link RecyclerView} that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p>
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in {@link #onRestoreInstanceState(Bundle)}.
     *
     * @param savedInstanceState The {@code Bundle} into which to store the
     *                           expanded state map
     * @return The saved instance state {@code Bundle} with the expanded state
     *         map added
     */
    public Bundle onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap());
        return savedInstanceState;
    }

    /**
     * Fetches the expandable state map from the saved instance state {@link Bundle}
     * and restores the expanded states of all of the list items.
     * <p>
     * Should be called from {@link Activity#onRestoreInstanceState(Bundle)} in
     * the {@link Activity} that hosts the {@link RecyclerView} that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p>
     * Assumes that the list of parent list items is the same as when the saved
     * instance state was stored.
     *
     * @param savedInstanceState The {@code Bundle} from which the expanded
     *                           state map is loaded
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceState.getSerializable(EXPANDED_STATE_MAP);
        int fullCount = 0;
        int childCount = 0;
        Object listItem;
        ParentWrapper parentWrapper;
        List<?> childItemList;
        int listItemCount = mItemList.size();
        while (fullCount < listItemCount) {
            listItem = getListItem(fullCount);

            if (listItem instanceof ParentWrapper) {
                parentWrapper = (ParentWrapper) listItem;

                if (expandedStateMap.containsKey(fullCount - childCount)) {
                    parentWrapper.setExpanded(expandedStateMap.get(fullCount - childCount));


                    if (parentWrapper.isExpanded() && !parentWrapper.isInitiallyExpanded()) {
                        childItemList = parentWrapper.getChildItemList();

                        if (childItemList != null) {
                            int childListItemCount = childItemList.size();
                            for (int j = 0; j < childListItemCount; j++) {
                                fullCount++;
                                childCount++;
                                mItemList.add(fullCount, childItemList.get(j));
                            }
                        }
                    } else if (!parentWrapper.isExpanded() && parentWrapper.isInitiallyExpanded()) {
                        childItemList = parentWrapper.getChildItemList();
                        int childListItemCount = childItemList.size();
                        for (int j = 0; j < childListItemCount; j++) {
                            mItemList.remove(fullCount + 1);
                        }
                    }
                }
            } else {
                childCount++;
            }

            fullCount++;
        }

        notifyDataSetChanged();
    }

    /**
     * Gets the list item held at the specified adapter position.
     *
     * @param position The index of the list item to return
     * @return The list item at the specified position
     */
    protected Object getListItem(int position) {
        return mItemList.get(position);
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
    private void expandViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && !viewHolder.isExpanded()) {
                viewHolder.setExpanded(true);
                viewHolder.onExpansionToggled(false);
            }

            expandParentListItem(parentWrapper, parentIndex, false);
        }
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
    private void collapseViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && viewHolder.isExpanded()) {
                viewHolder.setExpanded(false);
                viewHolder.onExpansionToggled(true);
            }

            collapseParentListItem(parentWrapper, parentIndex, false);
        }
    }

    /**
     * Expands a specified parent item. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper The ParentWrapper of the parent to expand
     * @param parentIndex The index of the parent to expand
     * @param expansionTriggeredByListItemClick true if expansion was triggered
     *                                          by a click event, false otherwise.
     */
    private void expandParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);

            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    mItemList.add(parentIndex + i + 1, childItemList.get(i));
                    notifyItemInserted(parentIndex + i + 1);
                }
            }
        }
    }

    /**
     * Collapses a specified parent item. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper The ParentWrapper of the parent to collapse
     * @param parentIndex The index of the parent to collapse
     * @param collapseTriggeredByListItemClick true if expansion was triggered
     *                                         by a click event, false otherwise.
     */
    private void collapseParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                for (int i = childItemList.size() - 1; i >= 0; i--) {
                    mItemList.remove(parentIndex + i + 1);
                    notifyItemRemoved(parentIndex + i + 1);
                }
            }
        }
    }

    /**
     * Gets the number of expanded child list items before the specified position.
     *
     * @param position The index before which to return the number of expanded
     *                 child list items
     * @return The number of expanded child list items before the specified position
     */
    private int getExpandedItemCount(int position) {
        if (position == 0) {
            return 0;
        }

        int expandedCount = 0;
        for (int i = 0; i < position; i++) {
            Object listItem = getListItem(i);
            if (!(listItem instanceof ParentWrapper)) {
                expandedCount++;
            }
        }
        return expandedCount;
    }

    // endregion

    // region Data Manipulation

    public void notifyParentItemInserted(int parentPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);

        int wrapperIndex;
        if (parentPosition < mParentItemList.size() - 1) {
            wrapperIndex = getParentWrapperIndex(parentPosition);
        } else {
            wrapperIndex = mItemList.size();
        }

        addParentWrapper(wrapperIndex, parentListItem);
    }

    private void addParentWrapper(int wrapperIndex, ParentListItem parentListItem) {
        int sizeChanged = 1;
        ParentWrapper parentWrapper = new ParentWrapper(parentListItem);
        mItemList.add(wrapperIndex, parentWrapper);
        if (parentWrapper.isInitiallyExpanded()) {
            parentWrapper.setExpanded(true);
            List<?> childItemList = parentWrapper.getChildItemList();
            mItemList.addAll(wrapperIndex + sizeChanged, childItemList);
            sizeChanged += childItemList.size();
        }
        notifyItemRangeInserted(wrapperIndex, sizeChanged);
    }

    public void notifyParentItemRemoved(int parentPosition) {
        int sizeChanged = 1;
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.remove(wrapperIndex);
        if (parentWrapper.isExpanded()) {
            int childListSize = parentWrapper.getChildItemList().size();
            for (int i = 0; i < childListSize; i++) {
                mItemList.remove(wrapperIndex);
                sizeChanged++;
            }
        }

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    public void notifyParentItemChanged(int parentPosition) {
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(wrapperIndex);
        int sizeChanged = 1;
        if (parentWrapper.isExpanded()) {
            int childListSize = parentWrapper.getChildItemList().size();
            for (int i = 0; i < childListSize; i++) {
                sizeChanged++;
            }
        }

        notifyItemRangeChanged(wrapperIndex, sizeChanged);
    }

    public void notifyChildItemInserted(int parentPosition, int childPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        Object child =  parentListItem.getChildItemList().get(childPosition);

        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        if (parentWrapper.isExpanded()) {
            mItemList.add(parentWrapperIndex + childPosition + 1, child);
            notifyItemInserted(parentWrapperIndex + childPosition + 1);
        }
    }

    public void notifyChildItemRemoved(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.isExpanded()) {
            mItemList.remove(parentWrapperIndex + childPosition + 1);
            notifyItemRemoved(parentWrapperIndex + childPosition + 1);
        }
    }

    public void notifyChildItemChanged(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        if (parentWrapper.isExpanded()) {
            notifyItemChanged(parentWrapperIndex + childPosition + 1);
        }
    }



    // endregion

    /**
     * Generates a HashMap used to store expanded state for items in the list
     * on configuration change or whenever onResume is called.
     *
     * @return A HashMap containing the expanded state of all parent list items
     */
    private HashMap<Integer, Boolean> generateExpandedStateMap() {
        HashMap<Integer, Boolean> parentListItemHashMap = new HashMap<>();
        int childCount = 0;

        Object listItem;
        ParentWrapper parentWrapper;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) != null) {
                listItem = getListItem(i);
                if (listItem instanceof ParentWrapper) {
                    parentWrapper = (ParentWrapper) listItem;
                    parentListItemHashMap.put(i - childCount, parentWrapper.isExpanded());
                } else {
                    childCount++;
                }
            }
        }

        return parentListItemHashMap;
    }

    /**
     * Gets the index of a ParentWrapper within the helper item list based on
     * the index of the ParentWrapper.
     *
     * @param parentIndex The index of the parent in the list of parent items
     * @return The index of the parent in the list of all views in the adapter
     */
    private int getParentWrapperIndex(int parentIndex) {
        int parentCount = 0;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) instanceof ParentWrapper) {
                parentCount++;

                if (parentCount > parentIndex) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Gets the ParentWrapper for a specified ParentListItem from the list of
     * parents.
     *
     * @param parentListItem A ParentListItem in the list of parents
     * @return If the parent exists on the list, returns its ParentWrapper.
     *         Otherwise, returns null.
     */
    private ParentWrapper getParentWrapper(ParentListItem parentListItem) {
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            Object listItem = mItemList.get(i);
            if (listItem instanceof ParentWrapper) {
                if (((ParentWrapper) listItem).getParentListItem().equals(parentListItem)) {
                    return (ParentWrapper) listItem;
                }
            }
        }

        return null;
    }
}
