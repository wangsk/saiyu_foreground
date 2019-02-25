package com.saiyu.foreground.https;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.RegistRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.activitys.MainActivity_;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiRequest {

    //用账号密码登陆
    public static void accountLogin(String account, String password, final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == "+aliPushId);

        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);
//        requestParams.put("codeType", codeType);
//        requestParams.put("code", code);
//        requestParams.put("aliPushId", aliPushId);



        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.login(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    //用手机验证码登陆
    public static void loginMobile(String Phone, String code, final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == "+aliPushId);

        RequestParams requestParams = new RequestParams();
        requestParams.put("Phone", Phone);
        requestParams.put("code", code);
//        requestParams.put("codeType", codeType);
//        requestParams.put("code", code);
//        requestParams.put("aliPushId", aliPushId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.loginMobile(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();

                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }

                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

//    public static void unLogin(final Context mContext) {
//        RequestParams requestParams = new RequestParams();
//        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
//        apiService.unLogin(requestParams.getBody())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BooleanRet>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.print("onError == " + e.toString());
//                        SPUtils.putString("accessToken", "");
//                        SPUtils.putString( ConstValue.AUTO_LOGIN_FLAG, "");
//                        Intent intent = new Intent(mContext, ContainerActivity_.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mContext.startActivity(intent);
//
//                    }
//
//                    @Override
//                    public void onNext(BooleanRet ret) {
//                        SPUtils.putString("accessToken", "");
//                        SPUtils.putString( ConstValue.AUTO_LOGIN_FLAG, "");
//                        Intent intent = new Intent(mContext, ContainerActivity_.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mContext.startActivity(intent);
//                        if (ret == null) {
//                            return;
//                        }
//                        if (ret.getCode() != 200 || ret.getData() == null) {
//                            return;
//                        }
//
//                    }
//                });
//    }

    //
    /*
     * 发送验证码
     * **/
    public static void sendVcode(String mobile,String bizType,final CountDownTimerUtils countDownTimerUtils) {
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("bizType", bizType);//业务类型 0认证 1手机解绑 2找回密码
        requestParams.put("whichinterface", "0");

        RequestBody body = requestParams.getBody();
        apiService.sendVCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try{
                            countDownTimerUtils.onFinish();
                        }catch (Exception e1){
                            LogUtils.print("countDownTimerUtils close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
                            try{
                                countDownTimerUtils.onFinish();
                            }catch (Exception e1){
                                LogUtils.print("countDownTimerUtils close Exception");
                            }
                            Intent intentshowShort = new Intent();
                            intentshowShort.setAction("sy_close_msg");
                            App.getApp().sendBroadcast(intentshowShort);
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(App.getApp(), "验证码已发送，请注意查收！", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //用户注册
    public static void regist(String account, String password, final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.regist(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegistRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RegistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    //第三方用户注册
    public static void unionIDRegist(String account, String password, String oauthType, String openId,final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);
        requestParams.put("oauthType", oauthType);
        requestParams.put("openId", openId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.unionIDRegist(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegistRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RegistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void isAccountExist(String account,final String callBackKey) {
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);

        RequestBody body = requestParams.getBody();
        apiService.isAccountExist(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IsAccountExistRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(IsAccountExistRet ret) {
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void searchPswFace(String account,final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);

        RequestBody body = requestParams.getBody();
        apiService.searchPswFace(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void searchPswIdCard(String account,String name,String IDCard,final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("name", name);
        requestParams.put("IDCard", IDCard);

        RequestBody body = requestParams.getBody();
        apiService.searchPswIdCard(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void searchPswMobile(String account,String checkCode,final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("checkCode", checkCode);

        RequestBody body = requestParams.getBody();
        apiService.searchPswMobile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void retsetPsw(String account,String pwd,final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("pwd", pwd);

        RequestBody body = requestParams.getBody();
        apiService.retsetPsw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    //不登录获取用户信息
    public static void getAccountInfoNoLogin(String account, final String callBackKey,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.getAccountInfoNoLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountInfoNoLoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(AccountInfoNoLoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void getMessage(Object response, final Tencent mTencent,final Activity activity,final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        try {
            //获取openid和access_token
            String openidString = ((JSONObject) response).getString("openid");
            String access_token = ((JSONObject) response).getString("access_token");
            String expires_in = ((JSONObject) response).getString("expires_in");
            // 1）如果开发者没有调用mTencent实例的setOpenId、setAccessToken API，
            // 则该API执行正常的登录操作；
            //2）如果开发者先调用mTencent实例的setOpenId、setAccessToken
            // API，则该API执行校验登录态的操作。如果登录态有效，则返回成功给应用，
            // 如果登录态失效，则会自动进入登录流程，将最新的登录态数据返回给应用
            mTencent.setOpenId(openidString);
            mTencent.setAccessToken(access_token, expires_in);
            LogUtils.print("openidString === " + openidString + " ; access_token === " + access_token + " ;expires_in ===" + expires_in);

            //因为access_token是有时效性的，所以需要用access_token去QQ官方获取unionID以及其他用户信息
            String path = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token + "&unionid=1";

            OkHttpUtils.get().url(path).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    try {
                        pb_loading.setVisibility(View.GONE);
                    } catch (Exception e1){
                        LogUtils.print("pb_loading close Exception");
                    }

                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.print(" QQ Response===================" + response);
                    if(TextUtils.isEmpty(response)){
                        return;
                    }
                    try {
                        Pattern p = Pattern.compile("(?<=\\()[^\\)]+");
                        Matcher m = p.matcher(response);
                        String str = "";
                        while (m.find()) {
                            str = m.group(0);
                            LogUtils.print("QQ Response===================" + m.group(0));
                        }
                        if(TextUtils.isEmpty(str)){
                            return;
                        }

                        JSONObject obj = new JSONObject(str);
                        final String unionid = (String) obj.get("unionid");
                        LogUtils.print(" unionid =================== " + unionid);
                        if(TextUtils.isEmpty(unionid)){
                            return;
                        }
                        //获取QQ头像和昵称
                        QQToken qqToken = mTencent.getQQToken();
                        com.tencent.connect.UserInfo userInfo = new com.tencent.connect.UserInfo(App.getApp(), qqToken);
                        userInfo.getUserInfo(new IUiListener() {
                            @Override
                            public void onComplete(Object response) {
                                String info = response.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(info);
                                    String nickname = (String) jsonObject.get("nickname");
                                    String img = (String) jsonObject.get("figureurl_qq_2");
                                    LogUtils.print("qq信息 nickname == " + nickname + " ;img === " + img);
                                    //拿到唯一标示unionID判断是否此号已经绑定
                                    isUnionIdBind(unionid, "0",nickname,img,activity,pb_loading);

                                    mTencent.logout(App.getApp());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(UiError uiError) {
                                LogUtils.print("获取QQ信息失败");
                                try {
                                    pb_loading.setVisibility(View.GONE);
                                } catch (Exception e1){
                                    LogUtils.print("pb_loading close Exception");
                                }
                            }

                            @Override
                            public void onCancel() {
                                LogUtils.print("取消QQ获取信息");
                                try {
                                    pb_loading.setVisibility(View.GONE);
                                } catch (Exception e1){
                                    LogUtils.print("pb_loading close Exception");
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.print(" Exception===================" + e.toString());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void isUnionIdBind(final String unionID, final String type, String nickname, String img, final Activity activity,final ProgressBar pb_loading){
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("openId", unionID);
        requestParams.put("type", type);//1代表qq,2代表微信
        apiService.isUnionIDExist(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IsAccountExistRet>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print(" onError =================== " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(IsAccountExistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Boolean exist = ret.getData().isExist();
                        LogUtils.print(" exist =================== " + exist);
                        //如果为true即为绑定直接登录
                        if (exist) {
                            unionIDLogin(type, unionID, activity,pb_loading);
                        } else {

                            Intent intent = new Intent(activity, ContainerActivity_.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RegistUnionIdFragmentTag);
                            bundle.putString("type", type);
                            bundle.putString("unionID", unionID);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    }
                });
    }

    //第三方登陆
    public static void unionIDLogin(String type, String unionID, final Activity activity,final ProgressBar pb_loading) {
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == "+aliPushId);

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", type);
        requestParams.put("openId", unionID);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.unionIDLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1){
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200 || ret.getData() == null) {
                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SPUtils.putString("accessToken", ret.getData().getAccessToken());
                        SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, true);

                        Intent intent = new Intent(activity, MainActivity_.class);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                });
    }



}
