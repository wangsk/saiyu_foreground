package com.saiyu.foreground.cashe;

import android.app.Activity;

import com.saiyu.foreground.ui.activitys.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jishu on 2017/10/12.
 */

public class CacheActivity {
    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    public static void finishActivity() {
        if (activityList != null || activityList.size() >= 0) {
            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i).isDestroyed()) {
                } else {
                    activityList.get(i).finish();
                }
            }
        }
    }

    //结束掉其它activity
    public static void finishActivity(Activity activity) {
        if (activityList != null || activityList.size() >= 0) {
            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i).isDestroyed() || activityList.get(i) == activity) {
                } else {
                    activityList.get(i).finish();
                }
            }
        }
    }

    public static boolean removeActivity(Activity activity) {
        boolean remove = false;
        if (activityList != null && activity != null && activityList.contains(activity)) {
            remove = activityList.remove(activity);
        }
        if (activityList != null && activityList.size() == 0) {
            activityList = null;
        }
        return remove;
    }

    public static boolean activityInForeground(Activity activity) {
        return ((BaseActivity) activity).isForegroud();
    }

    public static boolean hasActivityInForeground() {
        for (Activity activity : activityList) {
            if (activityInForeground(activity)) {
                return true;
            }
        }
        return false;
    }

}
