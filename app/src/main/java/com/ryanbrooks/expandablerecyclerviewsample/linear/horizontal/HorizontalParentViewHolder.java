package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ryanbrooks.expandablerecyclerviewsample.R;

/**
 * Custom parent ViewHolder. Any views should be found and set to public variables here to be
 * referenced in your custom ExpandableAdapter later.
 * <p>
 * Must extend ParentViewHolder.
 */
public class HorizontalParentViewHolder extends ParentViewHolder {

    public TextView mNumberTextView;
    public TextView mDataTextView;
    public ImageView mArrowExpandImageView;

    /**
     * Public constructor for the CustomViewHolder.
     *
     * @param itemView the view of the parent item. Find/modify views using this.
     */
    public HorizontalParentViewHolder(View itemView) {
        super(itemView);

        mNumberTextView = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_number_textView);
        mDataTextView = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_parent_textView);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.list_item_parent_horizontal_arrow_imageView);
    }

    public void bind(int parentNumber, String parentText) {
        mNumberTextView.setText(String.valueOf(parentNumber));
        mDataTextView.setText(parentText);
    }
}