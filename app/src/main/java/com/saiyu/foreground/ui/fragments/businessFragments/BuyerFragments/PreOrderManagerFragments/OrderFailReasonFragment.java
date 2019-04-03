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
import com.saiyu.foreground.https.response.OrderFailReasonRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_order_failreason)
public class OrderFailReasonFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_order_fail_reason;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    private String orderId;

    public static OrderFailReasonFragment newInstance(Bundle bundle) {
        OrderFailReasonFragment_ fragment = new OrderFailReasonFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderFailReason(orderId,"OrderFailReasonFragment_orderFailReason",pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("OrderFailReasonFragment_orderFailReason")) {
            OrderFailReasonRet ret = (OrderFailReasonRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_order_fail_reason.setText(ret.getData().getAuditRemarks());
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("失败原因");
        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_confirm:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
