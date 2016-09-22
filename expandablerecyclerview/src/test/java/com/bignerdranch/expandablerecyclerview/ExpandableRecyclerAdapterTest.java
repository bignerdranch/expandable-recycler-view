package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.model.ParentWrapper;

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
    public void setup() throws NoSuchFieldException, IllegalAccessException {
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

        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);
        TestUtils.fixAdapterForTesting(mExpandableRecyclerAdapter);
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParents() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyCollapsed() {
        for (ParentListItem parentListItem : mParentListItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(false);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);

        assertEquals(10, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyExpanded() {
        for (ParentListItem parentListItem : mParentListItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(true);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mParentListItems);

        assertEquals(40, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void collapsingExpandedParentRemovesChildren() {
        ParentListItem firstParentListItem = mParentListItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertEquals(firstParentListItem.getChildItemList().get(1), mExpandableRecyclerAdapter.getListItem(2));
        assertEquals(firstParentListItem.getChildItemList().get(2), mExpandableRecyclerAdapter.getListItem(3));

        mExpandableRecyclerAdapter.collapseParent(0);

        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);
        assertEquals(mParentListItems.get(0), parentWrapper.getParentListItem());
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(1);
        assertEquals(mParentListItems.get(1), parentWrapper.getParentListItem());
    }


    @Test
    public void collapsingCollapsedParentHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(4);
        assertEquals(mParentListItems.get(1), parentWrapper.getParentListItem());
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(5);
        assertEquals(mParentListItems.get(2), parentWrapper.getParentListItem());

        mExpandableRecyclerAdapter.collapseParent(1);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(4);
        assertEquals(mParentListItems.get(1), parentWrapper.getParentListItem());
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(5);
        assertEquals(mParentListItems.get(2), parentWrapper.getParentListItem());
    }
}
