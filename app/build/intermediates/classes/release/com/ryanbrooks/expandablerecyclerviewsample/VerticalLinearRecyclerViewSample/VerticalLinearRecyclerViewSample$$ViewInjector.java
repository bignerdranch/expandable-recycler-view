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
    view = finder.findRequiredView(source, 2131492969, "field 'mAnimationEnabledCheckBox'");
    target.mAnimationEnabledCheckBox = finder.castView(view, 2131492969, "field 'mAnimationEnabledCheckBox'");
    view = finder.findRequiredView(source, 2131492970, "field 'mToolbarSpinner'");
    target.mToolbarSpinner = finder.castView(view, 2131492970, "field 'mToolbarSpinner'");
  }

  @Override public void reset(T target) {
    target.mToolbar = null;
    target.mRecyclerView = null;
    target.mAnimationEnabledCheckBox = null;
    target.mToolbarSpinner = null;
  }
}
