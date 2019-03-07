package com.saiyu.foreground.https;


import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import java.io.IOException;

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
                .build();
        LogUtils.print("token  === " + SPUtils.getString(ConstValue.ACCESS_TOKEN, ""));
        return chain.proceed(updateRequest);
    }
}
