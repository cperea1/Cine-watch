package com.example.cinewatch20;

import android.app.Application;
import android.util.Log;

import com.example.cinewatch20.models.User;

public class CineWatchApplication extends Application {

    private static final String TAG = "CineWatch-Application";
    private User activeSessionUser;


    public User getActiveSessionUser() {
        return activeSessionUser;
    }

    public void setActiveSessionUser(User activeSessionUser) {
        Log.i(TAG, "Active session user updated: "+activeSessionUser);
        this.activeSessionUser = activeSessionUser;
    }
}
