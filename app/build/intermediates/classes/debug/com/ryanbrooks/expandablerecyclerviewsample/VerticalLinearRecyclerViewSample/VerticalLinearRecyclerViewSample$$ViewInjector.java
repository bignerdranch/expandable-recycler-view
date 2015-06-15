// Generated code from Butter Knife. Do not modify!
package com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class VerticalLinearRecyclerViewSample$$ViewInjector<T extends com.ryanbrooks.expandablerecyclerviewsample.VerticalLinearRecyclerViewSample.VerticalLinearRecyclerViewSample> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492949, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131492949, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131492950, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131492950, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131492970, "field 'mAnimationEnabledCheckBox' and method 'onCheckChanged'");
    target.mAnimationEnabledCheckBox = finder.castView(view, 2131492970, "field 'mAnimationEnabledCheckBox'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.onCheckChanged(p1);
        }
      });
    view = finder.findRequiredView(source, 2131492971, "field 'mToolbarSpinner' and method 'onItemSelected'");
    target.mToolbarSpinner = finder.castView(view, 2131492971, "field 'mToolbarSpinner'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.onItemSelected(p2);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
  }

  @Override public void reset(T target) {
    target.mToolbar = null;
    target.mRecyclerView = null;
    target.mAnimationEnabledCheckBox = null;
    target.mToolbarSpinner = null;
  }
}
