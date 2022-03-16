package com.passurvey.model;

/**
 * Created by iadmin on 29/9/16.
 */
public class AttachedImageModel {
    String imagepath;
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }


    public AttachedImageModel(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImagepath() {
        return imagepath;
    }


}
