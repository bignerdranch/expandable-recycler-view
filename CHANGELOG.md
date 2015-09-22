Version 2.0.0
----------------------------
- Significant renaming/reorganizing of library
    - `ParentObject` is now named `ParentListItem`.
    - `ExpandableRecyclerAdapter#mHelperItemList` now has `protected` visibility and is named `mItemList`. This `List` can be used to gain access to all expanded list items.
    - `ParentItemClickListener` is now `ParentViewHolder.ParentListItemExpandCollapseListener`.
- Fleshed out Javadocs.
- Added ability to specify if a parent should be expanded initially.
- Added ability to expand and collapse list items programmatically (without relying on a click event).
- Added `ExpandableRecyclerAdapter.ExpandCollapseListener`.
- Added horizontal linear sample project, demoing many of the new features in 2.0.0.
- Added ability to add and remove parent list items to the adapter.
    - Note that adding/removing these list items does not affect the list passed into the adapter.
- Reorganized sample project.
- Removed `setChildObjectList` from `ParentListItem` interface.
- Removed inaccurate "stable" id map.
- Removed application tag from manifest.
- Removed animation and click handling details from the adapter, `ParentViewHolder` now has methods used for implementing that behavior in your own subclasses (examples given in the samples with this library).
- README updates
    - Added usage instructions for new features.
    - Updated references to the library where naming has changed.
- Bug fixes: [#42](https://github.com/bignerdranch/expandable-recycler-view/pull/42), [#46](https://github.com/bignerdranch/expandable-recycler-view/pull/46), [#47](https://github.com/bignerdranch/expandable-recycler-view/pull/47)


Version 1.0.3 *06/19/15*
----------------------------
- Initial Version
