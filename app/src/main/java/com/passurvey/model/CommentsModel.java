package com.passurvey.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class CommentsModel {

    private  String CommentsId,Comments;

    public CommentsModel(String commentsId, String comments) {
        CommentsId = commentsId;
        Comments = comments;
    }

    public String getCommentsId() {

        return CommentsId;
    }

    public void setCommentsId(String commentsId) {
        CommentsId = commentsId;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
