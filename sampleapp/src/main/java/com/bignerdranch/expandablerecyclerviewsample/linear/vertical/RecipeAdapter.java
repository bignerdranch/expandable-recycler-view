package com.bignerdranch.expandablerecyclerviewsample.linear.vertical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.bignerdranch.expandablerecyclerviewsample.R;

import java.util.List;

public class RecipeAdapter extends ExpandableRecyclerAdapter<Recipe, Ingredient, RecipeViewHolder, IngredientViewHolder> {

    private static final int PARENT_VEGETARIAN = 0;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_VEGETARIAN = 2;
    private static final int CHILD_NORMAL = 3;

    private LayoutInflater mInflator;
    private List<Recipe> mRecipeList;

    public RecipeAdapter(Context context, @NonNull List<Recipe> recipeList) {
        super(recipeList);
        mRecipeList = recipeList;
        mInflator = LayoutInflater.from(context);
    }

    @UiThread
    @NonNull
    @Override
    public RecipeViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
                recipeView = mInflator.inflate(R.layout.recipe_view, parentViewGroup, false);
                break;
            case PARENT_VEGETARIAN:
                recipeView = mInflator.inflate(R.layout.vegetarian_recipe_view, parentViewGroup, false);
                break;
        }
        return new RecipeViewHolder(recipeView);
    }

    @UiThread
    @NonNull
    @Override
    public IngredientViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        switch (viewType) {
            default:
            case CHILD_NORMAL:
                ingredientView = mInflator.inflate(R.layout.ingredient_view, childViewGroup, false);
                break;
            case CHILD_VEGETARIAN:
                ingredientView = mInflator.inflate(R.layout.vegetarian_ingredient_view, childViewGroup, false);
                break;
        }
        return new IngredientViewHolder(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int parentPosition, @NonNull Recipe recipe) {
        recipeViewHolder.bind(recipe);
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull Ingredient ingredient) {
        ingredientViewHolder.bind(ingredient);
    }

    @Override
    public int getParentItemViewType(int parentPosition) {
        if (mRecipeList.get(parentPosition).isVegetarian()) {
            return PARENT_VEGETARIAN;
        } else {
            return PARENT_NORMAL;
        }
    }

    @Override
    public int getChildItemViewType(int parentPosition, int childPosition) {
        Ingredient ingredient = mRecipeList.get(parentPosition).getIngredient(childPosition);
        if (ingredient.isVegetarian()) {
            return CHILD_VEGETARIAN;
        } else {
            return CHILD_NORMAL;
        }
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_VEGETARIAN || viewType == PARENT_NORMAL;
    }

}
