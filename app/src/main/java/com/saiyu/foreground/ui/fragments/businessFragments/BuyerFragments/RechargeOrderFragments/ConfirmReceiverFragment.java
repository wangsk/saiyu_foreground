package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.OrderReceiveConfirmRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.PhotoViewDialog;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_confirm_receiver)
public class ConfirmReceiverFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_order,tv_rechargetime,tv_rechargenum,tv_ordercount,tv_successnum;
    @ViewById
    ImageView iv_success,iv_info,iv_record;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;

    private String orderReceiveId,pic_success,pic_info,pic_record;

    public static ConfirmReceiverFragment newInstance(Bundle bundle) {
        ConfirmReceiverFragment_ fragment = new ConfirmReceiverFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        Bundle bundle = getArguments();
        if(bundle != null){
            orderReceiveId = bundle.getString("orderReceiveId");
        }
        CallbackUtils.setCallback(this);
        ApiRequest.orderReceiveConfirm(orderReceiveId,"ConfirmReceiverFragment_orderReceiveConfirm",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("确认收货");

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("ConfirmReceiverFragment_orderReceiveConfirmP")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"确认收货成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        } else if(method.equals("ConfirmReceiverFragment_orderReceiveConfirm")){
            OrderReceiveConfirmRet ret = (OrderReceiveConfirmRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_order.setText(ret.getData().getReceiveOrderNum());
            tv_rechargetime.setText(ret.getData().getRechargeTime());
            tv_rechargenum.setText(ret.getData().getReserveAccount());
            tv_ordercount.setText(ret.getData().getReserveQBCount());
            tv_successnum.setText(ret.getData().getSuccQBCount());
            pic_success = ret.getData().getPic_RechargeSucc();
            pic_info = ret.getData().getPic_TradeInfo();
            pic_record = ret.getData().getPic_BillRecord();
            if(!TextUtils.isEmpty(pic_success)){
                Glide.with(App.getApp())
                        .load(pic_success)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_success);
            }
            if(!TextUtils.isEmpty(pic_record)){
                Glide.with(App.getApp())
                        .load(pic_record)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_record);
            }
            if(!TextUtils.isEmpty(pic_info)){
                Glide.with(App.getApp())
                        .load(pic_info)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_info);
            }
        }

    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.iv_info,R.id.iv_record,R.id.iv_success,R.id.btn_cancel})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_cancel:
                Bundle bundle = new Bundle();
                bundle.putString("orderReceiveId",orderReceiveId);
                RightFragment rightFragment = RightFragment.newInstance(bundle);
                start(rightFragment);
                break;
            case R.id.btn_confirm:
                if(!TextUtils.isEmpty(orderReceiveId)){
                    ApiRequest.orderReceiveConfirmP(orderReceiveId,"ConfirmReceiverFragment_orderReceiveConfirmP",pb_loading);
                }
                break;
            case R.id.iv_info:
                if(!TextUtils.isEmpty(pic_info)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_info);
                    photoViewDialog.show();
                }
                break;
            case R.id.iv_record:
                if(!TextUtils.isEmpty(pic_record)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_record);
                    photoViewDialog.show();
                }
                break;
            case R.id.iv_success:
                if(!TextUtils.isEmpty(pic_success)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(pic_success);
                    photoViewDialog.show();
                }
                break;
        }
    }
}
