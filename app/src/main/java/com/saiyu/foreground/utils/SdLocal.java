package com.saiyu.foreground.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by qb on 2015/10/25.
 */
public class SdLocal {
    public static final String YLT_DOCTOR_Folder = "/work/syforeground";

    public static String getYLTDoctorRootPath(Context context) {
        String resultPath = null;
        String path = SdUtilBase.getStorgePath(context);
        if (path != null) {
            resultPath = path + YLT_DOCTOR_Folder;
        }
        return resultPath;
    }

    // =====================================================================================
    // 获取cache file folder
    public static String getCacheDataFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getYLTDoctorRootPath(context),
                "CacheData");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    // 获取download folder
    public static String getDownloadFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getYLTDoctorRootPath(context),
                "Download");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    // 获取temp folder
    public static String getTempFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getYLTDoctorRootPath(context),
                "Temp");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    public static String getDownloadApkPath(String path,Context context) {
        return StringUtilBase.combinePath(getDownloadFolder(context), path + ".apk");
    }
}
