package com.ryanbrooks.expandablerecyclerviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ryanbrooks.expandablerecyclerviewsample.linear.horizontal.HorizontalLinearRecyclerViewSampleActivity;
import com.ryanbrooks.expandablerecyclerviewsample.linear.vertical.VerticalLinearRecyclerViewSampleActivity;

/**
 * Main Activity that contains navigation for sample application.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class MainActivity extends AppCompatActivity {

    private Button mVerticalSampleButton;
    private Button mHorizontalSampleButton;
    private Button mGridSampleButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(mToolbar);

        mVerticalSampleButton = (Button) findViewById(R.id.activity_main_vertical_linear_sample_button);
        mVerticalSampleButton.setOnClickListener(mVerticalSampleButtonClickListener);

        mHorizontalSampleButton = (Button) findViewById(R.id.activity_main_horizontal_linear_sample_button);
        mHorizontalSampleButton.setOnClickListener(mHorizontalSampleButtonClickListener);

        mGridSampleButton = (Button) findViewById(R.id.activity_main_grid_sample_button);
        mGridSampleButton.setOnClickListener(mGridSampleButtonClickListener);
    }

    private View.OnClickListener mVerticalSampleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(VerticalLinearRecyclerViewSampleActivity.newIntent(v.getContext()));
        }
    };

    private View.OnClickListener mHorizontalSampleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(HorizontalLinearRecyclerViewSampleActivity.newIntent(v.getContext()));
        }
    };

    private View.OnClickListener mGridSampleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
        }
    };
}
