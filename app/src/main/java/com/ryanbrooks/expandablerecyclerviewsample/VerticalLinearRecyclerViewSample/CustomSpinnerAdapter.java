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

    private LayoutInflater mInflater;

    public CustomSpinnerAdapter(Context context, ArrayList<Long> speedList) {
        super(context, R.layout.spinner_item_layout, speedList);
        mInflater = LayoutInflater.from(getContext());
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
        RowViewHolder rowViewHolder;
        if (convertView == null) {
            rowViewHolder = new RowViewHolder();
            convertView = mInflater.inflate(R.layout.spinner_item_layout, parent, false);
            rowViewHolder.rowItemText = (TextView) convertView.findViewById(R.id.spinner_item_text);

            if (getItem(position) == -1) {
                rowViewHolder.rowItemText.setText(NO_ANIMATION_TEXT);
            } else if (getItem(position) == 1000) {
                rowViewHolder.rowItemText.setText(ONE_SECOND);
            } else {
                rowViewHolder.rowItemText.setText(getItem(position).toString() + MS);
            }
        } else {
            rowViewHolder = (RowViewHolder) convertView.getTag();
        }

        convertView.setTag(rowViewHolder);
        return convertView;
    }

    private class RowViewHolder {
        TextView rowItemText;
    }
}
