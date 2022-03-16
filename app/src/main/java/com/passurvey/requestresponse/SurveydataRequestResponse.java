package com.passurvey.requestresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iadmin on 20/9/16.
 */
public class SurveydataRequestResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<Datum>();
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * @return The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

  /*  public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("survey_id")
        @Expose
        private Integer surveyId;
        @SerializedName("group_id")
        @Expose
        private Integer groupId;
        @SerializedName("survey_name")
        @Expose
        private String surveyName;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("question_name")
        @Expose
        private String questionName;

        *//**
     *
     * @return
     *     The id
     *//*
        public Integer getId() {
            return id;
        }

        *//**
     *
     * @param id
     *     The id
     *//*
        public void setId(Integer id) {
            this.id = id;
        }

        *//**
     *
     * @return
     *     The surveyId
     *//*
        public Integer getSurveyId() {
            return surveyId;
        }

        *//**
     *
     * @param surveyId
     *     The survey_id
     *//*
        public void setSurveyId(Integer surveyId) {
            this.surveyId = surveyId;
        }

        *//**
     *
     * @return
     *     The groupId
     *//*
        public Integer getGroupId() {
            return groupId;
        }

        *//**
     *
     * @param groupId
     *     The group_id
     *//*
        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        *//**
     *
     * @return
     *     The surveyName
     *//*
        public String getSurveyName() {
            return surveyName;
        }

        *//**
     *
     * @param surveyName
     *     The survey_name
     *//*
        public void setSurveyName(String surveyName) {
            this.surveyName = surveyName;
        }

        *//**
     *
     * @return
     *     The groupName
     *//*
        public String getGroupName() {
            return groupName;
        }

        *//**
     *
     * @param groupName
     *     The group_name
     *//*
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        *//**
     *
     * @return
     *     The questionName
     *//*
        public String getQuestionName() {
            return questionName;
        }
        */
    /**
     *
     *//*
        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }
    }*/
    public class Datum {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("dateoflastcertificate")
        @Expose
        private String dateoflastcertificate;
        @SerializedName("qustion_srno")
        @Expose
        private Double qustionSrno;
        @SerializedName("survey_id")
        @Expose
        private Integer surveyId;
        @SerializedName("group_id")
        @Expose
        private Integer groupId;
        @SerializedName("survey_name")
        @Expose
        private String surveyName;
        @SerializedName("qustion_group_srno")
        @Expose
        private String qustionGroupSrno;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("question_name")
        @Expose
        private String questionName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDateoflastcertificate() {
            return dateoflastcertificate;
        }

        public void setDateoflastcertificate(String dateoflastcertificate) {
            this.dateoflastcertificate = dateoflastcertificate;
        }

        public Double getQustionSrno() {
            return qustionSrno;
        }

        public void setQustionSrno(Double qustionSrno) {
            this.qustionSrno = qustionSrno;
        }

        public Integer getSurveyId() {
            return surveyId;
        }

        public void setSurveyId(Integer surveyId) {
            this.surveyId = surveyId;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getSurveyName() {
            return surveyName;
        }

        public void setSurveyName(String surveyName) {
            this.surveyName = surveyName;
        }

        public String getQustionGroupSrno() {
            return qustionGroupSrno;
        }

        public void setQustionGroupSrno(String qustionGroupSrno) {
            this.qustionGroupSrno = qustionGroupSrno;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getQuestionName() {
            return questionName;
        }

        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }

    }
}