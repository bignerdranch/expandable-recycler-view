package com.ryanbrooks.expandablerecyclerviewsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ryanbrooks.expandablerecyclerview.ClickListener.ChildItemClickListener;
import com.ryanbrooks.expandablerecyclerview.ClickListener.RecyclerItemClickListener;

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
        MyExpandableAdapter expandableAdapter = new MyExpandableAdapter(this, setUpTestData(10));
        mRecyclerView.setAdapter(expandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<TestDataModel> setUpTestData(int numItems) {
        ArrayList<TestDataModel> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            ChildDataModel childDataModel = new ChildDataModel();
            childDataModel.setData("Child" + i);
            TestDataModel dataModel = new TestDataModel(childDataModel);
            dataModel.setNumber(i);
            dataModel.setData("Parent " + i);
            data.add(dataModel);
        }
        return data;
    }
}
