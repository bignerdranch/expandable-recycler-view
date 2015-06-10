package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * The Base class for an Expandable RecyclerView Adapter
 * <p>
 * Provides the base for a user to implement binding custom views to a Parent ViewHolder and a
 * Child ViewHolder
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
    private static final String TAG = ExpandableRecyclerAdapter.class.getClass().getSimpleName();
    private static final String STABLE_ID_MAP = "ExpandableRecyclerAdapter.StableIdMap";
    private static final String STABLE_ID_LIST = "ExpandableRecyclerAdapter.StableIdList";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;
    public static final int CUSTOM_ANIMATION_VIEW_NOT_SET = -1;
    public static final long DEFAULT_ROTATE_DURATION_MS = 200l;
    public static final long CUSTOM_ANIMATION_DURATION_NOT_SET = -1l;

    protected Context mContext;
    protected List<Object> mItemList;
    private HashMap<Long, Boolean> mStableIdMap;
    private boolean mParentAndIconClickable = false;
    private boolean mHasStableIds = false;
    private int mCustomParentAnimationViewId = CUSTOM_ANIMATION_VIEW_NOT_SET;
    private long mAnimationDuration = CUSTOM_ANIMATION_DURATION_NOT_SET;

    /**
     * Public constructor for the base ExpandableRecyclerView. This constructor takes in no
     * extra parameters for custom clickable views and animation durations. This means a click of
     * the parent item will trigger the expansion.
     *
     * @param context
     * @param itemList
     */
    public ExpandableRecyclerAdapter(Context context, List<Object> itemList) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
    }

    /**
     * Public constructor for a more robust ExpandableRecyclerView. This constructor takes in an
     * id for a custom clickable view that will trigger the expansion or collapsing of the child.
     * By default, a parent item click is the trigger for the expanding/collapsing.
     *
     * @param context
     * @param itemList
     * @param customParentAnimationViewId
     */
    public ExpandableRecyclerAdapter(Context context, List<Object> itemList,
                                     int customParentAnimationViewId) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
        mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    /**
     * Public constructor for even more robust ExpandableRecyclerView. This constructor takes in
     * both an id for a custom clickable view that will trigger the expansion or collapsing of the
     * child along with a long for a custom duration in MS for the rotation animation.
     *
     * @param context
     * @param itemList
     * @param customParentAnimationViewId
     * @param animationDuration
     */
    public ExpandableRecyclerAdapter(Context context, List<Object> itemList,
                                     int customParentAnimationViewId, long animationDuration) {
        mContext = context;
        mItemList = itemList;
        mStableIdMap = generateStableIdMapFromList(itemList);
        mCustomParentAnimationViewId = customParentAnimationViewId;
        mAnimationDuration = animationDuration;
    }

    /**
     * Override of RecyclerView's default onCreateViewHolder.
     * <p>
     * This implementation determines if the item is a child or a parent view and will then call
     * the respective onCreateViewHolder method that the user must implement in their custom
     * implementation.
     *
     * @param viewGroup
     * @param viewType
     * @return the ViewHolder that cooresponds to the item at the position.
     */
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

    /**
     * Override of RecyclerView's default onBindViewHolder
     * <p>
     * This implementation determines first if the ViewHolder is a ParentViewHolder or a
     * ChildViewHolder. The respective onBindViewHolders for ParentObjects and ChildObject are then
     * called.
     * <p>
     * If the item is a ParentObject, setting the ParentViewHolder's animation settings are then handled
     * here.
     *
     * @param holder
     * @param position
     * @throws IllegalStateException if the item in the list is neither a ParentObject or ChildObject
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;

            if (mParentAndIconClickable) {
                if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET
                        && mAnimationDuration != CUSTOM_ANIMATION_DURATION_NOT_SET) {
                    parentViewHolder.setCustomClickableViewAndItem(mCustomParentAnimationViewId);
                    parentViewHolder.setAnimationDuration(mAnimationDuration);
                } else if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET) {
                    parentViewHolder.setCustomClickableViewAndItem(mCustomParentAnimationViewId);
                    parentViewHolder.cancelAnimation();
                } else {
                    parentViewHolder.setMainItemClickToExpand();
                }
            } else {
                if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET
                        && mAnimationDuration != CUSTOM_ANIMATION_DURATION_NOT_SET) {
                    parentViewHolder.setCustomClickableViewOnly(mCustomParentAnimationViewId);
                    parentViewHolder.setAnimationDuration(mAnimationDuration);
                } else if (mCustomParentAnimationViewId != CUSTOM_ANIMATION_VIEW_NOT_SET) {
                    parentViewHolder.setCustomClickableViewOnly(mCustomParentAnimationViewId);
                    parentViewHolder.cancelAnimation();
                } else {
                    parentViewHolder.setMainItemClickToExpand();
                }
            }

            parentViewHolder.setExpanded(((ParentObject) mItemList.get(position)).isExpanded());
            onBindParentViewHolder(parentViewHolder, position);
        } else if (mItemList.get(position) instanceof ChildObject) {
            onBindChildViewHolder((ChildViewHolder) holder, position);
        } else {
            throw new IllegalStateException("Incorrect ViewHolder found");
        }
    }

    /**
     * Creates the Parent ViewHolder. Called from onCreateViewHolder when the item is a ParenObject.
     *
     * @param parentViewGroup
     * @return ParentViewHolder that the user must create and inflate.
     */
    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup);

    /**
     * Creates the Child ViewHolder. Called from onCreateViewHolder when the item is a ChildObject.
     *
     * @param childViewGroup
     * @return ChildViewHolder that the user must create and inflate.
     */
    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup);

    /**
     * Binds the data to the ParentViewHolder. Called from onBindViewHolder when the item is a
     * ParentObject
     *
     * @param parentViewHolder
     * @param position
     */
    public abstract void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position);

    /**
     * Binds the data to the ChildViewHolder. Called from onBindViewHolder when the item is a
     * ChildObject
     *
     * @param childViewHolder
     * @param position
     */
    public abstract void onBindChildViewHolder(ChildViewHolder childViewHolder, int position);

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

    /**
     * On click listener implementation for the ParentObject. This is called from ParentViewHolder.
     * See OnClick in ParentViewHolder
     *
     * @param position
     */
    @Override
    public void onParentItemClickListener(int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentObject parentObject = (ParentObject) mItemList.get(position);
            expandParent(parentObject, position);
        }
    }

    /**
     * Setter for the Default rotation duration (200 MS)
     */
    public void setParentClickableViewAnimationDefaultDuration() {
        mAnimationDuration = DEFAULT_ROTATE_DURATION_MS;
    }

    /**
     * Setter for a custom rotation animation duration in MS
     *
     * @param animationDuration in MS
     */
    public void setParentClickableViewAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    /**
     * Setter for a custom clickable view to expand or collapse the item. This should be passed
     * as a reference to the View's R.id
     *
     * @param customParentAnimationViewId
     */
    public void setCustomParentAnimationViewId(int customParentAnimationViewId) {
        mCustomParentAnimationViewId = customParentAnimationViewId;
    }

    /**
     * Set the ability to be able to click both the whole parent view and the custom button to trigger
     * expanding and collapsing
     *
     * @param parentAndIconClickable
     */
    public void setParentAndIconExpandOnClick(boolean parentAndIconClickable) {
        mParentAndIconClickable = parentAndIconClickable;
    }

    /**
     * Call this when removing the animtion. This will set the parent item to be the expand/collapse
     * trigger. It will also disable the rotation animation.
     */
    public void removeAnimation() {
        mCustomParentAnimationViewId = CUSTOM_ANIMATION_VIEW_NOT_SET;
        mAnimationDuration = CUSTOM_ANIMATION_DURATION_NOT_SET;
    }

    /**
     * Call this to save expanded states when rotating or when onResume() is called
     */
    public void setHasStableIds() {
        if (!mHasStableIds) {
            mStableIdMap = generateStableIdMapFromList(mItemList);
        }
        mHasStableIds = true;
    }

    /**
     * Method called to expand a ParentObject when clicked. This handles saving state, adding the
     * corresponding child object to the list and updating the list.
     *
     * @param parentObject
     * @param position
     */
    private void expandParent(ParentObject parentObject, int position) {
        if (parentObject.isExpanded()) {
            parentObject.setExpanded(false);
            if (mHasStableIds) {
                mStableIdMap.put(parentObject.getStableId(), false);
            }
            mItemList.remove(position + 1);
            notifyItemRemoved(position + 1);
        } else {
            parentObject.setExpanded(true);
            if (mHasStableIds) {
                mStableIdMap.put(parentObject.getStableId(), true);
            }
            mItemList.add(position + 1, parentObject.getChildObject());
            notifyItemInserted(position + 1);
        }
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
            if (itemList.get(i) instanceof ParentObject) {
                ParentObject parentObject = (ParentObject) itemList.get(i);
                parentObjectHashMap.put(parentObject.getStableId(), parentObject.isExpanded());
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
        if (mHasStableIds) {
            savedInstanceStateBundle.putSerializable(STABLE_ID_MAP, mStableIdMap);
        }
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
        if (!mHasStableIds) {
            return;
        }
        if (savedInstanceStateBundle == null) {
            return;
        }
        if (!savedInstanceStateBundle.containsKey(STABLE_ID_MAP)) {
            return;
        }
        mStableIdMap = (HashMap<Long, Boolean>) savedInstanceStateBundle.getSerializable(STABLE_ID_MAP);
        Log.d(TAG, mStableIdMap.toString());
        int i = 0;
        while (i < mItemList.size()) {
            if (mItemList.get(i) instanceof ParentObject) {
                ParentObject parentObject = (ParentObject) mItemList.get(i);
                if (mStableIdMap.containsKey(parentObject.getStableId())) {
                    parentObject.setExpanded(mStableIdMap.get(parentObject.getStableId()));
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