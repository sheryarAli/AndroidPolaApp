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
    private String userProfilePicture;
    @SerializedName("profession")
    private String profession;
    @SerializedName("sourceType")
    private int sourceType;
    @SerializedName("source")
    private String source;
    @SerializedName("titleText")
    private String titleText;
    @SerializedName("postDate")
    private String postDate;
    @SerializedName("postLikes")
    private int postLikes;

    @SerializedName("Comments")
    private ArrayList<Comment> postComments;
    @SerializedName("Made")
    private ArrayList<Made> madeList;

    public Post() {
    }

    public Post(int userType, String userName, String userProfilePicture, String profession,
                int sourceType, String source, String titleText, String postDate, int postLikes,
                ArrayList<Comment> postComments, ArrayList<Made> madeList) {
        this.userType = userType;
        this.userName = userName;
        this.userProfilePicture = userProfilePicture;
        this.profession = profession;
        this.sourceType = sourceType;
        this.source = source;
        this.titleText = titleText;
        this.postDate = postDate;
        this.postLikes = postLikes;
        this.postComments = postComments;
        this.madeList = madeList;
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

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }

    public ArrayList<Comment> getPostComments() {
        return postComments;
    }

    public void setPostComments(ArrayList<Comment> postComments) {
        this.postComments = postComments;
    }

    public ArrayList<Made> getMadeList() {
        return madeList;
    }

    public void setMadeList(ArrayList<Made> madeList) {
        this.madeList = madeList;
    }
}
