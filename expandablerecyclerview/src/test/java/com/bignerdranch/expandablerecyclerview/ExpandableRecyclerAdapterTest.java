package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ExpandableWrapper;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpandableRecyclerAdapterTest {

    private TestExpandableRecyclerAdapter mExpandableRecyclerAdapter;
    private List<ParentListItem<Object>> mBaseParentItems;
    private AdapterDataObserver mDataObserver;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        mBaseParentItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<Object> childObjects = new ArrayList<>();
            childObjects.add(new Object());
            childObjects.add(new Object());
            childObjects.add(new Object());

            ParentListItem<Object> parentListItem = (ParentListItem<Object>) mock(ParentListItem.class);
            Mockito.when(parentListItem.getChildItemList()).thenReturn(childObjects);
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
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);
        verifyParentItemsMatch(mBaseParentItems.get(1), false, 4);

        mExpandableRecyclerAdapter.collapseParent(0);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), false, 0);
        verifyParentItemsMatch(mBaseParentItems.get(1), false, 1);
    }

    @Test
    public void collapsingExpandedParentWithObjectRemovesChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);
        verifyParentItemsMatch(mBaseParentItems.get(1), false, 4);

        ParentListItem<Object> firstParentListItem = mBaseParentItems.get(0);
        mExpandableRecyclerAdapter.collapseParent(firstParentListItem);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), false, 0);
        verifyParentItemsMatch(mBaseParentItems.get(1), false, 1);
    }

    @Test
    public void collapsingCollapsedParentWithIndexHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);

        mExpandableRecyclerAdapter.collapseParent(9);

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);
    }

    @Test
    public void collapsingCollapsedParentWithObjectHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);

        mExpandableRecyclerAdapter.collapseParent(mBaseParentItems.get(9));

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);
    }

    @Test
    public void expandingParentWithIndexAddsChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);

        mExpandableRecyclerAdapter.expandParent(9);

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), true, 24);
    }

    @Test
    public void expandingParentWithObjectAddsChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), false, 24);

        mExpandableRecyclerAdapter.expandParent(mBaseParentItems.get(9));

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(9), true, 24);
    }

    @Test
    public void expandingExpandedParentWithIndexHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);

        mExpandableRecyclerAdapter.expandParent(0);

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);
    }

    @Test
    public void expandingExpandedWithObjectParentHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);

        mExpandableRecyclerAdapter.expandParent(mBaseParentItems.get(0));

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParentItems.get(0), true, 0);
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyCollapsedItem() {
        ParentListItem<Object> originalFirstItem = mBaseParentItems.get(0);
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalFirstItem, true, 0);

        ParentListItem<Object> insertedItem = (ParentListItem<Object>) mock(ParentListItem.class);
        when(insertedItem.isInitiallyExpanded()).thenReturn(false);
        mBaseParentItems.add(0, insertedItem);
        mExpandableRecyclerAdapter.notifyParentItemInserted(0);

        verify(mDataObserver).onItemRangeInserted(0, 1);
        assertEquals(26, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(insertedItem, false, 0);
        verifyParentItemsMatch(originalFirstItem, true, 1);
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyExpandedItem() {
        ParentListItem<Object> originalLastParentListItem = mBaseParentItems.get(9);
        List<Object> childObjects = new ArrayList<>();
        childObjects.add(new Object());
        childObjects.add(new Object());
        childObjects.add(new Object());
        ParentListItem<Object> insertedItem = (ParentListItem<Object>) mock(ParentListItem.class);
        Mockito.when(insertedItem.getChildItemList()).thenReturn(childObjects);
        when(insertedItem.isInitiallyExpanded()).thenReturn(true);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalLastParentListItem, false, 24);

        mBaseParentItems.add(insertedItem);
        mExpandableRecyclerAdapter.notifyParentItemInserted(10);

        verify(mDataObserver).onItemRangeInserted(25, 4);
        assertEquals(29, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalLastParentListItem, false, 24);
        verifyParentItemsMatch(insertedItem, true, 25);
    }

    @Test
    public void notifyParentItemRemovedOnExpandedItem() {
        ParentListItem<Object> removedItem = mBaseParentItems.get(0);
        ParentListItem<Object> originalSecondItem = mBaseParentItems.get(1);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(removedItem, true, 0);
        verifyParentItemsMatch(originalSecondItem, false, 4);

        mBaseParentItems.remove(0);
        mExpandableRecyclerAdapter.notifyParentItemRemoved(0);

        verify(mDataObserver).onItemRangeRemoved(0, 4);
        assertEquals(21, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalSecondItem, false, 0);
    }

    @Test
    public void notifyParentItemRemovedOnCollapsedItem() {
        ParentListItem<Object> removedItem = mBaseParentItems.get(9);
        ParentListItem<Object> originalSecondToLastItem = mBaseParentItems.get(8);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(removedItem, false, 24);
        verifyParentItemsMatch(originalSecondToLastItem, true, 20);

        mBaseParentItems.remove(9);
        mExpandableRecyclerAdapter.notifyParentItemRemoved(9);

        verify(mDataObserver).onItemRangeRemoved(24, 1);
        assertEquals(24, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalSecondToLastItem, true, 20);
    }

    private void verifyParentItemsMatch(ParentListItem<Object> expectedParentListItem, boolean expectedExpansion, int actualParentIndex) {
        assertEquals(expectedParentListItem, getListItem(actualParentIndex));
        assertEquals(expectedExpansion, mExpandableRecyclerAdapter.mItemList.get(actualParentIndex).isExpanded());

        if (expectedExpansion) {
            List<Object> expectedChildList = expectedParentListItem.getChildItemList();
            int actualChildIndex = actualParentIndex + 1;
            for (int i = 0; i < expectedChildList.size(); i++) {
                assertEquals(expectedChildList.get(i), getListItem(actualChildIndex));
                actualChildIndex++;
            }
        }
    }

    protected Object getListItem(int flatPosition) {
        ExpandableWrapper<ParentListItem<Object>, Object> listItem = mExpandableRecyclerAdapter.mItemList.get(flatPosition);
        if (listItem.isParent()) {
            return listItem.getParentListItem();
        } else {
            return listItem.getChildListItem();
        }
    }

    private static class TestExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<ParentListItem<Object>, Object, ParentViewHolder, ChildViewHolder> {

        public TestExpandableRecyclerAdapter(@NonNull List<ParentListItem<Object>> parentItemList) {
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
        public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition, ParentListItem<Object> parentListItem) {

        }

        @Override
        public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition, int childPosition, Object childListItem) {

        }
    }
}
