package com.bignerdranch.expandablerecyclerview.Adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Brooks on 6/11/15.
 */
public class ExpandableRecyclerAdapterHelper {
    private static final String TAG = ExpandableRecyclerAdapterHelper.class.getSimpleName();
    private static final int INITIAL_STABLE_ID = 0;

    private List<Object> mHelperItemList;
    private static int sCurrentId;

    public ExpandableRecyclerAdapterHelper(List<? extends ParentObject> itemList) {
        sCurrentId = INITIAL_STABLE_ID;
        mHelperItemList = generateHelperItemList(itemList);
    }

    public List<Object> getHelperItemList() {
        return mHelperItemList;
    }

    public Object getHelperItemAtPosition(int position) {
        return mHelperItemList.get(position);
    }

    public List<Object> generateHelperItemList(List<? extends ParentObject> itemList) {
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
        return parentWrapperList;
    }
}
