package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.CountDownTimerUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_ordersubmit_checkmsg)
public class OrderSubmitCheckMsgFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_orderMobile,tv_orderNum,tv_orderMoney,tv_msg_count;
    @ViewById
    Button btn_title_back;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    Button btn_confirm;
    @ViewById
    EditText et_orderCode;
    private static CountDownTimerUtils countDownTimerUtils;
    private String OrderNum,ProductId,ProductName,ProductProperty1,ProductProperty2,ProductProperty3,ReserveAccount,
            ReserveQBCount,ReserveUnitCount,ReservePrice,ReserveDiscount,ReserveTitle,OnlineTimeBegin,OnlineTimeEnd,
            OrderExpiryTime,OrderPwd,OnceMinCount,LessChargeDiscount,OrderInterval,IsAgentConfirm,ReservePwd,
            OftenLoginProvince,OftenLoginCity,IsPicConfirm,Remarks,ContactMobile,ContactQQ,RoleName;


    public static OrderSubmitCheckMsgFragment newInstance(Bundle bundle) {
        OrderSubmitCheckMsgFragment_ fragment = new OrderSubmitCheckMsgFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterView() {
        Utils.setButtonClickable(btn_confirm,false);
        tv_title_content.setText("订单支付");
        countDownTimerUtils = new CountDownTimerUtils(tv_msg_count, 60000, 1000);
        countDownTimerUtils.setClickable(false);
        Bundle bundle = getArguments();
        if(bundle != null){

            OrderNum = bundle.getString("OrderNum");
            ProductId = bundle.getString("ProductId");
            ProductName = bundle.getString("ProductName");
            ProductProperty1 = bundle.getString("ProductProperty1");
            ProductProperty2 = bundle.getString("ProductProperty2");
            ProductProperty3 = bundle.getString("ProductProperty3");
            ReserveAccount = bundle.getString("ReserveAccount");
            ReserveQBCount = bundle.getString("ReserveQBCount");
            ReserveUnitCount = bundle.getString("ReserveUnitCount");
            ReservePrice = bundle.getString("ReservePrice");
            ReserveDiscount = bundle.getString("ReserveDiscount");
            ReserveTitle = bundle.getString("ReserveTitle");
            OnlineTimeBegin = bundle.getString("OnlineTimeBegin");
            OnlineTimeEnd = bundle.getString("OnlineTimeEnd");
            OrderExpiryTime = bundle.getString("OrderExpiryTime");
            OrderPwd = bundle.getString("OrderPwd");
            OnceMinCount = bundle.getString("OnceMinCount");
            LessChargeDiscount = bundle.getString("LessChargeDiscount");
            OrderInterval = bundle.getString("OrderInterval");
            IsAgentConfirm = bundle.getString("IsAgentConfirm");
            ReservePwd = bundle.getString("ReservePwd");
            OftenLoginProvince = bundle.getString("OftenLoginProvince");
            OftenLoginCity = bundle.getString("OftenLoginCity");
            IsPicConfirm = bundle.getString("IsPicConfirm");
            Remarks = bundle.getString("Remarks");
            ContactMobile = bundle.getString("ContactMobile");
            ContactQQ = bundle.getString("ContactQQ");
            RoleName = bundle.getString("RoleName");
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
       if(method.equals("OrderSubmitCheckMsgFragment_orderPublish")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
           if(ret.getData().isResult()){
               Toast.makeText(mContext,"发布成功",Toast.LENGTH_SHORT).show();
               getActivity().finish();
           }
        }
    }

    @Click({R.id.btn_confirm,R.id.tv_msg_count,R.id.btn_title_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.tv_msg_count:
                countDownTimerUtils.start();
                ApiRequest.sendVcode(ContactMobile,"",countDownTimerUtils);
                break;
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(et_orderCode.getText().toString())){
                    Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams requestParams = new RequestParams();
                requestParams.put("OrderNum",OrderNum);
                requestParams.put("ProductId",ProductId);
                requestParams.put("ProductName",ProductName);
                requestParams.put("ProductProperty1",ProductProperty1);
                requestParams.put("ProductProperty2",ProductProperty2);
                requestParams.put("ProductProperty3",ProductProperty3);
                requestParams.put("ReserveAccount",ReserveAccount);
                requestParams.put("ReserveQBCount",ReserveQBCount);
                requestParams.put("ReserveUnitCount",ReserveUnitCount);
                requestParams.put("ReservePrice",ReservePrice);
                requestParams.put("ReserveDiscount",ReserveDiscount);
                requestParams.put("ReserveTitle",ReserveTitle);
                requestParams.put("OnlineTimeBegin",OnlineTimeBegin);
                requestParams.put("OnlineTimeEnd",OnlineTimeEnd);
                requestParams.put("OrderExpiryTime",OrderExpiryTime);
                requestParams.put("OrderPwd",OrderPwd);
                requestParams.put("OnceMinCount",OnceMinCount);
                requestParams.put("LessChargeDiscount",LessChargeDiscount);
                requestParams.put("OrderInterval",OrderInterval);
                requestParams.put("IsAgentConfirm",IsAgentConfirm);
                requestParams.put("ReservePwd",ReservePwd);
                requestParams.put("OftenLoginProvince",OftenLoginProvince);
                requestParams.put("OftenLoginCity",OftenLoginCity);
                requestParams.put("IsPicConfirm",IsPicConfirm);
                requestParams.put("Remarks",Remarks);
                requestParams.put("ContactMobile",ContactMobile);
                requestParams.put("ContactQQ",ContactQQ);
                requestParams.put("code",et_orderCode.getText().toString());
                requestParams.put("RoleName",RoleName);
                ApiRequest.orderPublish(requestParams,"OrderSubmitCheckMsgFragment_orderPublish",pb_loading);

                break;
        }
    }

    @TextChange({R.id.et_orderCode})
    void textChange(CharSequence s, TextView hello, int before, int start, int count){
        if(hello.getId() == R.id.et_orderCode){
            String text = s.toString();
            if(!TextUtils.isEmpty(text)){
                countDownTimerUtils.setClickable(true);
                Utils.setButtonClickable(btn_confirm,true);
            } else {
                countDownTimerUtils.setClickable(false);
                Utils.setButtonClickable(btn_confirm,false);
            }
        }
    }
}
