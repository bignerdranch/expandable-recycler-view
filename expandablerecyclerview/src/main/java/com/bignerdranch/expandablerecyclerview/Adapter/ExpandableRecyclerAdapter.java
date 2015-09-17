package com.bignerdranch.expandablerecyclerview.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Listener.ExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Listener.ParentListItemExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Base class for an Expandable RecyclerView Adapter
 *
 * Provides the base for a user to implement binding custom views to a Parent ViewHolder and a
 * Child ViewHolder
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentListItemExpandCollapseListener {

    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    protected List<Object> mItemList;
    protected List<ParentListItem> mParentItemList;
    private ExpandCollapseListener mExpandCollapseListener;
    private List<RecyclerView> mAttachedRecyclerViewPool;

    /**
     * Public constructor for the base ExpandableRecyclerView.
     *
     * @param parentItemList List of all parent objects that make up the recyclerview
     */
    public ExpandableRecyclerAdapter(@NonNull List<ParentListItem> parentItemList) {
        super();
        mParentItemList = parentItemList;
        mItemList = ExpandableRecyclerAdapterHelper.generateParentChildItemList(parentItemList);
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    /**
     * Override of RecyclerView's default onCreateViewHolder.
     *
     * This implementation determines if the item is a child or a parent view and will then call
     * the respective onCreateViewHolder method that the user must implement in their custom
     * implementation.
     *
     * @param viewGroup
     * @param viewType
     * @return the ViewHolder that corresponds to the item at the position.
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
     * Override of RecyclerView's default onBindViewHolder
     *
     * This implementation determines first if the ViewHolder is a ParentViewHolder or a
     * ChildViewHolder. The respective onBindViewHolders for ParentObjects and ChildObject are then
     * called.
     *
     * If the item is a ParentObject, sets the entire row to trigger expansion if instructed to
     *
     * @param holder
     * @param position
     * @throws IllegalStateException if the item in the list is neither a ParentObject or ChildObject
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
     * Creates the Parent ViewHolder. Called from onCreateViewHolder when the item is a ParenObject.
     *
     * @param parentViewGroup
     * @return ParentViewHolder that the user must create and inflate.
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parentViewGroup);

    /**
     * Creates the Child ViewHolder. Called from onCreateViewHolder when the item is a ChildObject.
     *
     * @param childViewGroup
     * @return ChildViewHolder that the user must create and inflate.
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup childViewGroup);

    /**
     * Binds the data to the ParentViewHolder. Called from onBindViewHolder when the item is a
     * ParentObject
     *
     * @param parentViewHolder
     * @param position
     */
    public abstract void onBindParentViewHolder(PVH parentViewHolder, int position, Object parentObject);

    /**
     * Binds the data to the ChildViewHolder. Called from onBindViewHolder when the item is a
     * ChildObject
     *
     * @param childViewHolder
     * @param position
     */
    public abstract void onBindChildViewHolder(CVH childViewHolder, int position, Object childObject);

    /**
     * Returns the size of the list that contains Parent and Child objects
     *
     * @return integer value of the size of the Parent/Child list
     */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * Returns the type of view that the item at the given position is.
     *
     * @param position
     * @return TYPE_PARENT (0) for ParentObjects and TYPE_CHILD (1) for ChildObjects
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

    @Override
    public void onParentListItemExpanded(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            expandParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    @Override
    public void onParentListItemCollapsed(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            collapseParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.add(recyclerView);
    }

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
     * @param parentListItem The {@code ParentObject} of the parent to expand
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
     * @param parentIndex The index of the parent to expand
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
     * @param parentListItem The {@code ParentObject} of the parent to collapse
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
     * Calls through to the {@link ParentViewHolder} to expand views for each
     * {@link RecyclerView} a specified parent is a child of. These calls to
     * the {@code ParentViewHolder} are made so that animations can be
     * triggered at the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * level.
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
     * Calls through to the {@link ParentViewHolder} to collapse views for each
     * {@link RecyclerView} a specified parent is a child of. These calls to
     * the {@code ParentViewHolder} are made so that animations can be
     * triggered at the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * level.
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
     * Expands a specified parent item. Calls through to the {@link ExpandCollapseListener}
     * and adds children of the specified parent to the total list of items.
     *
     * @param parentWrapper The {@link ParentWrapper} of the parent to expand
     * @param parentIndex The index of the parent to expand
     * @param expansionTriggeredByListItemClick {@value true} if expansion was triggered by a
     *                                                         click event, {@value false} otherwise.
     */
    private void expandParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);

            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }

            List<Object> childItemList = parentWrapper.getParentListItem().getChildItemList();
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
     * Collapses a specified parent item. Calls through to the {@link ExpandCollapseListener}
     * and adds children of the specified parent to the total list of items.
     *
     * @param parentWrapper The {@link ParentWrapper} of the parent to collapse
     * @param parentIndex The index of the parent to collapse
     * @param collapseTriggeredByListItemClick {@value true} if expansion was triggered by a
     *                                                         click event, {@value false} otherwise.
     */
    private void collapseParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }

            List<Object> childItemList = parentWrapper.getParentListItem().getChildItemList();
            if (childItemList != null) {
                for (int i = childItemList.size() - 1; i >= 0; i--) {
                    mItemList.remove(parentIndex + i + 1);
                    notifyItemRemoved(parentIndex + i + 1);
                }
            }
        }
    }

    /**
     * Method to get the number of expanded children before the specified position.
     *
     * @param position
     * @return number of expanded children before the specified position
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

    public void addParent(ParentListItem parentObject) {
        mParentItemList.add(parentObject);

        int sizeChanged = 1;
        ParentWrapper parentWrapper = new ParentWrapper(parentObject);
        mItemList.add(parentWrapper);
        if (parentObject.isInitiallyExpanded()) {
            parentWrapper.setExpanded(true);
            List<Object> childObjectList = parentObject.getChildItemList();
            for (int i = 0; i < childObjectList.size(); i++) {
                mItemList.add(childObjectList.get(i));
                sizeChanged++;
            }
        }
        notifyItemRangeInserted(mItemList.size() - sizeChanged, sizeChanged);
    }

    public void addParent(int location, ParentListItem parentObject) {
        mParentItemList.add(location, parentObject);

        int sizeChanged = 1;
        int wrapperIndex = getParentWrapperIndex(location);
        ParentWrapper parentWrapper = new ParentWrapper(parentObject);
        mItemList.add(wrapperIndex, parentWrapper);
        if (parentObject.isInitiallyExpanded()) {
            parentWrapper.setExpanded(true);
            List<Object> childObjectList = parentObject.getChildItemList();
            for (int i = 0; i < childObjectList.size(); i++) {
                mItemList.add(wrapperIndex + sizeChanged, childObjectList.get(i));
                sizeChanged++;
            }
        }
        notifyItemRangeInserted(wrapperIndex, sizeChanged);

    }

    public boolean removeParent(ParentListItem parentObject) {

        int index = mParentItemList.indexOf(parentObject);
        if (index == -1) {
            return false;
        }

        removeParent(index);
        return true;
    }

    public ParentListItem removeParent(int parentPosition) {
        ParentListItem parentObject = mParentItemList.remove(parentPosition);

        int sizeChanged = 1;
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        if (wrapperIndex == -1) {
            throw new IllegalStateException("Parent not found");
        }

        ParentWrapper parentWrapper = (ParentWrapper) mItemList.remove(wrapperIndex);
        if (parentWrapper.isExpanded()) {
            for (int i = 0; i < parentObject.getChildItemList().size(); i++) {
                mItemList.remove(wrapperIndex);
                sizeChanged++;
            }
        }

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);


        return parentObject;
    }


    // endregion

    /**
     * Generates a HashMap for storing expanded state when activity is rotated or onResume() is called.
     *
     * @param itemList
     * @return HashMap containing the Parents expanded stated stored at the position relative to other parents
     */
    private HashMap<Integer, Boolean> generateExpandedStateMap(List<Object> itemList) {
        HashMap<Integer, Boolean> parentListItemHashMap = new HashMap<>();
        int childCount = 0;

        Object listItem;
        ParentWrapper parentWrapper;
        int listItemCount = itemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (itemList.get(i) != null) {
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
     * Should be called from onSaveInstanceState of Activity that holds the RecyclerView. This will
     * make sure to add the generated HashMap as an extra to the bundle to be used in
     * OnRestoreInstanceState().
     *
     * @param savedInstanceStateBundle
     * @return the Bundle passed in with the Id HashMap added if applicable
     */
    public Bundle onSaveInstanceState(Bundle savedInstanceStateBundle) {
        savedInstanceStateBundle.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap(mItemList));
        return savedInstanceStateBundle;
    }

    /**
     * Should be called from onRestoreInstanceState of Activity that contains the ExpandingRecyclerView.
     * This will fetch the HashMap that was saved in onSaveInstanceState() and use it to restore
     * the expanded states before the rotation or onSaveInstanceState was called.
     *
     * Assumes list of parent objects is the same as when saveinstancestate was stored
     *
     * @param savedInstanceStateBundle
     */
    public void onRestoreInstanceState(Bundle savedInstanceStateBundle) {
        if (savedInstanceStateBundle == null
                || !savedInstanceStateBundle.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceStateBundle.getSerializable(EXPANDED_STATE_MAP);
        int fullCount = 0;
        int childCount = 0;
        Object listItem;
        ParentWrapper parentWrapper;
        List<Object> childItemList;
        int listItemCount = mItemList.size();
        while (fullCount < listItemCount) {
            listItem = getListItem(fullCount);

            if (listItem instanceof ParentWrapper) {
                parentWrapper = (ParentWrapper) listItem;

                if (expandedStateMap.containsKey(fullCount - childCount)) {
                    parentWrapper.setExpanded(expandedStateMap.get(fullCount - childCount));


                    if (parentWrapper.isExpanded() && !parentWrapper.getParentListItem().isInitiallyExpanded()) {
                        childItemList = parentWrapper.getParentListItem().getChildItemList();

                        if (childItemList != null) {
                            int childListItemCount = childItemList.size();
                            for (int j = 0; j < childListItemCount; j++) {
                                fullCount++;
                                childCount++;
                                mItemList.add(fullCount, childItemList.get(j));
                            }
                        }
                    } else if (!parentWrapper.isExpanded() && parentWrapper.getParentListItem().isInitiallyExpanded()) {
                        childItemList = parentWrapper.getParentListItem().getChildItemList();
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
     * Returns the list item held at the adapter position
     *
     * @param position the index of the list item to return
     * @return Object at that index, may be a ParentWrapper or child Object
     */
    protected Object getListItem(int position) {
        return mItemList.get(position);
    }

    /**
     * Gets the index of a {@link ParentWrapper} within the helper item list
     * based on the index of the {@code ParentWrapper}.
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
     * Gets the {@link ParentWrapper} for a specified {@link ParentListItem} from
     * the list of parents.
     *
     * @param parentListItem A {@code ParentObject} in the list of parents
     * @return If the parent exists on the list, returns its {@code ParentWrapper}.
     *         Otherwise, returns {@value null}.
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
