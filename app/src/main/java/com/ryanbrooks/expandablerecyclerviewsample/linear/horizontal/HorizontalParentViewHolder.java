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
 * Must extend ParentViewHolder
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class HorizontalParentViewHolder extends ParentViewHolder {

    public TextView numberText;
    public TextView dataText;
    public ImageView arrowExpand;

    /**
     * Public constructor for the CustomViewHolder.
     *
     * @param itemView the view of the parent item. Find/modify views using this.
     */
    public HorizontalParentViewHolder(View itemView) {
        super(itemView);

        numberText = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_number_textView);
        dataText = (TextView) itemView.findViewById(R.id.list_item_parent_horizontal_parent_textView);
        arrowExpand = (ImageView) itemView.findViewById(R.id.list_item_parent_horizontal_arrow_imageView);
    }
}