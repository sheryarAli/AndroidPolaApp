package com.shaheryarbhatti.polaroidapp.dataclasses;

import java.io.Serializable;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Comment implements Serializable {

    private String commentId;
    private String userName;
    private String postId;
    private String userId;
    private String userPicture;
    private String professon;
    private String commentDuration;
    private String commentText;

    public Comment() {
    }

    public Comment(String userName, String userPicture, String professon, String commentDuration, String commentText) {
        this.userName = userName;
        this.userPicture = userPicture;
        this.professon = professon;
        this.commentDuration = commentDuration;
        this.commentText = commentText;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getProfesson() {
        return professon;
    }

    public void setProfesson(String professon) {
        this.professon = professon;
    }

    public String getCommentDuration() {
        return commentDuration;
    }

    public void setCommentDuration(String commentDuration) {
        this.commentDuration = commentDuration;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
