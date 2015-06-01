package com.ryanbrooks.expandablerecyclerviewsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample.VerticalLinearRecyclerViewSample;

/**
 * Created by Ryan Brooks on 5/19/15.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button mVerticalSampleButton;
    private Button mHorizontalSampleButton;
    private Button mGridSampleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVerticalSampleButton = (Button) findViewById(R.id.main_vertical_linear_button);
        mHorizontalSampleButton = (Button) findViewById(R.id.main_horizontal_linear_button);
        mGridSampleButton = (Button) findViewById(R.id.main_grid_button);

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
