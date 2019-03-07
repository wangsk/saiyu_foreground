package com.saiyu.foreground.ui.fragments.FindPswFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_phone_identify_layout)
public class PhoneIdentifyFragment extends BaseFragment implements CallbackUtils.ResponseCallback {

    @ViewById
    TextView tv_title_content, tv_phone, tv_msg_count, et_msg, tv_response_msg;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    ProgressBar pb_loading;
    private static CountDownTimerUtils countDownTimerUtils;
    private String account,mobile;

    public static PhoneIdentifyFragment newInstance(Bundle bundle) {
        PhoneIdentifyFragment_ fragment = new PhoneIdentifyFragment_();
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
        tv_title_content.setText("找回密码");
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mobile = bundle.getString("mobile");
            account = bundle.getString("account");
        }
        tv_phone.setText("手机号码: " + mobile);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PhoneIdentifyFragment_searchPswMobile")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }

            if(ret.getData().isResult()){
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                RevisePswFragment revisePswFragment = RevisePswFragment.newInstance(bundle);
                start(revisePswFragment);
            } else {
                LogUtils.print("PhoneIdentifyFragment_searchPswMobile == ");
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next, R.id.tv_msg_count})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getFragmentManager().popBackStack();
                    break;
                case R.id.btn_next:
                    String checkCode = et_msg.getText().toString();
                    if(TextUtils.isEmpty(checkCode)){
                        tv_response_msg.setText("*请输入验证码");
                        tv_response_msg.setVisibility(View.VISIBLE);
                        return;
                    }

                    if(TextUtils.isEmpty(mobile)){
                        return;
                    }

                    ApiRequest.searchPswMobile(mobile,checkCode,"PhoneIdentifyFragment_searchPswMobile",pb_loading);

                    break;
                case R.id.tv_msg_count:
                    if(!TextUtils.isEmpty(mobile)){
                        countDownTimerUtils.start();
                        ApiRequest.sendVcode(mobile,"2",countDownTimerUtils);
                    }
                    break;

            }
        }
    }

    @TextChange({R.id.et_msg})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()){
            case R.id.et_msg:
                if(!TextUtils.isEmpty(s.toString())){
                    tv_response_msg.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

}
