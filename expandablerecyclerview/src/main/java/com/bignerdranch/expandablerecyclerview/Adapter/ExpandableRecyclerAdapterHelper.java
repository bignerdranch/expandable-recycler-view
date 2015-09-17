package com.bignerdranch.expandablerecyclerview.Adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ExpandableRecyclerAdapterHelper {

    public static List<Object> generateParentChildItemList(List<? extends ParentObject> parentItemList) {
        List<Object> parentWrapperList = new ArrayList<>();
        ParentObject parentObject;
        ParentWrapper parentWrapper;

        int numItems = parentItemList.size();
        for (int i = 0; i < numItems; i++) {
            parentObject = parentItemList.get(i);
            parentWrapper = new ParentWrapper(parentObject);
            parentWrapperList.add(parentWrapper);

            if (parentObject.isInitiallyExpanded()) {
                parentWrapper.setExpanded(true);

                int numChildObjects = parentObject.getChildObjectList().size();
                for (int j = 0; j < numChildObjects; j++) {
                    parentWrapperList.add(parentObject.getChildObjectList().get(j));
                }
            }
        }

        return parentWrapperList;
    }
}
