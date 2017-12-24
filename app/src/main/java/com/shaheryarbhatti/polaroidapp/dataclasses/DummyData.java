package com.shaheryarbhatti.polaroidapp.dataclasses;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.shaheryarbhatti.polaroidapp.utilities.UtilFile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class DummyData implements Serializable {
    @SerializedName("posts")
    private ArrayList<Post> posts = new ArrayList<>();

    /*private String dashboardString = "\n" +
            "{\n" +
            "  \"posts\": [\n" +
            "    {\n" +
            "      \"userType\": \"1\",\n" +
            "      \"userName\": \"Jason\",\n" +
            "      \"userPicture\": \"jason\",\n" +
            "      \"profession\": \"Artist\",\n" +
            "      \"sourceType\": \"1\",\n" +
            "      \"source\": \"boat\",\n" +
            "      \"titleText\": \"test1\",\n" +
            "      \"postDuration\": \"10h\",\n" +
            "      \"postLikes\": \"25\",\n" +
            "      \"comments\": [],\n" +
            "      \"made\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"userType\": \"1\",\n" +
            "      \"userName\": \"Megan\",\n" +
            "      \"userPicture\": \"megan\",\n" +
            "      \"profession\": \"Blogger\",\n" +
            "      \"sourceType\": \"1\",\n" +
            "      \"source\": \"butterfly\",\n" +
            "      \"titleText\": \"test2\",\n" +
            "      \"postDuration\": \"2d\",\n" +
            "      \"postLikes\": \"25\",\n" +
            "      \"comments\": [],\n" +
            "      \"made\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"userType\": \"1\",\n" +
            "      \"userName\": \"Bale\",\n" +
            "      \"userPicture\": \"bale\",\n" +
            "      \"profession\": \"Animator\",\n" +
            "      \"sourceType\": \"1\",\n" +
            "      \"source\": \"carsusol\",\n" +
            "      \"titleText\": \"test3\",\n" +
            "      \"postDuration\": \"10d\",\n" +
            "      \"postLikes\": \"25\",\n" +
            "      \"comments\": [],\n" +
            "      \"made\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";*/


    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Post> getDashboardPosts(Context context) {
        String jsonData = UtilFile.readFromfile("DashboardDummyJsonData.txt", context);
        DummyData dummyData = new GsonBuilder().create().fromJson(jsonData, DummyData.class);
        return dummyData.getPosts();

    }





    //    public ArrayList<>
}
