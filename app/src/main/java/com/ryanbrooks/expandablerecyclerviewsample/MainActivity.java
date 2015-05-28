package com.ryanbrooks.expandablerecyclerviewsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 5/19/15.
 */
public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        MyExpandableAdapter expandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        mRecyclerView.setAdapter(expandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // TODO: Set one to null and test result
    private ArrayList<Object> setUpTestData(int numItems) {
        ArrayList<Object> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            CustomChildObject customChildObject = new CustomChildObject();
            customChildObject.setData("Child" + i);
            CustomParentObject customParentObject = new CustomParentObject(customChildObject);
            customParentObject.setNumber(i);
            customParentObject.setData("Parent " + i);
            data.add(customParentObject);
        }
        return data;
    }
}
