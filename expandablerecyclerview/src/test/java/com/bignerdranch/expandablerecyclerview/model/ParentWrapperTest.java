package com.bignerdranch.expandablerecyclerview.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

        assertThat(mParentWrapper.isInitiallyExpanded(), is(equalTo(expected)));
    }

    @Test
    public void parentWrapperIsNotInitiallyExpandedWhenParentListItemIsNotInitiallyExpanded() {
        boolean expected = false;

        when(mParentListItem.isInitiallyExpanded()).thenReturn(expected);

        assertThat(mParentWrapper.isInitiallyExpanded(), is(equalTo(expected)));
    }
}
