package com.ryanbrooks.expandablerecyclerviewsample.linear.vertical;

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

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Activity for the vertical linear RecyclerView sample.
 * Uses ButterKnife to inject view resources.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class VerticalLinearRecyclerViewSampleActivity extends AppCompatActivity implements ExpandableRecyclerAdapter.ExpandCollapseListener {

    private static final int NUM_TEST_DATA_ITEMS = 20;

    private VerticalExpandableAdapter mExpandableAdapter;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    public static Intent newIntent(Context context) {
        return new Intent(context, VerticalLinearRecyclerViewSampleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_linear_recycler_view_sample);

        mToolbar = (Toolbar) findViewById(R.id.activity_vertical_linear_recycler_view_sample_toolbar);
        setupToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_vertical_linear_recycler_view_sample_recyclerView);

        // Create a new adapter with 20 test data items
        mExpandableAdapter = new VerticalExpandableAdapter(this, setUpTestData(NUM_TEST_DATA_ITEMS));

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.setExpandCollapseListener(this);

        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        mRecyclerView.setAdapter(mExpandableAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void onListItemExpanded(int position) {
        String toastMessage = getString(R.string.item_expanded, position);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemCollapsed(int position) {
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
     *
     * Each child list item contains a string.
     * Each parent list item contains a number corresponding to the number of the parent and a string
     * that contains a message.
     * Each parent also contains a list of children which is generated in this. Every odd numbered
     * parent gets one child and every even numbered parent gets two children.
     *
     * @return A List of Objects that contains all parent items. Expansion of children are handled in the adapter
     */
    private List<VerticalParent> setUpTestData(int numItems) {
        List<VerticalParent> verticalParentList = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            List<Object> childItemList = new ArrayList<>();

            VerticalChild verticalChild = new VerticalChild();
            verticalChild.setChildText(getString(R.string.child_text, i));
            childItemList.add(verticalChild);

            // Evens get 2 children, odds get 1
            if (i % 2 == 0) {
                VerticalChild verticalChild2 = new VerticalChild();
                verticalChild2.setChildText(getString(R.string.second_child_text, i));
                childItemList.add(verticalChild2);
            }

            VerticalParent verticalParent = new VerticalParent();
            verticalParent.setChildItemList(childItemList);
            verticalParent.setParentNumber(i);
            verticalParent.setParentText(getString(R.string.parent_text, i));
            if (i == 0) {
                verticalParent.setInitiallyExpanded(true);
            }
            verticalParentList.add(verticalParent);
        }

        return verticalParentList;
    }
}
