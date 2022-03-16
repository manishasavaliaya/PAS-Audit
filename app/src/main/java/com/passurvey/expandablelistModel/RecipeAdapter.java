package com.passurvey.expandablelistModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.passurvey.R;

import java.util.List;

public class RecipeAdapter extends ExpandableRecyclerAdapter<Recipe, Ingredient, RecipeViewHolder, IngredientViewHolder> {

    private static final int PARENT_VEGETARIAN = 0;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_VEGETARIAN = 2;
    private static final int CHILD_NORMAL = 3;

    private LayoutInflater mInflater;
    private List<Recipe> mRecipeList;
    Context mContext;

    public RecipeAdapter(Context context, @NonNull List<Recipe> recipeList) {
        super(recipeList);
        mRecipeList = recipeList;
        mContext=context;
        mInflater = LayoutInflater.from(context);
    }

    @UiThread
    @NonNull
    @Override
    public RecipeViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
              //  recipeView = mInflater.inflate(R.layout.recipe_view, parentViewGroup, false);
                recipeView = mInflater.inflate(R.layout.drawer_question_group_row, parentViewGroup, false);
                //drawer_question_group_row
                break;
            case PARENT_VEGETARIAN:
                recipeView = mInflater.inflate(R.layout.drawer_question_group_row, parentViewGroup, false);
                break;
        }
        return new RecipeViewHolder(recipeView,mContext);
    }

    @UiThread
    @NonNull
    @Override
    public IngredientViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        ingredientView = mInflater.inflate(R.layout.ingredient_view, childViewGroup, false);
        /*switch (viewType) {
            default:
            case CHILD_NORMAL:
                ingredientView = mInflater.inflate(R.layout.ingredient_view, childViewGroup, false);
                break;
            case CHILD_VEGETARIAN:
                ingredientView = mInflater.inflate(R.layout.vegetarian_ingredient_view, childViewGroup, false);
                break;
        }*/
        return new IngredientViewHolder(mContext,ingredientView);
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
    public int getParentViewType(int parentPosition) {
       /* if (mRecipeList.get(parentPosition).isVegetarian()) {
            return PARENT_VEGETARIAN;
        } else {

        }*/
        return PARENT_NORMAL;
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        Ingredient ingredient = mRecipeList.get(parentPosition).getIngredient(childPosition);

            return CHILD_NORMAL;

    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_VEGETARIAN || viewType == PARENT_NORMAL;
    }

}
