package com.saiyu.foreground.https;

import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.https.response.RegistRet;


import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    //登录
    @POST("Login/accout")
    Observable<LoginRet> login(@Body RequestBody body);

    //手机验证码登录
    @POST("Login/Mobile")
    Observable<LoginRet> loginMobile(@Body RequestBody body);

    //第三方登录
    @POST("Authorize/OauthLogin")
    Observable<LoginRet> unionIDLogin(@Body RequestBody body);

    //退出登录
    @POST("manager/signOut")
    Observable<BooleanRet> unLogin(@Body RequestBody body);

    //用户注册
    @POST("reg/register")
    Observable<RegistRet> regist(@Body RequestBody body);

    //第三方注册
    @POST("Authorize/register")
    Observable<RegistRet> unionIDRegist(@Body RequestBody body);

    //发送验证码
    @POST("Tool/SendMobileCode")
    Observable<BooleanRet> sendVCode(@Body RequestBody body);

    @POST("Tool/isExist")
    Observable<IsAccountExistRet> isAccountExist(@Body RequestBody body);

    // 判断唯一标示unionID判断是否此号已经绑定
    @POST("Authorize/IsExistOAuth")
    Observable<IsAccountExistRet> isUnionIDExist(@Body RequestBody body);
    //不登录获取账号信息
    @POST("tool/nologinuserinfo")
    Observable<AccountInfoNoLoginRet> getAccountInfoNoLogin(@Body RequestBody body);

    //身份证找回密码
    @POST("SearchPwd/IDCardCodeSearch")
    Observable<BooleanRet> searchPswIdCard(@Body RequestBody body);
    //刷脸认证找回密码
    @POST("SearchPwd/FaceSearch")
    Observable<BooleanRet> searchPswFace(@Body RequestBody body);
    //手机号找回密码
    @POST("SearchPwd/CheckCodeSearch")
    Observable<BooleanRet> searchPswMobile(@Body RequestBody body);
    //重置密码
    @POST("SearchPwd/LoginPwdResetApi")
    Observable<BooleanRet> retsetPsw(@Body RequestBody body);




//    @GET("orderAudit/history")
//    Observable<HistoryRet> historyOrder(@Query("page") String page
//            , @Query("pageSize") String pageSize);


}
