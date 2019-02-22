package com.saiyu.foreground.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_forgot_psw_layout)
public class ForgotPswFragment extends BaseFragment implements CallbackUtils.ResponseCallback {

    @ViewById
    TextView tv_title_content, tv_response_msg;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    EditText et_account;
    private String account;

    public static ForgotPswFragment newInstance(Bundle bundle) {
        ForgotPswFragment_ fragment = new ForgotPswFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("找回密码");

        et_account.setText("space123");

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("ForgotPswFragment_isAccountExist")) {
            IsAccountExistRet ret = (IsAccountExistRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isExist()) {
                tv_response_msg.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("account", account);
                FindPswByLevelFragment findPswByLevelFragment = FindPswByLevelFragment.newInstance(bundle);
                start(findPswByLevelFragment);
            } else {
                tv_response_msg.setText("*账号不存在");
            }

        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.btn_next:
                    account = et_account.getText().toString();
                    if (TextUtils.isEmpty(account)) {
                        tv_response_msg.setVisibility(View.VISIBLE);
                        tv_response_msg.setText("*请输入账号");
                        return;
                    }
                    CallbackUtils.setCallback(this);
                    ApiRequest.isAccountExist(account, "ForgotPswFragment_isAccountExist");

                    break;
            }
        }
    }

    @TextChange({R.id.et_account})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        if (hello.getId() == R.id.et_account) {
            String text = s.toString();
            if (TextUtils.isEmpty(text)) {
                tv_response_msg.setVisibility(View.INVISIBLE);
            }
        }
    }

}
