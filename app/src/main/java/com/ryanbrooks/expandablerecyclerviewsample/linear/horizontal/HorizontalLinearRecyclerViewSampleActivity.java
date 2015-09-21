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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Listener.ExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLinearRecyclerViewSampleActivity extends AppCompatActivity implements ExpandCollapseListener {

    private static final int NUM_TEST_DATA_ITEMS = 20;
    private static final int EXPAND_COLLAPSE_SINGLE_PARENT_INDEX = 2;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Button mExpandParentTwoButton;
    private Button mCollapseParentTwoButton;
    private Button mExpandAllButton;
    private Button mCollapseAllButton;

    private List<ParentListItem> mTestDataItemList;

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

        mExpandParentTwoButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_expand_parent_two_button);
        mExpandParentTwoButton.setOnClickListener(mExpandParentTwoClickListener);

        mCollapseParentTwoButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_collapse_parent_two_button);
        mCollapseParentTwoButton.setOnClickListener(mCollapseParentTwoClickListener);

        mExpandAllButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_expand_all_button);
        mExpandAllButton.setOnClickListener(mExpandAllClickListener);

        mCollapseAllButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_collapse_all_button);
        mCollapseAllButton.setOnClickListener(mCollapseAllClickListener);

        // Create a new adapter with 20 test data items
        mTestDataItemList = setUpTestData(NUM_TEST_DATA_ITEMS);
        mExpandableAdapter = new HorizontalExpandableAdapter(this, mTestDataItemList);

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.setExpandCollapseListener(this);

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
    public void onListItemExpanded(int position) {
        String toastMessage = getString(R.string.item_expanded, position);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemCollapsed(int position) {
        String toastMessage = getString(R.string.item_collapsed, position);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener mExpandParentTwoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.expandParent(EXPAND_COLLAPSE_SINGLE_PARENT_INDEX);
        }
    };

    private View.OnClickListener mCollapseParentTwoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.collapseParent(EXPAND_COLLAPSE_SINGLE_PARENT_INDEX);
        }
    };

    private View.OnClickListener mExpandAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.expandAllParents();
        }
    };

    private View.OnClickListener mCollapseAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.collapseAllParents();
        }
    };

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
    private List<ParentListItem> setUpTestData(int numItems) {
        List<ParentListItem> parentListItemList = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            List<Object> childItemList = new ArrayList<>();

            HorizontalChild horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.child_text, i));
            childItemList.add(horizontalChild);

            // Evens get 2 children, odds get 1
            if (i % 2 == 0) {
                HorizontalChild horizontalChild2 = new HorizontalChild();
                horizontalChild2.setChildText(getString(R.string.second_child_text, i));
                childItemList.add(horizontalChild2);
            }

            HorizontalParent horizontalParent = new HorizontalParent();
            horizontalParent.setChildItemList(childItemList);
            horizontalParent.setParentNumber(i);
            horizontalParent.setParentText(getString(R.string.parent_text, i));
            if (i == 0) {
                horizontalParent.setInitiallyExpanded(true);
            }
            parentListItemList.add(horizontalParent);
        }

        return parentListItemList;
    }
}
