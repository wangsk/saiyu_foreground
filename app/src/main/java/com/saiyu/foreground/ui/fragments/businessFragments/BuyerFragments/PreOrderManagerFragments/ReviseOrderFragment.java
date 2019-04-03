package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.BuyerOrderInfoRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DropdownLayout;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.City;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Province;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_reviseorder)
public class ReviseOrderFragment  extends BaseFragment implements CallbackUtils.ResponseCallback,CallbackUtils.OnActivityCallBack{
    @ViewById
    TextView tv_title_content,tv_ordernum,tv_recharge_product,tv_producttype,tv_rechargenum,
            tv_ordercount,tv_orderprice,tv_orderremark,tv_submit,tv_time_online,tv_orderlimit,
            tv_contacttype,tv_releasetime,tv_latertime,tv_completnum,tv_confirmtype,tv_averagetime,tv_orderstatus
            ,tv_reviewstatus,tv_completetime,tv_balancetime,tv_balancemoney,tv_penalty,tv_rechargeorders,tv_cancel
            ,tv_canceltime,tv_refunds,tv_betweentime,tv_qq,tv_province,tv_city;
    @ViewById
    ImageView iv_orderPsw,iv_pic;
    @ViewById
    Button btn_title_back,btn_confirm,btn_submit,btn_cancel;
    @ViewById
    LinearLayout ll_isAgentConfirm;
    @ViewById
    RelativeLayout rl_onlinetime,rl_orderpsw,rl_betweentime,rl_picconfirm,rl_remark;
    @ViewById
    EditText et_psw;
    @ViewById
    Spinner sp_province,sp_city;

    @ViewById
    ProgressBar pb_loading;

    private List<Province> list = new ArrayList<Province>();
    private ArrayAdapter<Province> arrayAdapter1;
    private ArrayAdapter<City> arrayAdapter2;
    private Province province = null;
    private City city = null;

    private String onLineStartTime,onLineStopTime,orderPsw,onceMinCount,lessChargeDiscount,orderInterval,IsPicConfirm,Remarks;
    private String orderId;

    public static ReviseOrderFragment newInstance(Bundle bundle) {
        ReviseOrderFragment_ fragment = new ReviseOrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnActivityCallBack(this);
        ApiRequest.orderInfo(orderId,"ReviseOrderFragment_getOrderById",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("已发布订单修改");

        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }

        list= Utils.parser(getActivity());
        arrayAdapter1 = new ArrayAdapter<Province>(getActivity(),R.layout.item_spinner, R.id.tv_spinner,list);
        arrayAdapter2 = new ArrayAdapter<City>(getActivity(),R.layout.item_spinner, R.id.tv_spinner,list.get(0).getCitys());
        sp_province.setAdapter(arrayAdapter1);
        sp_province.setSelection(0, true);
        sp_city.setAdapter(arrayAdapter2);
        sp_city.setSelection(0, true);

        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = list.get(position);
                arrayAdapter2 = new ArrayAdapter<City>(getActivity(), R.layout.item_spinner, R.id.tv_spinner, list.get(position).getCitys());
                sp_city.setAdapter(arrayAdapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = province.getCitys().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("ReviseOrderFragment_getOrderById")) {
            BuyerOrderInfoRet ret = (BuyerOrderInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_ordernum.setText(ret.getData().getOrderNum());//预定订单号
            tv_recharge_product.setText(ret.getData().getProductName());//充值产品
            tv_producttype.setText(ret.getData().getProductType());//产品类型
            tv_rechargenum.setText(ret.getData().getReserveAccount());//充值号码
            tv_ordercount.setText(ret.getData().getReserveQBCount());//预定数量
            tv_orderprice.setText(ret.getData().getReservePrice() + "元");//预定出价
            tv_orderremark.setText(ret.getData().getReserveTitle());//预定附言
            tv_time_online.setText(ret.getData().getOnlineTime());//在线时间
            if(TextUtils.isEmpty(ret.getData().getOrderPwd())){//订单加密
                iv_orderPsw.setVisibility(View.GONE);
            } else {
                iv_orderPsw.setVisibility(View.VISIBLE);
            }

            tv_orderlimit.setText(ret.getData().getOnceMinCount());//单次限制                      ?
            tv_betweentime.setText(ret.getData().getOrderInterval()+"分钟");//接单间隔时间          ?
            if(ret.getData().getIsPicConfirm() == 0){//支持验图确认
                iv_pic.setVisibility(View.GONE);
            } else {
                iv_pic.setVisibility(View.VISIBLE);
            }

            int productIsAgentConfirm  = ret.getData().getProductIsAgentConfirm();
            if(productIsAgentConfirm == 0){
                //不支持客服代理确认
                ll_isAgentConfirm.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                tv_submit.setText(ret.getData().getProductName()+"不支持客服代理确认");
            } else if(productIsAgentConfirm == 1){
                int agentConfirmAuditStatus = ret.getData().getAgentConfirmAuditStatus();
                switch (agentConfirmAuditStatus){
                    case 0:
                        tv_submit.setText("审核中");
                        btn_submit.setText("放弃申请");
                        tv_qq.setText(ret.getData().getReserveAccount());
                        et_psw.setText(ret.getData().getReservePwd());
                        et_psw.setFocusable(false);
                        et_psw.setClickable(false);
                        tv_province.setText(ret.getData().getOftenLoginProvince());
                        tv_city.setText(ret.getData().getOftenLoginCity());
                        tv_province.setVisibility(View.VISIBLE);
                        tv_city.setVisibility(View.VISIBLE);
                        sp_province.setVisibility(View.GONE);
                        sp_city.setVisibility(View.GONE);
                        break;
                    case 1:
                        tv_submit.setText("审核成功");
                        btn_submit.setText("取消客服代理确认");
                        tv_qq.setText(ret.getData().getReserveAccount());
                        et_psw.setText(ret.getData().getReservePwd());
                        et_psw.setFocusable(false);
                        et_psw.setClickable(false);
                        tv_province.setText(ret.getData().getOftenLoginProvince());
                        tv_city.setText(ret.getData().getOftenLoginCity());
                        tv_province.setVisibility(View.VISIBLE);
                        tv_city.setVisibility(View.VISIBLE);
                        sp_province.setVisibility(View.GONE);
                        sp_city.setVisibility(View.GONE);
                        break;
                    case 2:
                        tv_submit.setText("审核失败");
                        btn_submit.setText("立即申请");
                        tv_qq.setText(ret.getData().getReserveAccount());
                        et_psw.setText("");
                        et_psw.setFocusable(true);
                        et_psw.setClickable(true);
                        tv_province.setVisibility(View.GONE);
                        tv_city.setVisibility(View.GONE);
                        sp_province.setVisibility(View.VISIBLE);
                        sp_city.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        tv_submit.setText("未申请");
                        btn_submit.setText("立即申请");
                        tv_qq.setText(ret.getData().getReserveAccount());
                        et_psw.setText("");
                        et_psw.setFocusable(true);
                        et_psw.setClickable(true);
                        tv_province.setVisibility(View.GONE);
                        tv_city.setVisibility(View.GONE);
                        sp_province.setVisibility(View.VISIBLE);
                        sp_city.setVisibility(View.VISIBLE);
                        break;
                }
            }

            tv_contacttype.setText("手机"+ret.getData().getContactMobile()+";QQ"+ret.getData().getContactQQ()+";\n"+ret.getData().getIsAllowShowContactStr());//联系方式

           // tv_rechargeremark.setText(ret.getData().getRemarks());//充值留言
            tv_releasetime.setText(ret.getData().getCreateTime());//发布时间
            tv_latertime.setText(ret.getData().getOrderExpiryTime());//到期时间              ?
            tv_completnum.setText(ret.getData().getSuccQBCount());//完成数量
            tv_confirmtype.setText(ret.getData().getConfirmStr());//确认方式
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
        } else if(method.equals("ReviseOrderFragment_orderModify")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        } else if(method.equals("ReviseOrderFragment_agentConfirmApply")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){

                tv_submit.setText("审核中");
                btn_submit.setText("放弃申请");
                et_psw.setClickable(false);
                et_psw.setFocusable(false);
                tv_province.setVisibility(View.VISIBLE);
                tv_city.setVisibility(View.VISIBLE);
                if(province != null && !"请选择".equals(province.getName())){
                    tv_province.setText(province.getName());
                }
                if(city != null && !"请选择".equals(city.getName())){
                    tv_city.setText(city.getName());
                }
                sp_province.setVisibility(View.GONE);
                sp_city.setVisibility(View.GONE);

                Toast.makeText(mContext,"申请成功",Toast.LENGTH_SHORT).show();
            }
        } else if(method.equals("ReviseOrderFragment_agentConfirmCancel")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                tv_submit.setText("未申请");
                btn_submit.setText("立即申请");
                et_psw.setText("");
                et_psw.setFocusable(true);
                et_psw.setClickable(true);
                tv_province.setVisibility(View.GONE);
                tv_city.setVisibility(View.GONE);
                sp_province.setVisibility(View.VISIBLE);
                sp_city.setVisibility(View.VISIBLE);
                Toast.makeText(mContext,"放弃申请成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel,R.id.btn_submit,R.id.rl_onlinetime,R.id.rl_orderpsw,R.id.rl_betweentime,R.id.rl_picconfirm,R.id.rl_remark})
    void onClick(View view) {
        Intent intent = new Intent(mContext,ContainerActivity_.class);
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                RequestParams requestParams = new RequestParams();
                requestParams.put("OrderId",orderId);
                requestParams.put("OnlineTimeBegin",onLineStartTime);
                requestParams.put("OnlineTimeEnd",onLineStopTime);
                requestParams.put("OrderPwd",orderPsw);
//                requestParams.put("OnceMinCount",onceMinCount);
//                requestParams.put("LessChargeDiscount",lessChargeDiscount);
                requestParams.put("OrderInterval",orderInterval);
                requestParams.put("IsPicConfirm",IsPicConfirm);
                requestParams.put("Remarks",Remarks);

                ApiRequest.orderModify(requestParams,"ReviseOrderFragment_orderModify",pb_loading);
                break;
            case R.id.rl_onlinetime:
                bundle.putString("onLineTime",tv_time_online.getText().toString());
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOnlineTimeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,ContainerActivity.SetOnlineTimeFragmentTag);
                break;
            case R.id.rl_orderpsw:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOrderPswFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,ContainerActivity.SetOrderPswFragmentTag);
                break;
            case R.id.rl_betweentime:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetBetweenTimeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,ContainerActivity.SetBetweenTimeFragmentTag);
                break;
            case R.id.rl_picconfirm:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetPicReviewFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,ContainerActivity.SetPicReviewFragmentTag);
                break;
            case R.id.rl_remark:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetRemarkFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,ContainerActivity.SetRemarkFragmentTag);
                break;
            case R.id.btn_submit:
                if("立即申请".equals(btn_submit.getText().toString())){
                    tv_submit.setText("未申请");
                    btn_submit.setText("确认提交");
                    et_psw.setText("");
                    et_psw.setFocusable(true);
                    et_psw.setClickable(true);
                    tv_province.setVisibility(View.GONE);
                    tv_city.setVisibility(View.GONE);
                    sp_province.setVisibility(View.VISIBLE);
                    sp_city.setVisibility(View.VISIBLE);

                } else if("确认提交".equals(btn_submit.getText().toString())){

                    ApiRequest.agentConfirmApply(orderId,"",province == null?"":province.getName(),city == null?"":city.getName(),"ReviseOrderFragment_agentConfirmApply",pb_loading);

                } else if("取消客服代理确认".equals(btn_submit.getText().toString()) || "放弃申请".equals(btn_submit.getText().toString())){

                    ApiRequest.agentConfirmCancel(orderId,"ReviseOrderFragment_agentConfirmCancel",pb_loading);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("resultCode ==== " + resultCode + " requestCode ==== " + requestCode);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case ContainerActivity.SetOnlineTimeFragmentTag:
                if(data == null || data.getExtras() == null){
                    return;
                }
                try {
                    onLineStartTime = data.getExtras().getString("onLineStartTime");
                    onLineStopTime = data.getExtras().getString("onLineStopTime");
                    if(TextUtils.isEmpty(onLineStartTime) || TextUtils.isEmpty(onLineStopTime)){
                        tv_time_online.setText("00:00-23:59");
                    } else {
                        tv_time_online.setText(onLineStartTime + "-" + onLineStopTime);
                    }

                }catch (Exception e){

                }
                break;

            case ContainerActivity.SetOrderPswFragmentTag:
                if(data == null || data.getExtras() == null){
                    return;
                }
                try {
                    orderPsw = data.getExtras().getString("orderPsw");
                    if(TextUtils.isEmpty(orderPsw)){
                        iv_orderPsw.setVisibility(View.INVISIBLE);
                    } else {
                        iv_orderPsw.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){

                }
                break;
            case ContainerActivity.SetBetweenTimeFragmentTag:
                if(data == null || data.getExtras() == null){
                    return;
                }
                try {
                    orderInterval = data.getExtras().getString("orderInterval");
                    try {
                        orderInterval = data.getExtras().getString("orderInterval");
                        tv_betweentime.setText(orderInterval+"分钟");

                    }catch (Exception e){

                    }

                }catch (Exception e){

                }
                break;

            case ContainerActivity.SetPicReviewFragmentTag:
                if(data == null || data.getExtras() == null){
                    return;
                }
                try {
                    IsPicConfirm = data.getExtras().getString("IsPicConfirm");
                    if(TextUtils.equals("0",IsPicConfirm)){
                        iv_pic.setVisibility(View.GONE);
                    } else if(TextUtils.equals("1",IsPicConfirm)){
                        iv_pic.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){

                }
                break;
            case ContainerActivity.SetRemarkFragmentTag:
                if(data == null || data.getExtras() == null){
                    return;
                }
                try {
                    Remarks = data.getExtras().getString("Remarks");

                }catch (Exception e){

                }
                break;
        }
    }
}
