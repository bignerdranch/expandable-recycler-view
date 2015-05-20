package com.ryanbrooks.expandablerecyclerview.ClickListener;

import android.content.Context;
import android.view.View;

/**
 * Created by Ryan Brooks on 5/20/15.
 */
public class ExpandItemClickListener implements View.OnClickListener {

    private Context context;
    private int openViewId;

    public ExpandItemClickListener(Context context, int openViewId) {
        this.context = context;
        this.openViewId = openViewId;
    }
    @Override
    public void onClick(View v) {

    }
}
