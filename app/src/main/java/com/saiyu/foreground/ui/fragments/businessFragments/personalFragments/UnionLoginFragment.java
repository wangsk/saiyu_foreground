package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.QQCallback;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.LoginFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
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
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_unionid_login)
public class UnionLoginFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {

    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_qq,btn_wechat;
    @ViewById
    RelativeLayout rl_qq,rl_wechat;
    private String QQUnionid,WXUnionid;

    private Tencent mTencent;
    private QQCallback qqCallback;
    private IWXAPI wxapi;
    private WeChatReceiver weChatReceiver;

    public static UnionLoginFragment newInstance(Bundle bundle) {
        UnionLoginFragment_ fragment = new UnionLoginFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("互联登录");
        Bundle bundle = getArguments();
        if (bundle != null) {
            QQUnionid = bundle.getString("QQOpenId");
            WXUnionid = bundle.getString("WXOpenId");
            if(TextUtils.isEmpty(QQUnionid)){
                btn_qq.setText("未绑定");
            } else {
                btn_qq.setText("已绑定");
            }
            if(TextUtils.isEmpty(WXUnionid)){
                btn_wechat.setText("未绑定");
            } else {
                btn_wechat.setText("已绑定");
            }
        }

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

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("UnionLoginFragment_unBind")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"解绑成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        } else if(method.equals("UnionLoginFragment_Bind")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"绑定成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.rl_qq,R.id.rl_wechat})
    void onClick(View v) {
        if (!ButtonUtils.isFastDoubleClick(v.getId())) {
            switch (v.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.rl_qq:
                    DialogUtils.showDialog(getActivity(), "提示", TextUtils.isEmpty(QQUnionid)?"需要绑定QQ吗？":"需要解绑QQ吗？", "取消", TextUtils.isEmpty(QQUnionid)?"绑定":"解绑", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(TextUtils.isEmpty(QQUnionid)){
                                qqCallback = new QQCallback();
                                //此为成功返回数据,拿到openId转换成uniodid根据此判断是否已经绑定
                                if (!mTencent.isSessionValid()) {
                                    mTencent.login(UnionLoginFragment.this, "all", new IUiListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            //此为成功返回数据,拿到openId转换成uniodid根据此判断是否已经绑定
                                            ApiRequest.getMessage(response, mTencent,getActivity(),pb_loading,false);
                                        }

                                        @Override
                                        public void onError(UiError uiError) {
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });
                                }
                            } else {
                                ApiRequest.unBind("0","UnionLoginFragment_unBind",pb_loading);
                            }
                        }
                    });
                    break;
                case R.id.rl_wechat:
                    DialogUtils.showDialog(getActivity(), "提示", TextUtils.isEmpty(WXUnionid)?"需要绑定微信吗？":"需要解绑微信吗？", "取消", TextUtils.isEmpty(WXUnionid)?"绑定":"解绑", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(TextUtils.isEmpty(WXUnionid)){
                                boolean weixinAvilible = Utils.isWeixinAvilible();
                                if (weixinAvilible) {
                                    SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "diandi_wx_login";
                                    wxapi.sendReq(req);
                                } else {
                                    Toast.makeText(mContext, "请先安装微信", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ApiRequest.unBind("1","UnionLoginFragment_unBind",pb_loading);
                            }
                        }
                    });
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

    public class WeChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (intent.getAction().equals("Action_WeChat_UnionId")) {
                //获取微信界面返回的数据
                Bundle weChatBundle = intent.getExtras();
                if (weChatBundle != null) {
                    String wechat_unionid = weChatBundle.getString("wechat_unionid");
                    String openid = weChatBundle.getString("openid");
                    if(TextUtils.isEmpty(wechat_unionid) || TextUtils.isEmpty(openid)){
                        return;
                    }
                    LogUtils.print("wechat_unionid === " + wechat_unionid + " openid == " + openid);

                    ApiRequest.isUnionIdBind(wechat_unionid, openid,"1",getActivity(),pb_loading,false);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (weChatReceiver != null) {
            try {
                mContext.unregisterReceiver(weChatReceiver);
            }catch (Exception e){

            }
            weChatReceiver = null;
        }

    }

}
