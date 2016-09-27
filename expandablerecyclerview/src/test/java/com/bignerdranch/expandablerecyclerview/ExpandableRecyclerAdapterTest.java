package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.model.ParentWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpandableRecyclerAdapterTest {

    private TestExpandableRecyclerAdapter mExpandableRecyclerAdapter;
    private List<ParentListItem> mBaseParentItems;
    private AdapterDataObserver mDataObserver;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        mBaseParentItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<Object> childObjects = new ArrayList<>();
            childObjects.add(new Object());
            childObjects.add(new Object());
            childObjects.add(new Object());

            ParentListItem parentListItem = mock(ParentListItem.class);
            Mockito.<List<?>>when(parentListItem.getChildItemList()).thenReturn(childObjects);
            when(parentListItem.isInitiallyExpanded()).thenReturn(i % 2 == 0); // Every other parent will return true

            mBaseParentItems.add(parentListItem);
        }

        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParentItems);
        mDataObserver = TestUtils.fixAdapterForTesting(mExpandableRecyclerAdapter);
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParents() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyCollapsed() {
        for (ParentListItem parentListItem : mBaseParentItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(false);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParentItems);

        assertEquals(10, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyExpanded() {
        for (ParentListItem parentListItem : mBaseParentItems) {
            when(parentListItem.isInitiallyExpanded()).thenReturn(true);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParentItems);

        assertEquals(40, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void collapsingExpandedParentWithIndexRemovesChildren() {
        ParentListItem firstParentListItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertEquals(firstParentListItem.getChildItemList().get(1), mExpandableRecyclerAdapter.getListItem(2));
        assertEquals(firstParentListItem.getChildItemList().get(2), mExpandableRecyclerAdapter.getListItem(3));
        assertTrue(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.collapseParent(0);
        ParentWrapper firstParentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);
        ParentWrapper secondParentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(1);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(0), firstParentWrapper.getParentListItem());
        assertEquals(mBaseParentItems.get(1), secondParentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());
    }

    @Test
    public void collapsingExpandedParentWithObjectRemovesChildren() {
        ParentListItem firstParentListItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertEquals(firstParentListItem.getChildItemList().get(1), mExpandableRecyclerAdapter.getListItem(2));
        assertEquals(firstParentListItem.getChildItemList().get(2), mExpandableRecyclerAdapter.getListItem(3));
        assertTrue(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.collapseParent(firstParentListItem);
        ParentWrapper firstParentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);
        ParentWrapper secondParentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(1);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(0), firstParentWrapper.getParentListItem());
        assertEquals(mBaseParentItems.get(1), secondParentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());
    }

    @Test
    public void collapsingCollapsedParentWithIndexHasNoEffect() {
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.collapseParent(9);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());
    }

    @Test
    public void collapsingCollapsedParentWithObjectHasNoEffect() {
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.collapseParent(mBaseParentItems.get(9));
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());
    }

    @Test
    public void expandingParentWithIndexAddsChildren() {
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.expandParent(9);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);
        ParentListItem lastParentListItem = mBaseParentItems.get(9);

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(lastParentListItem, parentWrapper.getParentListItem());
        assertEquals(lastParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(25));
        assertEquals(lastParentListItem.getChildItemList().get(1), mExpandableRecyclerAdapter.getListItem(26));
        assertEquals(lastParentListItem.getChildItemList().get(2), mExpandableRecyclerAdapter.getListItem(27));
        assertTrue(parentWrapper.isExpanded());
    }

    @Test
    public void expandingParentWithObjectAddsChildren() {
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(9), parentWrapper.getParentListItem());
        assertFalse(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.expandParent(mBaseParentItems.get(9));
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);
        ParentListItem lastParentListItem = mBaseParentItems.get(9);

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(lastParentListItem, parentWrapper.getParentListItem());
        assertEquals(lastParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(25));
        assertEquals(lastParentListItem.getChildItemList().get(1), mExpandableRecyclerAdapter.getListItem(26));
        assertEquals(lastParentListItem.getChildItemList().get(2), mExpandableRecyclerAdapter.getListItem(27));
        assertTrue(parentWrapper.isExpanded());
    }

    @Test
    public void expandingExpandedWithIndexParentHasNoEffect() {
        ParentListItem firstParentListItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertTrue(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.expandParent(0);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertTrue(parentWrapper.isExpanded());
    }

    @Test
    public void expandingExpandedWithObjectParentHasNoEffect() {
        ParentListItem firstParentListItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertTrue(parentWrapper.isExpanded());

        mExpandableRecyclerAdapter.expandParent(firstParentListItem);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());
        assertEquals(firstParentListItem.getChildItemList().get(0), mExpandableRecyclerAdapter.getListItem(1));
        assertTrue(parentWrapper.isExpanded());
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyCollapsedItem() {
        ParentListItem firstParentListItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(firstParentListItem, parentWrapper.getParentListItem());

        ParentListItem insertedItem = mock(ParentListItem.class);
        when(insertedItem.isInitiallyExpanded()).thenReturn(false);
        mBaseParentItems.add(0, insertedItem);
        mExpandableRecyclerAdapter.notifyParentItemInserted(0);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        verify(mDataObserver).onItemRangeInserted(0, 1);
        assertEquals(26, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(insertedItem, parentWrapper.getParentListItem());
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyExpandedItem() {
        List<Object> childObjects = new ArrayList<>();
        childObjects.add(new Object());
        childObjects.add(new Object());
        childObjects.add(new Object());
        ParentListItem lastParentListItem = mBaseParentItems.get(9);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(lastParentListItem, parentWrapper.getParentListItem());

        ParentListItem insertedItem = mock(ParentListItem.class);
        Mockito.<List<?>>when(insertedItem.getChildItemList()).thenReturn(childObjects);
        when(insertedItem.isInitiallyExpanded()).thenReturn(true);
        mBaseParentItems.add(insertedItem);
        mExpandableRecyclerAdapter.notifyParentItemInserted(10);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(25);

        verify(mDataObserver).onItemRangeInserted(25, 4);
        assertEquals(29, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(insertedItem, parentWrapper.getParentListItem());
        assertEquals(childObjects.get(0), mExpandableRecyclerAdapter.getListItem(26));
        assertEquals(childObjects.get(1), mExpandableRecyclerAdapter.getListItem(27));
        assertEquals(childObjects.get(2), mExpandableRecyclerAdapter.getListItem(28));
    }

    @Test
    public void notifyParentItemRemovedOnExpandedItem() {
        ParentListItem removedItem = mBaseParentItems.get(0);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(removedItem, parentWrapper.getParentListItem());

        mBaseParentItems.remove(0);
        mExpandableRecyclerAdapter.notifyParentItemRemoved(0);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(0);

        verify(mDataObserver).onItemRangeRemoved(0, 4);
        assertEquals(21, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(0), parentWrapper.getParentListItem());
    }

    @Test
    public void notifyParentItemRemovedOnCollapsedItem() {
        ParentListItem removedItem = mBaseParentItems.get(9);
        ParentWrapper parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(24);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(removedItem, parentWrapper.getParentListItem());

        mBaseParentItems.remove(9);
        mExpandableRecyclerAdapter.notifyParentItemRemoved(9);
        parentWrapper = (ParentWrapper) mExpandableRecyclerAdapter.getListItem(20);

        verify(mDataObserver).onItemRangeRemoved(24, 1);
        assertEquals(24, mExpandableRecyclerAdapter.getItemCount());
        assertEquals(mBaseParentItems.get(8), parentWrapper.getParentListItem());
    }


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
}
