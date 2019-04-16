package com.saiyu.foreground.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.FaceRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.FindPswFragments.RevisePswFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/*
 * 支付宝刷脸的类，有3个功能需要调用该类：1.刷脸改密；2.刷脸认证；3.刷脸解绑手机
 * **/
@EFragment(R.layout.fragment_face_identify_layout)
public class FaceIdentifyFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_account;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    LinearLayout ll_account;
    @ViewById
    EditText et_realname, et_identity;
    @ViewById
    ProgressBar pb_loading;

    private String RealName, IDNum, faceOrderNum, account;

    public final static String FaceIdentifyFunctionType = "FaceIdentifyFunctionType";//bundle传值的key
    private int type;//0:刷脸认证；1：解绑手机；2：找回密码
    private boolean IsModify;

    public static FaceIdentifyFragment newInstance(Bundle bundle) {
        FaceIdentifyFragment_ fragment = new FaceIdentifyFragment_();
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
        if (identifyReceiver == null) {
            identifyReceiver = new IdentifyReceiver();
            IntentFilter intentFilter = new IntentFilter("Action_Identify_Callback");
            mContext.registerReceiver(identifyReceiver, intentFilter);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            RealName = bundle.getString("RealName", "");
            IDNum = bundle.getString("IDNum", "");

            type = bundle.getInt(FaceIdentifyFunctionType, 0);
            switch (type) {
                case 0://刷脸认证
                    //只有在刷脸认证的时候才能修改姓名和身份证号码（解绑手机和修改密码都不能修改姓名和身份证号码，防止其他人借用用户的号码实名认证）
                    ll_account.setVisibility(View.GONE);
                    tv_title_content.setText("刷脸认证");
                    et_realname.setText(RealName);
                    et_identity.setText(IDNum);
                    IsModify = bundle.getBoolean("IsModify",true);
                    if(!IsModify){//如果该为true说明用户已经通过实名认证，修改过一次姓名和身份证号码了
                        et_realname.setClickable(false);
                        et_realname.setFocusable(false);
                        et_identity.setClickable(false);
                        et_identity.setFocusable(false);
                    }
                    break;
                case 1://解绑手机
                    et_realname.setFocusable(false);
                    et_realname.setClickable(false);
                    et_identity.setFocusable(false);
                    et_identity.setClickable(false);
                    ll_account.setVisibility(View.GONE);
                    tv_title_content.setText("绑定手机管理");
                    et_realname.setText(RealName);
                    if (!TextUtils.isEmpty(IDNum)) {
                        try {
                            et_identity.setText(IDNum.substring(0, 3) + "***********" + IDNum.substring(IDNum.length() - 4));
                        } catch (Exception e) {
                            LogUtils.print("身份证号码截取错误");
                        }
                    }
                    break;
                case 2://刷脸改密
                    et_realname.setFocusable(false);
                    et_realname.setClickable(false);
                    et_identity.setFocusable(false);
                    et_identity.setClickable(false);
                    tv_title_content.setText("找回密码");
                    account = bundle.getString("account");
                    tv_account.setText(account);
                    et_realname.setText(RealName);
                    if (!TextUtils.isEmpty(IDNum)) {
                        try {
                            et_identity.setText(IDNum.substring(0, 3) + "***********" + IDNum.substring(IDNum.length() - 4));
                        } catch (Exception e) {
                            LogUtils.print("身份证号码截取错误");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("FaceIdentifyFragment_faceIdentify") || method.equals("FaceIdentifyFragment_mobileUnBindByFace") || method.equals("FaceIdentifyFragment_searchPswFace")) {
            FaceRet ret = (FaceRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            faceOrderNum = ret.getData().getFaceOrderNum();
            String url = ret.getData().getFaceurl();
            doVerify(url);
        } else if (method.equals("FaceIdentifyFragment_faceQuery") || method.equals("FaceIdentifyFragment_faceSearchQuery")) {
            BooleanRet ret = (BooleanRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isResult()) {
                switch (type) {
                    case 0://刷脸认证
                        Toast.makeText(mContext, "刷脸认证成功", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        break;
                    case 1://解绑手机
                        Intent intent = new Intent(mContext, ContainerActivity_.class);
                        Bundle bundle_3 = new Bundle();
                        bundle_3.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                        intent.putExtras(bundle_3);
                        mContext.startActivity(intent);
                        getActivity().finish();
                        break;
                    case 2://刷脸改密
                        Bundle bundle = new Bundle();
                        bundle.putString("account", account);
                        RevisePswFragment revisePswFragment = RevisePswFragment.newInstance(bundle);
                        start(revisePswFragment);
                        break;
                }
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    switch (type) {
                        case 0:
                            getActivity().finish();
                            break;
                        default:
                            getFragmentManager().popBackStack();
                            break;
                    }

                    break;
                case R.id.btn_next:
                    switch (type) {
                        case 0://刷脸认证
                            String realName = et_realname.getText().toString();
                            String identity = et_identity.getText().toString();
                            if (TextUtils.isEmpty(realName)) {
                                Toast.makeText(mContext, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(identity)) {
                                Toast.makeText(mContext, "请输入身份证号", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //刷脸认证
                            ApiRequest.faceIdentify(realName, identity, "FaceIdentifyFragment_faceIdentify", pb_loading);
                            break;
                        case 1://解绑手机
                            //刷脸认证解绑手机
                            ApiRequest.mobileUnBindByFace("FaceIdentifyFragment_mobileUnBindByFace", pb_loading);
                            break;
                        case 2://刷脸改密
                            if (!TextUtils.isEmpty(account)) {
                                //刷脸改密
                                ApiRequest.searchPswFace(account, "FaceIdentifyFragment_searchPswFace", pb_loading);
                            }
                            break;
                    }

                    break;
            }
        }
    }

    private IdentifyReceiver identifyReceiver;

    public class IdentifyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (intent.getAction().equals("Action_Identify_Callback")) {
                //获取支付宝人脸识别结果
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int faceStatus = 1;//成功
                    String failed_reason = "阿里验证成功";
                    String result = bundle.getString("Action_Identify_Callback");
                    if (!TextUtils.isEmpty(result)) {
                        if (result.equals("false")) {
                            faceStatus = 0;//失败
                            failed_reason = bundle.getString("failed_reason", "");
                        }
                        switch (type) {
                            case 0:
                                ApiRequest.faceQuery(faceStatus + "", failed_reason, "FaceIdentifyFragment_faceQuery", pb_loading);
                                break;
                            case 1:
                                ApiRequest.mobileUnBindByFaceSumbit(faceStatus + "", failed_reason, "FaceIdentifyFragment_faceQuery", pb_loading);
                                break;
                            case 2:
                                ApiRequest.faceSearchQuery(account, faceOrderNum, faceStatus + "", failed_reason, "FaceIdentifyFragment_faceSearchQuery", pb_loading);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (identifyReceiver != null) {
            try {
                mContext.unregisterReceiver(identifyReceiver);
            } catch (Exception e) {
            }
            identifyReceiver = null;
        }
    }

    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (Utils.hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
//            builder.append(URLEncoder.encode(url));
            builder.append(url);
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            //处理没有安装支付宝的情况
            new AlertDialog.Builder(mContext)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

}
