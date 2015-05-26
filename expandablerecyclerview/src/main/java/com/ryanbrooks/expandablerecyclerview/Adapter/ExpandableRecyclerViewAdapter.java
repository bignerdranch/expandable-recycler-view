package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ChildItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;
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
    private int listSize;
    private ArrayList<RecyclerView.ViewHolder> viewHolders;

    public ExpandableRecyclerViewAdapter(Context context, ArrayList<? extends ExpandableItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = inflater.from(context);
        this.listSize = itemList.size();
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
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            parentViewHolder.setParentItemClickListener(this);
            onBindParentViewHolder(parentViewHolder, position);
        } else if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            if (position > 0) {
                onBindChildViewHolder(childViewHolder, position - 1);
            } else {
                onBindChildViewHolder(childViewHolder, position);
            }

        } else {
            return; // ERROR
        }
    }

    @Override
    public int getItemCount() {
        return this.listSize;
    }

    // TODO: Look into getAdapterPosition and getLayoutPosition and how I can use them to get correct view
    @Override
    public int getItemViewType(int position) {
        Log.d("getItemViewType",  "Position: " + position);

        /**
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
         */
        if (position == 0) {
            return TYPE_PARENT;
        } else {
            // GIT TEST

        }
    }

    private int getExpandedBeforePosition(int passedPosition) {
        int expandedItems = 0;
        if (passedPosition == 0) {
            return 0;
        } else {
            for (int i = 0; i <= passedPosition; i++) {
                if (itemList.get(passedPosition) != null) {
                    if (itemList.get(passedPosition).isExpanded()) {
                        expandedItems++;
                    }
                } else {
                    return expandedItems;
                }
            }
        }
        return expandedItems;
    }

    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType);

    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindParentViewHolder(ParentViewHolder holder, int position);

    public abstract void onBindChildViewHolder(ChildViewHolder holder, int position);

    // TODO: Figure this out
    @Override
    public void onParentItemClickListener(int position, int viewType) {
        ExpandableItem expandableItem = itemList.get(position);
        if (expandableItem.isExpanded()) {
            expandableItem.setExpanded(false);
            notifyItemRemoved(position + 1);
            listSize--;
        } else {
            expandableItem.setExpanded(true);
            notifyItemInserted(position + 1);
            listSize++;
        }
        Log.d(TAG, "GetAdapterPosition() from PVH " + position);
        Log.d(TAG, "Item list size: " + itemList.size());
        Log.d(TAG, "Calculated list size: " + listSize);
        notifyItemChanged(position);
    }
}