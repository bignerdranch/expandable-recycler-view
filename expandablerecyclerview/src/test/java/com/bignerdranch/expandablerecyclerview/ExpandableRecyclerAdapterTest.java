package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpandableRecyclerAdapterTest {

    private static class TestExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<ParentViewHolder, ChildViewHolder> {

        public TestExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
            super(parentItemList);
        }

        @Override
        public ParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType) {
            return null;
        }

        @Override
        public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
            return null;
        }

        @Override
        public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition, ParentListItem parentListItem) {

        }

        @Override
        public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition, int childPosition, Object childListItem) {

        }
    }

    private TestExpandableRecyclerAdapter mExpandableRecyclerAdapter;
    private List<ParentListItem> mParentListItems;

    @Before
    public void setup() {
        mParentListItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<Object> childObjects = new ArrayList<>();
            childObjects.add(new Object());
            childObjects.add(new Object());
            childObjects.add(new Object());

            ParentListItem parentListItem = mock(ParentListItem.class);
            Mockito.<List<?>>when(parentListItem.getChildItemList()).thenReturn(childObjects);
            when(parentListItem.isInitiallyExpanded()).thenReturn(i % 2 == 0); // Every other parent will return true

            mParentListItems.add(parentListItem);
        }
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParents() {
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);

        assertEquals(mExpandableRecyclerAdapter.getItemCount(), 25);
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyCollapsed() {
        for (ParentListItem parentListItem : mParentListItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(false);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);

        assertEquals(mExpandableRecyclerAdapter.getItemCount(), 10);
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyExpanded() {
        for (ParentListItem parentListItem : mParentListItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(true);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);

        assertEquals(mExpandableRecyclerAdapter.getItemCount(), 40);
    }
}
