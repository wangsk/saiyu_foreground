package com.saiyu.foreground.https.response;

import java.io.Serializable;

/**
 * Created by jiushubu on 2017/7/3.
 */
public class BaseRet implements Serializable {
    int code;
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public boolean isSuccess() {
        if (code == 200) {
            return true;
        } else if (code == 410 || code == 411 || code == 413) {
//            CacheActivity.finishActivity();
//            Intent intent = new Intent(App.getApp(), LoginActivity_.class);
//            App.getApp().startActivity(intent);
            return false;
        } else {
            return false;
        }
    }


}


