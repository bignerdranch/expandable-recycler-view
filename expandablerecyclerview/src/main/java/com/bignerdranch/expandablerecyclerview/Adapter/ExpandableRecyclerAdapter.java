package com.bignerdranch.expandablerecyclerview.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * The Base class for an Expandable RecyclerView Adapter
 * <p/>
 * Provides the base for a user to implement binding custom views to a Parent ViewHolder and a
 * Child ViewHolder
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
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
    private ExpandableRecyclerAdapterHelper mExpandableRecyclerAdapterHelper;
    private boolean mParentAndIconClickable = false;
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
        mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(itemList);
        mStableIdMap = generateStableIdMapFromList(mExpandableRecyclerAdapterHelper.getHelperItemList());
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
        mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(itemList);
        mStableIdMap = generateStableIdMapFromList(mExpandableRecyclerAdapterHelper.getHelperItemList());
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
        mExpandableRecyclerAdapterHelper = new ExpandableRecyclerAdapterHelper(itemList);
        mStableIdMap = generateStableIdMapFromList(mExpandableRecyclerAdapterHelper.getHelperItemList());
        mCustomParentAnimationViewId = customParentAnimationViewId;
        mAnimationDuration = animationDuration;
    }

    /**
     * Override of RecyclerView's default onCreateViewHolder.
     * <p/>
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
     * <p/>
     * This implementation determines first if the ViewHolder is a ParentViewHolder or a
     * ChildViewHolder. The respective onBindViewHolders for ParentObjects and ChildObject are then
     * called.
     * <p/>
     * If the item is a ParentObject, setting the ParentViewHolder's animation settings are then handled
     * here.
     *
     * @param holder
     * @param position
     * @throws IllegalStateException if the item in the list is neither a ParentObject or ChildObject
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position) instanceof ParentWrapper) {
            PVH parentViewHolder = (PVH) holder;

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

            parentViewHolder.setExpanded(((ParentWrapper) mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position)).isExpanded());
            onBindParentViewHolder(parentViewHolder, position, mItemList.get(position));
        } else if (mItemList.get(position) == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((CVH) holder, position, mItemList.get(position));
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
        if (mItemList.get(position) instanceof ParentObject) {
            return TYPE_PARENT;
        } else if (mItemList.get(position) == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_CHILD;
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
     * Method called to expand a ParentObject when clicked. This handles saving state, adding the
     * corresponding child objects to the list (the recyclerview list) and updating that list.
     *
     * @param parentObject
     * @param position
     */
    private void expandParent(ParentObject parentObject, int position) {
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(position);
        if (parentWrapper == null) {
            return;
        }
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);
            mStableIdMap.put(parentWrapper.getStableId(), false);
            List<Object> childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
            if (childObjectList != null) {
                for (int i = childObjectList.size() - 1; i >= 0; i--) {
                    mItemList.remove(position + i + 1);
                    mExpandableRecyclerAdapterHelper.getHelperItemList().remove(position + i + 1);
                    notifyItemRemoved(position + i + 1);
                    Log.d(TAG, "Removed " + childObjectList.get(i).toString());
                }
            }
        } else {
            parentWrapper.setExpanded(true);
            mStableIdMap.put(parentWrapper.getStableId(), true);
            List<Object> childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
            if (childObjectList != null) {
                for (int i = 0; i < childObjectList.size(); i++) {
                    mItemList.add(position + i + 1, childObjectList.get(i));
                    mExpandableRecyclerAdapterHelper.getHelperItemList().add(position + i + 1, childObjectList.get(i));
                    notifyItemInserted(position + i + 1);
                }
            }
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
            if (itemList.get(i) != null) {
                ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i);
                parentObjectHashMap.put(parentWrapper.getStableId(), parentWrapper.isExpanded());
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
        while (i < mExpandableRecyclerAdapterHelper.getHelperItemList().size()) {
            if (mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i) instanceof ParentWrapper) {
                ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapterHelper.getHelperItemAtPosition(i);
                if (mStableIdMap.containsKey(parentWrapper.getStableId())) {
                    parentWrapper.setExpanded(mStableIdMap.get(parentWrapper.getStableId()));
                    if (parentWrapper.isExpanded()) {
                        List<Object> childObjectList = ((ParentObject) parentWrapper.getParentObject()).getChildObjectList();
                        if (childObjectList != null) {
                            for (int j = 0; j < childObjectList.size(); j++) {
                                i++;
                                mItemList.add(i, childObjectList.get(j));
                                mExpandableRecyclerAdapterHelper.getHelperItemList().add(i, childObjectList.get(j));
                            }
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
}