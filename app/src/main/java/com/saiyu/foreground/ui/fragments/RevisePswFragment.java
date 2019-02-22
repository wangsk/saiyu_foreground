package com.saiyu.foreground.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_revisepsw_layout)
public class RevisePswFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content,tv_revise_account,tv_revisepsw_prompt_1,tv_revisepsw_prompt_2;
    @ViewById
    Button btn_title_back,btn_next;
    @ViewById
    EditText et_psw_1,et_psw_2;
    private String account;

    public static RevisePswFragment newInstance(Bundle bundle) {
        RevisePswFragment_ fragment = new RevisePswFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterView(){
        tv_title_content.setText("找回密码");
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

    }

    @Click({R.id.btn_title_back,R.id.btn_next,R.id.tv_msg_count})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_next:
                SuccessFindPswFragment successFindPswFragment = SuccessFindPswFragment.newInstance(null);
                start(successFindPswFragment);
                break;

        }
    }

    @TextChange({R.id.et_psw_1, R.id.et_psw_2})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()) {
            case R.id.et_password:
                String password = et_psw_1.getText().toString();
                tv_revisepsw_prompt_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                if (TextUtils.isEmpty(password)) {
                    tv_revisepsw_prompt_1.setText("*请输入密码");
                    return;
                }
                if (password.length() < 6) {
                    tv_revisepsw_prompt_1.setText("*密码不能小于6位");
                    return;
                }
                if (password.length() > 32) {
                    tv_revisepsw_prompt_1.setText("*密码不能大于32位");
                    return;
                }
                tv_revisepsw_prompt_1.setText("*密码可用");
                break;
            case R.id.et_password_confirm:
                String password_confirm = et_psw_2.getText().toString();
                tv_revisepsw_prompt_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                if (TextUtils.isEmpty(password_confirm)) {
                    tv_revisepsw_prompt_2.setText("*请再次输入密码");
                    return;
                }
                if (password_confirm.equals(et_psw_1.getText().toString())) {
                    tv_revisepsw_prompt_2.setText("*密码可用");
                } else {
                    tv_revisepsw_prompt_2.setText("*两次输入不一致");
                }
                break;
        }
    }

}
