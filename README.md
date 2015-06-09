# Expandable RecyclerView
[![Build Status](https://magnum.travis-ci.com/bignerdranch/expandable-recycler-view.svg?token=cHtfwvsfoiWYD2CaiRkc&branch=master)](https://magnum.travis-ci.com/bignerdranch/expandable-recycler-view)

## About
The Expandable RecyclerView is a library written to allow for an expanded view to be attached to each ViewHolder. To allow for full functionality of a normal RecyclerView in both the parent ViewHolder and the expanded child ViewHolder, the recyclerview has been modified to use two types of ViewHolders, a child and a parent with the ability to customize each separately.

##Project Setup
To use this library, clone the repository along with the sample project with
```
git clone git@github.com:bignerdranch/expandable-recycler-view.git
```

Then, navigate inside the directory, clean and build from Android Studio or with
```
./gradlew clean app:assemble
```

Now run the sample app on any device or emulator/simulator to view the basic functionality of the list. Code for the sample is located under ```/app/src/main```. All library code is located under ```/expandablerecyclerview/src/main```.

##Overview
Expandable RecyclerView can be used with any default RecyclerView. **As of now it cannot work with other variants or libraries of RecyclerView**.

The main difference between a regular RecyclerView and the Expandable RecyclerView is you will now use two different ViewHolders, a parent ViewHolder (your main, non-expanding view) and a child ViewHolder (the view that will drop down from the parent when the parent or defined view item is clicked). Inside each ViewHolder, you will customize and set the ViewHolders' views as you usually would respectively in ```onBindParentViewHolder``` and ```onBindChildViewHolder```.

The objects you wish to display in your RecyclerView as a Parent will need to extend ```ParentObject``` and any data you wish to display in the dropdown will need to be included in an Object that extends ```ChildObject```. When constructing a ParentObject, you will need to pass a child object in that contains the data. **This child object does not have to contain any data, as long as you handle the view in the onBindChildViewHolder method. If there is no data to display, simply create a placeholder object with an empty constructor.** The child object should simply hold data. That data can be set by the parent object itself or another object as long as it extends ```ChildObject```. If the object changes later, you can call or Override ```setChildObject(ChildObject childObject)```, but this value can never be null.

##Usage
 First, Create a RecyclerView in your xml view and instantiate/define it in your activity/fragment as you would usually do. All expansion of items is handled in the adapter and ParentViewHolder. To use Expandable RecyclerView, you must create your own Custom adapter that extends ```ExpandableRecyclerAdapter```. The ExpandableRecyclerAdapter takes in the context from the Activity containing the RecyclerView and a List of Objects. This List of Objects should initially contain all parent objects. Children are added dynamically when expand is triggered.

 In onCreate or onCreateView of your activity or fragment, it should look like this:
```
RecyclerView mRecyclerView = (RecyclerView) findViewById(YOUR RECYCLERVIEW ID);
MyExpandable mExpandableAdapter = new MyExpandableAdapter(getActivity(), PARENT ITEM LIST);
mRecyclerView.setAdapter(mExpandableAdapter);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
```
 Inside your ExpandableRecyclerAdapter, you can create and bind your Parent and Child ViewHolders just as you would create and bind ViewHolders in a normal RecyclerView.

 Then, define both a parent object and a child object. The child object simply needs to be a dedicated object to hold any data to be displayed in the expanded view that **must** implement ```ChildObject``` and its cooresponding methods. Similarly, the parent object **must** impement ```ParentObject```. Here is an example of how the methods in the Object that implements ```ParentObject``` should be implemented:
```
public class MyCustomParentObject implements ParentObject {

    private boolean mExpanded = false;
    private long mStableId;
    private Object mChildObject;

    /**
     * Your variables, data and methods for your Object go here
     */
     
    @Override
    public long getStableId() {
        return mStableId;
    }

    @Override
    public void setStableId(long stableId) {
        mStableId = stableId;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    @Override
    public Object getChildObject() {
        return mChildObject;
    }

    @Override
    public void setChildObject(Object childObject) {
        mChildObject = childObject;
    }
}
```
**Please note that all ParentObjects must have a reference to their cooresponding ChildObject. Without this, the expand and collapse will not work correctly.**

 You must also create two separate ViewHolders, a parent ViewHolder and a child ViewHolder, either as a class inside your custom Adapter or as a separate class altogether. The parent ViewHolder must extend ParentViewHolder, and the child ViewHolder must extend ChildViewHolder. This also means you must create two separate XML layouts for the ParentViewHolder and ChildViewHolder respectively. For ParentViewHolder, make sure to include ```super(itemView, parentItemClickListener)``` in your constructor, then modify as you would a normal viewHolder. The same should be done for ChildViewHolder, but rather ```super(itemView)```. Also, make sure to cast your passed ParentViewHolder and ChildViewHolder in ```onBindParentViewHolder``` and ```onBindChildViewHolder``` of the adapter to your custom viewHolders you created for each to allow for proper binding of data.
 
  ####Extras
  You can define a custom button, image or view to trigger the expansion rather than clicking the whole item (default). To do this, after defining your clickable trigger button or view, call ```myCustomExpandingAdapter.setCustomClickableView(Your Custom View ID)``` and pass in the id.
  
  If you do set a custom clickable view, you can also set an animation for the view to rotate 180 degrees when expanding and collapsing. This is useful primarily with arrows which signifies for the user to click it to change the expansion. You can do this by calling ```myCustomExpandingAdapter.setRotation(long duration)``` in the constructor below where you called ```setCustomClickableView()```.
  
  After implementing these, in the activity or fragment that is holding your RecyclerView, simply set the adapter to your custom adapter, and set the layout manager to a new LinearLayoutManager. An example is here:
  
  ```
  MyCustomExpandingAdapter myCustomExpandingAdapter = new MyCustomExpandingAdapter(this, objectList);
  mRecyclerView.setAdapter(myCustomExpandingAdapter);
  mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  ```
  
  ####Saving Expanded States onResune() or on Roatation
  To do this, simply call ```myCustomExpandingAdapter.setHasStableIds``` before you set the adapter to your RecyclerView. In your ParentObject, you must have set a unique id for each item. This id will now be used to save the expanded/collapsed state of the item. You must also override onSaveInstanceState and onRestoreInstanceState in the Activity or Fragment that contains the RecyclerView. Inside onSaveInstanceState(Bundle outState), you must call ```myCustomExpandingAdapter.onSaveInstanceState(outState)``` and call super in your implementation. In onRestoreInstanceState(Bundle savedInstanceState(Bundle savedInstanceState), you must call ```myCustomExpandingAdapter.onRestoreInstanceState(savedInstanceState)```. Here is an example of how to override in your activity or fragment:
  
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
Check out the sample application for a full working demo!
 
##Features Coming
  - Horizontal Expansion
  - GridLayout Expansion
  - Ability to click on child views
