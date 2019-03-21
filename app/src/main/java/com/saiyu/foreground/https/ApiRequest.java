package com.saiyu.foreground.https;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.AppVersionRet;
import com.saiyu.foreground.https.response.BuyerInfoRet;
import com.saiyu.foreground.https.response.CancelOrderInfoRet;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.CashRecordRet;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.https.response.FaceStatusRet;
import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.https.response.IsCountDoRet;
import com.saiyu.foreground.https.response.LoginRecordRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.RealNameStatusRet;
import com.saiyu.foreground.https.response.RechargeRateRet;
import com.saiyu.foreground.https.response.RechargeRecordRet;
import com.saiyu.foreground.https.response.RegistRet;
import com.saiyu.foreground.https.response.RewardRet;
import com.saiyu.foreground.https.response.SellerInfoRet;
import com.saiyu.foreground.https.response.FaceRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.activitys.MainActivity_;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.tencent.tauth.Tencent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiRequest {

    //用账号密码登陆
    public static void accountLogin(String account, String password, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == " + aliPushId);

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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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
    public static void loginMobile(String Phone, String code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == " + aliPushId);

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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }

                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void unLogin(final Activity activity) {
        RequestParams requestParams = new RequestParams();
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        apiService.unLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BooleanRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, false);
                        SPUtils.putString(ConstValue.ACCESS_TOKEN, "");
                        Toast.makeText(activity, "退出登录成功", Toast.LENGTH_SHORT).show();

                        CallbackUtils.doBottomSelectCallback(1);

                        Intent intent = new Intent(activity, ContainerActivity_.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        activity.finish();

                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, false);
                        SPUtils.putString(ConstValue.ACCESS_TOKEN, "");
                        Toast.makeText(activity, "退出登录成功", Toast.LENGTH_SHORT).show();

                        CallbackUtils.doBottomSelectCallback(1);

                        Intent intent = new Intent(activity, ContainerActivity_.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                });
    }

    //
    /*
     * 发送验证码
     * **/
    public static void sendVcode(String mobile, String bizType, final CountDownTimerUtils countDownTimerUtils) {
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
                        try {
                            countDownTimerUtils.onFinish();
                        } catch (Exception e1) {
                            LogUtils.print("countDownTimerUtils close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
                            try {
                                countDownTimerUtils.onFinish();
                            } catch (Exception e1) {
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

    public static void checkVCode(String mobile, String code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("code", code);

        RequestBody body = requestParams.getBody();
        apiService.checkVCode(body)
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //用户注册
    public static void regist(String account, String password, final String callBackKey, final ProgressBar pb_loading) {
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RegistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void hallIndex(String pType, String page, String pagesize, String rQBCount, String rDiscount, String pId, String extend, String sort, final String callBackKey, final ProgressBar pb_loading) {
        LogUtils.print("page : " + page + " pagesize : " + pagesize);
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("pType", pType);
        requestParams.put("page", page);
        requestParams.put("pagesize", pagesize);

        requestParams.put("rQBCount", rQBCount);
        requestParams.put("rDiscount", rDiscount);
        requestParams.put("pId", pId);
        requestParams.put("extend", extend);
        requestParams.put("sort", sort);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.hallIndex(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HallRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(HallRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void hallDetail(String orderId, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderId", orderId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.hallDetail(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HallDetailRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(HallDetailRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void hallDetailNoLogin(String orderId, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderId", orderId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.hallDetailNoLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HallDetailRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(HallDetailRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void hallDetailReceive(String orderId, String qbCount, String orderPwd, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderId", orderId);
        requestParams.put("qbCount", qbCount);
        requestParams.put("orderPwd", orderPwd);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.hallDetailReceive(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HallDetailReceiveRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(HallDetailReceiveRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void receiveSubmit(RequestParams requestParams, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.receiveSubmit(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void usableQBCountList(String orderId, String num, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderId", orderId);
        requestParams.put("num", num);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.usableQBCountList(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IsCountDoRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(IsCountDoRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void receiveCancel(String receiveId, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("receiveId", receiveId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.receiveCancel(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CancelOrderInfoRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(CancelOrderInfoRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void receiveCancelSubmit(String receiveId, String cancelBType, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("receiveId", receiveId);
        requestParams.put("cancelBType", cancelBType);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.receiveCancelSubmit(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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


    public static void financeRecord(String Page, String PageSize, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("Page", Page);
        requestParams.put("PageSize", PageSize);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.financeRecord(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CashDetailRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(CashDetailRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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


    public static void financeRechargeLog(String Page, String PageSize, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("Page", Page);
        requestParams.put("PageSize", PageSize);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.financeRechargeLog(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RechargeRecordRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RechargeRecordRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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


    public static void financeWithdrawLog(String Page, String PageSize, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("Page", Page);
        requestParams.put("PageSize", PageSize);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.financeWithdrawLog(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CashRecordRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(CashRecordRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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
    public static void unionIDRegist(String account, String password, String oauthType, String unionId, String openId, final String callBackKey, final ProgressBar pb_loading) {
        LogUtils.print("account == " + account + " password == " + password + " oauthType == " + oauthType + " unionId == " + unionId + " openId == " + openId);
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);
        requestParams.put("oauthType", oauthType);
        requestParams.put("openId", openId);
        requestParams.put("unionId", unionId);

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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RegistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void isAccountExist(String account, final String callBackKey) {
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

    public static void mobileUnBindByFace(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();

        RequestBody body = requestParams.getBody();
        apiService.mobileUnBindByFace(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(FaceRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void mobileUnBindByFaceSumbit(String faceStatus, String faceResult, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("faceStatus", faceStatus);
        requestParams.put("faceResult", faceResult);

        RequestBody body = requestParams.getBody();
        apiService.mobileUnBindByFaceSumbit(body)
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void searchPswFace(String account, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);

        RequestBody body = requestParams.getBody();
        apiService.searchPswFace(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(FaceRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void faceSearchQuery(String account, String faceOrderNum, String faceStatus, String faceResult, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("faceOrderNum", faceOrderNum);
        requestParams.put("faceStatus", faceStatus);
        requestParams.put("faceResult", faceResult);

        RequestBody body = requestParams.getBody();
        apiService.faceSearchQuery(body)
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void searchPswIdCard(String account, String name, String IDCard, final String callBackKey, final ProgressBar pb_loading) {
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void searchPswMobile(String mobile, String checkCode, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void uploadIdentity(File file, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        apiService.uploadIdentity(new RequestParams().getUploadBody(file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadIdentityRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(UploadIdentityRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void retsetPsw(String account, String pwd, final String callBackKey, final ProgressBar pb_loading) {
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //修改密码，在原密码知道的情况下
    public static void revisePsw(String originalPwd, String pwd, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("originalPwd", originalPwd);
        requestParams.put("pwd", pwd);

        RequestBody body = requestParams.getBody();
        apiService.revisePsw(body)
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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
    public static void getAccountInfoNoLogin(String account, final String callBackKey, final ProgressBar pb_loading) {
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(AccountInfoNoLoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //登录获取用户信息
    public static void getAccountInfoLogin(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.getAccountInfoLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountInfoLoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(AccountInfoLoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
//                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    //登录状态获取个别账号信息
    public static void getSmallAccountInfoLogin(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.getSmallAccountInfoLogin(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountInfoLoginRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(AccountInfoLoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
//                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }

    public static void getAppVersion(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.getAppVersion(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppVersionRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(AppVersionRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                        if (ret == null) {
                            return;
                        }
                        if (ret.getCode() != 200) {
//                            Toast.makeText(App.getApp(), ret.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallbackUtils.doResponseCallBackMethod(callBackKey, ret);

                    }
                });
    }


    //信息补填
    public static void wadInfo(String realName, String idNum, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("realName", realName);
        requestParams.put("idNum", idNum);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.wadInfo(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //查询实名认证状态
    public static void realNameStatus(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.realNameStatus(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealNameStatusRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RealNameStatusRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //实名认证
    public static void realNameSubmit(String realName, String idNum, String cardzmPic, String cardfmPic, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("realName", realName);
        requestParams.put("idNum", idNum);
        requestParams.put("cardzmPic", cardzmPic);
        requestParams.put("cardfmPic", cardfmPic);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.realNameSubmit(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //卖家激活
    public static void activeSeller(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.activeSeller(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //买家激活
    public static void activeBuyer(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.activeBuyer(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //查询刷脸认证状态
    public static void faceStatus(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.faceStatus(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceStatusRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(FaceStatusRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //刷脸认证
    public static void faceIdentify(String realName, String idNum, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("realName", realName);
        requestParams.put("idNum", idNum);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.faceIdentify(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceRet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(FaceRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //提交刷脸认证结果
    public static void faceQuery(String faceStatus, String faceResult, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("faceStatus", faceStatus);
        requestParams.put("faceResult", faceResult);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.faceQuery(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void cash(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.cash(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CashRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(CashRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void cashWays(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.cashWays(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CashChannelRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(CashChannelRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void addCashAccount(String wayId, String account, String code, String remarks, String accountId, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("wayId", wayId);
        requestParams.put("account", account);
        requestParams.put("code", code);
        requestParams.put("remarks", remarks);
        requestParams.put("accountId", accountId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.addCashAccount(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void delCashAccount(String accountId, String code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("accountId", accountId);
        requestParams.put("code", code);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.delCashAccount(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void submitCash(String accountId, String money, String Code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("accountId", accountId);
        requestParams.put("money", money);
        requestParams.put("Code", Code);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.submitCash(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void submitRecharge(String payType, String money, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("payType", payType);
        requestParams.put("money", money);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.submitRecharge(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RewardRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RewardRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void getRechargeRate(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.getRechargeRate(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RechargeRateRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(RechargeRateRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //安全限制
    public static void securitySet(String code, String orderMoney, String orderCount, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("code", code);
        requestParams.put("orderMoney", orderMoney);
        requestParams.put("orderCount", orderCount);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.securitySet(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //绑定手机
    public static void bindMobile(String mobile, String code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("code", code);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.bindMobile(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //解绑手机
    public static void unBindMobile(String code, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("code", code);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.unBindMobile(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //更换手机
    public static void changeMobile(String mobile, String oldCode, String newCode, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("oldCode", oldCode);
        requestParams.put("newCode", newCode);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.changeMobile(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //登录日志
    public static void userRecord(String pageIndex, String pageSize, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageIndex", pageIndex);
        requestParams.put("pageSize", pageSize);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.userRecord(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginRecordRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRecordRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //买家信息
    public static void buyerInfo(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.buyerInfo(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BuyerInfoRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BuyerInfoRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //卖家信息
    public static void sellerInfo(final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.sellerInfo(requestParams.getBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SellerInfoRet>() {
                    @Override
                    public void onCompleted() {
                    }


                    @Override
                    public void onError(Throwable e) {
                        LogUtils.print("onError == " + e.toString());
                        Toast.makeText(App.getApp(), "请求失败", Toast.LENGTH_SHORT).show();
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(SellerInfoRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //解绑 0: qq; 1:wechat
    public static void unBind(String IdentityType, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("IdentityType", IdentityType);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.unBind(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    //绑定 0: qq; 1:wechat
    public static void bind(String IdentityType, String openId, String unionId, final String callBackKey, final ProgressBar pb_loading) {
        pb_loading.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("IdentityType", IdentityType);
        requestParams.put("openId", openId);
        requestParams.put("unionId", unionId);

        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();

        apiService.bind(requestParams.getBody())
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(BooleanRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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

    public static void getMessage(Object response, final Tencent mTencent, final Activity activity, final ProgressBar pb_loading, final boolean isLogin) {
        pb_loading.setVisibility(View.VISIBLE);
        try {
            //获取openid和access_token
            final String openid = ((JSONObject) response).getString("openid");
            String access_token = ((JSONObject) response).getString("access_token");
            String expires_in = ((JSONObject) response).getString("expires_in");
            // 1）如果开发者没有调用mTencent实例的setOpenId、setAccessToken API，
            // 则该API执行正常的登录操作；
            //2）如果开发者先调用mTencent实例的setOpenId、setAccessToken
            // API，则该API执行校验登录态的操作。如果登录态有效，则返回成功给应用，
            // 如果登录态失效，则会自动进入登录流程，将最新的登录态数据返回给应用
            mTencent.setOpenId(openid);
            mTencent.setAccessToken(access_token, expires_in);
            LogUtils.print("openid === " + openid + " ; access_token === " + access_token + " ;expires_in ===" + expires_in);

            //因为access_token是有时效性的，所以需要用access_token去QQ官方获取unionID以及其他用户信息
            String path = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token + "&unionid=1";

            OkHttpUtils.get().url(path).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    try {
                        pb_loading.setVisibility(View.GONE);
                    } catch (Exception e1) {
                        LogUtils.print("pb_loading close Exception");
                    }

                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.print(" QQ Response===================" + response);
                    if (TextUtils.isEmpty(response)) {
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
                        if (TextUtils.isEmpty(str)) {
                            return;
                        }

                        JSONObject obj = new JSONObject(str);
                        final String unionid = (String) obj.get("unionid");
                        LogUtils.print(" unionid =================== " + unionid);
                        if (TextUtils.isEmpty(unionid)) {
                            return;
                        }

                        //拿到唯一标示unionID判断是否此号已经绑定
                        isUnionIdBind(unionid, openid, "0", activity, pb_loading, isLogin);

                        mTencent.logout(App.getApp());

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

    public static void isUnionIdBind(final String unionId, final String openId, final String type, final Activity activity, final ProgressBar pb_loading, final boolean isLogin) {
        pb_loading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiRetrofit.getRetrofit().getApiService();
        RequestParams requestParams = new RequestParams();
        requestParams.put("openId", openId);
        requestParams.put("unionId", unionId);
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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(IsAccountExistRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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
                            if (isLogin) {
                                //LoginFragment界面操作
                                unionIDLogin(type, unionId, openId, activity, pb_loading);
                            } else {
                                //UnionLoginFragment界面操作
                                if (type.equals("0")) {
                                    Toast.makeText(activity, "该QQ号已绑定", Toast.LENGTH_LONG).show();
                                } else if (type.equals("1")) {
                                    Toast.makeText(activity, "该微信号已绑定", Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            if (isLogin) {
                                //LoginFragment操作
                                Intent intent = new Intent(activity, ContainerActivity_.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RegistUnionIdFragmentTag);
                                bundle.putString("type", type);
                                bundle.putString("unionId", unionId);
                                bundle.putString("openId", openId);
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                            } else {
                                //UnionLoginFragment界面操作
                                bind(type, openId, unionId, "UnionLoginFragment_Bind", pb_loading);
                            }
                        }
                    }
                });
    }

    //第三方登陆
    public static void unionIDLogin(String type, String unionId, final String openId, final Activity activity, final ProgressBar pb_loading) {
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        String aliPushId = "";
        LogUtils.print("aliPushId == " + aliPushId);

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", type);
        requestParams.put("openId", openId);
        requestParams.put("unionId", unionId);

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
                        } catch (Exception e1) {
                            LogUtils.print("pb_loading close Exception");
                        }
                    }

                    @Override
                    public void onNext(LoginRet ret) {
                        try {
                            pb_loading.setVisibility(View.GONE);
                        } catch (Exception e1) {
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
