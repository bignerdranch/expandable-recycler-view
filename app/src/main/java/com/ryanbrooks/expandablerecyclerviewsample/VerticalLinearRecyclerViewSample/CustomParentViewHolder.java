package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ryanbrooks.expandablerecyclerviewsample.R;


/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomParentViewHolder extends ParentViewHolder {

    public TextView numberText;
    public TextView dataText;
    public ImageView arrowExpand;

    public CustomParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView, parentItemClickListener);

        numberText = (TextView) itemView.findViewById(R.id.recycler_item_number_parent);
        dataText = (TextView) itemView.findViewById(R.id.recycler_item_text_parent);
        arrowExpand = (ImageView) itemView.findViewById(R.id.recycler_item_arrow_parent);
    }
}
