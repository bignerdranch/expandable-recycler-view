# Migration Guide
This guide is intended to make it easier for you migrate from one version of **Expandable RecyclerView** to the next.
We'll keep this guide up-to-date with API changes.

*Note* that this guide doesn't go into new feature additions, but is a resource to help for when we make changes to old code.

## 1.0.3 to 2.0.0

##### `ParentObject` is now `ParentListItem`
Your custom parent list item should now extend `ParentListItem`.
You'll have to replace your references to `ParentObject` across your implementation.

`getChildObjectList()` is now called `getChildItemList()`. 
You'll need to override this method in your custom parent list item.

`setChildObjectList(List<Object>)` has been removed.
You should remove your attempt to override this method from your custom parent list item.

##### Built-in rotation animation has been removed from the app
This touches a lot.
We removed the built-in rotation animation from the app for a number of reasons, but rest assured that we've provided hooks and examples for that sort of thing to be triggered in your implementation.

`ExpandableRecyclerAdapter` now just has one constructor, so if you're subclassing `ExpandableRecyclerAdapter`, you'll want to simplify your `super` call to just hit the default constructor.

##### `Context` has been removed from `ExpandableRecyclerAdapter`'s constructor
We weren't using it internally, so you no longer have to provide it to us.
You should also remove it from your `super` calls if you subclass `ExpandableRecylerAdapter`.

##### `ExpandableRecyclerAdapter#onBindParentViewHolder` now provides a `ParentListItem` as a parameter
We still provide you the parent `ViewHolder` being bound and its adapter position. 
However, rather than giving you an `Object` with your data, we now specifically give you a `ParentListItem`.
You'll need to change your method header accordingly in your `ExpandableRecyclerAdapter` subclass

##### `ExpandCollapseListener` now lives in `ExpandableRecyclerAdapter`
It's still publicly available, but we moved the `ExpandCollapseListener` to where it was used.
If you want to listen for expand/collapse events in your `Activity`/`Fragment`, you should now implement `ExpandableRecyclerAdpater.ExpandCollapseListener` instead.

`ExpandCollapseListener#onRecyclerViewItemExpanded(int)` is now called `ExpandCollapseListener#onListItemExpanded(int)`.
`ExpandCollapseListener#onRecyclerViewItemCollapsed(int)` is now called `ExpandCollapseListener#onListItemCollapsed(int)`.
These are just name changes to match the fact that we use the term "list" elsewhere in the codebase.

##### `ExpandableRecyclerAdapter#onSaveInstanceState(Bundle)` now returns `void`
We were just returning you the same `Bundle` you passed in.
Because you already had a reference to the `Bundle`, this was extraneous. 
If you used the returned `Bundle` from `ExpandableRecyclerAdapter#onSaveInstanceState(Bundle)`, you can just use the same `Bundle` you passed in.

##### `ExpandableRecyclerAdapter#addExpandCollapseListener(ExpandCollapseListener)` is now `ExpandableRecyclerAdapter#setExpandCollapseListener(ExpandCollapseListener)`
"set" is a more appropriate term than "add", because the adapter was just keeping one `ExpandCollapseListener`.
This is just a method rename.