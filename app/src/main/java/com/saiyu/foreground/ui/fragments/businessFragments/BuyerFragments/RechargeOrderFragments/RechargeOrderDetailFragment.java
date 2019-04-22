package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.RechargeOrderInfoRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_recharge_orderdetail)
public class RechargeOrderDetailFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_recharge_ordernum,tv_ordernum,tv_creattime,tv_completetime,tv_rechargetime,tv_autoconfirmtime,tv_ordercount,
            tv_successcount,tv_successprice,tv_confirm_type,tv_averagetime,tv_orderstatus,
            tv_recharge_product,tv_producttype,tv_rechargenum,tv_time_online,tv_orderPsw,tv_orderlimit,tv_betweentime,
            tv_pic,tv_replaceconfirm,tv_contacttype,tv_rechargeremark;

    private String orderReceiveId;

    public static RechargeOrderDetailFragment newInstance(Bundle bundle) {
        RechargeOrderDetailFragment_ fragment = new RechargeOrderDetailFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            orderReceiveId = bundle.getString("orderReceiveId");
        }
        if(!TextUtils.isEmpty(orderReceiveId)){
            ApiRequest.orderReceiveInfo(orderReceiveId,"BuyerOrderDetailFragment_orderInfo",pb_loading);
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("订单详情");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("BuyerOrderDetailFragment_orderInfo")) {
            RechargeOrderInfoRet ret = (RechargeOrderInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_recharge_ordernum.setText(ret.getData().getReceiveOrderNum());//充值订单号
            tv_ordernum.setText(ret.getData().getOrderNum());//预定订单号
            tv_creattime.setText(ret.getData().getCreateTime());//接单时间
            tv_completetime.setText(ret.getData().getFinishTime());//完成时间
            tv_rechargetime.setText(ret.getData().getRechargeTime());//充值时间
            tv_autoconfirmtime.setText(ret.getData().getAutoConfirmTime());//自动确认时间
            tv_ordercount.setText(ret.getData().getReserveQBCount()+"Q币");//接单数量
            tv_successcount.setText(ret.getData().getSuccQBCount()+"Q币");//成功数量
            tv_successprice.setText(ret.getData().getSuccMoney()+"元");//成功金额
            tv_confirm_type.setText(ret.getData().getConfirmType());//确认方式
            tv_averagetime.setText(ret.getData().getAverageConfirmTime());//平均确认时间
            tv_orderstatus.setText(ret.getData().getReceiveOrderStatus());//订单状态
            tv_recharge_product.setText(ret.getData().getProductName());//充值产品
            tv_producttype.setText(ret.getData().getProductTypeName());//产品类型
            tv_rechargenum.setText(ret.getData().getReserveAccount());//充值号码
            tv_time_online.setText(ret.getData().getOnlineTime());//在线时间
            if(TextUtils.isEmpty(ret.getData().getOrderPwd())){//订单加密
                tv_orderPsw.setText("否");
            } else {
                tv_orderPsw.setText(ret.getData().getOrderPwd());
            }
            tv_orderlimit.setText(ret.getData().getOnceMinCount());//单次限制
            tv_betweentime.setText(ret.getData().getOrderInterval()+"分钟");//接单间隔时间
            tv_pic.setText(ret.getData().getIsPicConfirm());//验图确认
            tv_replaceconfirm.setText(ret.getData().getIsAgentConfirm());//客服代理确认
            tv_contacttype.setText("手机"+ret.getData().getContactMobile()+";qq"+ret.getData().getContactQQ()+";\n"+ret.getData().getIsAllowShowContactStr());//联系方式
            tv_rechargeremark.setText(ret.getData().getRemarks());//充值留言


        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back || view.getId() == R.id.btn_confirm) {
            getFragmentManager().popBackStack();
        }
    }
}
