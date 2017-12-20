package com.shaheryarbhatti.polaroidapp.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Made implements Serializable {
    // TODO madeSource to String
    private int madeSource;
    private int madeLikes;
    private ArrayList<Comment> madeCommments;

    public Made() {
    }

    public Made(int madeSource, int madeLikes, ArrayList<Comment> madeCommments) {
        this.madeSource = madeSource;
        this.madeLikes = madeLikes;
        this.madeCommments = madeCommments;
    }

    public int getMadeSource() {
        return madeSource;
    }

    public void setMadeSource(int madeSource) {
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
