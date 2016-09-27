package com.bignerdranch.expandablerecyclerview.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpandableWrapperTest {

    private ExpandableWrapper<ParentListItem<Object>, Object> mExpandableWrapper;
    private ParentListItem<Object> mParentListItem;

    @Before
    public void setup() {
        mParentListItem = (ParentListItem<Object>) mock(ParentListItem.class);
        mExpandableWrapper = new ExpandableWrapper<>(mParentListItem);
    }

    @Test
    public void expandableWrapperIsInitiallyExpandedWhenParentListItemIsInitiallyExpanded() {
        boolean expected = true;

        when(mParentListItem.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mExpandableWrapper.isParentInitiallyExpanded());
    }

    @Test
    public void expandableWrapperIsNotInitiallyExpandedWhenParentListItemIsNotInitiallyExpanded() {
        boolean expected = false;

        when(mParentListItem.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mExpandableWrapper.isParentInitiallyExpanded());
    }

    @Test
    public void getChildItemListReturnsWrappedChildItemListFromParentListItem() {
        final List<Object> expected = new ArrayList<>();
        expected.add(new Object());
        expected.add(new Object());
        expected.add(new Object());

        when(mParentListItem.getChildItemList()).thenReturn(expected);
        mExpandableWrapper = new ExpandableWrapper<>(mParentListItem);

        List<ExpandableWrapper<ParentListItem<Object>, Object>> wrappedChildList = mExpandableWrapper.getWrappedChildItemList();
        assertEquals(expected.size(), wrappedChildList.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), wrappedChildList.get(i).getChildListItem());
        }
    }

    @Test
    public void getChildItemListReturnsEmptyListFromParentListItem() {
        boolean expected = true;

        final List<Object> childItemList = new ArrayList<>();
        when(mParentListItem.getChildItemList()).thenReturn(childItemList);
        mExpandableWrapper = new ExpandableWrapper<>(mParentListItem);

        assertEquals(expected, mExpandableWrapper.getWrappedChildItemList().isEmpty());
    }
}
