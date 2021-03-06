package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_phone_identify_layout)
public class SubmitCashByMobileFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content, tv_phone, tv_msg_count, et_msg, tv_response_msg;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    ProgressBar pb_loading;
    private static CountDownTimerUtils countDownTimerUtils;
    private String money,Mobile,Id;

    public static SubmitCashByMobileFragment newInstance(Bundle bundle) {
        SubmitCashByMobileFragment_ fragment = new SubmitCashByMobileFragment_();
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
        Bundle bundle = getArguments();
        if(bundle != null){
            Mobile = bundle.getString("Mobile");
            money = bundle.getString("money");
            Id = bundle.getString("Id");
        }
        tv_title_content.setText("提现");
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);

        tv_phone.setText("手机号码: " + Mobile);
        btn_next.setText("确定");

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("SubmitCashByMobileFragment_submitCash")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"提现成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
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

                    ApiRequest.submitCash(Id,money,checkCode,"SubmitCashByMobileFragment_submitCash",pb_loading);

                    break;
                case R.id.tv_msg_count:
                    if(!TextUtils.isEmpty(Mobile)){
                        countDownTimerUtils.start();
                        ApiRequest.sendVcode(Mobile,"",countDownTimerUtils);
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
