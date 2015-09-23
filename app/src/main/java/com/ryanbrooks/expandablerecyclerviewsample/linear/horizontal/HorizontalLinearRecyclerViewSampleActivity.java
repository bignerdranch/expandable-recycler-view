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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLinearRecyclerViewSampleActivity extends AppCompatActivity implements ExpandableRecyclerAdapter.ExpandCollapseListener {

    private static final int NUM_TEST_DATA_ITEMS = 20;
    private static final int EXPAND_COLLAPSE_SINGLE_PARENT_INDEX = 2;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Button mExpandParentTwoButton;
    private Button mCollapseParentTwoButton;
    private Button mExpandAllButton;
    private Button mCollapseAllButton;

    private List<HorizontalParent> mTestDataItemList;

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

        Button addToEndButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_to_end_button);
        addToEndButton.setOnClickListener(mAddToEndClickListener);

        Button removeFromEndButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_from_end_button);
        removeFromEndButton.setOnClickListener(mRemoveFromEndClickListener);

        Button addToSecondButton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_add_to_second_button);
        addToSecondButton.setOnClickListener(mAddToSecondClickListener);

        Button removeSecondbutton = (Button) findViewById(R.id.activity_horizontal_linear_recycler_view_remove_second_button);
        removeSecondbutton.setOnClickListener(mRemoveSecondClickListener);

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

    private OnClickListener mAddToEndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            ArrayList<HorizontalChild> childList = new ArrayList<>();
            int parentNumber = mTestDataItemList.size();

            HorizontalChild horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.child_text, parentNumber));
            childList.add(horizontalChild);

            horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.second_child_text, parentNumber));
            childList.add(horizontalChild);

            horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.third_child_text, parentNumber));
            childList.add(horizontalChild);

            HorizontalParent horizontalParent = new HorizontalParent();
            horizontalParent.setChildItemList(childList);
            horizontalParent.setParentNumber(parentNumber);
            horizontalParent.setParentText(getString(R.string.parent_text, parentNumber));
            if (parentNumber % 2 == 0) {
                horizontalParent.setInitiallyExpanded(true);
            }


            mExpandableAdapter.addParent(horizontalParent);
            mTestDataItemList.add(horizontalParent);
        }
    };

    private OnClickListener mAddToSecondClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<HorizontalChild> childList = new ArrayList<>();
            int parentNumber = mTestDataItemList.size();

            HorizontalChild horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.child_insert_text, 1));
            childList.add(horizontalChild);

            horizontalChild = new HorizontalChild();
            horizontalChild.setChildText(getString(R.string.child_insert_text, 2));
            childList.add(horizontalChild);

            HorizontalParent horizontalParent = new HorizontalParent();
            horizontalParent.setChildItemList(childList);
            horizontalParent.setParentNumber(parentNumber);
            horizontalParent.setParentText(getString(R.string.inserted_parent_text));
            if (parentNumber % 2 == 0) {
                horizontalParent.setInitiallyExpanded(true);
            }

            mExpandableAdapter.addParent(1, horizontalParent);
            mTestDataItemList.add(1, horizontalParent);
        }
    };

    private OnClickListener mRemoveSecondClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTestDataItemList.size() < 2) {
                return;
            }

            HorizontalParent horizontalParent = mTestDataItemList.get(1);
            mExpandableAdapter.removeParent(horizontalParent);
            mTestDataItemList.remove(horizontalParent);

        }
    };

    private OnClickListener mRemoveFromEndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mExpandableAdapter.removeParent(mTestDataItemList.size() - 1);
            mTestDataItemList.remove(mTestDataItemList.size() - 1);
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
     *
     * Each child list item contains a string.
     * Each parent list item contains a number corresponding to the number of the parent and a string
     * that contains a message.
     * Each parent also contains a list of children which is generated in this. Every odd numbered
     * parent gets one child and every even numbered parent gets two children.
     *
     * @return A List of Objects that contains all parent items. Expansion of children are handled in the adapter
     */
    private List<HorizontalParent> setUpTestData(int numItems) {
        List<HorizontalParent> horizontalParentList = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            List<HorizontalChild> childItemList = new ArrayList<>();

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
            horizontalParentList.add(horizontalParent);
        }

        return horizontalParentList;
    }
}
