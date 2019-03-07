package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_active_buyer)
public class ActiveBuyerFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_agree,btn_close;

    public static ActiveBuyerFragment newInstance(Bundle bundle) {
        ActiveBuyerFragment_ fragment = new ActiveBuyerFragment_();
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
        tv_title_content.setText("点券充值(买家)服务协议");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("ActiveBuyerFragment_activeSeller")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"激活成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_close,R.id.btn_agree})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                case R.id.btn_close:
                    getActivity().finish();
                    break;
                case R.id.btn_agree:
                    ApiRequest.activeBuyer("ActiveBuyerFragment_activeSeller",pb_loading);
                    break;
            }
        }
    }
}
