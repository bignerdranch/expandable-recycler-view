package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandableItem;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;

/**
 * Created by ryanbrooks on 5/21/15.
 */
public abstract class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener{
    final static int TYPE_PARENT = 0;
    final static int TYPE_CHILD = 1;

    protected Context context;
    protected ArrayList<? extends ExpandableItem> itemList;
    protected LayoutInflater inflater;

    public ExpandableRecyclerViewAdapter(Context context, ArrayList<? extends ExpandableItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = inflater.from(context);
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
            onBindParentViewHolder(holder, position);
        } else if (holder instanceof ChildViewHolder) {
            onBindChildViewHolder(holder, position);
        } else {
            return; // ERROR
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public abstract RecyclerView.ViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindParentViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract void onBindChildViewHolder(RecyclerView.ViewHolder holder, int position);

    // TODO: Figure this out
    @Override
    public void onParentItemClickListener(int position, int viewType) {
        ExpandableItem expandableItem = itemList.get(position);
        if (expandableItem.isExpanded()) {
            expandableItem.setExpanded(false);
            notifyItemRangeRemoved(position + 1, 1);
        } else {
            expandableItem.setExpanded(true);
            notifyItemRangeInserted(position + 1, 1);
        }
        notifyItemChanged(position);
    }
}
