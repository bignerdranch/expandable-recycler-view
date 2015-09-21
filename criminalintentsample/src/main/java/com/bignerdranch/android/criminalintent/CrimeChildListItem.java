package com.bignerdranch.android.criminalintent;

import java.util.Date;

/**
 * Created by ryanbrooks on 6/17/15.
 */
public class CrimeChildListItem {

    private Date mDate;
    private boolean mSolved;

    public CrimeChildListItem(Date date, boolean solved) {
        mDate = date;
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
