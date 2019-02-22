package com.saiyu.foreground.https.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkai on 2017/12/5.
 */

public class CJsonSecret {
    public enum EN_TYPE {APP, USER}

    static Gson gson = new Gson();

    public static String tojson(HashMap params) {
        return tojson(params, EN_TYPE.APP);
    }

    public static String tojsonOfUser(HashMap params) {
        return tojson(params, EN_TYPE.USER);
    }

    public static String tojson(HashMap params, EN_TYPE type) {
        String strRet = "";
        if (params.size() > 0) {
            strRet = gson.toJson(params);
            String key = getKey(type);
            strRet = AESUtils.Encrypt(strRet, key);
        }
        return strRet;
    }

    public static String getKey(String url) {
        EN_TYPE type = EN_TYPE.APP;
        String strPath = url;
        if (url.length() > ConstValue.SERVR_URL.length())
            strPath = url.substring(ConstValue.SERVR_URL.length(), url.length());
        if (!getInterfaceAppKey().contains(strPath)) {
            type = EN_TYPE.USER;
        }

        return getKey(type);
    }

    public static String getKey(EN_TYPE type) {

        if (ConstValue.UserSecret == null) {
            ConstValue.UserSecret = SPUtils.getString("userKey", null);
        }
        if (ConstValue.UserSecret == null) {
            Log.i("123456","UserSecret === null!!!!");
        }

        String key = type == EN_TYPE.APP ? ConstValue.APPSecret : ConstValue.UserSecret;
        int lenKey = key.length();
        if (lenKey > 4) {
            key = key.substring(lenKey - 2, lenKey) + key.substring(2, lenKey - 2) + key.substring(0, 2);
        }
        key = CSecret.md5(key);
//        KLog.i("网络链接异常", "key========" + key);
        return key;
    }

    static List<String> listInterfaceAppKey = new ArrayList<String>() {{
        add("user/login");
        add("tool/sendVCode");
    }};

    public static synchronized List<String> getInterfaceAppKey() {
        return listInterfaceAppKey;
    }


}
