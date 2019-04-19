package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.OrderNumRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DropdownLayout;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.PopWindowUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.List;

@EFragment(R.layout.fragment_release_order)
public class ReleaseOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback, CallbackUtils.OnActivityCallBack {
    @ViewById
    TextView tv_title_content, tv_orderName, tv_release_count_prompt, tv_time_online, tv_function, tv_sw, tv_rechargeGame, tv_gamePlace;
    @ViewById
    Button btn_title_back;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    Button btn_confirm;
    @ViewById
    DropdownLayout dropdown_layout;
    @ViewById
    LinearLayout ll_extra, top_bar, ll_gamePlace, ll_onlineTime, ll_role;
    @ViewById
    RelativeLayout rl_orderEndTime, rl_orderPsw, rl_order_min;
    @ViewById
    ImageView iv_click, iv_orderEndTime, iv_orderPsw, iv_order_min, iv_replace, iv_pic,iv_betweentime;
    @ViewById
    Spinner sp_selector;
    @ViewById
    EditText et_recharge_qqnum, et_release_count, et_release_price, et_release_discount, et_qq, et_role,et_mobile;
    @ViewById
    Switch sw;
    private boolean IsNeedMobile;
    private List<String> postScriptList;
    private String name, unitName, ReserveAccount, mobile, orderNum, convertCount, productId, reserveTitle, onLineStartTime, onLineStopTime, orderEndTime, orderPsw,
             orderInterval = "3", IsAgentConfirm, ReservePwd, OftenLoginProvince, OftenLoginCity, IsPicConfirm, Remarks, IsAllowShowContact, ProductProperty1, ProductProperty2, ProductProperty3, RoleName;
    private int isRole;
    private float LessChargeDiscount = 1f,OnceMinCount = 0f;

    private BigDecimal convertCountBig = new BigDecimal(0);//QB和预定数量换算的折扣
    private BigDecimal qbConvertCountBig = new BigDecimal(0);

    public static ReleaseOrderFragment newInstance(Bundle bundle) {
        ReleaseOrderFragment_ fragment = new ReleaseOrderFragment_();
        fragment.setArguments(bundle);
        CallbackUtils.setCallback(fragment);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnActivityCallBack(this);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("发布预定订单");
        Utils.setButtonClickable(btn_confirm, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            tv_rechargeGame.setText(name);
            productId = bundle.getString("ProductId");
            unitName = bundle.getString("unitName");
            convertCount = bundle.getString("convertCount");
            String qbConvertCount = bundle.getString("qbCount");
            if(!TextUtils.isEmpty(qbConvertCount)){
                qbConvertCountBig = new BigDecimal(qbConvertCount);
            }
            if(!TextUtils.isEmpty(convertCount)){
                convertCountBig = new BigDecimal(convertCount);
            }
            LogUtils.print("convertCount === " + convertCount);
            LogUtils.print("qbConvertCount === " + qbConvertCount);
            tv_release_count_prompt.setText(name + unitName + "=" + "Q币");
            ProductProperty1 = bundle.getString("ProductProperty1", "");
            ProductProperty2 = bundle.getString("ProductProperty2", "");
            ProductProperty3 = bundle.getString("ProductProperty3", "");
            if(TextUtils.isEmpty(ProductProperty1)){
                ll_gamePlace.setVisibility(View.GONE);
            } else {
                tv_gamePlace.setText(ProductProperty1 + " " + ProductProperty2 + " " + ProductProperty3);
                ll_gamePlace.setVisibility(View.VISIBLE);
            }

            isRole = bundle.getInt("isRole");
            if (isRole == 0) {
                //无角色名
                ll_role.setVisibility(View.GONE);
            } else if (isRole == 1) {
                //有角色名
                ll_role.setVisibility(View.VISIBLE);
            }
        }

        IsAllowShowContact = "0";

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_sw.setText("允许接单卖家查看");
                    IsAllowShowContact = "1";
                } else {
                    tv_sw.setText("不允许接单卖家查看");
                    IsAllowShowContact = "0";
                }
            }
        });

        ApiRequest.getOrderNum("ReleaseOrderFragment_getOrderNum", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("ReleaseOrderFragment_orderPublish")) {
            BooleanRet ret = (BooleanRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isResult()) {
                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        } else if (method.equals("ReleaseOrderFragment_getOrderNum")) {
            OrderNumRet ret = (OrderNumRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            orderNum = ret.getData().getOrderNum();
            tv_orderName.setText(orderNum);
            mobile = ret.getData().getMobile();
            et_mobile.setText(mobile);
            IsNeedMobile = ret.getData().isNeedMobile();
            postScriptList = ret.getData().getPostScriptList();

            if (postScriptList != null && postScriptList.size() > 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tv_spinner, postScriptList);
                sp_selector.setAdapter(arrayAdapter);
                sp_selector.setSelection(0);
                reserveTitle = postScriptList.get(0);
                sp_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        LogUtils.print(postScriptList.get(position));
                        reserveTitle = postScriptList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else if (method.equals("ReleaseOrderFragment_checkLimitQQ")) {
            BooleanRet ret = (BooleanRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isResult()) {
                initPopWindow_9(name, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (IsNeedMobile) {
                            Bundle bundle = new Bundle();
                            bundle.putString("OrderNum", orderNum);
                            bundle.putString("ProductId", productId);
                            bundle.putString("ProductName", name);
                            bundle.putString("ProductProperty1", ProductProperty1);
                            bundle.putString("ProductProperty2", ProductProperty2);
                            bundle.putString("ProductProperty3", ProductProperty3);
                            bundle.putString("ReserveAccount", ReserveAccount);
                            bundle.putString("ReserveQBCount", qbCount.toString());
                            bundle.putString("ReserveUnitCount", et_release_count_str);
                            bundle.putString("ReservePrice", et_release_price_str);
                            bundle.putString("ReserveDiscount", et_release_discount_str);
                            bundle.putString("ReserveTitle", reserveTitle);
                            bundle.putString("OnlineTimeBegin", onLineStartTime);
                            bundle.putString("OnlineTimeEnd", onLineStopTime);
                            bundle.putString("OrderExpiryTime", orderEndTime);
                            bundle.putString("OrderPwd", orderPsw);
                            bundle.putFloat("OnceMinCount", OnceMinCount);
                            bundle.putFloat("LessChargeDiscount", LessChargeDiscount);
                            bundle.putString("OrderInterval", orderInterval);
                            bundle.putString("IsAgentConfirm", IsAgentConfirm);
                            bundle.putString("ReservePwd", ReservePwd);
                            bundle.putString("OftenLoginProvince", OftenLoginProvince);
                            bundle.putString("OftenLoginCity", OftenLoginCity);
                            bundle.putString("IsPicConfirm", IsPicConfirm);
                            bundle.putString("Remarks", Remarks);
                            bundle.putString("ContactMobile", mobile);
                            bundle.putString("ContactQQ", et_qq.getText().toString());
                            bundle.putString("RoleName", RoleName);
                            bundle.putString("IsAllowShowContact",IsAllowShowContact);
                            OrderSubmitCheckMsgFragment orderSubmitCheckMsgFragment = OrderSubmitCheckMsgFragment.newInstance(bundle);
                            start(orderSubmitCheckMsgFragment);
                        } else {
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("OrderNum", orderNum);
                            requestParams.put("ProductId", productId);
                            requestParams.put("ProductName", name);
                            requestParams.put("ProductProperty1", ProductProperty1);
                            requestParams.put("ProductProperty2", ProductProperty2);
                            requestParams.put("ProductProperty3", ProductProperty3);
                            requestParams.put("ReserveAccount", ReserveAccount);
                            requestParams.put("ReserveQBCount", qbCount.toString());
                            requestParams.put("ReserveUnitCount", et_release_count_str);
                            requestParams.put("ReservePrice", et_release_price_str);
                            requestParams.put("ReserveDiscount", et_release_discount_str);
                            requestParams.put("ReserveTitle", reserveTitle);
                            requestParams.put("OnlineTimeBegin", onLineStartTime);
                            requestParams.put("OnlineTimeEnd", onLineStopTime);
                            requestParams.put("OrderExpiryTime", orderEndTime);
                            requestParams.put("OrderPwd", orderPsw);
                            requestParams.put("OnceMinCount", OnceMinCount);
                            requestParams.put("LessChargeDiscount", LessChargeDiscount);
                            requestParams.put("OrderInterval", orderInterval);
                            requestParams.put("IsAgentConfirm", IsAgentConfirm);
                            requestParams.put("ReservePwd", ReservePwd);
                            requestParams.put("OftenLoginProvince", OftenLoginProvince);
                            requestParams.put("OftenLoginCity", OftenLoginCity);
                            requestParams.put("IsPicConfirm", IsPicConfirm);
                            requestParams.put("Remarks", Remarks);
                            requestParams.put("ContactMobile", mobile);
                            requestParams.put("ContactQQ", et_qq.getText().toString());
                            requestParams.put("code", "");
                            requestParams.put("RoleName", RoleName);
                            requestParams.put("IsAllowShowContact", IsAllowShowContact);
                            LogUtils.print("发布订单信息 ： " + requestParams.toString());
                            ApiRequest.orderPublish(requestParams, "ReleaseOrderFragment_orderPublish", pb_loading);
                        }
                    }
                });
            } else {
                Toast.makeText(mContext, ret.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Click({R.id.btn_confirm, R.id.top_bar, R.id.rl_remark, R.id.btn_title_back, R.id.ll_gamePlace, R.id.ll_onlineTime, R.id.rl_orderEndTime, R.id.rl_replace, R.id.rl_orderPsw, R.id.rl_order_min, R.id.rl_order_between, R.id.rl_pic})
    void onClick(View view) {
        Intent intent = new Intent(mContext, ContainerActivity_.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.top_bar:
                if(ll_extra.getVisibility() == View.GONE){
                    dropdown_layout.toggle(ll_extra, true);
                    iv_click.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.shang));
                } else {
                    dropdown_layout.toggle(ll_extra, false);
                    iv_click.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xia));
                }

                break;
            case R.id.btn_title_back:
            case R.id.ll_gamePlace:
                getFragmentManager().popBackStack();
                break;
            case R.id.ll_onlineTime:
                bundle.putString("onLineStartTime", onLineStartTime);
                bundle.putString("onLineStopTime", onLineStopTime);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOnlineTimeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetOnlineTimeFragmentTag);
                break;
            case R.id.rl_orderEndTime:
                bundle.putString("orderEndTime",orderEndTime);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOrderEndTimeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetOrderEndTimeFragmentTag);
                break;
            case R.id.rl_orderPsw:
                bundle.putString("orderPsw", orderPsw);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOrderPswFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetOrderPswFragmentTag);
                break;
            case R.id.rl_order_min:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetMinRechargeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetMinRechargeFragmentTag);
                break;
            case R.id.rl_order_between:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetBetweenTimeFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetBetweenTimeFragmentTag);
                break;
            case R.id.rl_replace:
                bundle.putString("ReserveAccount", ReserveAccount);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetOrderReplaceConfirmFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetOrderReplaceConfirmFragmentTag);
                break;
            case R.id.rl_pic:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetPicReviewFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetPicReviewFragmentTag);
                break;
            case R.id.rl_remark:
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetRemarkFragmentTag);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, ContainerActivity.SetRemarkFragmentTag);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(orderNum)) {
                    LogUtils.print("订单号为空" + orderNum);
                    return;
                }

                if (TextUtils.isEmpty(productId)) {
                    LogUtils.print("产品ID为空" + productId);
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    LogUtils.print("产品名称为空" + name);
                    return;
                }

                ReserveAccount = et_recharge_qqnum.getText().toString();
                if (TextUtils.isEmpty(ReserveAccount)) {
                    Toast.makeText(mContext, "请输入充值QQ号码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_release_count_str)) {
                    Toast.makeText(mContext, "请输入预定数量！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(qbCount.toString())) {
                    LogUtils.print("QB数量为空" + name);
                    return;
                }

                if (TextUtils.isEmpty(et_release_price_str)) {
                    Toast.makeText(mContext, "请输入价格！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_release_discount_str)) {
                    Toast.makeText(mContext, "请输入折扣！", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataFormat();//点击确认发布的时候再检测一下数据格式

                ApiRequest.checkLimitQQ(ReserveAccount, "ReleaseOrderFragment_checkLimitQQ", pb_loading);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("resultCode ==== " + resultCode + " requestCode ==== " + requestCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ContainerActivity.SetOnlineTimeFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    onLineStartTime = data.getExtras().getString("onLineStartTime");
                    onLineStopTime = data.getExtras().getString("onLineStopTime");
                    LogUtils.print("onLineStartTime === " + onLineStartTime + " onLineStopTime === " + onLineStopTime);
                    if (TextUtils.isEmpty(onLineStartTime) || TextUtils.isEmpty(onLineStopTime)) {
                        tv_time_online.setText("始终在线");
                    } else {
                        tv_time_online.setText(onLineStartTime + "-" + onLineStopTime);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetOrderEndTimeFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    orderEndTime = data.getExtras().getString("orderEndTime");
                    LogUtils.print("orderEndTime === " + orderEndTime);
                    if (TextUtils.isEmpty(orderEndTime)) {
                        iv_orderEndTime.setVisibility(View.GONE);
                    } else {
                        iv_orderEndTime.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetOrderPswFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    orderPsw = data.getExtras().getString("orderPsw");
                    LogUtils.print("orderPsw === " + orderPsw);
                    if (TextUtils.isEmpty(orderPsw)) {
                        iv_orderPsw.setVisibility(View.GONE);
                    } else {
                        iv_orderPsw.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetMinRechargeFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    OnceMinCount = data.getExtras().getFloat("onceMinCount");
                    LessChargeDiscount = data.getExtras().getFloat("lessChargeDiscount");
                    LogUtils.print("onceMinCount === " + OnceMinCount + " lessChargeDiscount === " + LessChargeDiscount);

                    if(OnceMinCount == 0f){
                        iv_order_min.setVisibility(View.GONE);
                    } else {
                        iv_order_min.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetBetweenTimeFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    orderInterval = data.getExtras().getString("orderInterval");
                    if(TextUtils.isEmpty(orderInterval)){
                        orderInterval = "3";
                        iv_betweentime.setVisibility(View.GONE);
                    } else {
                        iv_betweentime.setVisibility(View.VISIBLE);
                    }

                    LogUtils.print("orderInterval === " + orderInterval);

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetOrderReplaceConfirmFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    ReservePwd = data.getExtras().getString("ReservePwd");
                    OftenLoginProvince = data.getExtras().getString("OftenLoginProvince");
                    OftenLoginCity = data.getExtras().getString("OftenLoginCity");
                    LogUtils.print("ReserveAccount === " + ReserveAccount + " ReservePwd == " + ReservePwd + " OftenLoginProvince == " + OftenLoginProvince + " OftenLoginCity == " + OftenLoginCity);
                    if (TextUtils.isEmpty(ReservePwd)) {
                        IsAgentConfirm = "0";
                        iv_replace.setVisibility(View.GONE);
                    } else {
                        IsAgentConfirm = "1";
                        iv_replace.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetPicReviewFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    IsPicConfirm = data.getExtras().getString("IsPicConfirm");
                    LogUtils.print("IsPicConfirm === " + IsPicConfirm);
                    if (TextUtils.equals("0", IsPicConfirm)) {
                        iv_pic.setVisibility(View.GONE);
                    } else if (TextUtils.equals("1", IsPicConfirm)) {
                        iv_pic.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case ContainerActivity.SetRemarkFragmentTag:
                if (data == null || data.getExtras() == null) {
                    return;
                }
                try {
                    Remarks = data.getExtras().getString("Remarks");
                    LogUtils.print("Remarks === " + Remarks);

                } catch (Exception e) {

                }
                break;
        }
    }

    private String et_release_count_str, et_release_price_str, et_release_discount_str;

    @TextChange({R.id.et_recharge_qqnum, R.id.et_release_count, R.id.et_release_discount, R.id.et_release_price, R.id.et_role,R.id.et_mobile})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        String text = s.toString();
        switch (hello.getId()) {
            case R.id.et_mobile:
                mobile = text;
                break;
            case R.id.et_release_count:
                et_release_count_str = text;
                if (TextUtils.isEmpty(text)) {
                    et_release_price.setText("");
                    et_release_discount.setText("");
                    tv_release_count_prompt.setText(name + unitName + "=" + "Q币");
                    tv_function.setText("0英雄联盟点券=0Q币*0折=0元");
                }

                if (TextUtils.isEmpty(et_release_count_str) || TextUtils.isEmpty(et_release_discount_str) || TextUtils.isEmpty(et_release_price_str) || TextUtils.isEmpty(ReserveAccount)) {
                    Utils.setButtonClickable(btn_confirm, false);
                    return;
                } else {
                    if (isRole == 1) {
                        if (TextUtils.isEmpty(RoleName)) {
                            Utils.setButtonClickable(btn_confirm, false);
                            return;
                        }
                    }
                }
                Utils.setButtonClickable(btn_confirm, true);
                break;
            case R.id.et_release_discount:
                et_release_discount_str = text;
                if (TextUtils.isEmpty(et_release_count_str) || TextUtils.isEmpty(et_release_discount_str) || TextUtils.isEmpty(et_release_price_str) || TextUtils.isEmpty(ReserveAccount)) {
                    Utils.setButtonClickable(btn_confirm, false);
                    return;
                } else {
                    if (isRole == 1) {
                        if (TextUtils.isEmpty(RoleName)) {
                            Utils.setButtonClickable(btn_confirm, false);
                            return;
                        }
                    }
                }
                Utils.setButtonClickable(btn_confirm, true);
                break;
            case R.id.et_release_price:
                et_release_price_str = text;
                if (TextUtils.isEmpty(et_release_count_str) || TextUtils.isEmpty(et_release_discount_str) || TextUtils.isEmpty(et_release_price_str) || TextUtils.isEmpty(ReserveAccount)) {
                    Utils.setButtonClickable(btn_confirm, false);
                    return;
                } else {
                    if (isRole == 1) {
                        if (TextUtils.isEmpty(RoleName)) {
                            Utils.setButtonClickable(btn_confirm, false);
                            return;
                        }
                    }
                }
                Utils.setButtonClickable(btn_confirm, true);
                break;
            case R.id.et_recharge_qqnum:
                ReserveAccount = text;
                et_qq.setText(ReserveAccount);
                if (TextUtils.isEmpty(et_release_count_str) || TextUtils.isEmpty(et_release_discount_str) || TextUtils.isEmpty(et_release_price_str) || TextUtils.isEmpty(ReserveAccount)) {
                    Utils.setButtonClickable(btn_confirm, false);
                    return;
                } else {
                    if (isRole == 1) {
                        if (TextUtils.isEmpty(RoleName)) {
                            Utils.setButtonClickable(btn_confirm, false);
                            return;
                        }
                    }
                }
                Utils.setButtonClickable(btn_confirm, true);
                break;
            case R.id.et_role:
                RoleName = text;
                if (TextUtils.isEmpty(et_release_count_str) || TextUtils.isEmpty(et_release_discount_str) || TextUtils.isEmpty(et_release_price_str) || TextUtils.isEmpty(ReserveAccount)) {
                    Utils.setButtonClickable(btn_confirm, false);
                    return;
                } else {
                    if (isRole == 1) {
                        if (TextUtils.isEmpty(RoleName)) {
                            Utils.setButtonClickable(btn_confirm, false);
                            return;
                        }
                    }
                }
                Utils.setButtonClickable(btn_confirm, true);

                break;
        }
    }

    private BigDecimal qbCount;
    private BigDecimal inputCount;
    private BigDecimal disCount;
    private BigDecimal price;

    @FocusChange({R.id.et_release_count, R.id.et_release_discount, R.id.et_release_price})
    void focusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return;
        }

        dataFormat();

    }//focusChange end

    private void dataFormat(){
        /**
         * 三级联动，优先处理非格式化输入：
         * 1.预定数量的值换算成Q币非整数，Q币向上取整，然后再得出预定数量
         * 2.折扣不在65-99之间，就近取值，折扣非整数，四舍五入
         * 3.价格保留两位小数点,后三位四舍五入
         *
         * 格式化完毕后视情况（有两个有值）进行两两运算，（三个都有值的情况）优先级为数量和折扣算价格
         * */

        if (!TextUtils.isEmpty(et_release_count_str)) {
            //如果计算出QB不为整数，则QB向上取整，再反算出预定数量
            inputCount = new BigDecimal(et_release_count_str);
            BigDecimal[] yu = inputCount.divideAndRemainder(convertCountBig);
            if (yu[1].compareTo(new BigDecimal(0)) == 0) {
                //余数等于0
                if(yu[0].compareTo(new BigDecimal(20000)) == 1 || yu[0].compareTo(new BigDecimal(20000)) == 0){
                    //QB大于等于20000
                    BigDecimal bigDecimal_2w = new BigDecimal(20000);
                    inputCount = bigDecimal_2w.divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);
                    if (et_release_count != null) {
                        et_release_count.setText(inputCount + "");
                    }
                } else {
                    qbCount = yu[0].multiply(qbConvertCountBig);
                }

                if (tv_release_count_prompt != null) {
                    tv_release_count_prompt.setText(name + unitName + "=" + qbCount + "Q币");
                }

            } else {
                //余数不等于0
                qbCount = (yu[0].add(new BigDecimal(1))).multiply(qbConvertCountBig);
                if(qbCount.compareTo(new BigDecimal(20000)) == 1||qbCount.compareTo(new BigDecimal(20000)) == 0){
                    //QB大于等于20000
                    BigDecimal bigDecimal_2w = new BigDecimal(20000);
                    inputCount = bigDecimal_2w.divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);
                } else {
                    inputCount = qbCount.multiply(convertCountBig);
                }

                if (tv_release_count_prompt != null && et_release_count != null) {
                    tv_release_count_prompt.setText(name + unitName + "=" + qbCount + "Q币");
                    et_release_count.setText(inputCount + "");
                }
            }
        }

        if (!TextUtils.isEmpty(et_release_discount_str)) {
            disCount = new BigDecimal(et_release_discount_str);
            if (disCount.compareTo(new BigDecimal(65)) == -1) {
                //折扣小于65
                if (et_release_discount != null) {
                    et_release_discount.setText("65");
                }
            } else if (disCount.compareTo(new BigDecimal(99)) == 1) {
                //折扣大于99
                if (et_release_discount != null) {
                    et_release_discount.setText("99");
                }
            } else {
                disCount = disCount.setScale(0, BigDecimal.ROUND_HALF_UP);
                if (et_release_discount != null) {
                    et_release_discount.setText(disCount.toString());
                }
            }

        }

        if (!TextUtils.isEmpty(et_release_price_str)) {
            price = new BigDecimal(et_release_price_str);
            price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (et_release_price != null) {
                et_release_price.setText(price.toString());
            }

        }

        //上边3段代码已经对数据进行格式化，下边的计算条件可以不考虑非格式化的情况，（当然计算结果还是需要考虑非格式化，因为计算结果是新计算出来的）
        if (!TextUtils.isEmpty(et_release_count_str) && !TextUtils.isEmpty(et_release_discount_str)) {
            //根据数量 和 折扣 算出价， QB数量和折扣不必考虑非格式化的情况
            price = qbCount.multiply(disCount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            if (et_release_price != null) {
                et_release_price.setText(price + "");
            }

        } else if (!TextUtils.isEmpty(et_release_count_str) && !TextUtils.isEmpty(et_release_price_str)) {
            //根据数量 和 出价 算折扣，折扣作为计算结果，有可能是非格式化的值，如果是非格式化的值，需要先把折扣进行格式化，然后再和QB数量反算出价格
            disCount = price.multiply(new BigDecimal(100)).divide(qbCount,0,BigDecimal.ROUND_DOWN);
            if (disCount.compareTo(new BigDecimal(65)) == -1) {
                //折扣小于65
                price = qbCount.multiply(new BigDecimal(65)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                if (et_release_price != null && et_release_discount != null) {
                    et_release_price.setText(price + "");
                    et_release_discount.setText("65");
                }
            } else if (disCount.compareTo(new BigDecimal(99)) == 1) {
                //折扣大于99
                price = qbCount.multiply(new BigDecimal(99)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                if (et_release_price != null && et_release_discount != null) {
                    et_release_price.setText(price + "");
                    et_release_discount.setText("99");
                }
            } else {
                //这里是考虑到折扣可能会有小数点，所以先取整，然后再算一次价格
                disCount = disCount.setScale(0, BigDecimal.ROUND_HALF_UP);
                price = qbCount.multiply(disCount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                if (et_release_price != null && et_release_discount != null) {
                    et_release_price.setText(price.toString());
                    et_release_discount.setText(disCount.toString());
                }
            }

        } else if (!TextUtils.isEmpty(et_release_discount_str) && !TextUtils.isEmpty(et_release_price_str)) {
            //根据折扣 和 出价 算QB数量以及预定数量，QB数量作为计算结果，可能时非整数，所以需要先取整，然后再根据QB数量和折扣算一次价格
            BigDecimal[] yu = price.multiply(new BigDecimal(100)).divideAndRemainder(disCount);

            if (yu[1].compareTo(new BigDecimal(0)) == 0) {
                //余数等于0
                if(yu[0].compareTo(new BigDecimal(20000)) == 1 || yu[0].compareTo(new BigDecimal(20000)) == 0){
                    //大于等于2W
                    BigDecimal bigDecimal_2w = new BigDecimal(20000);
                    inputCount = bigDecimal_2w.divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);

                } else {
                    inputCount = yu[0].divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);
                }

            } else {
                //余数不等于0
                qbCount = (yu[0].add(new BigDecimal(1))).multiply(qbConvertCountBig);
                if(qbCount.compareTo(new BigDecimal(20000)) == 1 || qbCount.compareTo(new BigDecimal(20000)) == 0){
                    //QB大于等于20000
                    BigDecimal bigDecimal_2w = new BigDecimal(20000);
                    inputCount = bigDecimal_2w.divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);
                } else {
                    inputCount = yu[0].divide(convertCountBig).divide(qbConvertCountBig,0,BigDecimal.ROUND_DOWN);
                    qbCount = inputCount.multiply(convertCountBig).multiply(qbConvertCountBig);
                }
            }

            price = qbCount.multiply(disCount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);

            if (tv_release_count_prompt != null && et_release_count != null && et_release_price != null) {
                tv_release_count_prompt.setText(name + unitName + "=" + qbCount + "Q币");
                et_release_count.setText(inputCount.toString());
                et_release_price.setText(price.toString());
            }
        }

        if (tv_function != null && inputCount != null && disCount != null && price != null) {
            tv_function.setText(inputCount + name + unitName + "=" + qbCount + "币*" + disCount + "折=" + price + "元");
        }
    }


    public void initPopWindow_9(String gameName, final OnClickListener onClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(getActivity() == null){
            return;
        }
        View mPopView = getActivity().getLayoutInflater().inflate(R.layout.pop_orderrelease, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_9 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_9.setOutsideTouchable(true);
        mPopupWindow_9.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_9.setFocusable(true);
        mPopupWindow_9.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(getActivity(), 1f);
            }
        });

        final CheckBox cb_1 = (CheckBox)mPopView.findViewById(R.id.cb_1);
        cb_1.setText("我今天没有充值相同数量的"+gameName+"点券");
        final CheckBox cb_2 = (CheckBox)mPopView.findViewById(R.id.cb_2);
        final CheckBox cb_3 = (CheckBox)mPopView.findViewById(R.id.cb_3);
        final Button btn_confirm_pop = (Button)mPopView.findViewById(R.id.btn_confirm_pop);
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_2.isChecked() && cb_3.isChecked()){
                        Utils.setButtonClickable(btn_confirm_pop,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm_pop,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm_pop,false);
                }
            }
        });
        cb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_1.isChecked() && cb_3.isChecked()){
                        Utils.setButtonClickable(btn_confirm_pop,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm_pop,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm_pop,false);
                }
            }
        });
        cb_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_1.isChecked() && cb_2.isChecked()){
                        Utils.setButtonClickable(btn_confirm_pop,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm_pop,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm_pop,false);
                }
            }
        });

        btn_confirm_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb_1.isChecked()){
                    Toast.makeText(mContext,"请勾选以上3个选项后再发布订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!cb_2.isChecked()){
                    Toast.makeText(mContext,"请勾选以上3个选项后再发布订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!cb_3.isChecked()){
                    Toast.makeText(mContext,"请勾选以上3个选项后再发布订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
                if (mPopupWindow_9.isShowing()) {
                    mPopupWindow_9.dismiss();
                }
            }
        });

        mPopupWindow_9.setAnimationStyle(R.style.pop_animation_up);
        // 作为下拉视图显示
        mPopupWindow_9.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        Utils.backgroundAlpha(getActivity(), 0.7f);

    }

}
