# Expandable RecyclerView
[![Build Status](https://travis-ci.org/bignerdranch/expandable-recycler-view.svg)](https://travis-ci.org/bignerdranch/expandable-recycler-view)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Expandable%20RecyclerView-green.svg?style=flat)](https://android-arsenal.com/details/1/2119)

## About
The Expandable RecyclerView is a library written to allow for an expanded view to be attached to each `ViewHolder`. To allow for full functionality of a normal `RecyclerView`, the `RecyclerView` has been modified to use two types of ViewHolders, a child and a parent with the ability to customize each separately.

##Project Setup
**Gradle**

The [latest release](https://github.com/bignerdranch/expandable-recycler-view/releases/tag/v1.0.3) can be used by adding this to your app's build.gradle:
```gradle
compile 'com.bignerdranch.android:expandablerecyclerview:1.0.3'
```

Current development can be found at the following snapshot:
```gradle
compile 'com.bignerdranch.android:expandablerecyclerview:2.0.0-SNAPSHOT'
```
Add Sonatype's snapshots to your repositories closure in the root `build.gradle`:
```gradle
allprojects {
    repositories {
        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    }
}
```
You can also clone the project and add it as a module to your project.

## Overview
Expandable RecyclerView can be used with any stock Android `RecyclerView`.

**What you need to implement:**
- A custom adapter that extends `ExpandableRecyclerAdapter`
- Two custom ViewHolders: a parent `ViewHolder` that extends `ParentViewHolder` and a child `ViewHolder` that extends `ChildViewHolder`
- The list of objects you wish to display in your RecyclerView must extend `ParentListItem`.
  - It is best practice to separate your child data into its own Object, although it is not required.
- A parent layout and a child layout

## Tutorial

A more in depth tutorial can be found [here](https://www.bignerdranch.com/blog/expand-a-recyclerview-in-four-steps/).
Note that the above blog post was written during the 1.0.0 release, so some implementation details may differ from their current state.

Javadocs for the library and sample are available [here](http://bignerdranch.github.io/expandable-recycler-view/).

## Usage
First, create a stock `RecyclerView` in your layout file and inflate it in your `Activity`/`Fragment` as you would usually do.
 
Next, create an adapter that extends `ExpandableRecyclerAdapter`. Implement the required methods.
 
Then, create two ViewHolders and their respective layouts. One `ViewHolder` must extend `ParentViewHolder` and the other must extend `ChildViewHolder`. Create their respective views and create variables in these ViewHolders to access those views.
 
Next, the `Object` that contains the data to be displayed in your RecyclerView must extend `ParentListItem`. When you implement `ParentListItem` there are three methods that you must implement, shown in the example below:

```java
public class CustomParent implements ParentListItem {
    
    private List<Object> mChildItemList;

    /* Your constructors, variables, data and methods for your Object go here */

    /**
     * You can either return a newly created list of children here or attach them later
     */
    @Override
    public List<Object> getChildItemList() {
        return mChildObjectList;
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

In `onCreate` or `onCreateView` of your `Activity` or `Fragment`, create and attach your custom expandable adapter like so:
 
```java
RecyclerView mRecyclerView = (RecyclerView) findViewById(YOUR RECYCLERVIEW ID);
MyExpandable mExpandableAdapter = new MyExpandableAdapter(getActivity(), YOUR_PARENT_ITEM_LIST);
mRecyclerView.setAdapter(mExpandableAdapter);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
```

Inside your `ExpandableRecyclerAdapter`, you can create and bind your parent and child ViewHolders just as you would create and bind ViewHolders in a normal `RecyclerView`.
 
#### View Behaviors
You can define a custom button, image or view to trigger the expansion rather than clicking the whole item (default). To do this, your `ParentViewHolder` implementation should override `shouldItemViewClickToggleExpansion()` to return false. Then in your implementation set an click listener on your custom view and call `toggleExpansion()` to trigger the expansion.
 
You can also create your own animations for expansion by overriding `onExpansionToggled(boolean isExpanded)` which will be called for you when the itemView is expanded or collapsed.

The `VerticalLinearRecyclerViewSampleActivity` sample shows a rotation animation in response to expansion changing. `HorizontalLinearRecyclerViewSampleActivity` sample shows defining your own click target for expansion.

You can even trigger expansion/collapse without ever clicking a list item. `ExpandableRecyclerAdapter` features a
 
#### Listening for Expansion and Collapsing

 `onExpansionToggled(boolean isExpanded)` is useful for view changes, but if there are controller level changes (database persistence, web requests etc) you can listen for expansion and collapsing events by implementing ```ExpandCollapseListener``` in the activity or fragment hosting your RecyclerView. The interface contains two methods, ```onRecyclerViewItemExpanded(int position)``` and ```onRecyclerViewItemCollapsed(int position)```. This will allow you to listen for expansion and collapsing of ParentObjects. The position passed into these methods is the position of the item in the ParentObject list. Any expanded children before the item are not included in that position integer.

As an example, let's say we implemented ```ExpandCollapseListener``` in an activity. For this to work, after creating the adapter and before setting the RecyclerView's adapter, we must call ```addExpandCollapseListener(ExpandCollapseListener yourExpandCollapseListener``` on the adapter:

```java
 MyCustomExpandingAdapter myCustomExpandingAdapter = new MyCustomExpandingAdapter(this, objectList);
 myCustomExpandingAdapter.addExpandCollapseListener(this);
 mRecyclerView.setAdapter(myCustomExpandingAdapter);
 ```
 
#### Saving Expanded States onResume() or on Rotation

To save expanded/collapsed states, inside onSaveInstanceState(Bundle outState) of your activity or fragment, call ```myCustomExpandingAdapter.onSaveInstanceState(outState)```. In onRestoreInstanceState(Bundle savedInstanceState(Bundle savedInstanceState), call ```myCustomExpandingAdapter.onRestoreInstanceState(savedInstanceState)```. Here is an example of how to override in your activity or fragment:
 
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
 
You can also check out the two sample applications for a full working demo.

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
