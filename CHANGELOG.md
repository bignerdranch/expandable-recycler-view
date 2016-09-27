Version 3.0.0 SNAPSHOT
----------------------------
- Added generic types for the Parent and Child model objects inside of ExpandableRecyclerAdapter
    - ParentListItem interface now takes a generic object type, which allows for `getChildItemList` to return a typed List instead of `List<?>`
    - The `ExpandableRecyclerAdapter` takes two more generic object types
        - A child model object of any type
        - A parent model object that must extend `ParentListItem` which includes the previous child type
    - As a result `onBindParentViewHolder` and `onBindChildViewHolder` now give the specific object type specified in the class
    - Cleaned up a lot of internal logic within `ExpandableRecyclerAdapter` to avoid casting to Object
- Changed the name and visibility of `onParentListItemExpanded` and `onParentListItemCollapsed`
    - Now protected and named `parentListItemExpandedFromViewHolder` and `parentListItemCollapsedFromViewHolder`
    - Not meant to be called from outside the adapter and viewholder, can be overriden to give some custom behaviour for collapsing/expanding
- Added support for multiple view types within recyclerview
    - `getParentItemViewType()`, `getChildItemViewType()`, and `isParentViewType()` added with default implementation for single view type
    - Modified signatures of `onCreateParentViewHolder()` and `onCreateChildViewHolder()` to pass view type being created
- `ParentViewHolder` package location modified from `com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter` to `com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter`
- `ChildViewHolder` package location modified from `com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter` to `com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter`
- Added ParentViewHolder methods:
    - `getParentListItem()`
    - `getParentAdapterPosition()`
- Added ChildViewHolder methods:
    - `getChildListItem()`
    - `getParentAdapterPosition()`
    - `getChildAdapterPosition()`

Version 2.1.1 (2/26/16)
----------------------------
- Updated Support Library dependencies

Version 2.1.0 (1/8/16)
----------------------------
- Improved expand/collapse performance
    - Reduced frequency of notify calls in expand/collapse methods
- Added ability to expand and collapse list items by range
    - Added `ExpandableRecyclerAdapter.expandParentRange` so that a subset of parents in a list can be expanded at once with one call
    - Added `ExpandableRecyclerAdapter.collapseParentRange` so that a subset of parents in a list can be collapsed at once with one call
- Added additional `notifyDatasetChanged` methods
    - Added `ExpandableRecyclerAdapter.notifyParentItemRangeRemoved` to notify the RecyclerView when a subset of parents is removed
    - Added `ExpandableRecyclerAdapter.notifyParentItemRangeChanged` to notify the RecyclerView when a subset of parents is changed
    - Added `ExpandableRecyclerAdapter.notifyChildItemRangeInserted` to notify the RecyclerView when a subset of children is inserted
    - Added `ExpandableRecyclerAdapter.notifyChildItemRangeRemoved` to notify the RecyclerView when a subset of children is removed
    - Added `ExpandableRecyclerAdapter.notifyChildItemRangeChanged` to notify the RecyclerView when a subset of children is changed
    - Added `ExpandableRecyclerAdapter.notifyParentItemMoved` to notify the RecyclerView when a parent's adapter position is changed
    - Added `ExpandableRecyclerAdapter.notifyChildItemMoved` to notify the RecyclerView when a child's adapter position is changed
- Replaced content in vertical linear sample with a more realistic, food-themed example

Version 2.0.4 (11/20/15)
----------------------------
- Fix crash when restoring state when an initially expanded row has been collapsed

Version 2.0.3 (10/14/15)
----------------------------
- Change `onListItemExpanded` and `onListItemCollapsed` to be called after list is updated, fixes bug in issue #112

Version 2.0.2 (10/13/15)
----------------------------
- Fix `notifyChildItemChanged` and `notifyParentItemChanged`, previously child objects were not being replaced

Version 2.0.1 (10/05/15)
----------------------------
- Fix crash occurring when using notifyParentItemRangeInserted to add a group of items at the end of the list

Version 2.0.0 (09/24/15)
----------------------------
For a migration guide from 1.0.3 to 2.0.0, see [here](MIGRATION.md).

- Significant renaming/reorganizing of library
    - `ParentObject` is now named `ParentListItem`.
    - `ExpandableRecyclerAdapter#mHelperItemList` now has `protected` visibility and is named `mItemList`. This `List` can be used to gain access to all expanded list items.
    - `ParentItemClickListener` is now `ParentViewHolder.ParentListItemExpandCollapseListener`.
- Fleshed out Javadocs.
- Added ability to specify if a parent should be expanded initially.
- Added ability to expand and collapse list items programmatically (without relying on a click event).
- Added `ExpandableRecyclerAdapter.ExpandCollapseListener`.
- Added horizontal linear sample project, demoing many of the new features in 2.0.0.
- Added ability to notify the adapter of changes to the parent list items and child items. Methods begin with `notifyParentItem` or `notifyChildItem`
    - Note: RecyclerView.Adapter's notify methods will not work correctly with this adapter.
- Reorganized sample project.
- Removed `setChildObjectList` from `ParentListItem` interface.
- Removed inaccurate "stable" id map.
- Removed application tag from manifest.
- Removed animation and click handling details from the adapter, `ParentViewHolder` now has methods used for implementing that behavior in your own subclasses (examples given in the samples with this library).
- README updates
    - Added usage instructions for new features.
    - Updated references to the library where naming has changed.
- Bug fixes: [#42](https://github.com/bignerdranch/expandable-recycler-view/pull/42), [#46](https://github.com/bignerdranch/expandable-recycler-view/pull/46), [#47](https://github.com/bignerdranch/expandable-recycler-view/pull/47)


Version 1.0.3 (06/19/15)
----------------------------
- Initial Version
