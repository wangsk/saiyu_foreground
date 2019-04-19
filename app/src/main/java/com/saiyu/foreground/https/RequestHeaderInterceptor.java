package com.saiyu.foreground.https;


import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHeaderInterceptor implements Interceptor {
    public RequestHeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
        final long time = System.currentTimeMillis();
        Request updateRequest = originalRequest.newBuilder()
                .header("appId", ConstValue.APPID)
                .header("accessToken", SPUtils.getString(ConstValue.ACCESS_TOKEN, ""))
                .header("timestamp", String.valueOf(time))
                .header("clientVersion", App.getApp().getResources().getString(R.string.app_versionName))
                .header("equipmentType", "1")//设备类型
                .header("signature", SHA1(ConstValue.APPID + ConstValue.APPSecret + String.valueOf(time)))//加密
                .build();
        LogUtils.print("timestamp === " + String.valueOf(time) + " signature === " + ConstValue.APPID + ConstValue.APPSecret + String.valueOf(time));
        return chain.proceed(updateRequest);
    }

    private static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes("UTF-8"));
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte message : messageDigest) {
                String shaHex = Integer.toHexString(message & 0xFF);
                if (shaHex.length() < 2)
                    hexString.append(0);

                hexString.append(shaHex);
            }
            return hexString.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
