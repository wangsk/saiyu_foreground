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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.ActiveStatusRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.interfaces.QQCallback;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

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
    @ViewById
    ProgressBar pb_loading;
    private static CountDownTimerUtils countDownTimerUtils;
    private static final String LOGIN_TYPE_PSW = "账号密码登录";//账号登录
    private static final String LOGIN_TYPE_MSG = "短信验证码登录";//短信验证码登录

    private Tencent mTencent;
    private QQCallback qqCallback;
    private IWXAPI wxapi;
    private WeChatReceiver weChatReceiver;

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment_ fragment = new LoginFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //回调的注册放在这里是因为之前遇到从其他页面跳转回来的时候，回调不执行了，所以每次页面显示的时候都注册一次
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterView(){

        tv_login_type.setText(LOGIN_TYPE_MSG);

        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);
        countDownTimerUtils.setClickable(false);
        // QQ登录初始化
        mTencent = Tencent.createInstance(ConstValue.QQ_APP_ID, App.getApp());
        //注册微信登录
        wxapi = WXAPIFactory.createWXAPI(mContext, ConstValue.WECHAT_APP_ID, true);
        wxapi.registerApp(ConstValue.WECHAT_APP_ID);
        if (weChatReceiver == null) {
            weChatReceiver = new WeChatReceiver();
            IntentFilter weChat_loginFragment = new IntentFilter("Action_WeChat_UnionId");
            mContext.registerReceiver(weChatReceiver, weChat_loginFragment);
        }

 //       et_account.setText("space111");//四级小于100  林华 身份证号 512501197506045175
//        et_account.setText("space123");//一级
//        et_account.setText("space112");//三级手机验证
//        et_account.setText("space113");//二级风险 绑定手机
//        et_account.setText("space114");//五级风险
//        et_password.setText("space123");
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

            SPUtils.putString(ConstValue.ACCESS_TOKEN, ret.getData().getAccessToken());
            SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, true);

            ApiRequest.getUserStatus("LoginFragment_getUserStatus",pb_loading);


        } else if(method.equals("LoginFragment_getUserStatus")){
            ActiveStatusRet ret = (ActiveStatusRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            boolean UserInfoStatus = ret.getData().isUserInfoStatus();
            if(UserInfoStatus){
                SPUtils.putInt(ConstValue.IdentifyInfo,1);//已补填身份信息
            } else {
                SPUtils.putInt(ConstValue.IdentifyInfo,0);//未补填身份信息
            }
            boolean UserBuyerStatus = ret.getData().isUserBuyerStatus();
            boolean UserSellerStatus = ret.getData().isUserSellerStatus();

            if(UserBuyerStatus && UserSellerStatus){
                //全部激活
                SPUtils.putInt(ConstValue.MainBottomVisibleType,3);//全部显示
            }else if(!UserBuyerStatus && !UserSellerStatus){
                //全部未激活
                SPUtils.putInt(ConstValue.MainBottomVisibleType,0);//全部显示
            } else {
                if(!UserBuyerStatus){
                    //卖家激活，买家未激活
                    SPUtils.putInt(ConstValue.MainBottomVisibleType,1);//不显示买家
                } else if(!UserSellerStatus){
                    //买家激活，卖家未激活
                    SPUtils.putInt(ConstValue.MainBottomVisibleType,2);//不显示卖家
                }
            }

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLogin",true);
            intent.putExtras(bundle);
            getActivity().setResult(RESULT_OK, intent);//如果需要登录返回状态的时候可以用这段代码

            getActivity().finish();
        }
    }

    @Click({R.id.iv_close,R.id.tv_msg_count,R.id.btn_login,R.id.tv_forgot_psw,R.id.tv_login_type,R.id.tv_regist,R.id.tv_protocol,R.id.iv_psw,R.id.iv_account,R.id.iv_qq,R.id.iv_wechat})
    void onClick(View view){
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()){
                case R.id.iv_close:
                    getActivity().finish();
                    break;
                case R.id.btn_login://点击登录按钮
                    String userName = et_account.getText().toString().trim();
                    String userPassword = et_password.getText().toString().trim();
                    if(LOGIN_TYPE_MSG.equals(tv_login_type.getText().toString())){
                        //登录type文案为 短信验证码登录 的时候为 账号密码登录
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

                        ApiRequest.accountLogin(userName,userPassword,"login_accountLogin",pb_loading);
                    } else if(LOGIN_TYPE_PSW.equals(tv_login_type.getText().toString())){
                        //登录type文案为 账号密码登录 的时候为 短信验证码登录
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

                        ApiRequest.loginMobile(userName,userPassword,"login_loginMobile",pb_loading);
                    }

                    break;
                case R.id.tv_msg_count://发送短信验证码
                    String mobile = et_account.getText().toString().trim();
                    if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("手机号码格式错误!");
                        return;
                    } else {
                        tv_login_response_msg.setVisibility(View.INVISIBLE);
                    }
                    countDownTimerUtils.start();
                    ApiRequest.sendVcode(mobile,"",countDownTimerUtils);

                    break;
                case R.id.tv_forgot_psw://点击忘记密码
                    Bundle bundle_1 = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle_1.putInt(ContainerActivity.FragmentTag, ContainerActivity.ForgotPswFragmentTag);
                    intent.putExtras(bundle_1);
                    mContext.startActivity(intent);
                    break;
                case R.id.tv_login_type://点击切换登录方式
                    if(LOGIN_TYPE_MSG.equals(tv_login_type.getText().toString())){
                        tv_login_type.setText(LOGIN_TYPE_PSW);

                        countDownTimerUtils.onFinish();
                        et_account.setHint("请输入手机号");
                        et_account.setText("");
                        et_password.setHint("请输入验证码");
                        et_password.setText("");
                        //从密码不可见模式变为密码可见模式
                        et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        ll_psw.setVisibility(View.GONE);
                        tv_msg_count.setVisibility(View.VISIBLE);
                    } else if(LOGIN_TYPE_PSW.equals(tv_login_type.getText().toString())){
                        tv_login_type.setText(LOGIN_TYPE_MSG);

                        et_account.setHint("请输入用户名");
                        et_account.setText("");
                        et_password.setHint("请输入密码");
                        et_password.setText("");
                        //从密码可见模式变为密码不可见模式
                        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        ll_psw.setVisibility(View.VISIBLE);
                        tv_msg_count.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_regist://点击注册
                    Bundle bundle_2 = new Bundle();
                    Intent intent_2 = new Intent(mContext,ContainerActivity_.class);
                    bundle_2.putInt(ContainerActivity.FragmentTag, ContainerActivity.RegistFragmentTag);
                    intent_2.putExtras(bundle_2);
                    mContext.startActivity(intent_2);
                    break;
                case R.id.tv_protocol:
                    ProtocolFragment protocolFragment =  ProtocolFragment.newInstance(null);
                    start(protocolFragment);
                    break;
                case R.id.iv_psw:
                    //从密码不可见模式变为密码可见模式
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    if(!TextUtils.isEmpty(et_password.getText())){
                        et_password.setSelection(et_password.getText().length());
                    }
                    break;
                case R.id.iv_account://清空输入框
                    et_account.setText("");
                    et_password.setText("");
                    break;
                case R.id.iv_wechat://点击微信登录
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
                case R.id.iv_qq://点击QQ登录
                    qqCallback = new QQCallback();
                    //此为成功返回数据,拿到openId转换成uniodid根据此判断是否已经绑定
                    if (!mTencent.isSessionValid()) {
                        mTencent.login(this, "all", new IUiListener() {
                            @Override
                            public void onComplete(Object response) {
                                //QQ登录返回结果
                                ApiRequest.getMessage(response, mTencent,getActivity(),pb_loading,true);
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
        LogUtils.print("onActivityResul--------------requestCode=" + requestCode);
        //QQ登录必须加下面两句,不然会失败
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqCallback);
        }
    }

    //微信登录结果接收广播
    public class WeChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (intent.getAction().equals("Action_WeChat_UnionId")) {
                //获取微信界面返回的数据
                Bundle weChatBundle = intent.getExtras();
                if (weChatBundle != null) {
                    String wechat_unionid = weChatBundle.getString("wechat_unionid");
                    String openid = weChatBundle.getString("openid");
                    if (!TextUtils.isEmpty(wechat_unionid)) {
                        LogUtils.print("wechat_unionid === " + wechat_unionid + "  openid == " + openid);

                        ApiRequest.isUnionIdBind(wechat_unionid, openid,"1",getActivity(),pb_loading,true);

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
    public void onDestroy() {
        super.onDestroy();
        if (weChatReceiver != null) {
            try {
                mContext.unregisterReceiver(weChatReceiver);
            }catch (Exception e){
            }
            weChatReceiver = null;
        }
    }

}
