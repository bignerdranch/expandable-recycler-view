package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final long DEFAULT_ROTATE_DURATION = 200;

    private ParentItemClickListener mParentItemClickListener;
    private View mClickableView;
    private boolean mRotationEnabled;
    private boolean mIsRotated;
    private boolean mIsExpanded;
    private long mDuration;
    private float mRotation;

    public ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.mParentItemClickListener = parentItemClickListener;
        this.mRotationEnabled = false;
        this.mIsRotated = false;
        this.mIsExpanded = false;
        this.mRotation = DEFAULT_ROTATE_DURATION;
    }

    public void setCustomClickableView(View mClickableView) {
        this.mClickableView = mClickableView;
        itemView.setOnClickListener(null);
        mClickableView.setOnClickListener(this);
        mClickableView.setRotation(mRotation);
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean mIsExpanded) {
        this.mIsExpanded = mIsExpanded;
        if (mIsExpanded && mClickableView != null) {
            mClickableView.setRotation(ROTATED_POSITION);
        } else if (mClickableView != null) {
            mClickableView.setRotation(INITIAL_POSITION);
        }
    }

    public boolean isRoatationEnabled() {
        return mRotationEnabled;
    }

    public void setRotation(long duration) {
        this.mRotationEnabled = true;
        this.mDuration = duration;
    }

    public ParentItemClickListener getParentItemClickListener() {
        return mParentItemClickListener;
    }

    public void setParentItemClickListener(ParentItemClickListener mParentItemClickListener) {
        this.mParentItemClickListener = mParentItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mParentItemClickListener != null) {
            if (mClickableView != null) {
                if (mRotationEnabled) {
                    RotateAnimation rotateAnimation;
                    if (mIsRotated) {
                        rotateAnimation = new RotateAnimation(ROTATED_POSITION, INITIAL_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
                        mRotation = INITIAL_POSITION;
                    } else {
                        rotateAnimation = new RotateAnimation(INITIAL_POSITION, ROTATED_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
                        mRotation = ROTATED_POSITION;
                    }
                    rotateAnimation.setDuration(mDuration);
                    rotateAnimation.setFillAfter(true);
                    v.startAnimation(rotateAnimation);
                    this.mIsRotated = !mIsRotated;
                }
            }
            mParentItemClickListener.onParentItemClickListener(getLayoutPosition());
        }
    }


}
