package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.FragmentViewPagerAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.WaitingRechargeOrderRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.MyViewPager;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.TimeParseUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EFragment(R.layout.fragment_waiting_rechargeorder_base)
public class WaitingRechargeOrderFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    Button btn_last,btn_next,btn_title_back,btn_blank;
    @ViewById
    TextView tv_title_content,tv_prompt;
    @ViewById
    LinearLayout ll_body;
    @ViewById
    TabLayout layout_tab;
    @ViewById
    MyViewPager view_pager;
    private FragmentViewPagerAdapter fragmentViewPagerAdapter;
    private int totalCount,curPosition;
    private List<WaitingRechargeOrderRet.DatasBean.ItemsBean> mItem = new ArrayList<>();

    public static WaitingRechargeOrderFragment newInstance(Bundle bundle) {
        WaitingRechargeOrderFragment_ fragment = new WaitingRechargeOrderFragment_();
        fragment.setArguments(bundle);
        CallbackUtils.setCallback(fragment);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.waitRechargeOrder("WaitingRechargeOrderFragment_waitRechargeOrder",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("待充值订单");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("WaitingRechargeOrderFragment_waitRechargeOrder")) {
            WaitingRechargeOrderRet ret = (WaitingRechargeOrderRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            totalCount = ret.getData().getOrderReceivesCount();
            if(totalCount > 0){
                ll_body.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
                String str = "您当前有"+ "<font color = \"#148cf1\">" + totalCount + "</font>" +"笔需要立即充值的订单，如不能充值，请尽快取消，以免带来接单点数的损失！";
                tv_prompt.setText(Html.fromHtml(str));
                curPosition = 0;

                if(mItem == null){
                    mItem = new ArrayList<>();
                }
                mItem.clear();
                mItem.addAll(ret.getData().getOrderReceivesList());

                fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),mItem);
                view_pager.setAdapter(fragmentViewPagerAdapter);
                layout_tab.setupWithViewPager(view_pager);

            } else {
                ll_body.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
            }

        }
    }

    @Click({R.id.btn_last,R.id.btn_next,R.id.btn_title_back})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_last:
                    if(curPosition == 0){
                        Toast.makeText(mContext,"当前已经是第一条",Toast.LENGTH_SHORT).show();
                    } else {
                        curPosition--;
                        view_pager.setCurrentItem(curPosition);
                    }
                    break;
                case R.id.btn_next:
                    if(curPosition == (totalCount - 1)){
                        Toast.makeText(mContext,"当前已经是最后一条",Toast.LENGTH_SHORT).show();
                    } else {
                        curPosition++;
                        view_pager.setCurrentItem(curPosition);
                    }
                    break;
                case R.id.btn_title_back:
                    onBackPressedSupport();
                    break;
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        getActivity().finish();
        return true;
    }

}
