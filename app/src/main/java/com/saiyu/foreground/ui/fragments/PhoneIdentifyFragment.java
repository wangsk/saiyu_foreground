package com.saiyu.foreground.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
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
    private String account;
    private AccountInfoNoLoginRet accountInfoNoLoginRet;

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
            accountInfoNoLoginRet = (AccountInfoNoLoginRet)bundle.getSerializable("AccountInfoNoLoginRet");
            account = bundle.getString("account");
        }
        if(accountInfoNoLoginRet != null && accountInfoNoLoginRet.getData() != null){
            tv_phone.setText("手机号码: " + accountInfoNoLoginRet.getData().getMobile());
        }

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
                        return;
                    }

                    ApiRequest.searchPswMobile(account,checkCode,"PhoneIdentifyFragment_searchPswMobile",pb_loading);

                    break;
                case R.id.tv_msg_count:
                    if(!TextUtils.isEmpty(accountInfoNoLoginRet.getData().getMobile())){
                        countDownTimerUtils.start();
                        ApiRequest.sendVcode(accountInfoNoLoginRet.getData().getMobile(),"2",countDownTimerUtils);
                    }
                    break;

            }
        }
    }

}
