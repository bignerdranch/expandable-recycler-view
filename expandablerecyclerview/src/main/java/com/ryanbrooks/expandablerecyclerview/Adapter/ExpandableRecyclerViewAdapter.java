package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ChildItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.AbstractViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryanbrooks on 5/21/15.
 */
public abstract class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public final static int TYPE_PARENT = 0;
    public final static int TYPE_CHILD = 1;

    protected Context context;
    protected ArrayList<? extends ExpandableItem> itemList;
    protected LayoutInflater inflater;
    private ArrayList<RecyclerView.ViewHolder> viewHolders;
    private int childParentPosition;
    private int childParentOriginalPosition;

    public ExpandableRecyclerViewAdapter(Context context, ArrayList<? extends ExpandableItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = inflater.from(context);
        this.viewHolders = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT) {
            return onCreateParentViewHolder(parent, viewType);
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(parent, viewType);
        } else {
            return null; // ERROR
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "OnBind position: " + position);
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            parentViewHolder.setParentItemClickListener(this);
            if (parentViewHolder.getOriginalPosition() == -1) {
                Log.d("**$*$*$*$*$*$", "Got inside originalPosition");
                parentViewHolder.setOriginalPosition(position - getExpandedBeforePosition(position));
            }
            viewHolders.add(position, parentViewHolder);
            onBindParentViewHolder(parentViewHolder, position, parentViewHolder.getOriginalPosition());
        } else if (holder instanceof ChildViewHolder) {
            Log.d(TAG, "OnBind - OnClick global position: " + childParentPosition);
            Log.d(TAG, "OnBind - OnClick global original position: " + childParentOriginalPosition);

            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            if (childViewHolder.getOriginalPosition() == -1) {
                childViewHolder.setOriginalPosition(childParentOriginalPosition);
            }
            viewHolders.add(position, childViewHolder);
            onBindChildViewHolder(childViewHolder, position, childParentOriginalPosition);
        } else {
            return; // ERROR
        }
    }

    @Override
    public int getItemCount() {
        return getVisibleCount();
    }

    private int getVisibleCount() {
        int count = itemList.size();
        for (ExpandableItem expandableItem : itemList) {
            if (expandableItem.isExpanded()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int expandedItems = 0;
        for (ExpandableItem expandableItem : itemList) {
            if (expandedItems == position) {
                return TYPE_PARENT;
            }
            // Increase Expanded items by 1
            expandedItems++;
            if (!expandableItem.isExpanded()) {
                continue;
            }
            if (position == expandedItems) {
                return TYPE_CHILD;
            }
            expandedItems++;
        }
        return TYPE_CHILD;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        viewHolder.setOriginalPosition(-1);
    }

    private int getExpandedBeforePosition(int passedPosition) {
        int expandedItems = 0;
        int index = 0;
        for (ExpandableItem expandableItem : itemList) {
            if (index == passedPosition) {
                return expandedItems;
            }
            if (expandableItem.isExpanded()) {
                expandedItems++;
            }
            index++;
        }
        Log.d("EXPAND********", "Expanded items before " + passedPosition + ": " + expandedItems);
        return expandedItems;
    }

    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType);

    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindParentViewHolder(ParentViewHolder holder, int position, int originalPosition);

    public abstract void onBindChildViewHolder(ChildViewHolder holder, int position, int originalPosition);

    @Override
    public void onParentItemClickListener(int position, int viewType, int originalPosition) {
        ExpandableItem expandableItem = itemList.get(originalPosition);
        if (expandableItem.isExpanded()) {
            expandableItem.setExpanded(false);
            viewHolders.remove(position + 1);
            notifyItemRemoved(position + 1);
        } else {
            this.childParentPosition = position;
            this.childParentOriginalPosition = originalPosition;
            expandableItem.setExpanded(true);
            notifyItemInserted(position + 1);
        }
    }
}