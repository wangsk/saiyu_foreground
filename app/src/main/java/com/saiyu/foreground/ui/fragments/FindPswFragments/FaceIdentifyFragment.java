package com.saiyu.foreground.ui.fragments.FindPswFragments;

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
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.FaceRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_face_identify_layout)
public class FaceIdentifyFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_account;
    @ViewById
    Button btn_title_back, btn_next,btn_last;
    @ViewById
    LinearLayout ll_account;
    @ViewById
    EditText et_realname,et_identity;
    @ViewById
    ProgressBar pb_loading;
    private String account;

    private String RealName,IDNum,faceOrderNum;
    private boolean isPhoneBind,isFaceIdentify;

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

        if(identifyReceiver == null){
            identifyReceiver = new IdentifyReceiver();
            IntentFilter intentFilter = new IntentFilter("Action_Identify_Callback");
            mContext.registerReceiver(identifyReceiver, intentFilter);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            isPhoneBind = bundle.getBoolean("isPhoneBind",false);
            isFaceIdentify = bundle.getBoolean("isFaceIdentify",false);
            RealName = bundle.getString("RealName","");
            IDNum = bundle.getString("IDNum","");
            if(isFaceIdentify){
                //只有在刷脸认证的时候才能修改姓名和身份证号码
                ll_account.setVisibility(View.GONE);
                tv_title_content.setText("刷脸认证");
                et_realname.setText(RealName);
                et_identity.setText(IDNum);

            } else if(isPhoneBind){
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
            } else {
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
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("FaceIdentifyFragment_faceIdentify") || method.equals("FaceIdentifyFragment_mobileUnBindByFace") || method.equals("FaceIdentifyFragment_searchPswFace")){
            FaceRet ret = (FaceRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            faceOrderNum = ret.getData().getFaceOrderNum();
            String url = ret.getData().getFaceurl();
            doVerify(url);
        } else if(method.equals("FaceIdentifyFragment_faceQuery") || method.equals("FaceIdentifyFragment_faceSearchQuery")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                if(isFaceIdentify){
                    Toast.makeText(mContext,"刷脸认证成功",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else if(isPhoneBind){
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    Bundle bundle_3 = new Bundle();
                    bundle_3.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                    intent.putExtras(bundle_3);
                    mContext.startActivity(intent);
                    getActivity().finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("account",account);
                    RevisePswFragment revisePswFragment = RevisePswFragment.newInstance(bundle);
                    start(revisePswFragment);
                }
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next,R.id.btn_last})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                case R.id.btn_last:
                    if(isFaceIdentify){
                        getActivity().finish();
                    } else {
                        getFragmentManager().popBackStack();
                    }

                    break;
                case R.id.btn_next:
                    if(isFaceIdentify){
                        String realName = et_realname.getText().toString();
                        String identity = et_identity.getText().toString();
                        if(TextUtils.isEmpty(realName)){
                            Toast.makeText(mContext,"请输入真实姓名",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(identity)){
                            Toast.makeText(mContext,"请输入身份证号",Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        if (!realName.matches(ConstValue.nameCheck)) {
//                            Toast.makeText(mContext,"请输入汉字",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (!identity.matches(ConstValue.identityCheck)) {
//                            Toast.makeText(mContext,"请输入正确的身份证号",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        //刷脸认证
                        ApiRequest.faceIdentify(realName,identity,"FaceIdentifyFragment_faceIdentify",pb_loading);
                    } else if(isPhoneBind){
                        //刷脸认证解绑手机
                        ApiRequest.mobileUnBindByFace("FaceIdentifyFragment_mobileUnBindByFace",pb_loading);
                    } else {
                        if(!TextUtils.isEmpty(account)){
                            //刷脸改密
                            ApiRequest.searchPswFace(account,"FaceIdentifyFragment_searchPswFace",pb_loading);
                        }
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
                    if(!TextUtils.isEmpty(result)){
                        if(result.equals("false")){
                            faceStatus = 0;//失败
                            failed_reason = bundle.getString("failed_reason","");
                        }
                        if(isFaceIdentify){
                            ApiRequest.faceQuery(faceStatus+"",failed_reason,"FaceIdentifyFragment_faceQuery",pb_loading);
                        } else if(isPhoneBind){
                            ApiRequest.mobileUnBindByFaceSumbit(faceStatus+"",failed_reason,"FaceIdentifyFragment_faceQuery",pb_loading);
                        } else {
                            ApiRequest.faceSearchQuery(account,faceOrderNum,faceStatus+"",failed_reason,"FaceIdentifyFragment_faceSearchQuery",pb_loading);
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
            }catch (Exception e){
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
