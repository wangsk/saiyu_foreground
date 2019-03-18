package com.saiyu.foreground.ui.fragments.businessFragments.hallFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_order_confirm)
public class OrderConfirmFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_type, tv_successdian, tv_consumerpoint, tv_totalmoney;
    @ViewById
    Button btn_title_back, btn_submit;
    @ViewById
    EditText et_email, et_mobile;
    @ViewById
    ImageView iv_1, iv_2;
    @ViewById
    LinearLayout ll_1, ll_2;
    @ViewById
    ProgressBar pb_loading;
    private String orderNum;
    private String receiveId,SuccQBCount,RechargeTime,PicRechargeSucc,PicTradeInfo,PicBillRecord,
            ConfirmType,BillQQNum,BillQQPwd,OftenLoginProvince,OftenLoginCity;

    public static OrderConfirmFragment newInstance(Bundle bundle) {
        OrderConfirmFragment_ fragment = new OrderConfirmFragment_();
        fragment.setArguments(bundle);
        CallbackUtils.setCallback(fragment);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }


    @AfterViews
    void afterViews() {
        tv_title_content.setText("订单提交");
        Bundle bundle = getArguments();
        if (bundle != null) {
            receiveId= bundle.getString("receiveId");
            SuccQBCount = bundle.getString("SuccQBCount");
            RechargeTime = bundle.getString("RechargeTime");
            PicRechargeSucc = bundle.getString("PicRechargeSucc");
            PicTradeInfo = bundle.getString("PicTradeInfo");
            PicBillRecord = bundle.getString("PicBillRecord");
            ConfirmType = bundle.getString("ConfirmType");
            BillQQNum = bundle.getString("BillQQNum");
            BillQQPwd = bundle.getString("BillQQPwd");
            OftenLoginProvince = bundle.getString("OftenLoginProvince");
            OftenLoginCity = bundle.getString("OftenLoginCity");

            orderNum = bundle.getString("orderNum");
            String orderId = bundle.getString("orderId");
            LogUtils.print("orderId === " + orderId + "  SuccQBCount === " + SuccQBCount);
            ApiRequest.hallDetailReceive(orderId, SuccQBCount, "", "OrderConfirmFragment_hallDetailReceive", pb_loading);
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("OrderConfirmFragment_hallDetailReceive")) {
            HallDetailReceiveRet ret = (HallDetailReceiveRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            tv_successdian.setText(ret.getData().getSuccessDian());
            tv_consumerpoint.setText(ret.getData().getConsumeRPoint());
            tv_totalmoney.setText(ret.getData().getTotalMoney());
        } else if(method.equals("OrderConfirmFragment_receiveSubmit")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                DialogUtils.showOrderSuccessDialog(getActivity(), orderNum, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                });
            }
        }
    }

    @Click({R.id.btn_title_back, R.id.ll_1, R.id.ll_2, R.id.btn_submit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.ll_1:
                if (iv_1.getVisibility() == View.VISIBLE) {
                    iv_1.setVisibility(View.GONE);
                } else {
                    iv_1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_2:
                if (iv_2.getVisibility() == View.VISIBLE) {
                    iv_2.setVisibility(View.GONE);
                } else {
                    iv_2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_submit:
                String mobile = et_mobile.getText().toString();
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(mContext,"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = et_email.getText().toString();
                String noticeType = "0,1";
                if(iv_1.getVisibility() == View.VISIBLE && iv_2.getVisibility() == View.VISIBLE){
                    noticeType = "0,1";
                } else if(iv_1.getVisibility() == View.VISIBLE){
                    noticeType = "0";
                } else if(iv_2.getVisibility() == View.VISIBLE){
                    noticeType = "1";
                }
                RequestParams requestParams = new RequestParams();
                requestParams.put("receiveId",receiveId);
                requestParams.put("SuccQBCount",SuccQBCount);
                requestParams.put("RechargeTime",RechargeTime);
                requestParams.put("PicRechargeSucc",PicRechargeSucc);
                requestParams.put("PicTradeInfo",PicTradeInfo);
                requestParams.put("PicBillRecord",PicBillRecord);
                requestParams.put("ConfirmType",ConfirmType);
                requestParams.put("BillQQNum",BillQQNum);
                requestParams.put("BillQQPwd",BillQQPwd);
                requestParams.put("OftenLoginProvince",OftenLoginProvince);
                requestParams.put("OftenLoginCity",OftenLoginCity);
                requestParams.put("NoticeType",noticeType);
                requestParams.put("NoticeMobile",mobile);
                requestParams.put("NoticeEmail",email);
                ApiRequest.receiveSubmit(requestParams,"OrderConfirmFragment_receiveSubmit",pb_loading);
                break;
        }
    }
}
