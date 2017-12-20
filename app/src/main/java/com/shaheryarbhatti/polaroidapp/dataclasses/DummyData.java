package com.shaheryarbhatti.polaroidapp.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class DummyData implements Serializable {

    private ArrayList<Post> posts = new ArrayList<>();

    public ArrayList<Post> getPosts() {
        return posts;
    }

    //    public ArrayList<>
}
