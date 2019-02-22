package com.saiyu.foreground.utils;

import com.saiyu.foreground.https.response.BaseRet;

public class CallbackUtils {

    public interface ResponseCallback{
        public void setOnResponseCallback(String method, BaseRet baseRet);
    }

    private static ResponseCallback mResponseCallback;

    public static void setCallback(ResponseCallback callback){
        mResponseCallback = callback;
    }

    public static void doResponseCallBackMethod(String method,BaseRet baseRet){
        if(mResponseCallback != null){
            mResponseCallback.setOnResponseCallback(method,baseRet);
        }
    }

}
