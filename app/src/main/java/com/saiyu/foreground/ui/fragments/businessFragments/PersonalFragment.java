package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SetingFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    ImageView iv_seting;
    @ViewById
    Button btn_mem_info,btn_wad_info,btn_security_manage,btn_unionid_login,btn_active_seller,btn_active_buyer,btn_seller_info,btn_buyer_info,btn_recharge,
            btn_cash,btn_recharge_record,btn_cash_record,btn_identity,btn_face;

    public static PersonalFragment newInstance(Bundle bundle) {
        PersonalFragment_ fragment = new PersonalFragment_();
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
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("")) {

        }
    }

    @Click({R.id.iv_seting,R.id.btn_mem_info,R.id.btn_wad_info,R.id.btn_security_manage,R.id.btn_unionid_login,R.id.btn_active_seller,
            R.id.btn_active_buyer,R.id.btn_seller_info,R.id.btn_buyer_info,R.id.btn_recharge,R.id.btn_cash,R.id.btn_recharge_record,R.id.btn_cash_record,R.id.btn_identity,R.id.btn_face})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(mContext,ContainerActivity_.class);
            switch (view.getId()) {
                case R.id.iv_seting:
                    //设置
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
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.UnionLoginFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_active_seller:
                    //激活卖家
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveSellerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_active_buyer:
                    //激活买家
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveBuyerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_seller_info:
                    //卖家资料
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SellerInfoFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_buyer_info:
                    //买家资料
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BuyerInfoFragmentTag);
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

                    break;
                case R.id.btn_face:
                    //刷脸认证

                    break;
            }
        }
    }

}
