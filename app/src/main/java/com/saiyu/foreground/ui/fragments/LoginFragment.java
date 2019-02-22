package com.saiyu.foreground.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.cashe.CacheActivity;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.interfaces.QQCallback;
import com.saiyu.foreground.ui.activitys.ForgotPswActivity_;
import com.saiyu.foreground.ui.activitys.ProtocolActivity_;
import com.saiyu.foreground.ui.activitys.RegistActivity_;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.saiyu.foreground.utils.Utils;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

@EFragment(R.layout.fragment_login_layout)
public class LoginFragment extends BaseFragment implements CallbackUtils.ResponseCallback{

    @ViewById
    Button  btn_login;
    @ViewById
    TextView tv_forgot_psw,tv_login_type,tv_regist,tv_protocol,tv_msg_count,tv_login_response_msg;
    @ViewById
    EditText et_account,et_password;
    @ViewById
    ImageView iv_psw,iv_account,iv_qq,iv_wechat;
    @ViewById
    LinearLayout ll_psw;
    private static CountDownTimerUtils countDownTimerUtils;
    private static final int LOGIN_TYPE_PSW = 0;//账号登录
    private static final int LOGIN_TYPE_MSG = 1;//短信验证码登录
    private int loginType = 0;

    private Tencent mTencent;
    private QQCallback qqCallback;
    private IWXAPI wxapi;
    private String wechat_img;
    private String wechat_nickname;
    private WeChatReceiver weChatReceiver;

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment_ fragment = new LoginFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterView(){
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);
        countDownTimerUtils.setClickable(false);
        // QQ登录初始化
        mTencent = Tencent.createInstance(ConstValue.QQ_APP_ID, App.getApp());
        //注册微信登录
        wxapi = WXAPIFactory.createWXAPI(mContext, ConstValue.WECHAT_APP_ID, true);
        wxapi.registerApp(ConstValue.WECHAT_APP_ID);
        if (weChatReceiver == null) {
            weChatReceiver = new WeChatReceiver();
            IntentFilter weChat_loginFragment = new IntentFilter("weChat_loginFragment");
            mContext.registerReceiver(weChatReceiver, weChat_loginFragment);
        }

        if (loadingReciver == null) {
            loadingReciver = new LoadingReciver();
            IntentFilter filter = new IntentFilter("sy_close_msg");
            mContext.registerReceiver(loadingReciver, filter);
        }
        et_account.setText("space111");//四级小于100  林华 身份证号 512501197506045175
//        et_account.setText("space123");//一级
//        et_account.setText("space112");//三级手机验证
//        et_account.setText("space113");//二级风险 绑定手机
//        et_account.setText("space114");//五级风险
        et_password.setText("space123");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if(baseRet == null || TextUtils.isEmpty(method)){
            return;
        }
        if(method.equals("login_accountLogin")|| method.equals("login_loginMobile")){
            LoginRet ret = (LoginRet) baseRet;
            if(ret.getData() == null){
                return;
            }

            SPUtils.putString("accessToken", ret.getData().getAccessToken());
            SPUtils.putString(ConstValue.AUTO_LOGIN_FLAG, ConstValue.PWD_LOGIN);

//            Intent intentlogin = new Intent(mContext,
//                    MainActivity.class);
//            mContext.startActivity(intentlogin);
//            mContext.finish();


        }
    }

    @Click({R.id.tv_msg_count,R.id.btn_login,R.id.tv_forgot_psw,R.id.tv_login_type,R.id.tv_regist,R.id.tv_protocol,R.id.iv_psw,R.id.iv_account,R.id.iv_qq,R.id.iv_wechat})
    void onClick(View view){
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()){
                case R.id.btn_login:
                    String userName = et_account.getText().toString().trim();
                    String userPassword = et_password.getText().toString().trim();
                    if(loginType == LOGIN_TYPE_PSW){
                        if(TextUtils.isEmpty(userName)){
                            tv_login_response_msg.setVisibility(View.VISIBLE);
                            tv_login_response_msg.setText("请输入用户名");
                            return;
                        }
                        if(TextUtils.isEmpty(userPassword)){
                            tv_login_response_msg.setVisibility(View.VISIBLE);
                            tv_login_response_msg.setText("请输入密码");

                            return;
                        }

                        ApiRequest.accountLogin(userName,userPassword,"login_accountLogin");
                    }else if(loginType == LOGIN_TYPE_MSG){
                        if(TextUtils.isEmpty(userName)){
                            tv_login_response_msg.setVisibility(View.VISIBLE);
                            tv_login_response_msg.setText("请输入手机号");
                            return;
                        }
                        if(TextUtils.isEmpty(userPassword)){
                            tv_login_response_msg.setVisibility(View.VISIBLE);
                            tv_login_response_msg.setText("请输入验证码");
                            return;
                        }

                        ApiRequest.loginMobile(userName,userPassword,"login_loginMobile");
                    }

                    break;
                case R.id.tv_msg_count:
                    String mobile = et_account.getText().toString().trim();
                    if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("手机号码格式错误!");
                        return;
                    } else {
                        tv_login_response_msg.setVisibility(View.INVISIBLE);
                    }
                    countDownTimerUtils.start();
                    ApiRequest.sendVcode(mobile,"");

                    break;
                case R.id.tv_forgot_psw:
                    mContext.startActivity(new Intent(mContext,ForgotPswActivity_.class));
                    break;
                case R.id.tv_login_type:
                    if(loginType == LOGIN_TYPE_PSW){
                        countDownTimerUtils.onFinish();
                        tv_login_type.setText("账号密码登录");
                        et_account.setHint("请输入手机号");
                        et_account.setText("");
                        et_password.setHint("请输入验证码");
                        et_password.setText("");
                        //从密码不可见模式变为密码可见模式
                        et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        ll_psw.setVisibility(View.GONE);
                        tv_msg_count.setVisibility(View.VISIBLE);
                        loginType = LOGIN_TYPE_MSG;
                    } else if(loginType == LOGIN_TYPE_MSG){
                        tv_login_type.setText("短信验证码登录");
                        et_account.setHint("请输入用户名");
                        et_account.setText("");
                        et_password.setHint("请输入密码");
                        et_password.setText("");
                        //从密码可见模式变为密码不可见模式
                        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        ll_psw.setVisibility(View.VISIBLE);
                        tv_msg_count.setVisibility(View.GONE);
                        loginType = LOGIN_TYPE_PSW;
                    }
                    break;
                case R.id.tv_regist:
                    mContext.startActivity(new Intent(mContext,RegistActivity_.class));
                    break;
                case R.id.tv_protocol:
                    mContext.startActivity(new Intent(mContext,ProtocolActivity_.class));
                    break;
                case R.id.iv_psw:
                    //从密码不可见模式变为密码可见模式
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    if(!TextUtils.isEmpty(et_password.getText())){
                        et_password.setSelection(et_password.getText().length());
                    }
                    break;
                case R.id.iv_account:
                    et_account.setText("");
                    et_password.setText("");
                    break;
                case R.id.iv_wechat:
                    boolean weixinAvilible = Utils.isWeixinAvilible();
                    if (weixinAvilible) {
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "diandi_wx_login";
                        wxapi.sendReq(req);
                    } else {
                        Toast.makeText(mContext, "请先安装微信", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.iv_qq:
                    qqCallback = new QQCallback();
                    //此为成功返回数据,拿到openId转换成uniodid根据此判断是否已经绑定
                    if (!mTencent.isSessionValid()) {
                        mTencent.login(this, "all", new IUiListener() {
                            @Override
                            public void onComplete(Object response) {
                                //此为成功返回数据,拿到openId转换成uniodid根据此判断是否已经绑定
                                ApiRequest.getMessage(response, mTencent,getActivity());
                            }

                            @Override
                            public void onError(UiError uiError) {
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                    break;

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 必须加这段代码,不然不会回调!
        LogUtils.print("onActivityResul--------------requestCode=" + requestCode);
        //QQ登录必须加下面两句
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqCallback);
        }
    }



    public class WeChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (intent.getAction().equals("weChat_loginFragment")) {
                //获取微信界面返回的数据
                Bundle weChatBundle = intent.getExtras();
                if (weChatBundle != null) {
                    String wechat_unionid = weChatBundle.getString("wechat_unionid");
                    wechat_img = weChatBundle.getString("wechat_img");
                    wechat_nickname = weChatBundle.getString("wechat_nickname");
                    if (!TextUtils.isEmpty(wechat_unionid)) {
                        LogUtils.print("wechat_unionid === " + wechat_unionid + " ;wechat_nickname ===  " + wechat_nickname + " ;wechat_img === " + wechat_img);
//                        Login.isWechatBind(getActivity(), wechat_unionid, wechat_nickname, wechat_img);
                    }
                }
            }
        }
    }

    @TextChange({R.id.et_account,R.id.et_password})
    void textChange(CharSequence s, TextView hello, int before, int start, int count){
        if(hello.getId() == R.id.et_account){
            String text = s.toString();
            if(!TextUtils.isEmpty(text)){
                iv_account.setVisibility(View.VISIBLE);
                countDownTimerUtils.setClickable(true);
            } else {
                iv_account.setVisibility(View.GONE);
                countDownTimerUtils.setClickable(false);
                tv_login_response_msg.setVisibility(View.INVISIBLE);
            }
        } else if(hello.getId() == R.id.et_password){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (weChatReceiver != null) {
            mContext.unregisterReceiver(weChatReceiver);
            weChatReceiver = null;
        }
        if (loadingReciver != null) {
            try {
                mContext.unregisterReceiver(loadingReciver);
                loadingReciver = null;
            } catch (Exception e){

            }

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        CacheActivity.finishActivity();
        System.exit(0);
        return true;
    }

    private LoadingReciver loadingReciver;

    private class LoadingReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "sy_close_msg":
                    countDownTimerUtils.onFinish();
                    break;
            }
        }
    }
}
