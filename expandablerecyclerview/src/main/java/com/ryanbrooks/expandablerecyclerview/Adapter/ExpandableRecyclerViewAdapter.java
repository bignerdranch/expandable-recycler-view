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

    @Override
    public int getItemViewType(int position) {
        if (position > 0 && itemList.get(position - 1).isExpanded()) {
            return TYPE_CHILD;
        } else {
            return TYPE_PARENT;
        }
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
            listSize--;
            notifyItemRemoved(position + 1);
        } else {
            expandableItem.setExpanded(true);
            listSize++;
            notifyItemInserted(position + 1);
        }
        notifyItemChanged(position);
    }
}