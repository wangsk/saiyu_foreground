package com.saiyu.foreground.interfaces;

import com.saiyu.foreground.utils.LogUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


public class QQCallback implements IUiListener {
    @Override
    public void onComplete(Object response) {
        // TODO Auto-generated method stub
        //Toast.makeText(App.getApp(), "登录成功", Toast.LENGTH_SHORT).show();
        try {
            //获得的数据是JSON格式的，获得你想获得的内容
            //如果你不知道你能获得什么，看一下下面的LOG
            LogUtils.print("-------------" + response.toString());
            String openidString = ((JSONObject) response).getString("openid");
            LogUtils.print("-------------" + openidString.toString());
            //access_token= ((JSONObject) response).getString("access_token");
            // expires_in = ((JSONObject) response).getString("expires_in");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtils.print("JSONException == " + e.toString());
        }
        /**到此已经获得OpneID以及其他你想获得的内容了
         QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
         sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
         如何得到这个UserInfo类呢？  */
//        QQToken qqToken = mTencent.getQQToken();
//        UserInfoBean info = new UserInfoBean(App.getApp(), qqToken);
//        //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
    }

    @Override
    public void onError(UiError uiError) {
        LogUtils.print("onError == " );
    }

    @Override
    public void onCancel() {
        LogUtils.print("onCancel == ");
    }

}