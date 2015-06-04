package com.ryanbrooks.expandablerecyclerview.ViewHolder;

import android.os.Build;
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
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private ParentItemClickListener mParentItemClickListener;
    private View mClickableView;
    private boolean mRotationEnabled;
    private boolean mIsExpanded;
    private long mDuration;
    private float mRotation;

    public ParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView);

        mParentItemClickListener = parentItemClickListener;
        mIsExpanded = false;
        mDuration = DEFAULT_ROTATE_DURATION_MS;
        mRotation = INITIAL_POSITION;
    }

    public void setCustomClickableView(View clickableView) {
        mClickableView = clickableView;
        itemView.setOnClickListener(null);
        mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    public void setCustomClickableViewOnly(int clickableViewId) {
        mClickableView = itemView.findViewById(clickableViewId);
        itemView.setOnClickListener(null);
        mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    public void setCustomClickableViewAndItem(int clickableViewId) {
        mClickableView = itemView.findViewById(clickableViewId);
        itemView.setOnClickListener(this);
        mClickableView.setOnClickListener(this);
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    public void setAnimationDuration(long animationDuration) {
        mRotationEnabled = true;
        mDuration = animationDuration;
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    public void cancelAnimation() {
        mRotationEnabled = false;
        if (HONEYCOMB_AND_ABOVE && mRotationEnabled) {
            mClickableView.setRotation(mRotation);
        }
    }

    public void setMainItemClickToExpand() {
        if (mClickableView != null) {
            mClickableView.setOnClickListener(null);
        }
        itemView.setOnClickListener(this);
        mRotationEnabled = false;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

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

    public boolean isRotationEnabled() {
        return mRotationEnabled;
    }

    public void setRotation(float rotation) {
        mRotationEnabled = true;
        mRotation = rotation;
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
