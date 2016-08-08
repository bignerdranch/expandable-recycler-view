package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import java.util.List;

/**
 *
 */
public abstract class SimpleExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder> extends ExpandableRecyclerAdapter<PVH, CVH> {

    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public SimpleExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
    }

    @Override
    public int getParentItemViewType(int parentPosition) {
        return TYPE_PARENT;
    }

    @Override
    public int getChildItemViewType(int parentPosition, int childPosition) {
        return TYPE_CHILD;
    }



    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == TYPE_PARENT;
    }
}
