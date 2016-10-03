package com.bignerdranch.expandablerecyclerview.model;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParentWrapperTest {

    private ParentWrapper mParentWrapper;
    private ParentListItem mParentListItem;

    @Before
    public void setup() {
        mParentListItem = mock(ParentListItem.class);
        mParentWrapper = new ParentWrapper(mParentListItem);
    }

    @Test
    public void parentWrapperIsInitiallyExpandedWhenParentListItemIsInitiallyExpanded() {
        boolean expected = true;

        when(mParentListItem.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mParentWrapper.isInitiallyExpanded());
    }

    @Test
    public void parentWrapperIsNotInitiallyExpandedWhenParentListItemIsNotInitiallyExpanded() {
        boolean expected = false;

        when(mParentListItem.isInitiallyExpanded()).thenReturn(expected);

        assertEquals(expected, mParentWrapper.isInitiallyExpanded());
    }

    @Test
    public void getChildItemListReturnsChildItemListFromParentListItem() {
        final List<Object> expected = new ArrayList<>();
        expected.add(new Object());
        expected.add(new Object());
        expected.add(new Object());

        when(mParentListItem.getChildItemList()).thenAnswer(new Answer<Object>() {
            @NonNull
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return expected;
            }
        });

        assertEquals(expected, mParentWrapper.getChildItemList());
    }

    @Test
    public void getChildItemListReturnsEmptyListFromParentListItem() {
        boolean expected = true;

        final List<Object> childItemList = new ArrayList<>();

        when(mParentListItem.getChildItemList()).thenAnswer(new Answer<Object>() {
            @NonNull
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return childItemList;
            }
        });

        assertEquals(expected, mParentWrapper.getChildItemList().isEmpty());
    }
}
