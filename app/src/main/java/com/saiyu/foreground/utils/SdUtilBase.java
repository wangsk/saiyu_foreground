package com.saiyu.foreground.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.io.File;

/**
 * Created by qb on 2015/10/26.
 */
public class SdUtilBase {
    private static final int STORGE_VALUE = 50;//50MB

    public static boolean checkSdCanUse() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static boolean checkSdReadWrite(File file) {
        boolean isYes = false;
        if (checkSdCanUse()) {
            if (file.canRead() && file.canWrite())
                isYes = true;
        }
        return isYes;
    }

    /**
     * 获取存储路径
     * <p>
     * 优先获取外部的，如果不存在就取内部的存储空间
     */
    public static String getStorgePath(Context context) {

        boolean flag = false;
        String path = null;
        if (checkSdCanUse()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();

            if (!checkIfFreeSpace(path))
                flag = true;
        } else {
            flag = true;
        }

        if (flag) {
            StorageManager sm = (StorageManager) context
                    .getSystemService(Context.STORAGE_SERVICE);
            try {
                String[] paths = (String[]) sm.getClass()
                        .getMethod("getVolumePaths", new Class[0]).invoke(sm, new Object[0]);
                if (paths.length > 0) {
                    for (int i = 0; i < paths.length; i++) {
                        File file = new File(paths[i]);
                        if (file.canRead() && file.canWrite()) {
                            path = paths[i];
                            if (checkIfFreeSpace(path))
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static boolean checkIfFreeSpace(String path) {
        boolean canUse = false;

        StatFs statfs = null;
        try {
            statfs = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statfs == null)
            return canUse;

        long blockSize = statfs.getBlockSize();
        long availCount = statfs.getAvailableBlocks();
        int freeSpace = (int) (availCount * blockSize / 1024 / 1024);// 空闲容量

        //剩余空间大于50MB，存储空间可用
        canUse = (freeSpace > STORGE_VALUE);
        return canUse;
    }
}
