# Expandable RecyclerView
[![Build Status](https://magnum.travis-ci.com/bignerdranch/expandable-recycler-view.svg?token=cHtfwvsfoiWYD2CaiRkc&branch=master)](https://magnum.travis-ci.com/bignerdranch/expandable-recycler-view)

## About
The Expandable RecyclerView is a library written to allow for an expanded view to be attached to each ViewHolder. To allow for full functionality of a normal RecyclerView in both the parent ViewHolder and the expanded child ViewHolder, the recyclerview has been modified to use two types of ViewHolders, a child and a parent with the ability to customize each separately.

##Project Setup
**Gradle**

Simply add this to your app's build.gradle:
```gradle
compile 'com.bignerdranch.android:expandablerecyclerview:1.0.3'
```


You can also clone the project and add it as a module to your project.

Clone with:
```
git clone git@github.com:bignerdranch/expandable-recycler-view.git
```

Then, navigate inside the directory, clean and build from Android Studio or with
```
./gradlew clean app:assemble
```

Now run the sample app on any device or emulator/simulator to view the basic functionality of the list. Code for the sample is located under ```/app/src/main```. All library code is located under ```/expandablerecyclerview/src/main```. The CriminalIntent sample is located under ```/criminalintentsample/src/main```.

## Overview
Expandable RecyclerView can be used with any stock Android RecyclerView.

**What you need to implement:**
- A custom adapter that extends ```ExpandableRecyclerAdapter```
- Two custom ViewHolders: a parent ViewHolder that extends ```ParentViewHolder``` and a child ViewHolder that extends ```ChildViewHolder```
- The list of objects you wish to display in your RecyclerView must extend ```ParentObject```.
  - It is best practice to separate your child data into its own Object, although it is not required.
- A parent layout and a child layout

## Tutorial

I have written an in-depth tutorial but it has yet to be published yet. The link will be posted here when available.

Javadocs for the library and sample are available [here](http://bignerdranch.github.io/expandable-recycler-view/).

## Usage
 First, Create a RecyclerView in your xml layout and instantiate/define it in your activity/fragment as you would usually do.
 
 Next, create an adapter class that extends ExpandableRecyclerAdapter. Implement the required methods.
 
 Then, create two ViewHolders and their respective layouts. One ViewHolder must extend ```ParentViewHolder``` and the other must extend ```ChildViewHolder```. Create their respective views and create variables in these ViewHolders to access those views.
 
 Next, the Object that contains the data to be displayed in your RecyclerView must extend ```ParentObject```

 In onCreate or onCreateView of your activity or fragment, create and attach your custom expandable adapter like so:
 
```java
RecyclerView mRecyclerView = (RecyclerView) findViewById(YOUR RECYCLERVIEW ID);
MyExpandable mExpandableAdapter = new MyExpandableAdapter(getActivity(), PARENTOBJECT LIST);
mRecyclerView.setAdapter(mExpandableAdapter);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
```

 Inside your ExpandableRecyclerAdapter, you can create and bind your Parent and Child ViewHolders just as you would create and bind ViewHolders in a normal RecyclerView.

 Then, define a parent object and implement ```ParentObject```. It is also best practice to create a separate child object to store any data that you need to display in the child view, but it is not required. When you implement ```ParentObject```, you need to create an instance variable, List<Object>, to store all the children of the parent object in. It must be type Object. If it is null or empty, no child will be shown.
 
```java
public class MyCustomParentObject implements ParentObject {
    private List<Object> mChildObjectList;
    
    /**
     * Your constructors, variables, data and methods for your Object go here
     */
    
    @Override
    public List<Object> getChildObject() {
        /**
         * You can either return a newly created list of children here or attach them later
         */
     
        return mChildObjectList;
    }
    
    @Override
    public void setChildObject(List<Object> childObjectList) {
        mChildObjectList = childObjectList;
    }
}
```
When generating the list of parent objects, you should attach all children to them there. If the children share data with your ```ParentObject```, you can simply create a list of children in the constructor for your parent object or in the getter method for the list.
 
#### Extras
 You can define a custom button, image or view to trigger the expansion rather than clicking the whole item (default). To do this, in your activity or fragment, call ```myCustomExpandingAdapter.setCustomClickableView(Your Custom View ID)``` and pass in the id.
 
 If you do set a custom clickable view, you can also set an animation for the view to rotate 180 degrees when expanding and collapsing. This is useful primarily with arrows which signifies for the user to click it to change the expansion. You can do this by calling ```myCustomExpandingAdapter.setRotation(long durationInMS)``` in the constructor below where you called ```setCustomClickableView()```.
 
 By default the rotation is off. If you'd like to use the default state, call ```myCustomExpandingAdapter.setParentClickableViewAnimationDefaultDuration()```. This rotation's duration is 200 ms.
 
 After implementing these, in the activity or fragment that is holding your RecyclerView, simply set the adapter to your custom adapter, and set the layout manager to a new LinearLayoutManager. An example is here:
 
 ```
 MyCustomExpandingAdapter myCustomExpandingAdapter = new MyCustomExpandingAdapter(this, objectList);
 // Animation configuration goes here
 mRecyclerView.setAdapter(myCustomExpandingAdapter);
 mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
 ```
 
#### Saving Expanded States onResume() or on Rotation

To save expanded/collapsed states, inside onSaveInstanceState(Bundle outState) of your activity or fragment, call ```myCustomExpandingAdapter.onSaveInstanceState(outState)```. In onRestoreInstanceState(Bundle savedInstanceState(Bundle savedInstanceState), call ```myCustomExpandingAdapter.onRestoreInstanceState(savedInstanceState)```. Here is an example of how to override in your activity or fragment:
 
 ```
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
