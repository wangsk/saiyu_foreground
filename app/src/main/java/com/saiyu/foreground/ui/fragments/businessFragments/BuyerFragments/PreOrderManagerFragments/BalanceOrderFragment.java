package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.OrderSettlementRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_balanceorder)
public class BalanceOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_money;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    CheckBox cb_1,cb_2,cb_3;
    @ViewById
    ProgressBar pb_loading;
    private String orderId;

    public static BalanceOrderFragment newInstance(Bundle bundle) {
        BalanceOrderFragment_ fragment = new BalanceOrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderSettlement(orderId,"BalanceOrderFragment_orderSettlement",pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("BalanceOrderFragment_orderSettlement")) {
            OrderSettlementRet ret = (OrderSettlementRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().getUnConfirmCount() == 0){
                cb_1.setChecked(true);
            } else {
                cb_1.setChecked(false);
            }
            if(ret.getData().getAppealCount() == 0){
                cb_2.setChecked(true);
            } else {
                cb_2.setChecked(false);
            }
            if(ret.getData().getRechargeCount() == 0){
                cb_3.setChecked(true);
            } else {
                cb_3.setChecked(false);
            }
            tv_money.setText(ret.getData().getRefundMoney());

        } else if (method.equals("BalanceOrderFragment_orderSettlementP")) {
            BooleanRet ret = (BooleanRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"结算成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("结算订单");
        Bundle bundle = getArguments();
        if(bundle != null){
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
                if(!cb_1.isChecked()){
                    Toast.makeText(mContext,"不得含有未确认的充值订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!cb_2.isChecked()){
                    Toast.makeText(mContext,"不得含有维权中的充值订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!cb_3.isChecked()){
                    Toast.makeText(mContext,"不得含有正在充值的订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiRequest.orderSettlementP(orderId,"BalanceOrderFragment_orderSettlementP",pb_loading);
                break;
        }
    }
}
