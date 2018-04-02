package com.shaheryarbhatti.polaroidapp.dataclasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shaheryarbhatti on 31/03/2018.
 */

public class ServerPost implements Serializable {
    @SerializedName("_id")
    private String postId;
    private boolean isFeatured;
    private String thumbnail;
    private String source;
    private String title;
    private String type;
    @SerializedName("created_at")
    private String createdAt;
    private int comments;
    private int likes;
    @SerializedName("user")
    private ServerUser user;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }
}
