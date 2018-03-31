package com.shaheryarbhatti.polaroidapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shery on 10/21/2016.
 */
public class LocalStoragePreferences {


    private Context context;
    private static final String STORAGE_NAME = "polaroid_pref";
    private static final String ISLOGGEDIN = "isloggedin";
    private static final String TOKEN = "token";
    private static final String NAME = "name";
    private static final String USERID = "id";
    private static final String EMAIL = "email";
    private static final String DOB = "dob";


    public LocalStoragePreferences(Context context) {
        this.context = context;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putBoolean(ISLOGGEDIN, isLoggedIn);
        sp.apply();
    }


    public boolean isLoggedIn() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getBoolean(ISLOGGEDIN, false);
    }

    public void setName(String name) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(NAME, name);
        sp.apply();
    }

    public String getName() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getString(NAME, null);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(EMAIL, email);
        sp.apply();
    }

    public String getEmail() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getString(EMAIL, null);
    }


    public void setDOB(String dob) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(DOB, dob);
        sp.apply();
    }

    public String getDOB() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getString(DOB, null);
    }


    public void setUserId(String id) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(USERID, id);
        sp.apply();
    }

    public String getUserId() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getString(USERID, null);
    }

    public void setToken(String token) {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(TOKEN, token.trim());
        sp.apply();
    }

    public String getToken() {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getString(TOKEN, null);
    }


    public void clearAll() {
        SharedPreferences.Editor sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }

}
