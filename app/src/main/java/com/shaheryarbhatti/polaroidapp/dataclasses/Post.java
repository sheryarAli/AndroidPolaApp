package com.shaheryarbhatti.polaroidapp.dataclasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Post implements Serializable {
    @SerializedName("userType")
    private int userType;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userPicture")
    private String userPicture;
    @SerializedName("profession")
    private String profession;
    @SerializedName("sourceType")
    private int sourceType;
    @SerializedName("source")
    private String source;

    @SerializedName("titleText")
    private String titleText;
    @SerializedName("postDuration")
    private String postDuration;
    @SerializedName("postLikes")
    private int postLikes;

    @SerializedName("comments")
    private ArrayList<Comment> comments = new ArrayList<>();
    @SerializedName("made")
    private ArrayList<Made> made = new ArrayList<>();

    public Post() {
    }

    public Post(int userType, String userName, String userPicture, String profession,
                int sourceType, String source, String titleText, String postDuration, int postLikes,
                ArrayList<Comment> comments, ArrayList<Made> made) {
        this.userType = userType;
        this.userName = userName;
        this.userPicture = userPicture;
        this.profession = profession;
        this.sourceType = sourceType;
        this.source = source;
        this.titleText = titleText;
        this.postDuration = postDuration;
        this.postLikes = postLikes;
        this.comments = comments;
        this.made = made;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getPostDuration() {
        return postDuration;
    }

    public void setPostDuration(String postDuration) {
        this.postDuration = postDuration;
    }

    public int getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Made> getMade() {
        return made;
    }

    public void setMade(ArrayList<Made> made) {
        this.made = made;
    }
}
