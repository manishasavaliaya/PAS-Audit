package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class QuestionGroupModel {
    private  String QuestionGroupId;
    private String QuestionGroupName,QuestionGroupFlag;

    public QuestionGroupModel(String questionGroupId, String questionGroupName, String questionGroupFlag) {
        QuestionGroupId = questionGroupId;
        QuestionGroupName = questionGroupName;
        QuestionGroupFlag = questionGroupFlag;
    }

    public QuestionGroupModel() {

    }

    public String getQuestionGroupFlag() {
        return QuestionGroupFlag;
    }

    public void setQuestionGroupFlag(String questionGroupFlag) {
        QuestionGroupFlag = questionGroupFlag;
    }


    public String getQuestionGroupId() {
        return QuestionGroupId;
    }

    public void setQuestionGroupId(String questionGroupId) {
        QuestionGroupId = questionGroupId;
    }

    public String getQuestionGroupName() {
        return QuestionGroupName;
    }

    public void setQuestionGroupName(String questionGroupName) {
        QuestionGroupName = questionGroupName;
    }






}
