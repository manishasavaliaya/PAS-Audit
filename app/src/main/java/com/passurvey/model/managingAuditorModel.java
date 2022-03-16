package com.passurvey.model;

/**
 * Created by iadmin on 16/11/16.
 */
public class managingAuditorModel {
    String id;

    public managingAuditorModel(String id, String mangingauditor_name) {
        this.id = id;
        this.mangingauditor_name = mangingauditor_name;
    }

    public String getMangingauditor_name() {
        return mangingauditor_name;
    }

    public String getId() {
        return id;
    }

    public void setMangingauditor_name(String mangingauditor_name) {
        this.mangingauditor_name = mangingauditor_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    String mangingauditor_name;

}
