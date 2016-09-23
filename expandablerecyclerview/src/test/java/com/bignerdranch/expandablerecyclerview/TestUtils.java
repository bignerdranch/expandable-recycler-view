package com.bignerdranch.expandablerecyclerview;

import android.database.Observable;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public final class TestUtils {

    private TestUtils() {}

    /**
     * Fixes internal dependencies to android.database.Observable so that a RecyclerView.Adapter
     * can be tested using regular unit tests while verifying changes to the data.
     *
     * Pulled from: https://github.com/badoo/Chateau/blob/master/ExampleApp/src/test/java/com/badoo/chateau/example/ui/utils/TestUtils.java
     */
    public static RecyclerView.AdapterDataObserver fixAdapterForTesting(RecyclerView.Adapter adapter) throws NoSuchFieldException, IllegalAccessException {
        // Observables are not mocked by default so we need to hook the adapter up to an observer so we can track changes
        Field observableField = RecyclerView.Adapter.class.getDeclaredField("mObservable");
        observableField.setAccessible(true);
        Object observable = observableField.get(adapter);
        Field observersField = Observable.class.getDeclaredField("mObservers");
        observersField.setAccessible(true);
        final ArrayList<Object> observers = new ArrayList<>();
        RecyclerView.AdapterDataObserver dataObserver = mock(RecyclerView.AdapterDataObserver.class);
        observers.add(dataObserver);
        observersField.set(observable, observers);
        return dataObserver;
    }
}
