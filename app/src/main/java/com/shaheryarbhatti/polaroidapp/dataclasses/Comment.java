package com.shaheryarbhatti.polaroidapp.dataclasses;

import java.io.Serializable;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Comment implements Serializable {
    private String userName;
    private int userProfilePicture;
    private String professon;
    private String commentDate;
    private String commentText;

    public Comment() {
    }

    public Comment(String userName, int userProfilePicture, String professon, String commentDate, String commentText) {
        this.userName = userName;
        this.userProfilePicture = userProfilePicture;
        this.professon = professon;
        this.commentDate = commentDate;
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(int userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getProfesson() {
        return professon;
    }

    public void setProfesson(String professon) {
        this.professon = professon;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
