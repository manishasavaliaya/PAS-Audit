package com.passurvey.model;

/**
 * Created by iadmin on 17/11/16.
 */
public class QuestionNameModel {
    public QuestionNameModel(String questionName_id, String questionName) {
        QuestionName_id = questionName_id;
        QuestionName = questionName;
    }

    public String getQuestionName_id() {
        return QuestionName_id;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName_id(String questionName_id) {
        QuestionName_id = questionName_id;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    String QuestionName_id,QuestionName;

}
