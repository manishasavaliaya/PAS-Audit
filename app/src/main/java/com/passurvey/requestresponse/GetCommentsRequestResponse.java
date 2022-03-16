package com.passurvey.requestresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iadmin on 20/9/16.
 */
public class GetCommentsRequestResponse {

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
     *
     * @return
     *     The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     *     The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("survey_id")
        @Expose
        private Integer survey_id;

        @SerializedName("comments")
        @Expose
        private String comments;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSurvey_id() {
            return survey_id;
        }

        public void setSurvey_id(Integer survey_id) {
            this.survey_id = survey_id;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

    }
}
