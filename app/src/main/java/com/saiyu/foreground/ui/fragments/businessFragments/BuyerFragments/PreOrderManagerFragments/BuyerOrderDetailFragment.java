package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BuyerOrderInfoRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_buyer_orderdetail)
public class BuyerOrderDetailFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_ordernum,tv_paytype,tv_recharge_product,tv_producttype,tv_rechargenum,tv_ordercount,tv_orderprice,
            tv_orderremark,tv_time_online,tv_orderlimit,tv_betweentime,tv_replaceconfirm,
            tv_contacttype,tv_rechargeremark,tv_releasetime,tv_latertime,tv_completnum,tv_confirm_type,tv_averagetime,
            tv_orderstatus,tv_reviewstatus,tv_completetime,tv_balancetime,tv_balancemoney,tv_penalty,tv_rechargeorders
            ,tv_cancel,tv_canceltime,tv_refunds,tv_orderPsw,tv_pic;
    private String orderId;

    public static BuyerOrderDetailFragment newInstance(Bundle bundle) {
        BuyerOrderDetailFragment_ fragment = new BuyerOrderDetailFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }
        if(!TextUtils.isEmpty(orderId)){
            ApiRequest.orderInfo(orderId,"BuyerOrderDetailFragment_orderInfo",pb_loading);
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
            BuyerOrderInfoRet ret = (BuyerOrderInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_ordernum.setText(ret.getData().getOrderNum());//预定订单号
            tv_paytype.setText("全额付款预定");//付款方式
            tv_recharge_product.setText(ret.getData().getProductName());//充值产品
            tv_producttype.setText(ret.getData().getProductType());//产品类型
            tv_rechargenum.setText(ret.getData().getReserveAccount());//充值号码
            tv_ordercount.setText(ret.getData().getReserveQBCount()+"Q币");//预定数量
            tv_orderprice.setText(ret.getData().getReservePrice() + "元");//预定出价
            tv_orderremark.setText(ret.getData().getReserveTitle());//预定附言
            tv_time_online.setText(ret.getData().getOnlineTime());//在线时间
            if(TextUtils.isEmpty(ret.getData().getOrderPwd())){
                tv_orderPsw.setText("无");//订单加密
            } else {
                tv_orderPsw.setText(ret.getData().getOrderPwd());//订单加密
            }

            //单次限制
            float LessChargeDiscount = ret.getData().getLessChargeDiscount();
            float OnceMinCount = ret.getData().getOnceMinCount();
            if(OnceMinCount == 0){
                tv_orderlimit.setText("单次数量不限制");
            } else {
                if(LessChargeDiscount == 1){
                    tv_orderlimit.setText(OnceMinCount+"Q币 少充按原价");
                } else {
                    tv_orderlimit.setText(OnceMinCount+"Q币 少充则=" + LessChargeDiscount*100 + "%");
                }
            }
//            tv_orderlimit.setText(ret.getData().getOnceMinCount());//单次限制
            tv_betweentime.setText(ret.getData().getOrderInterval()+"分钟");//接单间隔时间
            if(ret.getData().getIsPicConfirm() == 0){//支持验图确认
                tv_pic.setText("否");
            } else {
                tv_pic.setText("是");
            }
            tv_replaceconfirm.setText(ret.getData().getIsAgentConfirmStr());//客服代理确认

            tv_contacttype.setText("手机"+ret.getData().getContactMobile()+";qq"+ret.getData().getContactQQ()+";\n"+ret.getData().getIsAllowShowContactStr());//联系方式

            tv_rechargeremark.setText(ret.getData().getRemarks());//充值留言
            tv_releasetime.setText(ret.getData().getCreateTime());//发布时间
            tv_latertime.setText(ret.getData().getOrderExpiryTime());//到期时间
            tv_completnum.setText(ret.getData().getSuccQBCount()+"Q币");//完成数量
            tv_confirm_type.setText(ret.getData().getConfirmStr());//确认方式
            tv_averagetime.setText(ret.getData().getAverageConfirmTime());//平均确认时间
            tv_orderstatus.setText(ret.getData().getOrderStatusType());//订单状态
            tv_reviewstatus.setText(ret.getData().getOrderAuditStatusType());//审核状态
            tv_completetime.setText(ret.getData().getOrderFinishTime());//完结时间
            tv_balancetime.setText(ret.getData().getSettlementTime());//结算时间
            tv_balancemoney.setText(ret.getData().getSettlementMoney() + "元");//结算金额
            tv_penalty.setText(ret.getData().getGetPenaltyMoney() + "元");//获得违约金
            tv_rechargeorders.setText(ret.getData().getOrderReceivesCount());//充值单数
            tv_cancel.setText(ret.getData().getCancelRemarks());//手动取消
            tv_canceltime.setText(ret.getData().getOrderCancelTime());//取消时间
            tv_refunds.setText(ret.getData().getRefundMoney() + "元");//退款金额

        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back || view.getId() == R.id.btn_confirm) {
            getFragmentManager().popBackStack();
        }
    }
}
