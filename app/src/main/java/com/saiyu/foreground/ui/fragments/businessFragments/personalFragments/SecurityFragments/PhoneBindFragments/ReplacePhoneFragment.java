package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments.PhoneBindFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_bind_phone)
public class ReplacePhoneFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content, tv_phone, tv_msg_count;
    @ViewById
    Button btn_title_back, btn_bind,btn_last;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    EditText et_mobile,et_msg;
    private static CountDownTimerUtils countDownTimerUtils;
    private String Mobile,checkCode;
    public static ReplacePhoneFragment newInstance(Bundle bundle) {
        ReplacePhoneFragment_ fragment = new ReplacePhoneFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }

    @AfterViews
    void afterViews(){
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Mobile = bundle.getString("Mobile");
            btn_bind.setText("更改绑定");
            tv_title_content.setText("绑定手机管理");
            et_mobile.setText(Mobile);
            et_mobile.setClickable(false);
            et_mobile.setFocusable(false);
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("ReplacePhoneFragment_checkVCode")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Bundle bundle = new Bundle();
                bundle.putString("checkCode",checkCode);
                ReplaceNewPhoneFragment replaceNewPhoneFragment = ReplaceNewPhoneFragment.newInstance(bundle);
                start(replaceNewPhoneFragment);
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_bind, R.id.tv_msg_count,R.id.btn_last})
    void onClick(View view){
        String mobile = et_mobile.getText().toString();
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_last:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_bind:
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(mContext,"请输入手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                    Toast.makeText(mContext,"手机号格式错误",Toast.LENGTH_SHORT).show();
                    return;
                }

                checkCode = et_msg.getText().toString();
                if(TextUtils.isEmpty(checkCode)){
                    Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiRequest.checkVCode(mobile,checkCode,"ReplacePhoneFragment_checkVCode",pb_loading);

                break;
            case R.id.tv_msg_count:
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(mContext,"请输入手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                    Toast.makeText(mContext,"手机号格式错误",Toast.LENGTH_SHORT).show();
                    return;
                }

                countDownTimerUtils.start();
                ApiRequest.sendVcode(mobile,"",countDownTimerUtils);
                break;
        }

    }
}
