package com.saiyu.foreground.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.saiyu.foreground.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public static String saveBitmap(Bitmap bitmap) {
        try {
            // 获取内置SD卡路径
            String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            // 图片文件路径
            File file = new File(sdCardPath);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file1 = files[i];
                String name = file1.getName();
                if (name.endsWith("twocode.png")) {
                    boolean flag = file1.delete();
                    LogUtils.print("删除 + " + flag);
                }
            }
            String filePath = sdCardPath + "/twocode.png";
            file = new File(filePath);
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(App.getApp().getContentResolver(),
                    file.getAbsolutePath(), "twocode.png", null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            App.getApp().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            Toast.makeText(App.getApp(),"二维码保存成功",Toast.LENGTH_SHORT).show();

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
