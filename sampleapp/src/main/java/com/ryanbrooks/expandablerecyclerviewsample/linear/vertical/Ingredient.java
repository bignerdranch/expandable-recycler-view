package com.ryanbrooks.expandablerecyclerviewsample.linear.vertical;

public class Ingredient {

    private String mName;
    private boolean mIsVegetarian;

    public Ingredient(String name, boolean isVegetarian) {
        mName = name;
        mIsVegetarian = isVegetarian;
    }

    public String getName() {
        return mName;
    }

    public boolean isVegetarian() {
        return mIsVegetarian;
    }
}
