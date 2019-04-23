package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.CashAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_cash)
public class CashFragment extends BaseFragment implements CallbackUtils.OnPositionListener,CallbackUtils.ResponseCallback,CallbackUtils.OnActivityCallBack,CashAdapter.OnItemClickListener,CashAdapter.OnSwipeListener,CashAdapter.OnItemLongClickListener {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content,tv_prompt,tv_get,tv_able;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    LinearLayout ll_add;
    @ViewById
    EditText et_count;
    private CashAdapter cashAdapter;
    private List<CashRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private CashRet.DatasBean.ItemsBean curItem,reviseItem;
    private String Mobile,RealName;
    private int RiskLevel;
    private boolean IsMobileVerification;
    private String totalMoney = "0.00";

    public static CashFragment newInstance(Bundle bundle) {
        CashFragment_ fragment = new CashFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnPositionListener(this);
        CallbackUtils.setOnActivityCallBack(this);
        ApiRequest.cash("CashFragment_cash",pb_loading);

    }

    @AfterViews
    void afterView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            Mobile = bundle.getString("Mobile");
            RealName = bundle.getString("RealName");
            RiskLevel = bundle.getInt("RiskLevel");
        }
        tv_title_content.setText("我要提现");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));

        cashAdapter = new CashAdapter(mItems);
        recyclerView.setAdapter(cashAdapter);
        cashAdapter.notifyDataSetChanged();

        Utils.setButtonClickable(btn_confirm,false);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("CashFragment_cash")){
            CashRet ret = (CashRet)baseRet;
            if(ret.getData() == null || ret.getData().getWithdrawWayAccountList() == null){
                return;
            }

            IsMobileVerification = ret.getData().isMobileVerification();
            totalMoney = ret.getData().getTotalMoney();
            tv_able.setText(totalMoney + "元");

            mItems.clear();
            mItems.addAll(ret.getData().getWithdrawWayAccountList());

            if(mItems.size() >= 5){
                ll_add.setVisibility(View.GONE);
            } else {
                ll_add.setVisibility(View.VISIBLE);
            }

            cashAdapter = new CashAdapter(mItems);
            cashAdapter.setOnDelListener(this);
            cashAdapter.setOnItemClickListener(this);
            cashAdapter.setOnItemLongClickListener(this);
            recyclerView.setAdapter(cashAdapter);
            cashAdapter.notifyDataSetChanged();
        } else if(method.equals("CashFragment_delCashAccount")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                ApiRequest.cash("CashFragment_cash",pb_loading);

            }
        } else if(method.equals("CashFragment_submitCash")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"申请提现成功",Toast.LENGTH_SHORT).show();

            }
        } else if(method.equals("CashFragment_cashWays")){
            CashChannelRet ret = (CashChannelRet)baseRet;
            if(ret.getData() == null || ret.getData().getWithdrawWaysList() == null ||ret.getData().getWithdrawWaysList().size() <= 0 || reviseItem == null){
                return;
            }

            DialogUtils.showCashChannelReviseDialog(getActivity(), reviseItem, ret.getData().getWithdrawWaysList(), new OnListCallbackListener() {
                @Override
                public void setOnListCallbackListener(List<String> callbackList) {
                    if(callbackList == null || callbackList.size() < 5){
                        return;
                    }
                    int type = Integer.parseInt(callbackList.get(4));
                    switch (type){
                        case 0://银行
                        case 1://支付宝
                            if(RiskLevel > 3){
                                //风控等级为4级/5级，随便添加/修改提现渠道
                                ApiRequest.addCashAccount(callbackList.get(0),callbackList.get(1),"",callbackList.get(2),callbackList.get(3),"CashFragment_addCashAccount",pb_loading);
                            } else {
                                //风控等级为1 2 3级，修改/添加渠道需要验证码
                                Bundle bundle = new Bundle();
                                bundle.putString("Mobile",Mobile);
                                bundle.putString("Id",callbackList.get(0));
                                bundle.putString("account",callbackList.get(1));
                                bundle.putString("remarks",callbackList.get(2));
                                bundle.putString("accountId",callbackList.get(3));
                                if(type == 0){
                                    bundle.putString("title","修改银行卡");
                                } else if(type == 1){
                                    bundle.putString("title","修改支付宝");
                                }
                                AddCashChannelWithSendMsgFragment addCashChannelWithSendMsgFragment = AddCashChannelWithSendMsgFragment.newInstance(bundle);
                                start(addCashChannelWithSendMsgFragment);
                            }
                            break;
                        case 2:
                            //修改成微信后所有操作由服务端完成，客户端只需要展示二维码即可，客户关闭修改弹窗后刷新渠道列表
                            ApiRequest.cash("CashFragment_cash",pb_loading);
                            break;
                    }
                }
            });
        } else if(method.equals("CashFragment_addCashAccount")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                ApiRequest.cash("CashFragment_cash",pb_loading);

            }
        }
    }

    @Click({R.id.btn_title_back,R.id.ll_add,R.id.btn_confirm,R.id.tv_all})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.ll_add:
                    if(RiskLevel <= 3 && TextUtils.isEmpty(Mobile)){
                        //风控等级为1 2 3级，修改/添加渠道需要验证码
                        DialogUtils.showDialog(getActivity(), "提示", "您的等级达到3级，添加提现渠道需要手机验证，是否绑定手机？", "取消", "绑定手机", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Bundle bundle = new Bundle();
                                final Intent intent = new Intent(mContext,ContainerActivity_.class);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivityForResult(intent,ContainerActivity.CashFragmentTag);
                            }
                        });
                        return;
                    }
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Mobile",Mobile);
                    bundle.putString("RealName",RealName);
                    bundle.putInt("RiskLevel",RiskLevel);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.CashChannelListFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_confirm:
                    String money = et_count.getText().toString();
                    if(TextUtils.isEmpty(money)){
                        Toast.makeText(mContext,"请输入提现金额",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(curItem == null || TextUtils.isEmpty(curItem.getId())){
                        Toast.makeText(mContext,"请选择提现渠道",Toast.LENGTH_SHORT).show();
                        LogUtils.print("curItem === " + curItem);
                        return;
                    }
                    if(IsMobileVerification){
                        if(TextUtils.isEmpty(Mobile)){
                            //风控等级为1 2 3级，第一次提现需要验证码
                            DialogUtils.showDialog(getActivity(), "提示", "您的等级达到3级，提现需要手机验证，是否绑定手机？", "取消", "绑定手机", new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Bundle bundle = new Bundle();
                                    final Intent intent = new Intent(mContext,ContainerActivity_.class);
                                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                    intent.putExtras(bundle);
                                    mContext.startActivityForResult(intent,ContainerActivity.CashFragmentTag);
                                }
                            });
                            return;
                        }
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Mobile",Mobile);
                        bundle1.putString("money",money);
                        bundle1.putString("Id",curItem.getId());
                        SubmitCashByMobileFragment submitCashByMobileFragment = SubmitCashByMobileFragment.newInstance(bundle1);
                        start(submitCashByMobileFragment);
                    } else {
                        ApiRequest.submitCash(curItem.getId(),money,"","CashFragment_submitCash",pb_loading);
                    }

                    break;
                case R.id.tv_all:
                    if(curItem == null){
                        return;
                    }
                    et_count.setText(totalMoney);
                    break;
            }
        }
    }

    @Override
    public void onItemClick(CashAdapter cashAdapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(CashAdapter cashAdapter, View view, final int pos) {
        if(mItems == null || mItems.size() <= pos || mItems.get(pos) == null || TextUtils.isEmpty(mItems.get(pos).getAccount())){
            return;
        }
        DialogUtils.showDialog(getActivity(), "提示", "确定删除该账号？", "取消", "确定", new OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRequest.delCashAccount(mItems.get(pos).getId(),"","CashFragment_delCashAccount",pb_loading);
            }
        });
    }

    @Override
    public void onRevise(CashAdapter cashAdapter, int pos) {
        if(RiskLevel <= 3 && TextUtils.isEmpty(Mobile)){
            //风控等级为1 2 3级，修改/添加渠道需要验证码
            DialogUtils.showDialog(getActivity(), "提示", "您的等级达到3级，添加提现渠道需要手机验证，是否绑定手机？", "取消", "绑定手机", new OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Bundle bundle = new Bundle();
                    final Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivityForResult(intent,ContainerActivity.CashFragmentTag);
                }
            });
            return;
        }

        if(mItems == null || mItems.size() <= pos || mItems.get(pos) == null){
            return;
        }
        reviseItem = mItems.get(pos);
        ApiRequest.cashWays("CashFragment_cashWays",pb_loading);
    }

    @Override
    public void onDelete(CashAdapter cashAdapter, final int pos) {
        if(mItems == null || mItems.size() <= pos || mItems.get(pos) == null || TextUtils.isEmpty(mItems.get(pos).getAccount())){
            return;
        }
        DialogUtils.showDialog(getActivity(), "提示", "确定删除该账号？", "取消", "确定", new OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRequest.delCashAccount(mItems.get(pos).getId(),"","CashFragment_delCashAccount",pb_loading);
            }
        });
    }

    @TextChange({R.id.et_count})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        String curmoney = s.toString();
        if(TextUtils.isEmpty(curmoney) || curItem == null || TextUtils.isEmpty(curItem.getCharge())|| TextUtils.isEmpty(curItem.getMinApplyMoney())){
            LogUtils.print("数据异常");
            return;
        }
        BigDecimal bigDecimal_1 = new BigDecimal(curItem.getCharge());
        BigDecimal bigDecimal_2 = new BigDecimal(curmoney);
        BigDecimal bigDecimal_3 = bigDecimal_1.multiply(bigDecimal_2);
        BigDecimal bigDecimal_100 = new BigDecimal(100);
        BigDecimal bigDecimal_4 = bigDecimal_3.divide(bigDecimal_100,2,BigDecimal.ROUND_HALF_UP);
        BigDecimal bigDecimal_5 = bigDecimal_2.subtract(bigDecimal_4);
        tv_get.setText(bigDecimal_5 + "元");

        BigDecimal bigDecimal_6 = new BigDecimal(curItem.getMinApplyMoney());

        switch (bigDecimal_2.compareTo(bigDecimal_6)){
            case -1://小于
                Utils.setButtonClickable(btn_confirm,false);
                break;
            case 0://等于
            case 1://大于
                Utils.setButtonClickable(btn_confirm,true);
                break;
        }


    }

    @Override
    public void setOnPositionListener(int pos) {
        if(mItems == null || mItems.size() <= pos || mItems.get(pos) == null){
            return;
        }

        curItem = mItems.get(pos);

        String count = et_count.getText().toString();
        if(!TextUtils.isEmpty(curItem.getCharge())&& !TextUtils.isEmpty(count) && !TextUtils.isEmpty(curItem.getMinApplyMoney())){
            BigDecimal bigDecimal_1 = new BigDecimal(curItem.getCharge());
            BigDecimal bigDecimal_2 = new BigDecimal(count);
            BigDecimal bigDecimal_3 = bigDecimal_1.multiply(bigDecimal_2);
            BigDecimal bigDecimal_100 = new BigDecimal(100);
            BigDecimal bigDecimal_4 = bigDecimal_3.divide(bigDecimal_100,2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bigDecimal_5 = bigDecimal_2.subtract(bigDecimal_4);
            tv_get.setText(bigDecimal_5 + "元");

            BigDecimal bigDecimal_6 = new BigDecimal(curItem.getMinApplyMoney());

            switch (bigDecimal_2.compareTo(bigDecimal_6)){
                case -1://小于
                    Utils.setButtonClickable(btn_confirm,false);
                    break;
                case 0://等于
                case 1://大于
                    Utils.setButtonClickable(btn_confirm,true);
                    break;
            }

        }

        tv_prompt.setText("当前渠道，最低提现"+mItems.get(pos).getMinApplyMoney()+"元,最低手续费"+mItems.get(pos).getStartMoney()+"元，封顶手续费"+mItems.get(pos).getTopMoney()+"元");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("resultCode ==== " + resultCode + " requestCode ==== " + requestCode);
        if(resultCode == RESULT_OK && requestCode == ContainerActivity.CashFragmentTag && data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Mobile = bundle.getString("Mobile");
            }
        }
    }
}
