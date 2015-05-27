# Expandable RecyclerView

The Expandable RecyclerView is a library written to allow for an expanded view to be attached to each ViewHolder. To allow for full functionality in the expanded view as well as in the original view, the recyclerview has been modified to use two types of ViewHolders, a child and a parent with the ability to customize each separately.

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
Expandable RecyclerView can be used with any default RecyclerView. **As of now it cannot work with other variants or libraries of RecyclerView**. Create a RecyclerView in your xml view and instantiate/define it in your activity/fragment as you would usually do.

The main difference between a regular RecyclerView and the Expandable RecyclerView is you will now use two different ViewHolders, a parent ViewHolder (your main, non-expanding view) and a child ViewHolder (the view that will drop down from the parent when the parent or defined view item is clicked). Inside each ViewHolder, you will customize and set the ViewHolders' views as you usually would respectively in ```onBindParentViewHolder``` and ```onBindChildViewHolder```. 

The objects you wish to display in your RecyclerView will need to extend ```ExpandableItem<ChildType>```. Inside ```ExpandableItem<>```, you must set a specific Child Object for each Parent. You will be forced to do this in the constructor. If the object changes later, you can call ```setChildObject(T childObject)```, but this value can never be null. The class inside the diamonds will define the class of the Child.

##Usage
First, define both a parent object and a child object. The child object simply needs to be a dedicated object to hold any data to be displayed in the expanded view. The parent object **MUST** extend ```ExpandableItem<ChildType>```. Inside the generic, include the class name of the child. Simply implement the generic constructor and you can add as modify the object just as you would any other object.

All expansion of items is handled in the adapter. To use Expandable RecyclerView, you must create your own Custom adapter that extends ```ExpandableRecyclerAdapter```. The ExpandableRecyclerAdapter takes in the context from the Activity containing the RecyclerView and a List of parent items (that extends ExpandableItem) that the RecyclerView will display. Here is an example expanding adapter:

```
public class MyCustomExpandingAdapter extends ExpandableRecyclerAdapter {

  public MyExpandableAdapter(Context context, List<? extends ExpandableItem> itemList) {
    super(context, itemList)
    // Any more objects can be set here
  }
  
  @Override
  public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int viewType) {
    // Inflate your parent layout here and return a ViewHolder that extends ParentViewHolder
  }
  
  @Override
  public ChildViewHolder onCreateChildViewHolder(ViewGroup parent) {
    // Inflate your child's layout here and return a ViewHolder that extends ChildViewHolder
  }
  
  @Override
  public void onBindParentViewHolder(ParentViewHolder holder, int position, int originalPosition) {
    // Use originalPosition for getting Parent data from list.
    // position is its literal position in the RecyclerView. originalPosition is its position in list
    // Use originalPosition rather than position unless you need actual position
    
    // Bind all Parent data to views here. 
    // Parent object at position can be retrieved with itemList.get(originalPosition)
  }
  
  @Override
  public void onBindChildViewHolder(ChildViewHolder holder, int position, int originalPosition) {
    // Use originalPosition and then get the child object from the parent object for getting Child data from list.       // position is its literal position in the RecyclerView. originalPosition is its position in list
    // Use originalPosition rather than position unless you need actual position
    
    // Bind all Child data to views here. 
    // Child object at position can be retrieved with itemList.get(originalPosition).getChildObject()
  }
}
```

 You must also create two separate ViewHolders, a parent ViewHolder and a child ViewHolder, either as a class inside the Adapter or as a separate class altogether. The parent ViewHolder must extend ParentViewHolder, and the child ViewHolder must extend ChildViewHolder. This also means you must create two separate XML layouts for the ParentViewHolder and ChildViewHolder respectively. For ParentViewHolder, make sure to imclude ```super(itemView, parentItemClickListener)``` in your constructor, then modify as you would a normal viewHolder. The same should be done for ChildViewHolder. Also, make sure to cast your passed ParentViewHolder and ChildViewHolder in ```onBindParentViewHolder``` and ```onBindChildViewHolder``` of the adapter to your custom viewHolders you created for each to allow for proper binding of data.
 
 After implementing these, in the activity or fragment that is holding your RecyclerView, simply set the adapter to your custom adapter, and set the layout manager to a new LinearLayoutManager. An example is here:
 
 ```
 MyCustomExpandingAdapter myCustomExpandingAdapter = new MyCustomExpandingAdapter(this, objectList);
 mRecyclerView.setAdapter(myCustomExpandingAdapter);
 mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
 ```
You should now be able to run the application and click on an item to expand it.
 
##Features Coming
  - Horizontal Expansion
  - GridLayout Expansion
  - Define a view to click that expands the layout
  - Ability to click on child views
