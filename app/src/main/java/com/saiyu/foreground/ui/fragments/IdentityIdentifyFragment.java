package com.saiyu.foreground.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_identity_identify_layout)
public class IdentityIdentifyFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_account, tv_identity_prompt, tv_name_prompt;
    @ViewById
    EditText et_name, et_identity;
    @ViewById
    Button btn_title_back, btn_next;
    @ViewById
    ProgressBar pb_loading;
    private String account;
    private AccountInfoNoLoginRet accountInfoNoLoginRet;

    public static IdentityIdentifyFragment newInstance(Bundle bundle) {
        IdentityIdentifyFragment_ fragment = new IdentityIdentifyFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        if (loadingReciver == null) {
            loadingReciver = new LoadingReciver();
            IntentFilter filter = new IntentFilter("sy_close_progressbar");
            mContext.registerReceiver(loadingReciver, filter);
        }
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("找回密码");
        Bundle bundle = getArguments();
        if (bundle != null) {
            account = bundle.getString("account");
            accountInfoNoLoginRet = (AccountInfoNoLoginRet)bundle.getSerializable("AccountInfoNoLoginRet");
        }
        tv_account.setText("赛鱼账号: "+account);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("IdentityIdentifyFragment_searchPswIdCard")) {
            pb_loading.setVisibility(View.GONE);
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
                LogUtils.print("IdentityIdentifyFragment_searchPswIdCard == ");
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getFragmentManager().popBackStack();
                    break;
                case R.id.btn_next:
                    if(et_name == null || et_identity == null){
                        return;
                    }
                    String name = et_name.getText().toString();
                    String identity = et_identity.getText().toString();
                    if(TextUtils.isEmpty(name)){
                        tv_name_prompt.setVisibility(View.VISIBLE);
                        tv_name_prompt.setText("*请输入您补填信息时的姓名");
                        return;
                    }

                    if(TextUtils.isEmpty(identity)){
                        tv_identity_prompt.setVisibility(View.VISIBLE);
                        tv_identity_prompt.setText("*请输入您补填信息时的身份证号码");
                        return;
                    }

                    if(accountInfoNoLoginRet != null && accountInfoNoLoginRet.getData() != null){
                        if(!name.equals(accountInfoNoLoginRet.getData().getRealName())){
                            tv_name_prompt.setVisibility(View.VISIBLE);
                            tv_name_prompt.setText("*请输入您补填信息时的姓名");
                            return;
                        }
                        if(!identity.equals(accountInfoNoLoginRet.getData().getIDNum())){
                            tv_identity_prompt.setVisibility(View.VISIBLE);
                            tv_identity_prompt.setText("*请输入您补填信息时的身份证号码");
                            return;
                        }
                    }

                    pb_loading.setVisibility(View.VISIBLE);
                    ApiRequest.searchPswIdCard(account,name,identity,"IdentityIdentifyFragment_searchPswIdCard");

                    break;

            }
        }
    }

    @TextChange({R.id.et_name, R.id.et_identity})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        switch (hello.getId()){
            case R.id.et_name:
                if(TextUtils.isEmpty(s.toString())){
                    tv_name_prompt.setVisibility(View.INVISIBLE);
                } else {
                    if(accountInfoNoLoginRet != null && accountInfoNoLoginRet.getData() != null){
                        if(!s.toString().equals(accountInfoNoLoginRet.getData().getRealName())){
                            tv_name_prompt.setVisibility(View.VISIBLE);
                            tv_name_prompt.setText("*请输入您补填信息时的姓名");
                        } else {
                            tv_name_prompt.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                break;
            case R.id.et_identity:
                if(TextUtils.isEmpty(s.toString())){
                    tv_identity_prompt.setVisibility(View.INVISIBLE);
                } else {
                    if(accountInfoNoLoginRet != null && accountInfoNoLoginRet.getData() != null){
                        if(!s.toString().equals(accountInfoNoLoginRet.getData().getIDNum())){
                            tv_identity_prompt.setVisibility(View.VISIBLE);
                            tv_identity_prompt.setText("*请输入您补填信息时的身份证号码");
                        } else {
                            tv_identity_prompt.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (loadingReciver != null) {
            mContext.unregisterReceiver(loadingReciver);
            loadingReciver = null;
        }
    }

    private LoadingReciver loadingReciver;

    private class LoadingReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "sy_close_progressbar":
                    if(pb_loading == null){
                        return;
                    }
                    pb_loading.setVisibility(View.GONE);
                    break;
            }
        }
    }

}
