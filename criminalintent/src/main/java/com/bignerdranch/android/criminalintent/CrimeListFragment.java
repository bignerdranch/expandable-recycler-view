package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CrimeExpandableAdapter mCrimeExpandableAdapter = new CrimeExpandableAdapter(getActivity(), generateCrimes());
        mCrimeExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        mCrimeExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        mCrimeExpandableAdapter.setParentAndIconExpandOnClick(true);

        mCrimeRecyclerView.setAdapter(mCrimeExpandableAdapter);

        return view;
    }

    private ArrayList<ParentObject> generateCrimes() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        for (Crime crime : crimes) {
            ArrayList<Object> childList = new ArrayList<>();
            childList.add(new CrimeChild(crime.getDate(), crime.isSolved()));
            crime.setChildObjectList(childList);
            parentObjects.add(crime);
        }
        return parentObjects;
    }
}
