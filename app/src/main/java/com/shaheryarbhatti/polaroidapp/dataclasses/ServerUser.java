package com.shaheryarbhatti.polaroidapp.dataclasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shaheryarbhatti on 31/03/2018.
 */

public class ServerUser implements Serializable {
    @SerializedName("_id")
    private String userId;
    private String name;
    @SerializedName("profile_pic")
    private String profilePic;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
