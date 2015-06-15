// Generated code from Butter Knife. Do not modify!
package com.ryanbrooks.expandablerecyclerviewsample;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends com.ryanbrooks.expandablerecyclerviewsample.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492946, "field 'mVerticalSampleButton'");
    target.mVerticalSampleButton = finder.castView(view, 2131492946, "field 'mVerticalSampleButton'");
    view = finder.findRequiredView(source, 2131492947, "field 'mHorizontalSampleButton'");
    target.mHorizontalSampleButton = finder.castView(view, 2131492947, "field 'mHorizontalSampleButton'");
    view = finder.findRequiredView(source, 2131492948, "field 'mGridSampleButton'");
    target.mGridSampleButton = finder.castView(view, 2131492948, "field 'mGridSampleButton'");
    view = finder.findRequiredView(source, 2131492944, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131492944, "field 'mToolbar'");
  }

  @Override public void reset(T target) {
    target.mVerticalSampleButton = null;
    target.mHorizontalSampleButton = null;
    target.mGridSampleButton = null;
    target.mToolbar = null;
  }
}
