package com.saiyu.foreground.https;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saiyu.foreground.App;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.utils.GsonUtils;
import com.saiyu.foreground.ui.activitys.SplashActivity;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by jingkai on 2017/12/4.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    TypeToken<?> m_t;


    public JsonResponseBodyConverter(Gson gson, TypeToken<?> t) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        m_t = t;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String response = responseBody.string();

        String strResult = response.substring(0, response.length());
        LogUtils.print("strResult === " + strResult);
        Map datamap = GsonUtils.changeGsonToMaps(strResult);

        if (datamap != null) {
            String msg = (String) datamap.get("msg");
            String code = String.valueOf((Double) datamap.get("code"));
            if (!"200.0".equals(code)) {
                LogUtils.print("code === " + code);
                if("411.0".equals(code) || "413.0".equals(code)){//重新登录

                    CallbackUtils.doExitCallback(code);

                    return null;
                }

                try {
                    Map hashMap = new HashMap();
                    hashMap.put("code", code);
                    hashMap.put("msg", msg);
                    strResult = gson.toJson(hashMap);
                    T pageBean = (T) gson.fromJson(strResult, m_t.getType());
                    return pageBean;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            return null;
        }
        T pageBean = (T) gson.fromJson(strResult, m_t.getType());
        return pageBean;
    }

}
