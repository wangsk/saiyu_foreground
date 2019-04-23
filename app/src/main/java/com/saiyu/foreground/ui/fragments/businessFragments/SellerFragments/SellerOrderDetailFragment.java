package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.SellerOrderReceiveInfoRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.PhotoViewDialog;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_seller_orderdetail)
public class SellerOrderDetailFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_ordernum,tv_creattime,tv_completetime,tv_recharge_time,tv_autoconfirmtime,tv_ordercount,tv_successcount,
            tv_successprice,tv_money,iv_serviceprice,tv_total,tv_recharge_product,
            tv_producttype,tv_recharge_num,tv_once_limit,tv_pic,tv_confirm_type,tv_averagetime,
            tv_contacttype,tv_rechargeremark;
    @ViewById
    ImageView iv_success,iv_info,iv_record;
    private String orderId;
    private String pic_success,pic_info,pic_record;
    private String OrderImgServerProcess;

    public static SellerOrderDetailFragment newInstance(Bundle bundle) {
        SellerOrderDetailFragment_ fragment = new SellerOrderDetailFragment_();
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
            ApiRequest.orderReceiveInfoSeller(orderId,"SellerOrderDetailFragment_orderReceiveInfoSeller",pb_loading);
        }
        OrderImgServerProcess = SPUtils.getString(ConstValue.OrderImgServerProcess,"");
        LogUtils.print("OrderImgServerProcess === " + OrderImgServerProcess);
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
        if (method.equals("SellerOrderDetailFragment_orderReceiveInfoSeller")) {
            SellerOrderReceiveInfoRet ret = (SellerOrderReceiveInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_ordernum.setText(ret.getData().getReceiveOrderNum());//充值订单号
            tv_creattime.setText(ret.getData().getCreateTime());//接单时间
            tv_completetime.setText(ret.getData().getFinishTime());//完成时间
            tv_recharge_time.setText(ret.getData().getRechargeTime());//充值时间
            tv_autoconfirmtime.setText(ret.getData().getAutoConfirmTime());//自动确认时间
            tv_ordercount.setText(ret.getData().getReserveQBCount()+"Q币");//接单数量
            tv_successcount.setText(ret.getData().getSuccQBCount()+"Q币");//成功数量
            tv_successprice.setText(ret.getData().getSuccMoney()+"元");//成功金额
            tv_money.setText(ret.getData().getLiquidatedMoney()+"元");//违约金
            iv_serviceprice.setText(ret.getData().getServiceMoney()+"元");//服务费
            tv_total.setText(ret.getData().getTotalMoney()+"元");//贷款金额
            tv_recharge_product.setText(ret.getData().getProductName());
            tv_producttype.setText(ret.getData().getProductTypeName());
            tv_recharge_num.setText(ret.getData().getReserveAccount());

            float LessChargeDiscount = ret.getData().getLessChargeDiscount();
            float OnceMinCount = ret.getData().getOnceMinCount();
            if(OnceMinCount == 0){
                tv_once_limit.setText("单次数量不限制");
            } else {
                if(LessChargeDiscount == 1){
                    tv_once_limit.setText(OnceMinCount+"Q币 少充按原价");
                } else {
                    tv_once_limit.setText(OnceMinCount+"Q币 少充则=" + LessChargeDiscount*100 + "%");
                }
            }

            if(ret.getData().getIsPicConfirm() == 0){//支持验图确认
                tv_pic.setText("否");
            } else {
                tv_pic.setText("是");
            }
            tv_confirm_type.setText(ret.getData().getConfirmStr());
            tv_averagetime.setText(ret.getData().getAverageConfirmTime());//平均确认时间
            tv_contacttype.setText("手机"+ret.getData().getContactMobile()+";qq"+ret.getData().getContactQQ()+";\n"+ret.getData().getIsAllowShowContactStr());//联系方式
            tv_rechargeremark.setText(ret.getData().getRemarks());//充值留言

            pic_success = ret.getData().getPic_RechargeSucc();
            pic_info = ret.getData().getPic_TradeInfo();
            pic_record = ret.getData().getPic_BillRecord();
            if(!TextUtils.isEmpty(pic_success)){
                Glide.with(App.getApp())
                        .load(pic_success + "?" + OrderImgServerProcess)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_success);
            }
            if(!TextUtils.isEmpty(pic_record)){
                Glide.with(App.getApp())
                        .load(pic_record + "?" + OrderImgServerProcess)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_record);
            }
            if(!TextUtils.isEmpty(pic_info)){
                Glide.with(App.getApp())
                        .load(pic_info + "?" + OrderImgServerProcess)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_info);
            }

        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.iv_success,R.id.iv_info,R.id.iv_record})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_confirm:
                getFragmentManager().popBackStack();
                break;
            case R.id.iv_success:
                if(!TextUtils.isEmpty(pic_success)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_success + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                }
                break;
            case R.id.iv_info:
                if(!TextUtils.isEmpty(pic_info)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_info + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                }
                break;
            case R.id.iv_record:
                if(!TextUtils.isEmpty(pic_record)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_record + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                }
                break;
        }
    }
}
