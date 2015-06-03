package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;

/**
 * Created by ryanbrooks on 6/3/15.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<Long> {
    private static final String NO_ANIMATION_TEXT = "No Animation";
    private static final String MS = " ms";
    private static final String ONE_SECOND = "1 s";

    private Context mContext;
    private ArrayList<Long> mSpeedList;
    private LayoutInflater mInflater;

    public CustomSpinnerAdapter(Context context, ArrayList<Long> speedList) {
        super(context, R.layout.spinner_item_layout, speedList);
        mContext = context;
        mSpeedList = speedList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.spinner_item_layout, parent, false);

        TextView rowItemText = (TextView) row.findViewById(R.id.spinner_item_text);

        if (mSpeedList.get(position) == -1) {
            rowItemText.setText(NO_ANIMATION_TEXT);
        } else if(mSpeedList.get(position) == 1000) {
            rowItemText.setText(ONE_SECOND);
        } else {
            rowItemText.setText(mSpeedList.get(position).toString() + MS);
        }

        return row;
    }
}
