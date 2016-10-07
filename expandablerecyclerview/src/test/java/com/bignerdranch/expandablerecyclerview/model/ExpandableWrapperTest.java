package com.bignerdranch.expandablerecyclerview.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpandableWrapperTest {

    private ExpandableWrapper<Parent<Object>, Object> mExpandableWrapper;
    private Parent<Object> mParent;

    @Before
    public void setup() {
        mParent = (Parent<Object>) mock(Parent.class);
        mExpandableWrapper = new ExpandableWrapper<>(mParent);
    }

    @Test
    public void expandableWrapperIsInitiallyExpandedWhenParentListItemIsInitiallyExpanded() {
        boolean expected = true;

        when(mParent.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mExpandableWrapper.isParentInitiallyExpanded());
    }

    @Test
    public void expandableWrapperIsNotInitiallyExpandedWhenParentListItemIsNotInitiallyExpanded() {
        boolean expected = false;

        when(mParent.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mExpandableWrapper.isParentInitiallyExpanded());
    }

    @Test
    public void getChildItemListReturnsWrappedChildItemListFromParentListItem() {
        final List<Object> expected = new ArrayList<>();
        expected.add(new Object());
        expected.add(new Object());
        expected.add(new Object());

        when(mParent.getChildList()).thenReturn(expected);
        mExpandableWrapper = new ExpandableWrapper<>(mParent);

        List<ExpandableWrapper<Parent<Object>, Object>> wrappedChildList = mExpandableWrapper.getWrappedChildList();
        assertEquals(expected.size(), wrappedChildList.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), wrappedChildList.get(i).getChild());
        }
    }

    @Test
    public void getChildItemListReturnsEmptyListFromParentListItem() {
        boolean expected = true;

        final List<Object> childItemList = new ArrayList<>();
        when(mParent.getChildList()).thenReturn(childItemList);
        mExpandableWrapper = new ExpandableWrapper<>(mParent);

        assertEquals(expected, mExpandableWrapper.getWrappedChildList().isEmpty());
    }
}
