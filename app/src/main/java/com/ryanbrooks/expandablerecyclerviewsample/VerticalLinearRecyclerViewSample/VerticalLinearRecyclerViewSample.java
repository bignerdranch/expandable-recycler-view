package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Brooks on 5/29/15.
 */
public class VerticalLinearRecyclerViewSample extends Activity {

    private RecyclerView mRecyclerView;
    private MyExpandableAdapter mExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_recyclerview_sample);

        mRecyclerView = (RecyclerView) findViewById(R.id.vertical_recyclerview_sample);
        mExpandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        mRecyclerView.setAdapter(mExpandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<ExpandingObject> setUpTestData(int numItems) {
        ArrayList<ExpandingObject> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            CustomChildObject customChildObject = new CustomChildObject();
            customChildObject.setData("Child " + i);

            CustomParentObject customParentObject = new CustomParentObject(i, customChildObject);
            customParentObject.setNumber(i);
            customParentObject.setData("Parent " + i);
            data.add(customParentObject);
        }
        return data;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = mExpandableAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }
}
