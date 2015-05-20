package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanbrooks.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class MyExpandableAdapter extends ExpandableRecyclerAdapter<CustomParentViewHolder, CustomChildViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<TestDataModel> testData;

    public MyExpandableAdapter(Context context, ArrayList<TestDataModel> testData) {
        this.context = context;
        this.testData = testData;
        this.inflater = inflater.from(context);
    }

    @Override
    public CustomParentViewHolder onCreateParentViewHolder(ViewGroup parentView) {
        View view = inflater.inflate(R.layout.recycler_item_layout_parent, parentView, false);
        CustomParentViewHolder parentViewHolder = new CustomParentViewHolder(view);
        return parentViewHolder;
    }

    @Override
    public CustomChildViewHolder onCreateChildViewHolder(ViewGroup parentView) {
        View view = inflater.inflate(R.layout.recycler_item_layout_child, parentView, false);
        CustomChildViewHolder childViewHolder = new CustomChildViewHolder(view);
        return childViewHolder;
    }

    @Override
    public void onBindParentViewHolder(CustomParentViewHolder holder, int position) {
        TestDataModel data = testData.get(position);
        holder.numberText.setText(data.getNumber() + "");
        holder.dataText.setText(data.getData());
    }

    @Override
    public void onBindChildViewHolder(CustomChildViewHolder holder, int position) {
        TestDataModel data = testData.get(position);

        if (data.hasChildObject()) {
            ChildDataModel childData = (ChildDataModel) data.getChildObject();
            holder.dataText.setText(childData.getData());
        }
    }
}
