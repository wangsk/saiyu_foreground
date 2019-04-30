package com.saiyu.foreground;

import android.app.Application;

import com.baidu.mobstat.StatService;
import com.vondear.rxtool.RxTool;

public class App extends Application {

    private static App mApp;
    public static App getApp(){return mApp;}

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        RxTool.init(this);

        // 百度统计 开发时调用，建议上线前关闭，以免影响性能
        StatService.setDebugOn(false);
    }
}
