# Expandable RecyclerView
[![Build Status](https://travis-ci.org/bignerdranch/expandable-recycler-view.svg?branch=master)](https://travis-ci.org/bignerdranch/expandable-recycler-view)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Expandable%20RecyclerView-green.svg?style=flat)](https://android-arsenal.com/details/1/2119)

## About
**Expandable RecyclerView** is a library written to allow for an expanded view to be attached to each `ViewHolder`. To allow for full functionality of a normal `RecyclerView`, the `RecyclerView` has been modified to use two types of ViewHolders, a child and a parent with the ability to customize each separately.

##Project Setup
**Gradle**

The [latest release](https://github.com/bignerdranch/expandable-recycler-view/releases/tag/v2.0.4) can be used by adding the following to your app's build.gradle:
```gradle
compile 'com.bignerdranch.android:expandablerecyclerview:2.0.4'
```

## Overview
**Expandable RecyclerView** can be used with any stock Android `RecyclerView`. Javadocs can be found [here](http://bignerdranch.github.io/expandable-recycler-view/).

**What you need to implement:**
- A custom adapter that extends `ExpandableRecyclerAdapter`
- Two custom ViewHolders: a parent `ViewHolder` that extends `ParentViewHolder` and a child `ViewHolder` that extends `ChildViewHolder`
- The list of objects you wish to display in your RecyclerView must extend `ParentListItem`.
  - It is best practice to separate your child data into its own `Object`, although it is not required.
- A parent layout and a child layout

You can also check out the two sample applications for a full working demo of the features.

## Usage
First, create a stock `RecyclerView` in your layout file and inflate it in your `Activity`/`Fragment` as you would usually do.
 
Next, create an adapter that extends `ExpandableRecyclerAdapter`. Implement the required methods.
 
Then, create two ViewHolders and their respective layouts. One `ViewHolder` must extend `ParentViewHolder` and the other must extend `ChildViewHolder`. Create their respective views and create variables in these ViewHolders to access those views.
 
Next, the `Object` that contains the data to be displayed in your `RecyclerView` must extend `ParentListItem`. When you implement `ParentListItem`, there are three methods that you must implement, shown in the example below:

```java
public class CustomParent implements ParentListItem {
    
    private List<CustomChild> mChildItemList;

    /* Your constructors, variables, data and methods for your Object go here */

    /**
     * You can either return a newly created list of children here or attach them later
     */
    @Override
    public List<CustomChild> getChildItemList() {
        return mChildItemList;
    }

    /**
     * Allows you to specify if the list item should be expanded when first shown to the user
     */
    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
```
When generating the list of parent list items, you should attach all children to them there. If the children share data with your `ParentListItem`, you can simply create a list of children in the constructor for your parent list item or in the getter method for the list.

In `onCreate()` or `onCreateView()` of your `Activity` or `Fragment`, create and attach your custom expandable adapter like so:
 
```java
RecyclerView mRecyclerView = (RecyclerView) findViewById(YOUR RECYCLERVIEW ID);
MyExpandable mExpandableAdapter = new MyExpandableAdapter(getActivity(), YOUR_PARENT_ITEM_LIST);
mRecyclerView.setAdapter(mExpandableAdapter);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
```

Inside your `ExpandableRecyclerAdapter`, you can create and bind your parent and child ViewHolders just as you would create and bind ViewHolders in a normal `RecyclerView`.
 
#### View Behaviors
You can define a custom button, image or view to trigger the expansion rather than clicking the whole item (default). To do this, your `ParentViewHolder` implementation should override `shouldItemViewClickToggleExpansion()` to return false. Then in your implementation set a click listener on your custom view and call `expandView()` to trigger the expansion or `collapseView()` to trigger a collapse.
 
You can also create your own animations for expansion by overriding `ParentViewHolder#onExpansionToggled(boolean)`, which will be called for you when the itemView is expanded or collapsed.

The `VerticalLinearRecyclerViewSampleActivity` sample shows a rotation animation in response to expansion changing. `HorizontalLinearRecyclerViewSampleActivity` sample shows defining your own click target for expansion.

You can even trigger expansion/collapse without ever clicking a list item. 
`ExpandableRecyclerAdapter` features `expandParent(int)` and `expandParent(ParentListItem)` to expand a list item by its adapter position or by its own reference. 
The same calls for collapsing list items can be found at `collapseParent(int)` and `collapseParent(ParentListItem)`. 
To expand or collapse all items in the list at once, we've provided `ExpandableRecyclerAdapter#expandAllParents()` and `ExpandableRecyclerAdapter#collapseAllParents()`.
 
#### Listening for Expansion and Collapsing
`ParentViewHolder#onExpansionToggled(boolean)` is useful for view changes, but if there are controller level changes (database persistence, web requests, etc.) you can listen for expansion and collapse events by implementing `ExpandableRecyclerAdapter.ExpandCollapseListener` in the `Activity` or `Fragment` hosting your `RecyclerView`. The interface contains two methods, `onListItemExpanded(int)` and `onListItemCollapsed(int)`. This will allow you to listen for expansion and collapse of list items. The position passed into these methods is the position of the item in the `ParentListItem` list. Any expanded children before the item are not included in that position index.

As an example, let's say we implemented `ExpandCollapseListener` in an activity. For this to work, after creating the adapter and before setting the RecyclerView's adapter, we must call `ExpandableRecyclerAdapter#setExpandCollapseListener(ExpandCollapseListener)` on the adapter:

```java
MyCustomExpandingAdapter myCustomExpandingAdapter = new MyCustomExpandingAdapter(this, parentList);
myCustomExpandingAdapter.setExpandCollapseListener(this);
mRecyclerView.setAdapter(myCustomExpandingAdapter);
```
 
#### Saving Expanded States onResume() or on Rotation
To save expanded/collapsed states, inside `#onSaveInstanceState(Bundle)` of your `Activity` or `Fragment`, call `myCustomExpandingAdapter.onSaveInstanceState(outState)`. In `onRestoreInstanceState(Bundle)`, call `myCustomExpandingAdapter.onRestoreInstanceState(savedInstanceState)`. Here is an example of how to override in your `Activity` or `Fragment`:
 
 ```java
  @Override
  protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState = myCustomExpandingAdapter.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
  }
 ```

#### Data Manipulation

 During the life of your `RecyclerView` you may need to add or remove items from the list. Please note that the traditional `notifyDataSetChanged()` of `RecyclerView.Adapter` do not work as intended.

 Instead we provide a set of notify methods to give you the ability to inform the adapter of changes to your list of `ParentListItem`s. They are:
 ```java
 // Parent Changes
 notifyParentItemInserted(int parentPosition)
 notifyParentItemRemoved(int parentPosition)
 notifyParentItemChanged(int parentPosition)
 notifyParentItemMoved(int fromParentPosition, int toParentPosition)
 notifyParentItemRangeInserted(int parentPositionStart, int itemCount)
 notifyParentItemRangeRemoved(int parentPositionStart, int itemCount)
 notifyParentItemRangeChanged(int parentPositionStart, int itemCount)
 // Child Changes
 notifyChildItemInserted(int parentPosition, int childPosition)
 notifyChildItemRemoved(int parentPosition, int childPosition)
 notifyChildItemChanged(int parentPosition, int childPosition)
 notifyChildItemMoved(int parentPosition, int fromChildPosition, int toChildPosition)
 notifyChildItemRangeInserted(int parentPosition, int childPositionStart, int itemCount)
 notifyChildItemRangeRemoved(int parentPosition, int childPositionStart, int itemCount)
 notifyChildItemRangeChanged(int parentPosition, int childPositionStart, int itemCount)
 ```

 `HorizontalLinearRecyclerViewSampleActivity` has examples of these methods in action.

## Contributing
If you have an idea for a feature enhancement or a bug fix, we'd love to know! 
We handle issue tracking using GitHub's issue system, so feel free to add your comment there. We also welcome all pull requests.

The `dev` branch is used for future 2.X releases, so if you're adding a feature, we ask that you target the `dev` branch.
If you have a bug fix, targetting `master` is your best bet. This way, we're flexible to pull in bug fixes as soon as possible.

License
-------

      The MIT License
      
      Copyright (c) 2015 Big Nerd Ranch
      
      Permission is hereby granted, free of charge, to any person obtaining a copy
      of this software and associated documentation files (the "Software"), to deal
      in the Software without restriction, including without limitation the rights
      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      copies of the Software, and to permit persons to whom the Software is
      furnished to do so, subject to the following conditions:
      
      The above copyright notice and this permission notice shall be included in
      all copies or substantial portions of the Software.
      
      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      THE SOFTWARE.
