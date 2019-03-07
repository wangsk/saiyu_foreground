package com.saiyu.foreground;

import android.app.Application;

import com.vondear.rxtool.RxTool;

public class App extends Application {

    private static App mApp;
    public static App getApp(){return mApp;}

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        RxTool.init(this);
    }
}
