package com.bignerdranch.expandablerecyclerview.Adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ExpandableRecyclerAdapterHelper {
    private static int sCurrentId;


    public static List<Object> generateHelperItemList(List<? extends ParentObject> itemList) {
        ArrayList<Object> parentWrapperList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            ParentObject parentObject = itemList.get(i);
            ParentWrapper parentWrapper = new ParentWrapper(parentObject, sCurrentId);
            sCurrentId++;
            parentWrapperList.add(parentWrapper);
            if (parentObject.isInitiallyExpanded()) {
                for (int j = 0; j < parentObject.getChildObjectList().size(); j++) {
                    parentWrapperList.add(parentObject.getChildObjectList().get(j));
                }
            }
        }
        sCurrentId = 0;
        return parentWrapperList;
    }
}
