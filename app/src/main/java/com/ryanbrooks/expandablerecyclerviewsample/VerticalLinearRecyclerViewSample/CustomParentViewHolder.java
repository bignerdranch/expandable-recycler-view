package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ParentItemClickListener;
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
public class CustomParentViewHolder extends ParentViewHolder {

    public TextView numberText;
    public TextView dataText;
    public ImageView arrowExpand;

    /**
     * Public constructor for the CustomViewHolder.
     *
     * @param itemView the view of the parent item. Find/modify views using this.
     * @param parentItemClickListener used by the adapter. Do not modify
     */
    public CustomParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView, parentItemClickListener);

        numberText = (TextView) itemView.findViewById(R.id.recycler_item_number_parent);
        dataText = (TextView) itemView.findViewById(R.id.recycler_item_text_parent);
        arrowExpand = (ImageView) itemView.findViewById(R.id.recycler_item_arrow_parent);
    }
}
