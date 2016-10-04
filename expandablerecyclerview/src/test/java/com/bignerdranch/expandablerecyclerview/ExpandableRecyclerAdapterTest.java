package com.bignerdranch.expandablerecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.model.ExpandableWrapper;
import com.bignerdranch.expandablerecyclerview.model.Parent;

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
    private List<Parent<Object>> mBaseParents;
    private AdapterDataObserver mDataObserver;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        mBaseParents = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Parent<Object> parent = generateParent(i % 2 == 0, 3);
            mBaseParents.add(parent);
        }

        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParents);
        mDataObserver = TestUtils.fixAdapterForTesting(mExpandableRecyclerAdapter);
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParents() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyCollapsed() {
        for (Parent parent : mBaseParents) {
            when(parent.isInitiallyExpanded()).thenReturn(false);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParents);

        assertEquals(10, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void adapterCorrectlyInitializesExpandedParentsWhenAllParentItemsAreInitiallyExpanded() {
        for (Parent parent : mBaseParents) {
            when(parent.isInitiallyExpanded()).thenReturn(true);
        }
        mExpandableRecyclerAdapter = new TestExpandableRecyclerAdapter(mBaseParents);

        assertEquals(40, mExpandableRecyclerAdapter.getItemCount());
    }

    @Test
    public void collapsingExpandedParentWithIndexRemovesChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);
        verifyParentItemsMatch(mBaseParents.get(1), false, 4);

        mExpandableRecyclerAdapter.collapseParent(0);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), false, 0);
        verifyParentItemsMatch(mBaseParents.get(1), false, 1);
    }

    @Test
    public void collapsingExpandedParentWithObjectRemovesChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);
        verifyParentItemsMatch(mBaseParents.get(1), false, 4);

        Parent<Object> firstParent = mBaseParents.get(0);
        mExpandableRecyclerAdapter.collapseParent(firstParent);

        verify(mDataObserver).onItemRangeRemoved(1, 3);
        assertEquals(22, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), false, 0);
        verifyParentItemsMatch(mBaseParents.get(1), false, 1);
    }

    @Test
    public void collapsingCollapsedParentWithIndexHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);

        mExpandableRecyclerAdapter.collapseParent(9);

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);
    }

    @Test
    public void collapsingCollapsedParentWithObjectHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);

        mExpandableRecyclerAdapter.collapseParent(mBaseParents.get(9));

        verify(mDataObserver, never()).onItemRangeRemoved(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);
    }

    @Test
    public void expandingParentWithIndexAddsChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);

        mExpandableRecyclerAdapter.expandParent(9);

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), true, 24);
    }

    @Test
    public void expandingParentWithObjectAddsChildren() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), false, 24);

        mExpandableRecyclerAdapter.expandParent(mBaseParents.get(9));

        verify(mDataObserver).onItemRangeInserted(25, 3);
        assertEquals(28, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(9), true, 24);
    }

    @Test
    public void expandingExpandedParentWithIndexHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);

        mExpandableRecyclerAdapter.expandParent(0);

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);
    }

    @Test
    public void expandingExpandedWithObjectParentHasNoEffect() {
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);

        mExpandableRecyclerAdapter.expandParent(mBaseParents.get(0));

        verify(mDataObserver, never()).onItemRangeInserted(anyInt(), anyInt());
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(mBaseParents.get(0), true, 0);
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyCollapsedItem() {
        Parent<Object> originalFirstItem = mBaseParents.get(0);
        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalFirstItem, true, 0);

        Parent<Object> insertedItem = generateParent(false, 2);
        when(insertedItem.isInitiallyExpanded()).thenReturn(false);
        mBaseParents.add(0, insertedItem);
        mExpandableRecyclerAdapter.notifyParentInserted(0);

        verify(mDataObserver).onItemRangeInserted(0, 1);
        assertEquals(26, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(insertedItem, false, 0);
        verifyParentItemsMatch(originalFirstItem, true, 1);
    }

    @Test
    public void notifyParentItemInsertedWithInitiallyExpandedItem() {
        Parent<Object> originalLastParent = mBaseParents.get(9);
        Parent<Object> insertedItem = generateParent(true, 3);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalLastParent, false, 24);

        mBaseParents.add(insertedItem);
        mExpandableRecyclerAdapter.notifyParentInserted(10);

        verify(mDataObserver).onItemRangeInserted(25, 4);
        assertEquals(29, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalLastParent, false, 24);
        verifyParentItemsMatch(insertedItem, true, 25);
    }

    @Test
    public void notifyParentRangeInsertedMidList() {
        Parent<Object> firstInsertedItem = generateParent(true, 3);
        Parent<Object> secondInsertedItem = generateParent(false, 2);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());

        mBaseParents.add(4, firstInsertedItem);
        mBaseParents.add(5, secondInsertedItem);
        mExpandableRecyclerAdapter.notifyParentRangeInserted(4, 2);

        verify(mDataObserver).onItemRangeInserted(10, 5);
        assertEquals(30, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(firstInsertedItem, true, 10);
        verifyParentItemsMatch(secondInsertedItem, false, 14);
    }

    @Test
    public void notifyParentRangeInsertedEndList() {
        Parent<Object> firstInsertedItem = generateParent(true, 3);
        Parent<Object> secondInsertedItem = generateParent(false, 2);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());

        mBaseParents.add(firstInsertedItem);
        mBaseParents.add(secondInsertedItem);
        mExpandableRecyclerAdapter.notifyParentRangeInserted(10, 2);

        verify(mDataObserver).onItemRangeInserted(25, 5);
        assertEquals(30, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(firstInsertedItem, true, 25);
        verifyParentItemsMatch(secondInsertedItem, false, 29);
    }

    @Test
    public void notifyParentItemRemovedOnExpandedItem() {
        Parent<Object> removedItem = mBaseParents.get(0);
        Parent<Object> originalSecondItem = mBaseParents.get(1);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(removedItem, true, 0);
        verifyParentItemsMatch(originalSecondItem, false, 4);

        mBaseParents.remove(0);
        mExpandableRecyclerAdapter.notifyParentRemoved(0);

        verify(mDataObserver).onItemRangeRemoved(0, 4);
        assertEquals(21, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalSecondItem, false, 0);
    }

    @Test
    public void notifyParentItemRemovedOnCollapsedItem() {
        Parent<Object> removedItem = mBaseParents.get(9);
        Parent<Object> originalSecondToLastItem = mBaseParents.get(8);

        assertEquals(25, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(removedItem, false, 24);
        verifyParentItemsMatch(originalSecondToLastItem, true, 20);

        mBaseParents.remove(9);
        mExpandableRecyclerAdapter.notifyParentRemoved(9);

        verify(mDataObserver).onItemRangeRemoved(24, 1);
        assertEquals(24, mExpandableRecyclerAdapter.getItemCount());
        verifyParentItemsMatch(originalSecondToLastItem, true, 20);
    }

    private void verifyParentItemsMatch(Parent<Object> expectedParent, boolean expectedExpansion, int actualParentIndex) {
        assertEquals(expectedParent, getListItem(actualParentIndex));
        assertEquals(expectedExpansion, mExpandableRecyclerAdapter.mFlatItemList.get(actualParentIndex).isExpanded());

        if (expectedExpansion) {
            List<Object> expectedChildList = expectedParent.getChildList();
            int actualChildIndex = actualParentIndex + 1;
            for (int i = 0; i < expectedChildList.size(); i++) {
                assertEquals(expectedChildList.get(i), getListItem(actualChildIndex));
                actualChildIndex++;
            }
        }
    }

    private Parent<Object> generateParent(boolean initiallyExpanded, int childCount) {
        List<Object> childObjects = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            childObjects.add(new Object());
        }
        Parent<Object> parent = (Parent<Object>) mock(Parent.class);
        Mockito.when(parent.getChildList()).thenReturn(childObjects);
        when(parent.isInitiallyExpanded()).thenReturn(initiallyExpanded);

        return parent;
    }

    protected Object getListItem(int flatPosition) {
        ExpandableWrapper<Parent<Object>, Object> listItem = mExpandableRecyclerAdapter.mFlatItemList.get(flatPosition);
        if (listItem.isParent()) {
            return listItem.getParent();
        } else {
            return listItem.getChild();
        }
    }

    private static class TestExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<Parent<Object>, Object, ParentViewHolder, ChildViewHolder> {

        public TestExpandableRecyclerAdapter(@NonNull List<Parent<Object>> parentList) {
            super(parentList);
        }

        @NonNull
        @Override
        public ParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            return null;
        }

        @Override
        public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            return null;
        }

        @Override
        public void onBindParentViewHolder(@NonNull ParentViewHolder parentViewHolder, int parentPosition, @NonNull Parent<Object> parent) {

        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Object child) {

        }
    }
}
