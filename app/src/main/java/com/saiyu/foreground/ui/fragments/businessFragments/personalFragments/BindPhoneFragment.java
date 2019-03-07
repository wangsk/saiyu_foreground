package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
public class BindPhoneFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_phone, tv_msg_count, tv_response_msg,tv_prompt;
    @ViewById
    Button btn_title_back, btn_bind,btn_last;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    EditText et_mobile,et_msg;
    private static CountDownTimerUtils countDownTimerUtils;
    private String Mobile;
    public static BindPhoneFragment newInstance(Bundle bundle) {
        BindPhoneFragment_ fragment = new BindPhoneFragment_();
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
            if(TextUtils.isEmpty(Mobile)){
                //未绑定
                tv_title_content.setText("绑定手机");
                btn_bind.setText("立即绑定");
            } else {
                //已绑定
                btn_bind.setText("立即解绑");
                tv_title_content.setText("解绑手机");
                et_mobile.setText(Mobile);
                et_mobile.setClickable(false);
                et_mobile.setFocusable(false);
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("BindPhoneFragment_bindMobile")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"绑定手机号成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("Mobile",et_mobile.getText().toString());
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        } else if (method.equals("BindPhoneFragment_unBindMobile")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"解绑手机号成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
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
                    tv_prompt.setText("请输入手机号码");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                    tv_prompt.setText("手机号格式错误");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                tv_prompt.setVisibility(View.INVISIBLE);

                String checkCode = et_msg.getText().toString();
                if(TextUtils.isEmpty(checkCode)){
                    tv_response_msg.setText("*请输入验证码");
                    tv_response_msg.setVisibility(View.VISIBLE);
                    return;
                }
                tv_response_msg.setVisibility(View.INVISIBLE);
                if(TextUtils.isEmpty(Mobile)){
                    //未绑定
                    ApiRequest.bindMobile(mobile,checkCode,"BindPhoneFragment_bindMobile",pb_loading);
                } else {
                    //已绑定
                    ApiRequest.unBindMobile(checkCode,"BindPhoneFragment_unBindMobile",pb_loading);
                }

                break;
            case R.id.tv_msg_count:
                if(TextUtils.isEmpty(mobile)){
                    tv_prompt.setText("请输入手机号码");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                    tv_prompt.setText("手机号格式错误");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                tv_prompt.setVisibility(View.INVISIBLE);

                countDownTimerUtils.start();
                ApiRequest.sendVcode(mobile,"",countDownTimerUtils);
                break;
        }

    }
    @TextChange({R.id.et_msg,R.id.et_mobile})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()){
            case R.id.et_msg:
                if(!TextUtils.isEmpty(s.toString())){
                    tv_response_msg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_mobile:
                String mobile = et_mobile.getText().toString();
                if(TextUtils.isEmpty(mobile)){
                    tv_prompt.setText("请输入手机号码");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                if (!mobile.matches(ConstValue.REGEX_PHONE)) {
                    tv_prompt.setText("手机号格式错误");
                    tv_prompt.setVisibility(View.VISIBLE);
                    return;
                }
                tv_prompt.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
