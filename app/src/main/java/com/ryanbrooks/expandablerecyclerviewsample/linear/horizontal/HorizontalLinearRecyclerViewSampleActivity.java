package com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;

public class HorizontalLinearRecyclerViewSampleActivity extends AppCompatActivity implements ExpandCollapseListener {

    private static final int NUM_TEST_DATA_ITEMS = 20;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private HorizontalExpandableAdapter mExpandableAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, HorizontalLinearRecyclerViewSampleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_linear_recycler_view_sample);

        mToolbar = (Toolbar) findViewById(R.id.activity_horizontal_linear_recycler_view_toolbar);
        setupToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_horizontal_linear_recycler_view_sample_recyclerView);

        // Create a new adapter with 20 test data items
        mExpandableAdapter = new HorizontalExpandableAdapter(this, setUpTestData(NUM_TEST_DATA_ITEMS));

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.addExpandCollapseListener(this);

        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        mRecyclerView.setAdapter(mExpandableAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * Save the instance state of the adapter to keep expanded/collapsed states when rotating or
     * pausing the activity.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = mExpandableAdapter.onSaveInstanceState(outState);
    }

    /**
     * Load the expanded/collapsed states of the adapter back into the view when done rotating or
     * resuming the activity.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRecyclerViewItemExpanded(int position) {
        String toastMessage = getString(R.string.item_expanded, position);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemCollapsed(int position) {
        String toastMessage = getString(R.string.item_collapsed, position);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Method to set up test data used in the RecyclerView.
     * <p/>
     * Each child object contains a string.
     * Each parent object contains a number corresponding to the number of the parent and a string
     * that contains a message.
     * Each parent also contains a list of children which is generated in this. Every odd numbered
     * parent gets one child and every even numbered parent gets two children.
     *
     * @return an ArrayList of Objects that contains all parent items. Expansion of children are handled in the adapter
     */
    private ArrayList<ParentObject> setUpTestData(int numItems) {
        ArrayList<ParentObject> parentObjectList = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            ArrayList<Object> childObjectList = new ArrayList<>();

            HorizontalChildObject horizontalChildObject = new HorizontalChildObject();
            horizontalChildObject.setChildText(getString(R.string.child_text, i));
            childObjectList.add(horizontalChildObject);

            // Evens get 2 children, odds get 1
            if (i % 2 == 0) {
                HorizontalChildObject horizontalChildObject2 = new HorizontalChildObject();
                horizontalChildObject2.setChildText(getString(R.string.second_child_text, i));
                childObjectList.add(horizontalChildObject2);
            }

            HorizontalParentObject horizontalParentObject = new HorizontalParentObject();
            horizontalParentObject.setChildObjectList(childObjectList);
            horizontalParentObject.setParentNumber(i);
            horizontalParentObject.setParentText(getString(R.string.parent_text, i));
            if (i == 0) {
                horizontalParentObject.setInitiallyExpanded(true);
            }
            parentObjectList.add(horizontalParentObject);
        }
        return parentObjectList;
    }
}
