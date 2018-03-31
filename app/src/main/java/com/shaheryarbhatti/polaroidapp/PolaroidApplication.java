package com.shaheryarbhatti.polaroidapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.core.Twitter;

/**
 * Created by shaheryarbhatti on 20/12/2017.
 */

public class PolaroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Twitter.initialize(this);
        AndroidNetworking.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
