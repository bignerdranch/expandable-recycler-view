package com.ryanbrooks.expandablerecyclerviewsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample.VerticalLinearRecyclerViewSample;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Main Activity that contains navigation for sample application.
 * Uses ButterKnife to inject view resources.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.main_vertical_linear_button)
    Button mVerticalSampleButton;
    @InjectView(R.id.main_horizontal_linear_button)
    Button mHorizontalSampleButton;
    @InjectView(R.id.main_grid_button)
    Button mGridSampleButton;
    @InjectView(R.id.activity_main_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

        mVerticalSampleButton.setOnClickListener(this);
        mHorizontalSampleButton.setOnClickListener(this);
        mGridSampleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mVerticalSampleButton) {
            Intent i = new Intent(this, VerticalLinearRecyclerViewSample.class);
            startActivity(i);
        } else if (v == mHorizontalSampleButton) {
            Toast.makeText(this,
                    this.getResources().getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT)
                    .show();
        } else if (v == mGridSampleButton) {
            Toast.makeText(this,
                    this.getResources().getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this,
                    this.getResources().getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
