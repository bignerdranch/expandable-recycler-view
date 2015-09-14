package com.bignerdranch.expandablerecyclerview.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Listener.ExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Listener.ParentItemExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
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
public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemExpandCollapseListener {

    private static final String STABLE_ID_MAP = "ExpandableRecyclerAdapter.StableIdMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    protected Context mContext;
    protected List<? extends ParentObject> mParentItemList;
    private List<Object> mHelperItemList;
    private HashMap<Long, Boolean> mStableIdMap;
    private ExpandCollapseListener mExpandCollapseListener;
    private List<RecyclerView> mAttachedRecyclerViewPool;

    /**
     * Public constructor for the base ExpandableRecyclerView.
     *
     * @param context
     * @param parentItemList List of all parent objects that make up the recyclerview
     */
    public ExpandableRecyclerAdapter(Context context, @NonNull List<? extends ParentObject> parentItemList) {
        super();
        mContext = context;
        mParentItemList = parentItemList;
        mHelperItemList = ExpandableRecyclerAdapterHelper.generateHelperItemList(parentItemList);
        mStableIdMap = generateStableIdMapFromList(mHelperItemList);
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
            pvh.setParentItemExpandCollapseListener(this);
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
        Object helperItem = getHelperItem(position);
        if (helperItem instanceof ParentWrapper) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            ParentWrapper parentWrapper = (ParentWrapper) helperItem;
            parentViewHolder.setExpanded(parentWrapper.isExpanded());
            onBindParentViewHolder(parentViewHolder, position, parentWrapper.getParentObject());
        } else if (helperItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((CVH) holder, position, helperItem);
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
        return mHelperItemList.size();
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
        Object helperItem = getHelperItem(position);
        if (helperItem instanceof ParentWrapper) {
            return TYPE_PARENT;
        } else if (helperItem == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_CHILD;
        }
    }

    @Override
    public void onParentItemExpanded(int position) {
        Object helperItem = getHelperItem(position);
        if (helperItem instanceof ParentWrapper) {
            expandHelperItem((ParentWrapper) helperItem, position, false);
        }
    }

    @Override
    public void onParentItemCollapsed(int position) {
        Object helperItem = getHelperItem(position);
        if (helperItem instanceof ParentWrapper) {
            collapseHelperItem((ParentWrapper) helperItem, position, false);
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

    public void addExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    /**
     * Expands the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to expand
     */
    public void expandParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object helperItem = getHelperItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (helperItem instanceof ParentWrapper) {
             parentWrapper = (ParentWrapper) helperItem;
        } else {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands the parent associated with a specified {@link ParentObject} in
     * the list of parents.
     *
     * @param parentObject The {@code ParentObject} of the parent to expand
     */
    public void expandParent(ParentObject parentObject) {
        ParentWrapper parentWrapper = getParentWrapper(parentObject);
        int parentWrapperIndex = mHelperItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands all parents in the list.
     */
    public void expandAllParents() {
        for (ParentObject parentObject : mParentItemList) {
            expandParent(parentObject);
        }
    }

    /**
     * Collapses the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to expand
     */
    public void collapseParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object helperItem = getHelperItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (helperItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) helperItem;
        } else {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses the parent associated with a specified {@link ParentObject} in
     * the list of parents.
     *
     * @param parentObject The {@code ParentObject} of the parent to collapse
     */
    public void collapseParent(ParentObject parentObject) {
        ParentWrapper parentWrapper = getParentWrapper(parentObject);
        int parentWrapperIndex = mHelperItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses all parents in the list.
     */
    public void collapseAllParents() {
        for (ParentObject parentObject : mParentItemList) {
            collapseParent(parentObject);
        }
    }

    /**
     * Calls through to the {@link ParentViewHolder} to expand views for each
     * {@link RecyclerView} a specified parent is a child of. These calls to
     * the {@code ParentViewHolder} are made so that animations can be
     * triggered at the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * level.
     *
     * @param parentIndex The inject of the parent to expand
     */
    private void expandViews(ParentWrapper parentWrapper, int parentIndex) {
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null
                    && !((ParentViewHolder) viewHolder).isExpanded()) {
                ((ParentViewHolder) viewHolder).setExpanded(true);
                ((ParentViewHolder) viewHolder).onExpansionToggled(false);
            }

            expandHelperItem(parentWrapper, parentIndex, true);
        }
    }

    /**
     * Calls through to the {@link ParentViewHolder} to collapse views for each
     * {@link RecyclerView} a specified parent is a child of. These calls to
     * the {@code ParentViewHolder} are made so that animations can be
     * triggered at the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * level.
     *
     * @param parentIndex The inject of the parent to collapse
     */
    private void collapseViews(ParentWrapper parentWrapper, int parentIndex) {
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null
                    && ((ParentViewHolder) viewHolder).isExpanded()) {
                ((ParentViewHolder) viewHolder).setExpanded(false);
                ((ParentViewHolder) viewHolder).onExpansionToggled(true);
            }

            collapseHelperItem(parentWrapper, parentIndex, true);
        }
    }

    /**
     * Expands a specified parent item. Calls through to the {@link ExpandCollapseListener}
     * and adds children of the specified parent to the total list of items.
     *
     * @param parentWrapper The {@link ParentWrapper} of the parent to expand
     * @param parentIndex The index of the parent to expand
     * @param expansionTriggeredProgrammatically {@value false} if expansion was triggered by a
     *                                                         click event, {@value false} otherwise.
     */
    private void expandHelperItem(ParentWrapper parentWrapper, int parentIndex, boolean expansionTriggeredProgrammatically) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);

            if (expansionTriggeredProgrammatically
                    && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onRecyclerViewItemExpanded(parentIndex - expandedCountBeforePosition);
            }

            mStableIdMap.put(parentWrapper.getStableId(), true);
            List<Object> childObjectList = parentWrapper.getParentObject().getChildObjectList();
            if (childObjectList != null) {
                int numChildObjects = childObjectList.size();
                for (int i = 0; i < numChildObjects; i++) {
                    mHelperItemList.add(parentIndex + i + 1, childObjectList.get(i));
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
     * @param collapseTriggeredProgrammatically {@value false} if expansion was triggered by a
     *                                                         click event, {@value false} otherwise.
     */
    private void collapseHelperItem(ParentWrapper parentWrapper, int parentIndex, boolean collapseTriggeredProgrammatically) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            if (collapseTriggeredProgrammatically
                    && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onRecyclerViewItemCollapsed(parentIndex - expandedCountBeforePosition);
            }

            mStableIdMap.put(parentWrapper.getStableId(), false);
            List<Object> childObjectList = parentWrapper.getParentObject().getChildObjectList();
            if (childObjectList != null) {
                for (int i = childObjectList.size() - 1; i >= 0; i--) {
                    mHelperItemList.remove(parentIndex + i + 1);
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
            Object object = getHelperItem(i);
            if (!(object instanceof ParentWrapper)) {
                expandedCount++;
            }
        }
        return expandedCount;
    }

    /**
     * Generates a HashMap for storing expanded state when activity is rotated or onResume() is called.
     *
     * @param itemList
     * @return HashMap containing the Object's stable id along with a boolean indicating its expanded
     * state
     */
    private HashMap<Long, Boolean> generateStableIdMapFromList(List<Object> itemList) {
        HashMap<Long, Boolean> parentObjectHashMap = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) != null) {
                Object helperItem = getHelperItem(i);
                if (helperItem instanceof ParentWrapper) {
                    ParentWrapper parentWrapper = (ParentWrapper) helperItem;
                    parentObjectHashMap.put(parentWrapper.getStableId(), parentWrapper.isExpanded());
                }
            }
        }
        return parentObjectHashMap;
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
        savedInstanceStateBundle.putSerializable(STABLE_ID_MAP, mStableIdMap);
        return savedInstanceStateBundle;
    }

    /**
     * Should be called from onRestoreInstanceState of Activity that contains the ExpandingRecyclerView.
     * This will fetch the HashMap that was saved in onSaveInstanceState() and use it to restore
     * the expanded states before the rotation or onSaveInstanceState was called.
     *
     * @param savedInstanceStateBundle
     */
    public void onRestoreInstanceState(Bundle savedInstanceStateBundle) {
        if (savedInstanceStateBundle == null) {
            return;
        }
        if (!savedInstanceStateBundle.containsKey(STABLE_ID_MAP)) {
            return;
        }
        mStableIdMap = (HashMap<Long, Boolean>) savedInstanceStateBundle.getSerializable(STABLE_ID_MAP);
        int i = 0;
        while (i < mHelperItemList.size()) {
            Object helperItem = getHelperItem(i);
            if (helperItem instanceof ParentWrapper) {
                ParentWrapper parentWrapper = (ParentWrapper) helperItem;
                if (mStableIdMap.containsKey(parentWrapper.getStableId())) {
                    parentWrapper.setExpanded(mStableIdMap.get(parentWrapper.getStableId()));
                    if (parentWrapper.isExpanded() && !parentWrapper.getParentObject().isInitiallyExpanded()) {
                        List<Object> childObjectList = parentWrapper.getParentObject().getChildObjectList();
                        if (childObjectList != null) {
                            for (int j = 0; j < childObjectList.size(); j++) {
                                i++;
                                mHelperItemList.add(i, childObjectList.get(j));
                            }
                        }
                    } else if (!parentWrapper.isExpanded() && parentWrapper.getParentObject().isInitiallyExpanded()) {
                        List<Object> childObjectList = parentWrapper.getParentObject().getChildObjectList();
                        for (int j = 0; j < childObjectList.size(); j++) {
                            mHelperItemList.remove(i + 1);
                        }
                    }
                } else {
                    parentWrapper.setExpanded(false);
                }
            }
            i++;
        }
        notifyDataSetChanged();
    }

    private Object getHelperItem(int position) {
        return mHelperItemList.get(position);
    }

    /**
     * Gets the index of a {@link ParentWrapper} within the helper item list
     * based on the index of the {@code ParentWrapper}.
     *
     * @param parentIndex The index of the parent in the list of parent items
     * @return The index of the parent in the list of all views in the adapter
     */
    private int getParentWrapperIndex(int parentIndex) {
        int parentWrapperIndex = -1;
        int parentCount = 0;
        int numHelperItems = mHelperItemList.size();
        for (int i = 0; i < numHelperItems; i++) {
            if (mHelperItemList.get(i) instanceof ParentWrapper) {
                parentCount++;

                if (parentCount > parentIndex) {
                    parentWrapperIndex = i;
                    break;
                }
            }
        }

        return parentWrapperIndex;
    }

    /**
     * Gets the {@link ParentWrapper} for a specified {@link ParentObject} from
     * the list of parents.
     *
     * @param parentObject A {@code ParentObject} in the list of parents
     * @return If the parent exists on the list, returns its {@code ParentWrapper}.
     *         Otherwise, returns {@value null}.
     */
    private ParentWrapper getParentWrapper(ParentObject parentObject) {
        ParentWrapper parentWrapper = null;
        int numHelperItems = mHelperItemList.size();
        for (int i = 0; i < numHelperItems; i++) {
            Object helperItem = mHelperItemList.get(i);
            if (helperItem instanceof ParentWrapper) {
                if (((ParentWrapper) helperItem).getParentObject().equals(parentObject)) {
                    parentWrapper = (ParentWrapper) helperItem;
                }
            }
        }

        return parentWrapper;
    }
}
