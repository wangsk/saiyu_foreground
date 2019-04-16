package com.saiyu.foreground.ui.fragments.RegistFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.FindPswFragments.SuccessFindPswFragment;
import com.saiyu.foreground.ui.fragments.ProtocolFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_regist_layout)
public class RegistFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_regist_prompt_1, tv_regist_prompt_2, tv_regist_prompt_3, tv_protocol, tv_title_right;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    EditText et_account, et_password, et_password_confirm;
    @ViewById
    ImageView iv_account_clear, iv_psw_clear, iv_psw, iv_psw_confirm_clear, iv_psw_confirm;
    @ViewById
    ProgressBar pb_loading;
    private String accountExist = "*账号已存在";

    public static RegistFragment newInstance(Bundle bundle) {
        RegistFragment_ fragment = new RegistFragment_();
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
        tv_title_content.setText("注册");
        tv_title_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("RegistFragment_regist")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRegist", true);
            SuccessFindPswFragment successFindPswFragment = SuccessFindPswFragment.newInstance(bundle);
            start(successFindPswFragment);

        } else if (method.equals("RegistFragment_isAccountExist")) {
            IsAccountExistRet ret = (IsAccountExistRet) baseRet;
            if (ret.getData() == null) {
                return;
            }

            if (ret.getData().isExist()) {
                tv_regist_prompt_1.setVisibility(View.VISIBLE);
                tv_regist_prompt_1.setText(accountExist);
            } else {
                tv_regist_prompt_1.setText("");
                tv_regist_prompt_1.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next, R.id.tv_protocol, R.id.tv_title_right, R.id.iv_account_clear, R.id.iv_psw_clear, R.id.iv_psw, R.id.iv_psw_confirm_clear, R.id.iv_psw_confirm})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.tv_title_right:
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.btn_next:
                    String account = et_account.getText().toString();
                    if (TextUtils.isEmpty(account)) {
                        tv_regist_prompt_1.setVisibility(View.VISIBLE);
                        tv_regist_prompt_1.setText("*请输入账号");
                        Toast.makeText(mContext,"请输入账号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (accountExist.equals(tv_regist_prompt_1.getText().toString())) {
                        Toast.makeText(mContext,"账号已存在",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean isOk = true;

                    if (account.matches(ConstValue.checkAccout)) {
                        if (account.matches(ConstValue.checkAccout_2)) {
                            isOk = false;
                        }
                        if (account.matches(ConstValue.checkAccout_3)) {
                            isOk = false;
                        }
                        if (account.matches(ConstValue.checkAccout_4)) {
                            isOk = false;
                        }
                    } else {
                        isOk = false;
                    }
                    if (!isOk) {
                        tv_regist_prompt_1.setVisibility(View.VISIBLE);
                        tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
                        Toast.makeText(mContext,"账号格式错误",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password = et_password.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        tv_regist_prompt_2.setVisibility(View.VISIBLE);
                        tv_regist_prompt_2.setText("*请输入密码");
                        Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6) {
                        tv_regist_prompt_2.setVisibility(View.VISIBLE);
                        tv_regist_prompt_2.setText("*密码不能小于6位");
                        Toast.makeText(mContext,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() > 32) {
                        tv_regist_prompt_2.setVisibility(View.VISIBLE);
                        tv_regist_prompt_2.setText("*密码不能大于32位");
                        Toast.makeText(mContext,"密码不能大于32位",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password_confirm = et_password_confirm.getText().toString();
                    if (TextUtils.isEmpty(password_confirm)) {
                        tv_regist_prompt_3.setVisibility(View.VISIBLE);
                        tv_regist_prompt_3.setText("*请再次输入密码");
                        Toast.makeText(mContext,"请再次输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password_confirm.equals(et_password.getText().toString())) {
                        tv_regist_prompt_3.setVisibility(View.VISIBLE);
                        tv_regist_prompt_3.setText("*两次输入不一致");
                        Toast.makeText(mContext,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiRequest.regist(account, password, "RegistFragment_regist",pb_loading);
                    break;
                case R.id.tv_protocol:
                    ProtocolFragment protocolFragment =  ProtocolFragment.newInstance(null);
                    start(protocolFragment);
                    break;
                case R.id.iv_account_clear:
                    et_account.setText("");
                    break;
                case R.id.iv_psw_clear:
                    et_password.setText("");
                    break;
                case R.id.iv_psw_confirm_clear:
                    et_password_confirm.setText("");
                    break;
                case R.id.iv_psw:
                    if(flag_1){
                        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_psw.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.yanjing));
                        flag_1 = false;
                    } else {
                        //从密码不可见模式变为密码可见模式
                        et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_psw.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.eye_2));
                        flag_1 = true;
                    }

                    if (!TextUtils.isEmpty(et_password.getText())) {
                        et_password.setSelection(et_password.getText().length());
                    }
                    break;
                case R.id.iv_psw_confirm:
                    if(flag_2){
                        et_password_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_psw_confirm.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.yanjing));
                        flag_2 = false;
                    } else {
                        //从密码不可见模式变为密码可见模式
                        et_password_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_psw_confirm.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.eye_2));
                        flag_2 = true;
                    }

                    if (!TextUtils.isEmpty(et_password_confirm.getText())) {
                        et_password_confirm.setSelection(et_password_confirm.getText().length());
                    }
                    break;

            }
        }
    }

    private boolean flag_1 = false,flag_2 = false;

    @FocusChange(R.id.et_account)
    void focusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (et_account == null) {
                return;
            }
            String account = et_account.getText().toString();
            if (TextUtils.isEmpty(account)) {
                return;
            }
            boolean isOk = true;

            if (account.matches(ConstValue.checkAccout)) {
                if (account.matches(ConstValue.checkAccout_2)) {
                    isOk = false;
                }
                if (account.matches(ConstValue.checkAccout_3)) {
                    isOk = false;
                }
                if (account.matches(ConstValue.checkAccout_4)) {
                    isOk = false;
                }
            } else {
                isOk = false;
            }
            if (isOk) {
                ApiRequest.isAccountExist(account, "RegistFragment_isAccountExist");
            } else {
                tv_regist_prompt_1.setVisibility(View.VISIBLE);
                tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
            }
        }
    }

    @TextChange({R.id.et_account, R.id.et_password, R.id.et_password_confirm})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()) {
            case R.id.et_account:
                if(tv_regist_prompt_1.getVisibility() == View.VISIBLE){
                    tv_regist_prompt_1.setText("");
                    tv_regist_prompt_1.setVisibility(View.INVISIBLE);
                }
                String account = et_account.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    iv_account_clear.setVisibility(View.GONE);
                    return;
                }
                iv_account_clear.setVisibility(View.VISIBLE);

                break;
            case R.id.et_password:
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    tv_regist_prompt_2.setVisibility(View.INVISIBLE);
                    return;
                }
                if(tv_regist_prompt_2.getVisibility() == View.VISIBLE){
                    tv_regist_prompt_2.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_password_confirm:
                String password_confirm = et_password_confirm.getText().toString();
                if (TextUtils.isEmpty(password_confirm)) {
                    tv_regist_prompt_3.setVisibility(View.INVISIBLE);
                    return;
                }
                if(tv_regist_prompt_3.getVisibility() == View.VISIBLE){
                    tv_regist_prompt_3.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

}
