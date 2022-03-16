package com.passurvey.requestresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iadmin on 21/9/16.
 */
public class DefaultansRequestResponse{
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
     * The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The success
     */

    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The message
     */

    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */

    public void setMessage(String message) {
        this.message = message;
    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("srno")
        @Expose
        private Double srno;

        @SerializedName("question_id")
        @Expose
        private Integer question_id;

        @SerializedName("question_group_id")
        @Expose
        private Integer question_group_id;

        public String getDefaultAnswerPriority() {
            return defaultAnswerPriority;
        }

        public void setDefaultAnswerPriority(String defaultAnswerPriority) {
            this.defaultAnswerPriority = defaultAnswerPriority;
        }

        @SerializedName("priority")
        @Expose
        private String defaultAnswerPriority;

        @SerializedName("default_answer")
        @Expose
        private String defaultAnswer;

        /**
         *
         * @return
         * The id
         */
        public Integer getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        public Double getSrno() {
            return srno;
        }

        public void setSrno(Double srno) {
            this.srno = srno;
        }

        public Integer getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(Integer question_id) {
            this.question_id = question_id;
        }

        public Integer getQuestion_group_id() {
            return question_group_id;
        }

        public void setQuestion_group_id(Integer question_group_id) {
            this.question_group_id = question_group_id;
        }

        /**
         *
         * @return
         * The defaultAnswer
         */
        public String getDefaultAnswer() {
            return defaultAnswer;
        }

        /**
         *
         * @param defaultAnswer
         * The default_answer
         */
        public void setDefaultAnswer(String defaultAnswer) {
            this.defaultAnswer = defaultAnswer;
        }

    }
}