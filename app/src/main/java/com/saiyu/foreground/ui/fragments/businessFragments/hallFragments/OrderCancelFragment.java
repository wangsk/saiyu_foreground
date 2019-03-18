package com.saiyu.foreground.ui.fragments.businessFragments.hallFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.CancelOrderInfoRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_order_cancel)
public class OrderCancelFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_order_point,tv_time_point,tv_total_point;
    @ViewById
    LinearLayout ll_1,ll_2,ll_3;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    ImageView iv_1,iv_2,iv_3;
    private String receiveId;
    private String ConsumeRPoint,TimeoutPunishRPoint,TotalPoiint;
    private String OfficialConsumeRPoint,OfficialPunishRPoint,OfficialPoiint;

    public static OrderCancelFragment newInstance(Bundle bundle) {
        OrderCancelFragment_ fragment = new OrderCancelFragment_();
        fragment.setArguments(bundle);
        CallbackUtils.setCallback(fragment);
        return fragment;
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }
    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if(baseRet == null || TextUtils.isEmpty(method)){
            return;
        }
        if(method.equals("OrderCancelFragment_receiveCancel")){
            CancelOrderInfoRet ret = (CancelOrderInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            ConsumeRPoint = ret.getData().getConsumeRPoint();
            TimeoutPunishRPoint = ret.getData().getTimeoutPunishRPoint();
            TotalPoiint = ret.getData().getTotalPoiint();
            OfficialConsumeRPoint = ret.getData().getOfficialConsumeRPoint();
            OfficialPunishRPoint = ret.getData().getOfficialPunishRPoint();
            OfficialPoiint = ret.getData().getOfficialPoiint();

            tv_order_point.setText(ConsumeRPoint + "点");
            tv_time_point.setText(TimeoutPunishRPoint + "点");
            tv_total_point.setText(TotalPoiint + "点");

        } else if(method.equals("OrderCancelFragment_receiveCancelSubmit")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"订单取消成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }
    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("取消订单");
        Bundle bundle = getArguments();
        if(bundle != null){
            receiveId = bundle.getString("receiveId");
        }

        if(!TextUtils.isEmpty(receiveId)){
            ApiRequest.receiveCancel(receiveId,"OrderCancelFragment_receiveCancel",pb_loading);
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.ll_1,R.id.ll_2,R.id.ll_3})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                String cancelBType = "0";
                if(iv_1.getVisibility() == View.VISIBLE){
                    cancelBType = "0";
                } else  if(iv_2.getVisibility() == View.VISIBLE){
                    cancelBType = "1";
                } else  if(iv_3.getVisibility() == View.VISIBLE){
                    cancelBType = "2";
                }
                ApiRequest.receiveCancelSubmit(receiveId,cancelBType,"OrderCancelFragment_receiveCancelSubmit",pb_loading);
                break;
            case R.id.ll_1:
                if(iv_1.getVisibility() == View.INVISIBLE){
                    iv_1.setVisibility(View.VISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.INVISIBLE);

                    tv_order_point.setText(ConsumeRPoint + "点");
                    tv_time_point.setText(TimeoutPunishRPoint + "点");
                    tv_total_point.setText(TotalPoiint + "点");
                }

                break;
            case R.id.ll_2:
                if(iv_2.getVisibility() == View.INVISIBLE){
                    iv_1.setVisibility(View.INVISIBLE);
                    iv_2.setVisibility(View.VISIBLE);
                    iv_3.setVisibility(View.INVISIBLE);

                    tv_order_point.setText(OfficialConsumeRPoint + "点");
                    tv_time_point.setText(OfficialPunishRPoint + "点");
                    tv_total_point.setText(OfficialPoiint + "点");
                }

                break;
            case R.id.ll_3:
                if(iv_3.getVisibility() == View.INVISIBLE){
                    iv_1.setVisibility(View.INVISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.VISIBLE);

                    tv_order_point.setText(OfficialConsumeRPoint + "点");
                    tv_time_point.setText(OfficialPunishRPoint + "点");
                    tv_total_point.setText(OfficialPoiint + "点");
                }

                break;
        }
    }
}
