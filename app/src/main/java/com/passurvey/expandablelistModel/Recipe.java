package com.passurvey.expandablelistModel;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Recipe implements Parent<Ingredient> {

    private String mName;

    private  String QuestionGroupId;
    private String QuestionGroupFlag;



    public String getQuestionGroupId() {
        return QuestionGroupId;
    }

    public void setQuestionGroupId(String questionGroupId) {
        QuestionGroupId = questionGroupId;
    }

    public String getQuestionGroupFlag() {
        return QuestionGroupFlag;
    }

    public void setQuestionGroupFlag(String questionGroupFlag) {
        QuestionGroupFlag = questionGroupFlag;
    }

    private List<Ingredient> mIngredients;

    public Recipe(String QuestionGroupid,String name,String QuestionGroupflag, List<Ingredient> ingredients) {
        mName = name;
        mIngredients = ingredients;
        QuestionGroupFlag=QuestionGroupflag;
        QuestionGroupId=QuestionGroupid;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<Ingredient> getChildList() {
        return mIngredients;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public Ingredient getIngredient(int position) {
        return mIngredients.get(position);
    }

   /* public boolean isVegetarian() {
        for (Ingredient ingredient : mIngredients) {
           *//* if (!ingredient.isVegetarian()) {
                return false;
            }*//*
        }
        return true;
    }*/
}
