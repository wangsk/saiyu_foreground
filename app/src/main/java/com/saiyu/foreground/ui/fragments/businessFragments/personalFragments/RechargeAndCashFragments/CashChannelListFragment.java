package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.CashChannelAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.CallbackUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_base_record)
public class CashChannelListFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnItemClickListener {
    @ViewById
    TextView tv_title_content,tv_title_right;
    @ViewById
    Button btn_title_back;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private String RealName,Mobile;
    private int RiskLevel;
    private CashChannelAdapter cashChannelAdapter;
    private List<CashChannelRet.DatasBean.ItemsBean> mItems = new ArrayList<>();

    public static CashChannelListFragment newInstance(Bundle bundle) {
        CashChannelListFragment_ fragment = new CashChannelListFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.cashWays("CashChannelListFragment_cashWays",pb_loading);
    }

    @AfterViews
    void afterView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            Mobile = bundle.getString("Mobile");
            RealName = bundle.getString("RealName");
            RiskLevel = bundle.getInt("RiskLevel");
        }
        tv_title_content.setText("添加提现渠道");
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));

        cashChannelAdapter = new CashChannelAdapter(mItems);
        recyclerView.setAdapter(cashChannelAdapter);
        cashChannelAdapter.notifyDataSetChanged();

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("CashChannelListFragment_cashWays")){
            CashChannelRet ret = (CashChannelRet)baseRet;
            if(ret.getData() == null || ret.getData().getWithdrawWaysList() == null){
                return;
            }
            mItems.clear();
            mItems.addAll(ret.getData().getWithdrawWaysList());

            cashChannelAdapter = new CashChannelAdapter(mItems);
            recyclerView.setAdapter(cashChannelAdapter);
            cashChannelAdapter.setOnItemClickListener(this);
            cashChannelAdapter.notifyDataSetChanged();
        }
    }

    @Click({R.id.btn_title_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position||mItems.get(position) == null){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("Mobile",Mobile);
        bundle.putString("RealName",RealName);
        bundle.putString("Id",mItems.get(position).getId());
        bundle.putString("Name",mItems.get(position).getName());
        bundle.putInt("Type",mItems.get(position).getType());
        bundle.putString("withdrawWayConfigCharge",mItems.get(position).getWithdrawWayConfigCharge());
        bundle.putString("PayDateStr",mItems.get(position).getPayDateStr());
        bundle.putString("ImgUrl",mItems.get(position).getImgUrl());
        bundle.putString("withdrawWayConfigCharge",mItems.get(position).getWithdrawWayConfigCharge());
        bundle.putString("StartMoney",mItems.get(position).getStartMoney());
        bundle.putString("TopMoney",mItems.get(position).getTopMoney());
        bundle.putInt("RiskLevel",RiskLevel);

        AddCashChannelFragment addCashChannelFragment = AddCashChannelFragment.newInstance(bundle);
        start(addCashChannelFragment);
    }
}
