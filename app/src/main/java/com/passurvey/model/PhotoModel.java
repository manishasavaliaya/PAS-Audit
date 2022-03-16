package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class PhotoModel {
    String PhotoPath =  "PhotoPath";
    String PhotoComment =  "PhotoComment";
    String ProfileId =  "ProfileId";
    String UserId =  "UserId";
    String SurveyId=  "SurveyId";
    String SynFlag="synFlag";
    String CreatedDateTime ="CreatedDateTime";

    String SYNC_FLAG_IMAGE="sync_flag_image";
    String SYNC_FLAG_SURVEYCOMPLETE="sync_flag_survey_compleate";


    public PhotoModel(String photoPath, String photoComment, String profileId, String userId, String surveyId, String synFlag, String createdDateTime, String SYNC_FLAG_IMAGE, String SYNC_FLAG_SURVEYCOMPLETE) {
        PhotoPath = photoPath;
        PhotoComment = photoComment;
        ProfileId = profileId;
        UserId = userId;
        SurveyId = surveyId;
        SynFlag = synFlag;
        CreatedDateTime = createdDateTime;
        SYNC_FLAG_IMAGE = SYNC_FLAG_IMAGE;
       SYNC_FLAG_SURVEYCOMPLETE = SYNC_FLAG_SURVEYCOMPLETE;
    }




    public void setSYNC_FLAG_IMAGE(String SYNC_FLAG_IMAGE) {
        this.SYNC_FLAG_IMAGE = SYNC_FLAG_IMAGE;
    }

    public void setSYNC_FLAG_SURVEYCOMPLETE(String SYNC_FLAG_SURVEYCOMPLETE) {
        this.SYNC_FLAG_SURVEYCOMPLETE = SYNC_FLAG_SURVEYCOMPLETE;
    }



    public String getSYNC_FLAG_SURVEYCOMPLETE() {
        return SYNC_FLAG_SURVEYCOMPLETE;
    }

    public String getSYNC_FLAG_IMAGE() {
        return SYNC_FLAG_IMAGE;
    }


    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }



    public String getSynFlag() {
        return SynFlag;
    }

    public void setSynFlag(String synFlag) {
        SynFlag = synFlag;
    }




    public PhotoModel() {
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public String getPhotoComment() {
        return PhotoComment;
    }

    public void setPhotoComment(String photoComment) {
        PhotoComment = photoComment;
    }

    public String getProfileId() {
        return ProfileId;
    }

    public void setProfileId(String profileId) {
        ProfileId = profileId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSurveyId() {

        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

   /* public PhotoModel(String photoPath, String photoComment, String profileId, String userId, String surveyId,String synFlag,String createdDateTime) {
        PhotoPath = photoPath;
        PhotoComment = photoComment;
        ProfileId = profileId;
        UserId = userId;
        SurveyId = surveyId;
        SynFlag=synFlag;
        CreatedDateTime=createdDateTime;
    }
*/
}
