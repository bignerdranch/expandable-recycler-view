package com.ryanbrooks.expandablerecyclerviewsample.linear.vertical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ryanbrooks.expandablerecyclerviewsample.R;

import java.util.ArrayList;

/**
 * Custom adapter for the animation duration selection Spinner in the Toolbar.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public class VerticalSpinnerAdapter extends ArrayAdapter<Long> {

    private static final int MAX_ANIMATION_DURATION_MS = 1000;

    private LayoutInflater mInflater;

    public VerticalSpinnerAdapter(Context context, ArrayList<Long> speedList) {
        super(context, R.layout.list_item_animation_duration, speedList);
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
        Context context = getContext();
        RowViewHolder rowViewHolder;

        if (convertView == null) {
            rowViewHolder = new RowViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_animation_duration, parent, false);
            rowViewHolder.mRowItemTextView = (TextView) convertView.findViewById(R.id.list_item_animation_duration_textView);
        } else {
            rowViewHolder = (RowViewHolder) convertView.getTag();
        }

        long animationDurationMs = getItem(position);
        if (animationDurationMs == -1) {
            rowViewHolder.bind(context.getString(R.string.no_animation));
        } else if (animationDurationMs == MAX_ANIMATION_DURATION_MS) {
            rowViewHolder.bind(context.getString(R.string.spinner_adapter_time_s));
        } else {
            rowViewHolder.bind(context.getString(R.string.spinner_adapter_time_ms, animationDurationMs));
        }

        convertView.setTag(rowViewHolder);
        return convertView;
    }

    private class RowViewHolder {

        private TextView mRowItemTextView;

        public void bind(String rowItemText) {
            mRowItemTextView.setText(rowItemText);
        }
    }
}
