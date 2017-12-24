package com.shaheryarbhatti.polaroidapp.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Made implements Serializable {
    // TODO madeSource to String
    private String madeSource;
    private int madeLikes;
    private ArrayList<Comment> madeCommments;

    public Made() {
    }

    public Made(String madeSource, int madeLikes, ArrayList<Comment> madeCommments) {
        this.madeSource = madeSource;
        this.madeLikes = madeLikes;
        this.madeCommments = madeCommments;
    }

    public String getMadeSource() {
        return madeSource;
    }

    public void setMadeSource(String madeSource) {
        this.madeSource = madeSource;
    }

    public int getMadeLikes() {
        return madeLikes;
    }

    public void setMadeLikes(int madeLikes) {
        this.madeLikes = madeLikes;
    }

    public ArrayList<Comment> getMadeCommments() {
        return madeCommments;
    }

    public void setMadeCommments(ArrayList<Comment> madeCommments) {
        this.madeCommments = madeCommments;
    }
}
