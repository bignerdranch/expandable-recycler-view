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

    public ExpandableRecyclerAdapterHelper(List<Object> itemList) {
        sCurrentId = INITIAL_STABLE_ID;
        mHelperItemList = generateHelperItemList(itemList);
    }

    public List<Object> getHelperItemList() {
        return mHelperItemList;
    }

    public Object getHelperItemAtPosition(int position) {
        if (mHelperItemList.get(position) instanceof ParentWrapper) {
            return mHelperItemList.get(position);
        } else {
            return mHelperItemList.get(position);
        }
    }

    public List<Object> generateHelperItemList(List<Object> itemList) {
        ArrayList<Object> parentWrapperList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i) instanceof ParentObject) {
                ParentWrapper parentWrapper = new ParentWrapper(itemList.get(i), sCurrentId);
                sCurrentId++;
                parentWrapperList.add(parentWrapper);
            } else {
                parentWrapperList.add(itemList.get(i));
            }
        }
        return parentWrapperList;
    }
}
