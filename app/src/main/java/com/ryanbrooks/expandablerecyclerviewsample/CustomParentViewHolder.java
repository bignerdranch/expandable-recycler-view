package com.ryanbrooks.expandablerecyclerviewsample;

import android.view.View;
import android.widget.TextView;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomParentViewHolder extends ParentViewHolder {

    public TextView numberText, dataText;
    ParentItemClickListener parentItemClickListener;

    public CustomParentViewHolder(View itemView, ParentItemClickListener parentItemClickListener) {
        super(itemView, parentItemClickListener);

        numberText = (TextView) itemView.findViewById(R.id.recycler_item_number_parent);
        dataText = (TextView) itemView.findViewById(R.id.recycler_item_text_parent);
        this.parentItemClickListener = parentItemClickListener;
    }
}
