package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments.PhoneBindFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.math.BigDecimal;

@EFragment(R.layout.fragment_bind_phone_status)
public class BindPhoneStatusFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    TextView tv_title_content,tv_bind_status;
    @ViewById
    Button btn_title_back,btn_bind,btn_replace_phone;
    @ViewById
    ProgressBar pb_loading;
    private String Mobile;
    private boolean isFaceIdentity;
    private String RealName,IDNum;
    public static BindPhoneStatusFragment newInstance(Bundle bundle) {
        BindPhoneStatusFragment_ fragment = new BindPhoneStatusFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getSmallAccountInfoLogin("BindPhoneStatusFragment_getSmallAccountInfoLogin",pb_loading);

    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("绑定手机管理");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("BindPhoneStatusFragment_getSmallAccountInfoLogin")) {
            AccountInfoLoginRet ret = (AccountInfoLoginRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            Mobile = ret.getData().getMobile();
            RealName = ret.getData().getRealName();
            IDNum = ret.getData().getIDNum();
            if(TextUtils.isEmpty(Mobile)){
                tv_bind_status.setText("未绑定");
                btn_bind.setText("立即绑定");
                btn_replace_phone.setText("返回");
            } else {
                //已绑定
                btn_bind.setText("解除绑定");
                tv_bind_status.setText(Mobile);
                btn_replace_phone.setText("更换手机");
            }

            if(!TextUtils.isEmpty(ret.getData().getTotalMoney())){
                BigDecimal bigDecimal = new BigDecimal(ret.getData().getTotalMoney());
                BigDecimal bigDecimal_1000 = new BigDecimal(1000);
                switch (bigDecimal.compareTo(bigDecimal_1000)){
                    case -1://小于
                        isFaceIdentity = true;
                        break;
                    case 1://大于
                    case 0://等于
                        isFaceIdentity = false;
                        break;
                }
            }

        }
    }

    @Click({R.id.btn_title_back,R.id.btn_bind,R.id.btn_replace_phone})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_bind:
                Bundle bundle_3 = new Bundle();
                bundle_3.putString("Mobile",Mobile);
                if(TextUtils.isEmpty(Mobile)){
                    if(TextUtils.isEmpty(RealName) || TextUtils.isEmpty(IDNum)){
                        DialogUtils.showDialog( getActivity(),"提示", "绑定手机需要补填信息，是否补填信息", "稍后补填", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Bundle bundle = new Bundle();
                                final Intent intent = new Intent(mContext,ContainerActivity_.class);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance(bundle_3);
                    start(bindPhoneFragment);
                } else {
                    //已绑定
                    bundle_3.putBoolean("isFaceIdentity",isFaceIdentity);
                    if(isFaceIdentity){
                        bundle_3.putString("RealName",RealName);
                        bundle_3.putString("IDNum",IDNum);
                    }
                    UnBindPhoneTypeFragment unBindPhoneTypeFragment = UnBindPhoneTypeFragment.newInstance(bundle_3);
                    start(unBindPhoneTypeFragment);
                }

                break;
            case R.id.btn_replace_phone:
                if(TextUtils.isEmpty(Mobile)){
                    getActivity().finish();
                } else {
                    //已绑定
                    Bundle bundle = new Bundle();
                    bundle.putString("Mobile",Mobile);
                    ReplacePhoneFragment replacePhoneFragment = ReplacePhoneFragment.newInstance(bundle);
                    start(replacePhoneFragment);
                }
                break;
        }

    }
}
