package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;


/**
 * Created by Ryan Brooks on 5/27/15.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    // TODO: Animation constants

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
            mClickableView.setRotation(180f);
        } else if (mClickableView != null) {
            mClickableView.setRotation(0.0f);
        }
    }

    public boolean isRoatationEnabled() {
        return mRotationEnabled;
    }

    public void setRoataion(long duration) {
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
                        rotateAnimation = new RotateAnimation(180f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        mRotation = 0.0f;
                    } else {
                        rotateAnimation = new RotateAnimation(0.0f, 180f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        mRotation = 180f;
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
