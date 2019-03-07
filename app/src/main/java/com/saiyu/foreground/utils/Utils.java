package com.saiyu.foreground.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.saiyu.foreground.App;

import java.util.List;

public class Utils {
    public static boolean isWeixinAvilible() {
        final PackageManager packageManager = App.getApp().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    public static boolean hasApplication() {
        PackageManager manager = App.getApp().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    public static int getintVerName(String verName) {
        verName = verName.replaceAll("\\.", "");
        if (TextUtils.isEmpty(verName)) {
            return 0;
        } else {
            return Integer.parseInt(verName);
        }
    }
}
