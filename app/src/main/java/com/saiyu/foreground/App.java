package com.saiyu.foreground;

import android.app.Application;

public class App extends Application {

    private static App mApp;
    public static App getApp(){return mApp;}

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
