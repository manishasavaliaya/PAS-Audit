package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class QuestionAnswerModel {
    public QuestionAnswerModel(String userId, String questionId, String surveyId, String groupId, String surveyName, String groupName, String questionName, String userAnswer, String answerPriority, String createDateTime, String completeFlag,String completeSurveyFlag, String syncFlag ,String questionSrNo,String groupSrNo,String profileId,String hasCertificate,String certificateDate) {
        UserId = userId;
        QuestionId = questionId;
        SurveyId = surveyId;
        GroupId = groupId;
        SurveyName = surveyName;
        GroupName = groupName;
        QuestionName = questionName;
        UserAnswer = userAnswer;
        AnswerPriority = answerPriority;
        CreateDateTime = createDateTime;
        CompleteFlag = completeFlag;
        SyncFlag = syncFlag;
        CompleteSurveyFlag =completeSurveyFlag;
        QuestionSrNo =questionSrNo;
        GroupSrNo =groupSrNo;
        ProfileId=profileId;
        HasCertificate =hasCertificate;
        CertificateDate =certificateDate;
    }

    String UserId;
    String QuestionId;
    String SurveyId;
    String GroupId;
    String SurveyName;
    String GroupName;
    String QuestionName;
    String UserAnswer;
    String AnswerPriority;
    String CreateDateTime;
    String CompleteFlag;

    String GroupSrNo ;
    String QuestionSrNo ;
    String ProfileId ;
    String HasCertificate ;
    String CertificateDate ;

    public String getHasCertificate() {
        return HasCertificate;
    }

    public void setHasCertificate(String hasCertificate) {
        HasCertificate = hasCertificate;
    }

    public String getCertificateDate() {
        return CertificateDate;
    }

    public void setCertificateDate(String certificateDate) {
        CertificateDate = certificateDate;
    }

    public String getProfileId() {
        return ProfileId;
    }

    public void setProfileId(String profileId) {
        ProfileId = profileId;
    }

    public String getQuestionSrNo() {
        return QuestionSrNo;
    }

    public void setQuestionSrNo(String questionSrNo) {
        QuestionSrNo = questionSrNo;
    }

    public String getGroupSrNo() {
        return GroupSrNo;
    }

    public void setGroupSrNo(String groupSrNo) {
        GroupSrNo = groupSrNo;
    }



    public String getCompleteSurveyFlag() {
        return CompleteSurveyFlag;
    }

    public void setCompleteSurveyFlag(String completeSurveyFlag) {
        CompleteSurveyFlag = completeSurveyFlag;
    }

    String CompleteSurveyFlag;

    public QuestionAnswerModel() {

    }

    public String getSyncFlag() {
        return SyncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        SyncFlag = syncFlag;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getSurveyName() {
        return SurveyName;
    }

    public void setSurveyName(String surveyName) {
        SurveyName = surveyName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public String getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        UserAnswer = userAnswer;
    }

    public String getAnswerPriority() {
        return AnswerPriority;
    }

    public void setAnswerPriority(String answerPriority) {
        AnswerPriority = answerPriority;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getCompleteFlag() {
        return CompleteFlag;
    }

    public void setCompleteFlag(String completeFlag) {
        CompleteFlag = completeFlag;
    }

    String SyncFlag;
    /*+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER  , "
            + QUESTION_ID + " INTEGER , "
            + SURVEY_ID + " INTEGER, "
            + GROUP_ID + " INTEGER, "
            + SURVEY_NAME + " TEXT, "
            + GROUP_NAME + " TEXT, "
            + QUESTION_NAME + " TEXT, "
            + USER_ANSWER + " TEXT, "
            + ANSWER_PRIORITY + " TEXT, "
            + CREATED_DATE_TIME + " TEXT, "
            + COMPLETE_FLAG + " INTEGER, "
            + SYNC_FLAG + " INTEGER " + ")*/

}
