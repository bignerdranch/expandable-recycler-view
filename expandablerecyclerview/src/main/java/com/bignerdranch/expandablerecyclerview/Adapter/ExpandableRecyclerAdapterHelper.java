package com.bignerdranch.expandablerecyclerview.Adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ExpandableRecyclerAdapterHelper {

    public static List<Object> generateParentChildItemList(List<? extends ParentListItem> parentItemList) {
        List<Object> parentWrapperList = new ArrayList<>();
        ParentListItem parentListItem;
        ParentWrapper parentWrapper;

        int numItems = parentItemList.size();
        for (int i = 0; i < numItems; i++) {
            parentListItem = parentItemList.get(i);
            parentWrapper = new ParentWrapper(parentListItem);
            parentWrapperList.add(parentWrapper);

            if (parentListItem.isInitiallyExpanded()) {
                parentWrapper.setExpanded(true);

                int numChildObjects = parentListItem.getChildItemList().size();
                for (int j = 0; j < numChildObjects; j++) {
                    parentWrapperList.add(parentListItem.getChildItemList().get(j));
                }
            }
        }

        return parentWrapperList;
    }
}
