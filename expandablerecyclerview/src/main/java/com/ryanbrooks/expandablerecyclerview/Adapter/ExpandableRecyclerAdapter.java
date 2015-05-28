package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;


import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;

    private boolean firstRun;

    protected Context context;
    protected static List<ExpandingObject> itemList;
    protected RecyclerView recyclerView;

    public ExpandableRecyclerAdapter(Context context, List<ExpandingObject> itemList) {
        this.context = context;
        this.itemList = itemList;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARENT) {
            return onCreateParentViewHolder(viewGroup);
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            return null; // Error, should never get here
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (itemList.get(position) instanceof ParentObject) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            parentViewHolder.setParentItemClickListener(this);
            onBindParentViewHolder(parentViewHolder, position);
        } else if (itemList.get(position) instanceof ChildObject) {
            onBindChildViewHolder((ChildViewHolder) holder, position);
        } else {
            return; // Error, should never get here
        }
    }

    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup);

    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup);

    public abstract void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position);

    public abstract void onBindChildViewHolder(ChildViewHolder childViewHolder, int position);

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof ParentObject) {
            return TYPE_PARENT;
        } else if (itemList.get(position) instanceof ChildObject) {
            return TYPE_CHILD;
        } else {
            return TYPE_PARENT; // TODO: Add null case
        }
    }

    @Override
    public void onParentItemClickListener(int position) {
        if (itemList.get(position) instanceof ParentObject) {
            ParentObject parentObject = (ParentObject) itemList.get(position);
            expandParent(parentObject, position);
        }
    }

    private void expandParent(ParentObject parentObject, int position) {
        if (parentObject.isExpanded()) {
            parentObject.setExpanded(false);
            itemList.remove(position + 1);
            notifyItemRemoved(position + 1);
        } else {
            parentObject.setExpanded(true);
            itemList.add(position + 1, parentObject.getChildObject());
            notifyItemInserted(position + 1);
        }
    }
}
