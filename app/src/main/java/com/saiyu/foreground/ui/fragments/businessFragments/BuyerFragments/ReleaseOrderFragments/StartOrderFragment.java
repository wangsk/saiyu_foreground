package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

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
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.PauseOrderFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.PauseOrderFragment_;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_pauseorder)
public class StartOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_2,tv_1;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;

    private String orderId;

    public static StartOrderFragment newInstance(Bundle bundle) {
        StartOrderFragment_ fragment = new StartOrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderStartInfo(orderId,"StartOrderFragment_orderStartInfo",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("启动订单");
        btn_confirm.setText("确认启动");
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
        if(method.equals("StartOrderFragment_orderStartInfo")){
            OrderPauseRet ret = (OrderPauseRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_1.setText(ret.getData().getOrderReceiveCount());
            tv_2.setText(ret.getData().getOrderReceiveMoney());
        } else if(method.equals("StartOrderFragment_orderStartP")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"启动成功",Toast.LENGTH_SHORT).show();
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
                ApiRequest.orderStartP(orderId,"StartOrderFragment_orderStartP",pb_loading);
                break;
        }
    }
}
