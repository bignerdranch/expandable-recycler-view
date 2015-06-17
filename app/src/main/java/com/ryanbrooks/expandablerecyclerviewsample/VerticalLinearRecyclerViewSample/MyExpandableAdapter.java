package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.List;

/**
 * An example custom implementation of the ExpandableRecyclerAdapter.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class MyExpandableAdapter extends ExpandableRecyclerAdapter<CustomParentViewHolder, CustomChildViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mInflater;

    /**
     * Public primary constructor.
     *
     * @param context for inflating views
     * @param itemList the list of items to be displayed in the RecyclerView
     */
    public MyExpandableAdapter(Context context, List<Object> itemList) {
        super(context, itemList);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Public secondary constructor. This constructor adds the ability to add a custom triggering
     * view when the adapter is created without having to set it later. This is here for demo
     * purposes.
     *
     * @param context for inflating views
     * @param itemList the list of items to be displayed in the RecyclerView
     * @param customClickableViewId the id of the view that triggers the expansion
     */
    public MyExpandableAdapter(Context context, List<Object> itemList,
                               int customClickableViewId) {
        super(context, itemList, customClickableViewId);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Public secondary constructor. This constructor adds the ability to add a custom triggering
     * view and a custom animation duration when the adapter is created without having to set them
     * later. This is here for demo purposes.
     *
     * @param context for inflating views
     * @param itemList the list of items to be displayed in the RecyclerView
     * @param customClickableViewId the id of the view that triggers the expansion
     * @param animationDuration the duration (in ms) of the rotation animation
     */
    public MyExpandableAdapter(Context context, List<Object> itemList,
                               int customClickableViewId, long animationDuration) {
        super(context, itemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * OnCreateViewHolder implementation for parent items. The desired ParentViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public CustomParentViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_parent, parent, false);
        return new CustomParentViewHolder(view, this);
    }

    /**
     * OnCreateViewHolder implementation for child items. The desired ChildViewHolder should
     * be inflated here
     *
     * @param parent for inflating the View
     * @return the user's custom parent ViewHolder that must extend ParentViewHolder
     */
    @Override
    public CustomChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.recycler_item_layout_child, parent, false);
        return new CustomChildViewHolder(view);
    }

    /**
     * OnBindViewHolder implementation for parent items. Any data or view modifications of the
     * parent view should be performed here.
     *
     * @param parentViewHolder the ViewHolder of the parent item created in OnCreateParentViewHolder
     * @param position the position in the RecyclerView of the item
     */
    @Override
    public void onBindParentViewHolder(CustomParentViewHolder parentViewHolder, int position, Object parentObject) {
        CustomParentObject customParentObject = (CustomParentObject) parentObject;
        parentViewHolder.numberText.setText(Integer.toString(customParentObject.getParentNumber()));
        parentViewHolder.dataText.setText(customParentObject.getParentText());
    }

    /**
     * OnBindViewHolder implementation for child items. Any data or view modifications of the
     * child view should be performed here.
     *
     * @param childViewHolder the ViewHolder of the child item created in OnCreateChildViewHolder
     * @param position the position in the RecyclerView of the item
     */
    @Override
    public void onBindChildViewHolder(CustomChildViewHolder childViewHolder, int position, Object childObject) {
        CustomChildObject customChildObject = (CustomChildObject) childObject;
        childViewHolder.dataText.setText(customChildObject.getChildText());
    }
}