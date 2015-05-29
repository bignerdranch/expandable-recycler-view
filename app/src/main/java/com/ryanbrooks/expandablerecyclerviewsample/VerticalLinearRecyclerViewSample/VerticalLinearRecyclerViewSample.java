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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_recyclerview_sample);

        mRecyclerView = (RecyclerView) findViewById(R.id.vertical_recyclerview_sample);
        MyExpandableAdapter expandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        mRecyclerView.setAdapter(expandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<ExpandingObject> setUpTestData(int numItems) {
        ArrayList<ExpandingObject> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            CustomChildObject customChildObject = new CustomChildObject();
            customChildObject.setData("Child " + i);
            CustomParentObject customParentObject = new CustomParentObject(customChildObject);
            customParentObject.setNumber(i);
            customParentObject.setData("Parent " + i);
            data.add(customParentObject);
        }
        return data;
    }
}
