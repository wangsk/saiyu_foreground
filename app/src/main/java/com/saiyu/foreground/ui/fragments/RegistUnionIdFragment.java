package com.saiyu.foreground.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.ui.activitys.ForgotPswActivity_;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.activity_regist_unionid_layout)
public class RegistUnionIdFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content, tv_regist_prompt_1, tv_regist_prompt_2, tv_regist_prompt_3,tv_forgot_psw,tv_login_response_msg;
    @ViewById
    Button btn_title_back,btn_regist_old,btn_regist_new;
    @ViewById
    RelativeLayout rl_newaccount,rl_oldaccount;
    @ViewById
    LinearLayout ll_new,ll_old;
    @ViewById
    EditText et_account_new,et_password_new,et_password_confirm,et_account_old,et_password_old;
    @ViewById
    ImageView iv_psw;
    String type,unionID;

    public static RegistUnionIdFragment newInstance(Bundle bundle) {
        RegistUnionIdFragment_ fragment = new RegistUnionIdFragment_();
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
        tv_title_content.setText("会员登录");
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            unionID = bundle.getString("unionID");
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("RegistUnionIdFragment_isAccountExist")) {
            IsAccountExistRet ret = (IsAccountExistRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isExist()) {
                tv_regist_prompt_1.setText("*账号已存在");
            } else {
                tv_regist_prompt_1.setText("*账号可用");
            }

        } else if (method.equals("RegistUnionIdFragment_regist")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRegist", true);
            SuccessFindPswFragment successFindPswFragment = SuccessFindPswFragment.newInstance(bundle);
            start(successFindPswFragment);

        } else if (method.equals("RegistUnionIdFragment_bind")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("bind", true);
            SuccessFindPswFragment successFindPswFragment = SuccessFindPswFragment.newInstance(bundle);
            start(successFindPswFragment);

        }

    }

    @Click({R.id.btn_title_back, R.id.rl_newaccount,R.id.rl_oldaccount,R.id.btn_regist_old,R.id.btn_regist_new,R.id.iv_psw,R.id.tv_forgot_psw})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.tv_forgot_psw:
                    mContext.startActivity(new Intent(mContext,ForgotPswActivity_.class));
                    break;
                case R.id.iv_psw:
                    //从密码不可见模式变为密码可见模式
                    et_password_old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    if(!TextUtils.isEmpty(et_password_old.getText())){
                        et_password_old.setSelection(et_password_old.getText().length());
                    }
                    break;
                case R.id.rl_newaccount:
                    ll_new.setVisibility(View.VISIBLE);
                    ll_old.setVisibility(View.GONE);
                    break;
                case R.id.rl_oldaccount:
                    ll_new.setVisibility(View.GONE);
                    ll_old.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_regist_new:
                    String account = et_account_new.getText().toString();
                    if (TextUtils.isEmpty(account)) {
                        tv_regist_prompt_1.setText("*请输入账号");
                        tv_regist_prompt_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                        return;
                    }
                    if (account.equals("*账号已存在")) {
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
                        tv_regist_prompt_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
                        return;
                    }

                    String password = et_password_new.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        tv_regist_prompt_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_2.setText("*请输入密码");
                        return;
                    }
                    if (password.length() < 6) {
                        tv_regist_prompt_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_2.setText("*密码不能小于6位");
                        return;
                    }
                    if (password.length() > 32) {
                        tv_regist_prompt_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_2.setText("*密码不能大于32位");
                        return;
                    }

                    String password_confirm = et_password_confirm.getText().toString();
                    if (TextUtils.isEmpty(password_confirm)) {
                        tv_regist_prompt_3.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_3.setText("*请再次输入密码");
                        return;
                    }
                    if (!password_confirm.equals(et_password_new.getText().toString())) {
                        tv_regist_prompt_3.setTextColor(mContext.getResources().getColor(R.color.blue));
                        tv_regist_prompt_3.setText("*两次输入不一致");
                        return;
                    }

                    ApiRequest.unionIDRegist(account, password, type,unionID,"RegistUnionIdFragment_regist");
                    break;
                case R.id.btn_regist_old:
                    String userName = et_account_old.getText().toString().trim();
                    String userPassword = et_password_old.getText().toString().trim();
                    if(TextUtils.isEmpty(userName)){
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("请输入用户名");
                        return;
                    }
                    if(TextUtils.isEmpty(userPassword)){
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("请输入密码");
                        return;
                    }
                    ApiRequest.unionIDRegist(userName, userPassword, type,unionID,"RegistUnionIdFragment_bind");

                    break;

            }
        }
    }

    @TextChange({R.id.et_account_new, R.id.et_password_new, R.id.et_password_confirm})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()) {
            case R.id.et_account_new:
                String account = et_account_new.getText().toString();
                tv_regist_prompt_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                if (TextUtils.isEmpty(account)) {
                    tv_regist_prompt_1.setText("*请输入账号");
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
                    tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
                } else {
                    tv_regist_prompt_1.setText("*账号格式正确");
                }

                break;
            case R.id.et_password_new:
                String password = et_password_new.getText().toString();
                tv_regist_prompt_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                if (TextUtils.isEmpty(password)) {
                    tv_regist_prompt_2.setText("*请输入密码");
                    return;
                }
                if (password.length() < 6) {
                    tv_regist_prompt_2.setText("*密码不能小于6位");
                    return;
                }
                if (password.length() > 32) {
                    tv_regist_prompt_2.setText("*密码不能大于32位");
                    return;
                }
                tv_regist_prompt_2.setText("*密码可用");
                break;
            case R.id.et_password_confirm:
                String password_confirm = et_password_confirm.getText().toString();
                tv_regist_prompt_3.setTextColor(mContext.getResources().getColor(R.color.blue));
                if (TextUtils.isEmpty(password_confirm)) {
                    tv_regist_prompt_3.setText("*请再次输入密码");
                    return;
                }
                if (password_confirm.equals(et_password_new.getText().toString())) {
                    tv_regist_prompt_3.setText("*密码可用");
                } else {
                    tv_regist_prompt_3.setText("*两次输入不一致");
                }
                break;
            case R.id.et_account_old:
                String text = s.toString();
                if(TextUtils.isEmpty(text)){
                    tv_login_response_msg.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @FocusChange(R.id.et_account_new)
    void focusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (et_account_new == null) {
                return;
            }
            String account = et_account_new.getText().toString();
            tv_regist_prompt_1.setTextColor(mContext.getResources().getColor(R.color.blue));
            if (TextUtils.isEmpty(account)) {
                tv_regist_prompt_1.setText("*请输入账号");
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
                ApiRequest.isAccountExist(account, "RegistUnionIdFragment_isAccountExist");
            } else {
                tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
            }
        }
    }
}
