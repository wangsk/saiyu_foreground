package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

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
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_cancelorder)
public class CancelOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_ordercancel;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    private String orderId;

    public static CancelOrderFragment newInstance(Bundle bundle) {
        CancelOrderFragment_ fragment = new CancelOrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("CancelOrderFragment_orderCancel")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"取消成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("取消订单");
        Bundle bundle = getArguments();
        if(bundle != null){
            tv_ordercancel.setText(bundle.getString("ReservePrice"));
            orderId = bundle.getString("orderId");
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                ApiRequest.orderCancel(orderId,"CancelOrderFragment_orderCancel",pb_loading);
                break;
        }
    }
}
