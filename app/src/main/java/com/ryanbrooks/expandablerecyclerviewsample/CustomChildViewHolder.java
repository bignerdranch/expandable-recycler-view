package com.ryanbrooks.expandablerecyclerviewsample;

import android.view.View;
import android.widget.TextView;

import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class CustomChildViewHolder extends ChildViewHolder {

    public TextView dataText;

    public CustomChildViewHolder(View itemView) {
        super(itemView);

        dataText = (TextView) itemView.findViewById(R.id.recycler_item_text_child);
    }
}
