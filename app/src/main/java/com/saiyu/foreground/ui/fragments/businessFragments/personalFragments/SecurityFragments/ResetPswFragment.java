package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments;

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
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_reset_psw)
public class ResetPswFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    EditText et_password, et_password_confirm,et_password_old;
    @ViewById
    ProgressBar pb_loading;
    private String account;

    public static ResetPswFragment newInstance(Bundle bundle) {
        ResetPswFragment_ fragment = new ResetPswFragment_();
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
        tv_title_content.setText("修改登录密码");
        Bundle bundle = getArguments();
        if (bundle != null) {
            account = bundle.getString("account");
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("ResetPswFragment_revisePsw")) {
            LoginRet ret = (LoginRet) baseRet;
            if (ret.getData() == null) {
                return;
            }

            SPUtils.putString(ConstValue.ACCESS_TOKEN, ret.getData().getAccessToken());
            Toast.makeText(mContext,"密码修改成功",Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();

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
                    String password_old = et_password_old.getText().toString();

                    if (TextUtils.isEmpty(password_old)) {
                        Toast.makeText(mContext,"请输入原密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password_old.length() < 6) {
                        Toast.makeText(mContext,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password_old.length() > 32) {
                        Toast.makeText(mContext,"密码不能大于32位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String password = et_password.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(mContext,"请输入新密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6) {
                        Toast.makeText(mContext,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() > 32) {
                        Toast.makeText(mContext,"密码不能大于32位",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password_confirm = et_password_confirm.getText().toString();
                    if (TextUtils.isEmpty(password_confirm)) {
                        Toast.makeText(mContext,"请再次输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password_confirm.equals(et_password.getText().toString())) {
                        Toast.makeText(mContext,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiRequest.revisePsw(password_old, password, "ResetPswFragment_revisePsw",pb_loading);
                    break;

            }
        }
    }
}
