package com.ryanbrooks.expandablerecyclerview;

import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;

import java.util.List;

/**
 * Created by ryanbrooks on 5/28/15.
 */
public class ExpandableHelper {

    private static List<ExpandingObject> expandingObjectList;

    public ExpandableHelper(List<ExpandingObject> expandingObjectList) {
        this.expandingObjectList = expandingObjectList;
    }

    public static List<ExpandingObject> getExpandingObjectList() {
        return expandingObjectList;
    }

    public static void setExpandingObjectList(List<ExpandingObject> expandingObjectList) {
        ExpandableHelper.expandingObjectList = expandingObjectList;
    }
}
