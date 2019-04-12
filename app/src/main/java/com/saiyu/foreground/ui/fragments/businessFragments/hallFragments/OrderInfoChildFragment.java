package com.saiyu.foreground.ui.fragments.businessFragments.hallFragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MyArrayAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments.OrderRechargeFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.List;

@EFragment(R.layout.fragment_orderinfo_child)
public class OrderInfoChildFragment extends BaseFragment {

    @ViewById
    TextView tv_recharge_account, tv_copy, tv_recharge_game, tv_recharge_scale, tv_recharge_place, tv_recharge_count, tv_friend, tv_remark, tv_get, tv_recharge_prompt;
    @ViewById
    Button btn_recharge, btn_copy, btn_revise;
    @ViewById
    EditText et_count;
    @ViewById
    Spinner sp_selector;
    private String RechargeUrl, ReceiveQBCount;
    private int OnceMinCount = 0;
    public static List<HallDetailRet.DataBean.YanBaoBean> selectorList = null;
    private MyArrayAdapter selectorAdapter;
    private int type;
    private String ReserveDiscount, ServiceRate;
    private float LessChargeDiscount;

    public static OrderInfoChildFragment newInstance(Bundle bundle) {
        OrderInfoChildFragment_ fragment = new OrderInfoChildFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ReceiveQBCount = bundle.getString("ReceiveQBCount");
            ReserveDiscount = bundle.getString("ReserveDiscount");//折扣
            ServiceRate = bundle.getString("ServiceRate");//服务费率
            LessChargeDiscount = bundle.getFloat("LessChargeDiscount");

            OnceMinCount = bundle.getInt("OnceMinCount");

            tv_recharge_account.setText(bundle.getString("ReserveAccount"));
            tv_recharge_game.setText(bundle.getString("ProductName"));
            tv_recharge_scale.setText(bundle.getString("OfficialRatio"));
            tv_recharge_place.setText(bundle.getString("ProductPropertyStr"));
            tv_recharge_count.setText(bundle.getString("RechargeNum"));
            tv_remark.setText(bundle.getString("Remarks"));

            tv_get.setText(setFormula(ReceiveQBCount));
            LogUtils.print("服务费：" + bundle.getString("ServiceMoney") + " 违约金: " + bundle.getString("LiquidatedMoney") + " 总计: " + bundle.getString("TotalMoney"));

            RechargeUrl = bundle.getString("RechargeUrl");
            if (bundle.getBoolean("IsFriendLimit", false)) {
                tv_friend.setText("需要添加好友");
            } else {
                tv_friend.setText("无需添加好友");
            }

            type = bundle.getInt("type", 0);
            initYuanBaoList(type);

        }
    }

    @Click({R.id.tv_copy, R.id.btn_recharge, R.id.btn_copy, R.id.btn_revise})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_revise:
                switch (type) {
                    case 0:
                        et_count.setText(ReceiveQBCount);
                        if (!TextUtils.isEmpty(ReceiveQBCount)) {
                            et_count.setSelection(ReceiveQBCount.length());
                        }
                        break;
                    case 1:
                    case 2:
                        for (int i = 0; i < selectorList.size(); i++) {
                            if (ReceiveQBCount.equals(selectorList.get(i).getValue())) {
                                sp_selector.setSelection(i);
                                break;
                            }
                        }
                        break;
                }
                break;
            case R.id.tv_copy:
                Utils.copyContent(tv_recharge_account.getText().toString());
                break;
            case R.id.btn_recharge:
                if (!TextUtils.isEmpty(RechargeUrl)) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext, ContainerActivity_.class);
                    bundle.putString("url", RechargeUrl);
                    bundle.putString("type", "充值");//
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.btn_copy:
                Utils.copyContent(RechargeUrl);
                break;
        }
    }

    @TextChange({R.id.et_count})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        String text = s.toString();
        isCountAble(text);
    }

    //实际充值数量是否可用
    private void isCountAble(String count) {
        if (TextUtils.isEmpty(count)) {
            return;
        }

        float qbCount = Float.parseFloat(count);
        if (qbCount < OnceMinCount) {
            tv_recharge_prompt.setText("最低成功数量为" + OnceMinCount + "Q币，请修改数量！");
            btn_revise.setVisibility(View.VISIBLE);
            btn_revise.setText("取消修改");
            CallbackUtils.doContentCallback("0");
            return;
        } else {
            tv_recharge_prompt.setText("");
            btn_revise.setVisibility(View.INVISIBLE);
        }

        float param = 0;

        if (!TextUtils.isEmpty(ReceiveQBCount)) {
            param = Float.parseFloat(ReceiveQBCount);
        }

        if (qbCount > param) {
            tv_recharge_prompt.setText("不允许输入大于接单数量，如果多充请另行接单！");
            btn_revise.setVisibility(View.VISIBLE);
            btn_revise.setText("新建此单");
            CallbackUtils.doContentCallback("0");
            return;
        } else {
            tv_recharge_prompt.setText("");
            btn_revise.setVisibility(View.INVISIBLE);
        }

        if (qbCount != param && LessChargeDiscount != 1) {
            tv_recharge_prompt.setText("低于接单数量视为违约，将扣除5%违约金，请慎重！");
        }

        tv_get.setText(setFormula(count));

        CallbackUtils.doContentCallback(count);
    }

    //计算公式
    private Spanned setFormula(String param_1) {
        BigDecimal bd_1 = new BigDecimal(param_1);//充值金额
        BigDecimal bd_2 = new BigDecimal(ReserveDiscount);//折扣
        BigDecimal bd_3 = new BigDecimal(ServiceRate);//服务费率
        BigDecimal bd_4 = bd_1.multiply(bd_2).setScale(2, BigDecimal.ROUND_HALF_UP);//产品金额
        BigDecimal bd_6 = bd_4.multiply(bd_3).setScale(2, BigDecimal.ROUND_HALF_UP);//服务费
        BigDecimal bd_7;
        if (LessChargeDiscount == 1) {
            //没有违约金
            bd_7 = new BigDecimal("0");
        } else {
            //有违约金
            BigDecimal bd_5 = new BigDecimal(LessChargeDiscount);
            bd_7 = bd_4.multiply(bd_5).setScale(2, BigDecimal.ROUND_HALF_UP);//违约金
        }
        BigDecimal bd_8 = bd_4.subtract(bd_6).subtract(bd_7);

        String text = "获得: " + "<font color = \"#148cf1\">" + param_1 + "</font>" + "Q币 * " + "<font color = \"#148cf1\">" + ReserveDiscount + "</font>" + "折 - " + "<font color = \"#148cf1\">" + bd_6.toString() + "</font>" + "元服务费 - " + "<font color = \"#148cf1\">" + bd_7.toString() + "</font>" + "元违约金 = " + "<font color = \"#148cf1\">" + bd_8.toString() + "</font>" + "元";
        return Html.fromHtml(text);
    }

    //初始化输入框
    private void initYuanBaoList(int type){
        LogUtils.print("type === " + type);
        switch (type) {
            case 0:
                et_count.setText(ReceiveQBCount);
                et_count.setVisibility(View.VISIBLE);
                sp_selector.setVisibility(View.GONE);
                break;
            case 1://金元宝
            case 2://银元宝
                Bundle bundle = ((ContainerActivity)getActivity()).getIntent().getExtras();
                if(bundle != null){
                    int fragmentTag = bundle.getInt(ContainerActivity.FragmentTag,0);
                    if(fragmentTag == ContainerActivity.WaitingRechargeOrderFragmentTag){
                        selectorList = ((OrderRechargeFragment) getParentFragment()).getSelectorList();
                    } else if(fragmentTag == ContainerActivity.HallOrderDetailFragmentTag){
                        selectorList = ((HallOrderDetailFragment) getParentFragment()).getSelectorList();
                    }
                }

                et_count.setVisibility(View.GONE);
                sp_selector.setVisibility(View.VISIBLE);
                selectorAdapter = new MyArrayAdapter(selectorList);
                sp_selector.setAdapter(selectorAdapter);

                for (int i = 0; i < selectorList.size(); i++) {
                    if (ReceiveQBCount.equals(selectorList.get(i).getValue())) {
                        sp_selector.setSelection(i);
                        break;
                    }
                }

                sp_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (selectorList == null || selectorList.size() <= position) {
                            return;
                        }
                        String rechargeNum = selectorList.get(position).getValue();
                        isCountAble(rechargeNum);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;

        }

    }

}
