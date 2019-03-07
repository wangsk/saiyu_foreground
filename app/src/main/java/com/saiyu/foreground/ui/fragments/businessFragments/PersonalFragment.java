package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.SytemNotifyAdapter;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.FaceStatusRet;
import com.saiyu.foreground.https.response.RealNameStatusRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.activitys.MainActivity;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    LinearLayout ll_active_seller,ll_active_buyer,ll_seller_info,ll_buyer_info;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    ImageView iv_seting,iv_identity,iv_face;
    @ViewById
    CircleImageView civ_headicon;
    @ViewById
    TextView tv_account,tv_seller_order,tv_buyer_pre,tv_remain,tv_active_seller,tv_seller_wait_pay,tv_seller_order_ing,tv_seller_order_wait,tv_seller_order_totle,tv_active_buyer,
            tv_buyer_time,tv_buyer_order_ing,tv_buyer_order_wait,tv_buyer_order_totle;
    @ViewById
    Button btn_mem_info,btn_wad_info,btn_security_manage,btn_unionid_login,btn_recharge,
            btn_cash,btn_recharge_record,btn_cash_record,btn_identity,btn_face;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private AccountInfoLoginRet accountInfoLoginRet;
    private SytemNotifyAdapter sytemNotifyAdapter;
    private List<AccountInfoLoginRet.DataBean.ItemsBean> mItems = new ArrayList<>();

    public static PersonalFragment newInstance(Bundle bundle) {
        PersonalFragment_ fragment = new PersonalFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getAccountInfoLogin("PersonalFragment_getAccountInfoLogin",pb_loading);

    }

    @AfterViews
    void afterView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        sytemNotifyAdapter = new SytemNotifyAdapter(mItems);
        recyclerView.setAdapter(sytemNotifyAdapter);
        sytemNotifyAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PersonalFragment_getAccountInfoLogin")) {
            AccountInfoLoginRet ret = (AccountInfoLoginRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            accountInfoLoginRet = ret;
            tv_account.setText(ret.getData().getAccount());

            tv_remain.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getTotalMoney() + "</font>" + "元"));
            tv_buyer_pre.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserBuyerCount() + "</font>" + "单" + "<font color = \"#148cf1\">" + ret.getData().getUserBuyerMoney() + "</font>" + "元"));
            tv_seller_order.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserSellerCount() + "</font>" + "单" + "<font color = \"#148cf1\">" + ret.getData().getUserSellerMoney() + "</font>" + "元"));

            boolean UserBuyerStatus = ret.getData().isUserBuyerStatus();
            boolean UserSellerStatus = ret.getData().isUserSellerStatus();

            if(UserBuyerStatus){
                tv_buyer_time.setText("平均确认时间:" + ret.getData().getAverageConfirmTime());
                tv_buyer_order_ing.setText("预定中的订单:" + ret.getData().getUserBuyerCount() + "单" + ret.getData().getUserBuyerMoney() + "元");
                tv_buyer_order_wait.setText("待确认的订单:" + ret.getData().getUserBuyerWaitConfirmOrdersCount() + "单" + ret.getData().getUserBuyerWaitConfirmOrdersMoney() + "元");
                tv_buyer_order_totle.setText("累计成交订单:" + ret.getData().getUserBuyerOrderRSettleTotalCount() + "单" + ret.getData().getUserBuyerOrderRSettleTotalMoney() + "元");
                tv_active_buyer.setVisibility(View.GONE);
                ll_buyer_info.setVisibility(View.VISIBLE);
            } else {
                tv_active_buyer.setVisibility(View.VISIBLE);
                ll_buyer_info.setVisibility(View.GONE);
            }
            if(UserSellerStatus){
                tv_seller_wait_pay.setText("待充值订单:" + ret.getData().getUserSellerWaitRechargeOrdersCount() + "单" + ret.getData().getUserSellerWaitRechargeOrdersMoney() + "元");
                tv_seller_order_ing.setText("审核中的订单:" + ret.getData().getUserSellerAuditOrdersCount() + "单" + ret.getData().getUserSellerAuditOrdersMoney() + "元");
                tv_seller_order_wait.setText("待确认的订单:" + ret.getData().getUserSellerWaitConfirmOrdersCount() + "单" + ret.getData().getUserSellerWaitConfirmOrdersMoney() + "元");
                tv_seller_order_totle.setText("累计出售订单:" + ret.getData().getUserSellerOrderRSettleTotalCount() + "单" + ret.getData().getUserSellerOrderRSettleTotalMoney() + "元");
                tv_active_seller.setVisibility(View.GONE);
                ll_seller_info.setVisibility(View.VISIBLE);
            } else {
                tv_active_seller.setVisibility(View.VISIBLE);
                ll_seller_info.setVisibility(View.GONE);
            }

            if(!(UserBuyerStatus && UserSellerStatus) && !(!UserBuyerStatus && !UserSellerStatus)){
                if(!UserBuyerStatus){
                    ((MainActivity) getActivity()).getBottomBar().hide(3);
                    SPUtils.putInt(ConstValue.MainBottomVisibleType,1);//不显示买家
                }
                if(!UserSellerStatus){
                    ((MainActivity) getActivity()).getBottomBar().hide(2);
                    SPUtils.putInt(ConstValue.MainBottomVisibleType,2);//不显示卖家
                }
            } else {
                SPUtils.putInt(ConstValue.MainBottomVisibleType,0);//全部显示
                if(((MainActivity) getActivity()).getBottomBar().checkStatus(3) == View.GONE){
                    ((MainActivity) getActivity()).getBottomBar().show(3);
                }
                if(((MainActivity) getActivity()).getBottomBar().checkStatus(2) == View.GONE){
                    ((MainActivity) getActivity()).getBottomBar().show(2);
                }
            }

            if(ret.getData().getXttzlist() != null && ret.getData().getXttzlist().size() > 0){
                mItems.clear();
                mItems.addAll(ret.getData().getXttzlist());
                sytemNotifyAdapter = new SytemNotifyAdapter(mItems);
                recyclerView.setAdapter(sytemNotifyAdapter);
                sytemNotifyAdapter.notifyDataSetChanged();
                sytemNotifyAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String url = mItems.get(position).getNewsDetailUrl();
                        if(!TextUtils.isEmpty(url)){
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(mContext,ContainerActivity_.class);
                            bundle.putString("url", url);
                            bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }

        } else if(method.equals("PersonalFragment_realNameStatus")){
            RealNameStatusRet ret = (RealNameStatusRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            int status = ret.getData().getRealNameStatus();
            switch (status){
                case 0://正在申请中
                    Toast.makeText(mContext,"您有实名认证任务正在处理中，请稍后",Toast.LENGTH_LONG).show();
                    break;
                case 1://通过
                    Toast.makeText(mContext,"您已经刷脸认证通过",Toast.LENGTH_LONG).show();
                    break;
                case 2://失败
                case 3://未申请
                    if(accountInfoLoginRet != null && accountInfoLoginRet.getData() != null){
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(mContext,ContainerActivity_.class);
                        bundle.putString("RealName",accountInfoLoginRet.getData().getRealName());
                        bundle.putString("IDNum",accountInfoLoginRet.getData().getIDNum());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.UploadIdentityFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                    break;
            }
        } else if(method.equals("PersonalFragment_faceStatus")){
            FaceStatusRet ret = (FaceStatusRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            int status = ret.getData().getFaceAuthStatus();
            switch (status){
                case 0:
                    Toast.makeText(mContext,"您有刷脸认证任务正在处理中，请稍后",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(mContext,"您已经刷脸认证通过",Toast.LENGTH_LONG).show();
                    break;
                case 2://认证失败
                case 3://没有认证申请记录
                    if(accountInfoLoginRet != null && accountInfoLoginRet.getData() != null){
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(mContext,ContainerActivity_.class);
                        bundle.putString("RealName",accountInfoLoginRet.getData().getRealName());
                        bundle.putString("IDNum",accountInfoLoginRet.getData().getIDNum());
                        bundle.putBoolean("isFaceIdentify",true);
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.FaceIdentifyFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                    break;
            }
        }
    }

    @Click({R.id.iv_seting,R.id.btn_mem_info,R.id.btn_wad_info,R.id.btn_security_manage,R.id.btn_unionid_login,R.id.ll_active_seller,
            R.id.ll_active_buyer,R.id.btn_recharge,R.id.btn_cash,R.id.btn_recharge_record,R.id.btn_cash_record,R.id.btn_identity,R.id.btn_face})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            final Bundle bundle = new Bundle();
            final Intent intent = new Intent(mContext,ContainerActivity_.class);
            switch (view.getId()) {
                case R.id.iv_seting:
                    //设置
                    if(accountInfoLoginRet != null && accountInfoLoginRet.getData() != null){
                        bundle.putString("account",accountInfoLoginRet.getData().getAccount());
                    }
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SetingFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_mem_info:
                    //会员资料
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.MemberInfoFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_wad_info:
                    //资料补填
                    if(accountInfoLoginRet != null && accountInfoLoginRet.getData() != null){
                        bundle.putString("RealName",accountInfoLoginRet.getData().getRealName());
                        bundle.putString("IDNum",accountInfoLoginRet.getData().getIDNum());
                    }
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_security_manage:
                    //安全管理
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SecurityManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_unionid_login:
                    //互联登陆
                    if(accountInfoLoginRet != null && accountInfoLoginRet.getData() != null){
                        bundle.putString("QQOpenId",accountInfoLoginRet.getData().getQQOpenId());
                        bundle.putString("WXOpenId",accountInfoLoginRet.getData().getWXOpenId());
                    }

                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.UnionLoginFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.ll_active_seller:
                    //我是卖家
                    if(accountInfoLoginRet == null || accountInfoLoginRet.getData() == null){
                        return;
                    }

                    if(accountInfoLoginRet.getData().isUserSellerStatus()){
                        CallbackUtils.doBottomSelectCallback(2);
                        return;
                    }

                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getRealName()) || TextUtils.isEmpty(accountInfoLoginRet.getData().getIDNum())){
                        DialogUtils.showDialog(getActivity(),"提示", "激活卖家需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveSellerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.ll_active_buyer:
                    //我是买家
                    if(accountInfoLoginRet == null || accountInfoLoginRet.getData() == null){
                        return;
                    }

                    if(accountInfoLoginRet.getData().isUserBuyerStatus()){
                        CallbackUtils.doBottomSelectCallback(3);
                        return;
                    }

                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getRealName()) || TextUtils.isEmpty(accountInfoLoginRet.getData().getIDNum())){
                        DialogUtils.showDialog(getActivity(), "提示", "激活买家需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveBuyerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;

                case R.id.btn_recharge:
                    //我要充值
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RechargeFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_cash:
                    if(accountInfoLoginRet == null || accountInfoLoginRet.getData() == null){
                        return;
                    }
                    bundle.putInt("RiskLevel",accountInfoLoginRet.getData().getRiskLevel());
                    bundle.putString("Mobile",accountInfoLoginRet.getData().getMobile());
                    bundle.putString("RealName",accountInfoLoginRet.getData().getRealName());
                    //我要提现
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.CashFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_recharge_record:
                    //充值记录
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RechargeRecordFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_cash_record:
                    //提现记录
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.CashRecordFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_identity:
                    //实名认证
                    if(accountInfoLoginRet == null || accountInfoLoginRet.getData() == null){
                        return;
                    }
                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getRealName()) || TextUtils.isEmpty(accountInfoLoginRet.getData().getIDNum())){
                        DialogUtils.showDialog(getActivity(), "提示", "实名认证需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getMobile())){
                        DialogUtils.showDialog(getActivity(), "提示", "实名认证需要绑定手机，是否绑定手机", "取消", "绑定手机", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    //查询实名认证状态
                    ApiRequest.realNameStatus("PersonalFragment_realNameStatus",pb_loading);
                    break;
                case R.id.btn_face:
                    //刷脸认证
                    if(accountInfoLoginRet == null || accountInfoLoginRet.getData() == null){
                        return;
                    }
                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getRealName()) || TextUtils.isEmpty(accountInfoLoginRet.getData().getIDNum())){
                        DialogUtils.showDialog(getActivity(), "提示", "刷脸认证需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }

                    if(TextUtils.isEmpty(accountInfoLoginRet.getData().getMobile())){
                        DialogUtils.showDialog(getActivity(), "提示", "刷脸认证需要绑定手机，是否绑定手机", "取消", "绑定手机", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }
                    //查询刷脸认证状态
                    ApiRequest.faceStatus("PersonalFragment_faceStatus",pb_loading);
                    break;
            }
        }
    }

}
