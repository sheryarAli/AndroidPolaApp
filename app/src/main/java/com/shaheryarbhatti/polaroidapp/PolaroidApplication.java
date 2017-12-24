package com.shaheryarbhatti.polaroidapp;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by shaheryarbhatti on 20/12/2017.
 */

public class PolaroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
