package com.bignerdranch.expandablerecyclerview.ViewHolder;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * ParentViewHolder that extends the Base RecyclerView ViewHolder. All expansion animation and click
 * handling is done here.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private ParentItemClickListener mParentItemClickListener;
    private View mClickableView;
    private boolean mRotationEnabled;
    private boolean mIsExpanded;
    private long mDuration;
    private float mRotation;

    /**
     * Public constructor that takes in an ItemView along with an implementation of
     * ParentItemClickListener to handle the clicks of either the Parent item or the custom defined
     * view.
     *
     * @param itemView
     */
    public ParentViewHolder(View itemView) {
        super(itemView);
        mIsExpanded = false;
        mDuration = DEFAULT_ROTATE_DURATION_MS;
        mRotation = INITIAL_POSITION;
    }

    /**
     * Setter for a custom Clickable view rather than the Parent item. By calling this method, the
     * Parent item is no longer responsible for triggering the expansion. The user should pass in
     * the id of the view that they wish to be the expansion trigger.
     *
     * @param clickableViewId
     */
    public void setCustomClickableViewOnly(int clickableViewId) {
        mClickableView = itemView.findViewById(clickableViewId);
        itemView.setOnClickListener(null);
        mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    /**
     * Setter for a custom Clickable view and the Parent item. By calling this method, the
     * Parent item and the clickable view are responsible for triggering the expansion. This means
     * that either can be clicked and an expansion will occur. The user should pass in an id of the
     * view they wish to set as the clickable expansion button.
     *
     * @param clickableViewId
     */
    public void setCustomClickableViewAndItem(int clickableViewId) {
        mClickableView = itemView.findViewById(clickableViewId);
        itemView.setOnClickListener(this);
        mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    /**
     * Setter method for a user defined Animation duration (in MS)
     *
     * @param animationDuration
     */
    public void setAnimationDuration(long animationDuration) {
        mRotationEnabled = true;
        mDuration = animationDuration;
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    /**
     * Disables animation of the custom clickable button.
     */
    public void cancelAnimation() {
        mRotationEnabled = false;
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    /**
     * Sets the Parent only as the trigger to expand the item. Also disables rotation of the custom
     * clickable view.
     */
    public void setMainItemClickToExpand() {
        if (mClickableView != null) {
            mClickableView.setOnClickListener(null);
        }
        itemView.setOnClickListener(this);
        mRotationEnabled = false;
    }

    /**
     * Returns if the item is currently expanded.
     *
     * @return true if expanded, false if not
     */
    public boolean isExpanded() {
        return mIsExpanded;
    }

    /**
     * Setter method for the item to be expanded or not. Also triggers the animation of the custom
     * clickable view if it and the rotation duration are both defined.
     *
     * @param isExpanded
     */
    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
        if (mRotationEnabled) {
            if (mIsExpanded && mClickableView != null && HONEYCOMB_AND_ABOVE) {
                mClickableView.setRotation(ROTATED_POSITION);
            } else if (mClickableView != null && HONEYCOMB_AND_ABOVE) {
                mClickableView.setRotation(INITIAL_POSITION);
            }
        }
    }

    /**
     * @return true if rotation is enabled, false if not
     */
    public boolean isRotationEnabled() {
        return mRotationEnabled;
    }

    /**
     * Sets the position of the custom clickable view. 0f is default and 180f is roatated
     *
     * @param rotation
     */
    public void setRotation(float rotation) {
        mRotationEnabled = true;
        mRotation = rotation;
    }

    /**
     * Getter for the ParentItemClickListener passed from the ExpandableRecyclerAdapter
     *
     * @return the ViewHolder's set ParentItemClickListner
     */
    public ParentItemClickListener getParentItemClickListener() {
        return mParentItemClickListener;
    }

    /**
     * Setter for the ParentItemClickListener implemented in ExpandableRecyclerAdapter
     *
     * @param mParentItemClickListener
     */
    public void setParentItemClickListener(ParentItemClickListener mParentItemClickListener) {
        this.mParentItemClickListener = mParentItemClickListener;
    }

    /**
     * Implementation of View.onClick to listen for the clicks on either the Parent item or the
     * custom clickable view if applicable. Triggers rotation if enabled on click.
     *
     * @param v the view that is the trigger for expansion
     */
    @Override
    public void onClick(View v) {
        if (mParentItemClickListener != null) {
            if (mClickableView != null) {
                if (mRotationEnabled) {
                    RotateAnimation rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                            RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
                    mRotation = INITIAL_POSITION;
                    rotateAnimation.setDuration(mDuration);
                    rotateAnimation.setFillAfter(true);
                    mClickableView.startAnimation(rotateAnimation);
                }
            }
            setExpanded(!mIsExpanded);
            mParentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }
}
