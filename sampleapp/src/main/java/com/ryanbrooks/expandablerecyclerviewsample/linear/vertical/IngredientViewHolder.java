package com.ryanbrooks.expandablerecyclerviewsample.linear.vertical;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerviewsample.R;

public class IngredientViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
    }

    public void bind(Ingredient ingredient) {
        mIngredientTextView.setText(ingredient.getName());
    }
}
