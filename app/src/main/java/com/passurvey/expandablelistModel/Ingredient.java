package com.passurvey.expandablelistModel;

public class Ingredient {

    private String mName;

    public String getQuestionSrNo() {
        return QuestionSrNo;
    }

    public void setQuestionSrNo(String questionSrNo) {
        QuestionSrNo = questionSrNo;
    }

    private String QuestionSrNo;

    public String getQuestionFlag() {
        return QuestionFlag;
    }

    public void setQuestionFlag(String questionFlag) {
        QuestionFlag = questionFlag;
    }

    private String QuestionFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
   // private boolean mIsVegetarian;

    public Ingredient( String Id,String name,String QuestionSrno,String Questionflag) {
        mName = name;
        //mIsVegetarian = isVegetarian;
        id=Id;
        QuestionSrNo=QuestionSrno;
        QuestionFlag=Questionflag;
    }

    public String getName() {
        return mName;
    }

   /* public boolean isVegetarian() {
        return mIsVegetarian;
    }*/
}
