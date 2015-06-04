package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
    private static final String TAG = ExpandableRecyclerAdapter.class.getClass().getSimpleName();
    private static final String STABLE_ID_MAP = "ExpandableRecyclerAdapter.StableIdMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;
    public static final int CUSTOM_ANIMATION_VIEW_NOT_SET = -1;
    public static final long DEFAULT_ROTATE_DURATION_MS = 200l;
    public static final long CUSTOM_ANIMATION_DURATION_NOT_SET = -1l;

    protected Context mContext;
    protected List<ExpandingObject> mItemList;
    private HashMap<Integer, Boolean> mStableIdMap;
    private int mCustomParentAnimationViewId = CUSTOM_ANIMATION_VIEW_NOT_SET;
    private long mAnimationDuration = CUSTOM_ANIMATION_DURATION_NOT_SET;

    public ExpandableRecyclerAdapter(Context context, List<ExpandingObject> itemList) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
    }

    public ExpandableRecyclerAdapter(Context context, List<ExpandingObject> itemList,
                                     int customParentAnimationViewId) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
        mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    public ExpandableRecyclerAdapter(Context context, List<ExpandingObject> itemList,
                                     int customParentAnimationViewId, long animationDuration) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
        mCustomParentAnimationViewId = customParentAnimationViewId;
        mAnimationDuration = animationDuration;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARENT) {
            return onCreateParentViewHolder(viewGroup);
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;

            if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET
                    && mAnimationDuration != CUSTOM_ANIMATION_DURATION_NOT_SET) {
                parentViewHolder.setCustomClickableView(mCustomParentAnimationViewId);
                parentViewHolder.setAnimationDuration(mAnimationDuration);
            } else if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET) {
                parentViewHolder.setCustomClickableView(mCustomParentAnimationViewId);
                parentViewHolder.cancelAnimation();
            } else {
                parentViewHolder.setMainItemClickToExpand();
            }

            parentViewHolder.setExpanded(((ParentObject) mItemList.get(position)).isExpanded());
            onBindParentViewHolder(parentViewHolder, position);
        } else if (mItemList.get(position) instanceof ChildObject) {
            onBindChildViewHolder((ChildViewHolder) holder, position);
        } else {
            throw new IllegalStateException("Incorrect ViewHolder found");
        }
    }

    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup);

    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup);

    public abstract void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position);

    public abstract void onBindChildViewHolder(ChildViewHolder childViewHolder, int position);

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            return TYPE_PARENT;
        } else if (mItemList.get(position) instanceof ChildObject) {
            return TYPE_CHILD;
        } else if (mItemList.get(position) == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_PARENT;
        }
    }

    @Override
    public void onParentItemClickListener(int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentObject parentObject = (ParentObject) mItemList.get(position);
            expandParent(parentObject, position);
        }
    }

    public void setParentClickableViewAnimationDefaultDuration() {
        mAnimationDuration = DEFAULT_ROTATE_DURATION_MS;
    }

    public void setParentClickableViewAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setCustomParentAnimationViewId(int customParentAnimationViewId) {
        mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    public void removeAnimation() {
        mCustomParentAnimationViewId = CUSTOM_ANIMATION_VIEW_NOT_SET;
        mAnimationDuration = CUSTOM_ANIMATION_DURATION_NOT_SET;
    }


    private void expandParent(ParentObject parentObject, int position) {
        if (parentObject.isExpanded()) {
            parentObject.setExpanded(false);
            mStableIdMap.put(parentObject.getStableID(), false);
            mItemList.remove(position + 1);
            notifyItemRemoved(position + 1);
        } else {
            parentObject.setExpanded(true);
            mStableIdMap.put(parentObject.getStableID(), true);
            mItemList.add(position + 1, parentObject.getChildObject());
            notifyItemInserted(position + 1);
        }
    }

    private HashMap<Integer, Boolean> generateStableIdMapFromList(List<ExpandingObject> itemList) {
        HashMap<Integer, Boolean> parentObjectHashMap = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) instanceof ParentObject) {
                ParentObject parentObject = (ParentObject) itemList.get(i);
                parentObjectHashMap.put(parentObject.getStableID(), parentObject.isExpanded());
            }
        }
        return parentObjectHashMap;
    }

    public Bundle onSaveInstanceState(Bundle savedInstanceStateBundle) {
        savedInstanceStateBundle.putSerializable(STABLE_ID_MAP, mStableIdMap);
        return savedInstanceStateBundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceStateBundle) {
        if (savedInstanceStateBundle == null) {
            return;
        }
        if (!savedInstanceStateBundle.containsKey(STABLE_ID_MAP)) {
            return;
        }
        mStableIdMap = (HashMap<Integer, Boolean>) savedInstanceStateBundle.getSerializable(STABLE_ID_MAP);
        int i = 0;
        while (i < mItemList.size()) {
            if (mItemList.get(i) instanceof ParentObject) {
                ParentObject parentObject = (ParentObject) mItemList.get(i);
                if (mStableIdMap.containsKey(parentObject.getStableID())) {
                    parentObject.setExpanded(mStableIdMap.get(parentObject.getStableID()));
                    if (parentObject.isExpanded()) {
                        i++;
                        mItemList.add(i, parentObject.getChildObject());
                    }
                } else {
                    parentObject.setExpanded(false);
                }
            }
            i++;
        }
        notifyDataSetChanged();
    }
}
