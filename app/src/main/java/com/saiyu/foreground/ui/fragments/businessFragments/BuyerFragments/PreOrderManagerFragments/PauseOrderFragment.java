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
import com.saiyu.foreground.https.response.OrderPauseRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_pauseorder)
public class PauseOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_2,tv_1;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;

    private String orderId;

    public static PauseOrderFragment newInstance(Bundle bundle) {
        PauseOrderFragment_ fragment = new PauseOrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderStopInfo(orderId,"PauseOrderFragment_orderStopInfo",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("暂停订单");
        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("PauseOrderFragment_orderStopInfo")){
            OrderPauseRet ret = (OrderPauseRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_1.setText(ret.getData().getOrderReceiveCount());
            tv_2.setText(ret.getData().getOrderReceiveMoney());
        } else if(method.equals("PauseOrderFragment_orderStopP")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"暂停成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }

    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                ApiRequest.orderStopP(orderId,"PauseOrderFragment_orderStopP",pb_loading);
                break;
        }
    }
}
