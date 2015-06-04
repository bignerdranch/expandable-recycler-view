package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

/**
 * Created by Ryan Brooks on 5/29/15.
 */
public class VerticalLinearRecyclerViewSample extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

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

    @OnItemSelected(R.id.vertical_sample_toolbar_spinner)
    void onItemSelected(int position) {
        if (mAnimationEnabledCheckBox.isChecked()) {
            if (mDurationList.get(position) == 0) {
                mExpandableAdapter.setParentClickableViewAnimationDuration(-1);
            } else {
                mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
            }
        } else {
            mExpandableAdapter.setParentClickableViewAnimationDuration(mDurationList.get(position));
        }
        mExpandableAdapter.notifyDataSetChanged();
    }

    @OnCheckedChanged(R.id.vertical_sample_toolbar_checkbox)
    void onCheckChanged(boolean isChecked) {
        if (isChecked) {
            if (mDurationList.get(mToolbarSpinner.getSelectedItemPosition()) == 0) {
                mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
                mExpandableAdapter.notifyDataSetChanged();
            } else {
                mExpandableAdapter.setCustomParentAnimationViewId(R.id.recycler_item_arrow_parent);
                mExpandableAdapter.setParentClickableViewAnimationDuration(
                        mDurationList.get(mToolbarSpinner.getSelectedItemPosition()));
                mExpandableAdapter.notifyDataSetChanged();
            }
        } else {
            mExpandableAdapter.removeAnimation();
            mExpandableAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_recyclerview_sample);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDurationList = generateSpinnerSpeeds();

        mExpandableAdapter = new MyExpandableAdapter(this, setUpTestData(20));
        mRecyclerView.setAdapter(mExpandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, mDurationList);
        mToolbarSpinner.setAdapter(customSpinnerAdapter);

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

    private ArrayList<Long> generateSpinnerSpeeds() {
        long initialSpeed = 100;
        ArrayList<Long> speedList = new ArrayList<>();
        speedList.add(mExpandableAdapter.CUSTOM_ANIMATION_DURATION_NOT_SET);
        for (int i = 1; i <= 10; i++) {
            speedList.add(initialSpeed * i);
        }
        return speedList;
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
