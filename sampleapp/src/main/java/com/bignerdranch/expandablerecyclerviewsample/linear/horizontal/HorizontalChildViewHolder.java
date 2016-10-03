package com.bignerdranch.expandablerecyclerviewsample.linear.horizontal;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerviewsample.R;

/**
 * Custom child ViewHolder. Any views should be found and set to public variables here to be
 * referenced in your custom ExpandableRecyclerAdapter later.
 *
 * Must extend ChildViewHolder.
 */
public class HorizontalChildViewHolder extends ChildViewHolder {

    private static final String TAG = "HorizontalChildVH";

    public TextView mDataTextView;

    /**
     * Public constructor for the custom child ViewHolder
     *
     * @param itemView the child ViewHolder's view
     */
    public HorizontalChildViewHolder(@NonNull View itemView) {
        super(itemView);

        mDataTextView = (TextView) itemView.findViewById(R.id.list_item_horizontal_child_textView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Child at " + getChildAdapterPosition() + ", with parent at " + getParentAdapterPosition()
                        + " clicked, child item is \"" + getChildListItem() + "\"");
            }
        });
    }

    public void bind(String childText) {
        mDataTextView.setText(childText);
    }
}
