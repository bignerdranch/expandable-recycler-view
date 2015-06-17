package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

/**
 * Sample Activity for the vertical linear RecyclerView sample.
 * Uses ButterKnife to inject view resources.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class VerticalLinearRecyclerViewSample extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static final String CUSTOM_EXPAND_BUTTON_CHECKED = "CUSTOM_EXPAND_BUTTON_CHECKED";
    private static final String CUSTOM_ANIMATION_DURATION_POSITION = "CUSTOM_ANIMATION_DURATION_POSITION";
    private static final String CHILD_TEXT = "Child ";
    private static final String SECOND_CHILD_TEXT = "_2";
    private static final String PARENT_TEXT = "Parent ";
    private static final long INITIAL_ROTATION_SPEED_MS = 100;

    private MyExpandableAdapter mExpandableAdapter;
    private ArrayList<Long> mDurationList;

    @InjectView(R.id.vertical_sample_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.vertical_recyclerview_sample)
    RecyclerView mRecyclerView;
    @InjectView(R.id.vertical_sample_toolbar_checkbox)
    CheckBox mAnimationEnabledCheckBox;
    @InjectView(R.id.vertical_sample_toolbar_spinner)
    Spinner mToolbarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_recyclerview_sample);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Generate spinner's list of rotation speeds (in ms)
        mDurationList = generateSpinnerSpeeds();

        // Create a new adapter with 20 test data items
        mExpandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        mRecyclerView.setAdapter(mExpandableAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set spinner adapter
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, mDurationList);
        mToolbarSpinner.setAdapter(customSpinnerAdapter);
    }

    /**
     * Save the instance state of the adapter to keep expanded/collapsed states when rotating or
     * pausing the activity.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState = mExpandableAdapter.onSaveInstanceState(outState);
    }

    /**
     * Load the expanded/collapsed states of the adapter back into the view when done rotating or
     * resuming the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * On item selected listener of the rotation speed spinner in the Toolbar.
     *
     * @param position
     */
    @OnItemSelected(R.id.vertical_sample_toolbar_spinner)
    void onItemSelected(int position) {
        if (mAnimationEnabledCheckBox.isChecked()) { // Only the custom triggering view triggers expansion
            if (mDurationList.get(position) == 0) {
                // Sets the rotation animation to off
                mExpandableAdapter.setParentClickableViewAnimationDuration(
                        mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
            } else {
                // Sets the animation duration to the corresponding duration at the selected position
                mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
            }
            // Disables clicking of both the item and the custom clickable view declared by the user
            mExpandableAdapter.setParentAndIconExpandOnClick(false);
        } else { // Both the custom triggering view and the parent item trigger expansion when clicked
            if (mDurationList.get(position) == 0) {
                // Sets the rotation animation to off
                mExpandableAdapter.setParentClickableViewAnimationDuration(
                        mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
                // Disable clicking of both parent and child to trigger expansion/collapsing
                mExpandableAdapter.setParentAndIconExpandOnClick(false);
            } else {
                // Sets the animation duration to the corresponding duration at the selected position
                mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
                // Sets the custom triggering view to the id of the view
                mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
                // Sets both the custom triggering view and the parent item to trigger expansion
                mExpandableAdapter.setParentAndIconExpandOnClick(true);
            }
        }
        mExpandableAdapter.notifyDataSetChanged();
    }

    /**
     * Check changed listener for the custom triggering view checkbox.
     *
     * @param isChecked
     */
    @OnCheckedChanged(R.id.vertical_sample_toolbar_checkbox)
    void onCheckChanged(boolean isChecked) {
        if (isChecked) { // Only the custom triggering view can trigger expansion
            mExpandableAdapter.setParentAndIconExpandOnClick(false);
            mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
            mExpandableAdapter.setParentClickableViewAnimationDuration((Long) mToolbarSpinner.getSelectedItem());
            mExpandableAdapter.notifyDataSetChanged();
        } else { // Both the custom triggering view and the parent item can trigger expansion
            mExpandableAdapter.setParentAndIconExpandOnClick(true);
            mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
            mExpandableAdapter.setParentClickableViewAnimationDuration((Long) mToolbarSpinner.getSelectedItem());
            mExpandableAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Method to set up test data used in the RecyclerView.
     * <p>
     * Each child object contains a string.
     * Each parent object contains a number corresponding to the number of the parent and a string
     * that contains a message.
     * Each parent also contains a list of children which is generated in this. Every odd numbered
     * parent gets one child and every even numbered parent gets two children.
     *
     * @param numItems
     * @return an ArrayList of Objects that contains all parent items. Expansion of children are handled in the adapter
     */
    private ArrayList<Object> setUpTestData(int numItems) {
        ArrayList<Object> data = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            ArrayList<Object> childObjectList = new ArrayList<>();

            // Evens get 2 children, odds get 1
            if (i % 2 == 0) {
                CustomChildObject customChildObject = new CustomChildObject();
                CustomChildObject customChildObject2 = new CustomChildObject();
                customChildObject.setChildText(CHILD_TEXT + i);
                customChildObject2.setChildText(CHILD_TEXT + i + SECOND_CHILD_TEXT);
                childObjectList.add(customChildObject);
                childObjectList.add(customChildObject2);
            } else {
                CustomChildObject customChildObject = new CustomChildObject();
                customChildObject.setChildText(CHILD_TEXT + i);
                childObjectList.add(customChildObject);
            }

            CustomParentObject customParentObject = new CustomParentObject();
            customParentObject.setChildObjectList(childObjectList);
            customParentObject.setParentNumber(i);
            customParentObject.setParentText(PARENT_TEXT + i);
            data.add(customParentObject);
        }
        return data;
    }

    /**
     * Method to set up the list of animation durations for the Toolbar's Spinner.
     * <p>
     * The list contains long values that correspond to the length of time (in ms) of the animation.
     *
     * @return the list of times (in ms) to be populated into the Toolbar's spinner.
     */
    private ArrayList<Long> generateSpinnerSpeeds() {
        ArrayList<Long> speedList = new ArrayList<>();
        speedList.add(mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
        for (int i = 1; i <= 10; i++) {
            speedList.add(INITIAL_ROTATION_SPEED_MS * i);
        }
        return speedList;
    }
}
