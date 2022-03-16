
package com.passurvey.requestresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PASLoginRequestResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("sucess")
    @Expose
    private Boolean sucess;

    @SerializedName("message")
    @Expose
    private String message;

    public String getmessage() {
        return message;
    }

    public void setmessage(Data data) {
        this.data = data;
    }

    public Boolean getSucess() {
        return sucess;
    }

    public void setSucess(Boolean sucess) {
        this.sucess = sucess;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("display_name")
        @Expose
        private String displayName;
        @SerializedName("profile_completed")
        @Expose
        private Integer profileCompleted;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Integer getProfileCompleted() {
            return profileCompleted;
        }

        public void setProfileCompleted(Integer profileCompleted) {
            this.profileCompleted = profileCompleted;
        }

    }
}