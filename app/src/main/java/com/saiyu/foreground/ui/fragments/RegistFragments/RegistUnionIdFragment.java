package com.saiyu.foreground.ui.fragments.RegistFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.FindPswFragments.SuccessFindPswFragment;
import com.saiyu.foreground.ui.views.DropdownLayout;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

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
    ImageView iv_psw,iv_jiantou_1,iv_jiantou_2;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    DropdownLayout dropdown_layout_new,dropdown_layout_old;
    private String type,unionId,openId;
    private String accountExist = "*账号已存在";

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
            unionId = bundle.getString("unionId");
            openId = bundle.getString("openId");
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
                tv_regist_prompt_1.setText(accountExist);
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

    private boolean flag_1 = false;

    @Click({R.id.btn_title_back, R.id.rl_newaccount,R.id.rl_oldaccount,R.id.btn_regist_old,R.id.btn_regist_new,R.id.iv_psw,R.id.tv_forgot_psw})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.tv_forgot_psw:
                    Bundle bundle_1 = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle_1.putInt(ContainerActivity.FragmentTag, ContainerActivity.ForgotPswFragmentTag);
                    intent.putExtras(bundle_1);
                    mContext.startActivity(intent);
                    break;
                case R.id.iv_psw:
                    if(flag_1){
                        et_password_old.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_psw.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.yanjing));
                        flag_1 = false;
                    } else {
                        //从密码不可见模式变为密码可见模式
                        et_password_old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_psw.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.eye_2));
                        flag_1 = true;
                    }

                    if(!TextUtils.isEmpty(et_password_old.getText())){
                        et_password_old.setSelection(et_password_old.getText().length());
                    }
                    break;
                case R.id.rl_newaccount:
                    if(ll_new.getVisibility() == View.GONE){
                        dropdown_layout_new.toggle(ll_new,true);
                        ll_old.setVisibility(View.GONE);
                        dropdown_layout_old.toggle(ll_old,false);
                        iv_jiantou_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xxjt));
                        iv_jiantou_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.jiantou));
                    }
//                    ll_new.setVisibility(View.VISIBLE);
//                    ll_old.setVisibility(View.GONE);
                    break;
                case R.id.rl_oldaccount:
                    if(ll_old.getVisibility() == View.GONE){
                        dropdown_layout_old.toggle(ll_old,true);
                        ll_new.setVisibility(View.GONE);
                        dropdown_layout_new.toggle(ll_new,false);
                        iv_jiantou_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.jiantou));
                        iv_jiantou_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xxjt));
                    }
//                    ll_new.setVisibility(View.GONE);
//                    ll_old.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_regist_new:
                    String account = et_account_new.getText().toString();
                    if (TextUtils.isEmpty(account)) {
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
                        tv_regist_prompt_1.setText("*账号由5-20位字母加数字组成，首位为字母");
                        Toast.makeText(mContext,"账号格式错误",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password = et_password_new.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        tv_regist_prompt_2.setText("*请输入密码");
                        Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6) {
                        tv_regist_prompt_2.setText("*密码不能小于6位");
                        Toast.makeText(mContext,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() > 32) {
                        tv_regist_prompt_2.setText("*密码不能大于32位");
                        Toast.makeText(mContext,"密码不能大于32位",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password_confirm = et_password_confirm.getText().toString();
                    if (TextUtils.isEmpty(password_confirm)) {
                        tv_regist_prompt_3.setText("*请再次输入密码");
                        Toast.makeText(mContext,"请再次输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password_confirm.equals(et_password_new.getText().toString())) {
                        tv_regist_prompt_3.setText("*两次输入不一致");
                        Toast.makeText(mContext,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiRequest.unionIDRegist(account, password, type,unionId,openId,"RegistUnionIdFragment_regist",pb_loading);
                    break;
                case R.id.btn_regist_old:
                    String userName = et_account_old.getText().toString().trim();
                    String userPassword = et_password_old.getText().toString().trim();
                    if(TextUtils.isEmpty(userName)){
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("请输入用户名");
                        Toast.makeText(mContext,"请输入用户名",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(userPassword)){
                        tv_login_response_msg.setVisibility(View.VISIBLE);
                        tv_login_response_msg.setText("请输入密码");
                        Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiRequest.unionIDRegist(userName, userPassword, type,unionId,openId,"RegistUnionIdFragment_bind",pb_loading);

                    break;

            }
        }
    }

    @TextChange({R.id.et_account_new, R.id.et_password_new, R.id.et_password_confirm})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()) {
            case R.id.et_account_new:
                String account = et_account_new.getText().toString();
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
                if(password.equals(et_password_confirm.getText().toString())){
                    tv_regist_prompt_3.setText("*密码可用");
                } else {
                    tv_regist_prompt_3.setText("*两次输入不一致");
                }
                break;
            case R.id.et_password_confirm:
                String password_confirm = et_password_confirm.getText().toString();
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
