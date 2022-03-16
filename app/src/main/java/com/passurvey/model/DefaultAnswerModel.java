package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class DefaultAnswerModel {
    public String getDefaultAnsId() {
        return DefaultAnsId;
    }

    public void setDefaultAnsId(String defaultAnsId) {
        DefaultAnsId = defaultAnsId;
    }

    public String getDefaultAnswer() {
        return DefaultAnswer;
    }

    public void setDefaultAnswer(String defaultAnswer) {
        DefaultAnswer = defaultAnswer;
    }

    private  String DefaultAnsId,DefaultAnswer,DefaultAnswerPriority;

    public DefaultAnswerModel(String defaultAnsId, String defaultAnswer) {
        DefaultAnsId = defaultAnsId;
        DefaultAnswer = defaultAnswer;

    }








}
