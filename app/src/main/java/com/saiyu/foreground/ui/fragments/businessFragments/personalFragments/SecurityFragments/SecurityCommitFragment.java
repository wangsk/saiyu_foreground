package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_bind_phone)
public class SecurityCommitFragment extends BaseFragment  implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_phone, tv_msg_count, et_msg, tv_response_msg;
    @ViewById
    Button btn_title_back, btn_bind;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    EditText et_mobile;
    private static CountDownTimerUtils countDownTimerUtils;
    private String Mobile,orderMoney,orderCount;
    public static SecurityCommitFragment newInstance(Bundle bundle) {
        SecurityCommitFragment_ fragment = new SecurityCommitFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("安全管理");
        btn_bind.setText("确认保存");
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Mobile = bundle.getString("Mobile");
            orderMoney = bundle.getString("orderMoney");
            orderCount = bundle.getString("orderCount");
            if(!TextUtils.isEmpty(Mobile)){
                et_mobile.setText(Mobile);
                et_mobile.setClickable(false);
                et_mobile.setFocusable(false);
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SecurityCommitFragment_securitySet")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Bundle bundle = new Bundle();
                Intent intent = new Intent(mContext,ContainerActivity_.class);
                if(orderCount.equals("0") && orderMoney.equals("0")){
                    Toast.makeText(mContext,"取消安全限制成功",Toast.LENGTH_SHORT).show();
                    bundle.putBoolean("IsUserLimit",false);
                } else {
                    Toast.makeText(mContext,"设置安全限制成功",Toast.LENGTH_SHORT).show();
                    bundle.putBoolean("IsUserLimit",true);
                }
                bundle.putString("Mobile",Mobile);
                bundle.putString("OrderMoneyLimitByDay",orderMoney);
                bundle.putString("OrderCountLimitByDay",orderCount);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SecurityManagerFragmentTag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_bind, R.id.tv_msg_count})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_bind:
                String checkCode = et_msg.getText().toString();
                if(TextUtils.isEmpty(checkCode)){
                    tv_response_msg.setText("*请输入验证码");
                    tv_response_msg.setVisibility(View.VISIBLE);
                    return;
                }

                ApiRequest.securitySet(checkCode,orderMoney,orderCount,"SecurityCommitFragment_securitySet",pb_loading);

                break;
            case R.id.tv_msg_count:
                if(!TextUtils.isEmpty(Mobile)){
                    countDownTimerUtils.start();
                    ApiRequest.sendVcode(Mobile,"",countDownTimerUtils);
                }
                break;
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
