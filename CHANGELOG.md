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
