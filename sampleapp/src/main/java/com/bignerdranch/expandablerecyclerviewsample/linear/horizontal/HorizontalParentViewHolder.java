package com.bignerdranch.expandablerecyclerviewsample.linear.horizontal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerviewsample.R;

/**
 * Custom parent ViewHolder. Any views should be found and set to public variables here to be
 * referenced in your custom ExpandableAdapter later.
 *
 * Must extend ParentViewHolder.
 */
public class HorizontalParentViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    private static final String TAG = "HorizontalParentVH";

    public TextView mNumberTextView;
    public TextView mDataTextView;
    public ImageView mArrowExpandImageView;

    /**
     * Public constructor for the CustomViewHolder.
     *
     * @param itemView the view of the parent item. Find/modify views using this.
     */
    public HorizontalParentViewHolder(@NonNull View itemView) {
        super(itemView);

        mNumberTextView = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_number_textView);
        mDataTextView = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_parent_textView);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.list_item_parent_horizontal_arrow_imageView);
        mArrowExpandImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            }
        });

        final Context context = itemView.getContext();
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Parent at " + getParentAdapterPosition() + " clicked, parent item is \"" + getParent() + "\"");
                Toast.makeText(context, "This sample shows how to make a row only expand upon clicking our custom arrow view", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bind(int parentNumber, String parentText) {
        mNumberTextView.setText(String.valueOf(parentNumber));
        mDataTextView.setText(parentText);
    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        if (expanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        RotateAnimation rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                INITIAL_POSITION,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
        rotateAnimation.setDuration(DEFAULT_ROTATE_DURATION_MS);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);
    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }
}