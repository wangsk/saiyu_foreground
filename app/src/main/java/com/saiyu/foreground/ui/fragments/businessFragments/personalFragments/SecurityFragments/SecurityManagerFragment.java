package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_security_manager)
public class SecurityManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content,tv_security_limit,tv_security_limit_2,tv_security_nolimit;
    @ViewById
    Switch sw_security;
    @ViewById
    Button btn_title_back;
    @ViewById
    RelativeLayout rl_bind_mobile,rl_revise_psw,rl_login_record,rl_security;
    private String Mobile,OrderMoneyLimitByDay,OrderCountLimitByDay;
    private boolean isUserLimit;

    public static SecurityManagerFragment newInstance(Bundle bundle) {
        SecurityManagerFragment_ fragment = new SecurityManagerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        //每次页面显示的时候即时的获取一些页面最新信息
        ApiRequest.getSmallAccountInfoLogin("SecurityManagerFragment_getSmallAccountInfoLogin",pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SecurityManagerFragment_getSmallAccountInfoLogin")) {
            AccountInfoLoginRet ret = (AccountInfoLoginRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            Mobile = ret.getData().getMobile();
            isUserLimit = ret.getData().isUserLimit();
            if(isUserLimit){
                OrderMoneyLimitByDay = ret.getData().getOrderMoneyLimitByDay();
                OrderCountLimitByDay = ret.getData().getOrderCountLimitByDay();
                tv_security_limit.setText("每日下单累计金额 " + OrderMoneyLimitByDay+"元");
                tv_security_limit_2.setText("每日下单累计笔数 " + OrderCountLimitByDay+"笔");
                tv_security_limit.setVisibility(View.VISIBLE);
                tv_security_limit_2.setVisibility(View.VISIBLE);
                tv_security_nolimit.setVisibility(View.GONE);
                sw_security.setChecked(true);
            } else {
                tv_security_nolimit.setVisibility(View.VISIBLE);
                tv_security_limit.setVisibility(View.GONE);
                tv_security_limit_2.setVisibility(View.GONE);
                sw_security.setChecked(false);
            }
        }
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("安全管理");

    }

    @Click({R.id.btn_title_back,R.id.rl_bind_mobile,R.id.rl_revise_psw,R.id.rl_login_record,R.id.rl_security})
    void onClick(View view) {
        if(view.getId() == R.id.btn_title_back){
            getActivity().finish();
            return;
        }
        Bundle bundle_3 = new Bundle();
        switch (view.getId()) {
            case R.id.rl_bind_mobile://手机绑定
                Intent intent = new Intent(mContext,ContainerActivity_.class);
                bundle_3.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                intent.putExtras(bundle_3);
                mContext.startActivity(intent);
                break;
            case R.id.rl_revise_psw://修改密码
                ResetPswFragment resetPswFragment =  ResetPswFragment.newInstance(bundle_3);
                start(resetPswFragment);
                break;
            case R.id.rl_login_record://登录记录
                LoginRecordFragment loginRecordFragment =  LoginRecordFragment.newInstance(bundle_3);
                start(loginRecordFragment);
                break;
            case R.id.rl_security://安全限制
                if(TextUtils.isEmpty(Mobile)){
                    String text;
                    if(isUserLimit){
                        text = "取消安全配置需要绑定手机，是否绑定手机?";
                    } else {
                        text = "安全配置需要绑定手机，是否绑定手机?";
                    }
                    DialogUtils.showDialog(getActivity(),"提示", text, "稍后绑定", "绑定手机", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Bundle bundle = new Bundle();
                            final Intent intent = new Intent(mContext,ContainerActivity_.class);
                            bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    });
                    return;
                }

                if(isUserLimit){
                    //取消安全限制
                    bundle_3.putString("Mobile",Mobile);
                    bundle_3.putString("orderMoney","0");
                    bundle_3.putString("orderCount","0");
                    SecurityCommitFragment securityCommitFragment = SecurityCommitFragment.newInstance(bundle_3);
                    start(securityCommitFragment);
                } else {
                    //设置安全限制
                    bundle_3.putString("Mobile",Mobile);
                    SecuritySetFragment securitySetFragment =  SecuritySetFragment.newInstance(bundle_3);
                    start(securitySetFragment);
                }
                break;

        }
    }

}
