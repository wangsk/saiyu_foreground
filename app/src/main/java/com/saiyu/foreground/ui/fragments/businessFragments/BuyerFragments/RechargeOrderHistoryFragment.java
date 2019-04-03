package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.RechargeOrderHistoryAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.OrderReceiveHistoryRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments.RechargeOrderDetailFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.PopWindowUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_base_record)
public class RechargeOrderHistoryFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private RechargeOrderHistoryAdapter rechargeOrderHistoryAdapter;
    private List<OrderReceiveHistoryRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static RechargeOrderHistoryFragment newInstance(Bundle bundle) {
        RechargeOrderHistoryFragment_ fragment = new RechargeOrderHistoryFragment_();
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
        tv_title_content.setText("充值订单历史");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        rechargeOrderHistoryAdapter = new RechargeOrderHistoryAdapter(mItems);
        recyclerView.setAdapter(rechargeOrderHistoryAdapter);
        rechargeOrderHistoryAdapter.notifyDataSetChanged();

        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("RechargeOrderHistoryFragment_orderReceiveHistory")) {
            OrderReceiveHistoryRet ret = (OrderReceiveHistoryRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceiveList() == null) {
                return;
            }

            int totalCount = ret.getData().getDataCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceiveList());

            rechargeOrderHistoryAdapter = new RechargeOrderHistoryAdapter(mItems);
            rechargeOrderHistoryAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(rechargeOrderHistoryAdapter);
            rechargeOrderHistoryAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishLoadmore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (page > 1) {
            page--;
        } else {
            Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        //确认完结 = 5,
        //    维权完结 = 7,
        //    已取消 = 8,
        final int status = mItems.get(position).getReceiveOrderStatus();
        final String orderReceiveId = mItems.get(position).getId();
        final String orderCancelRemarks = mItems.get(position).getOrderCancelRemarks();
        final Bundle bundle = new Bundle();
        bundle.putString("orderReceiveId",orderReceiveId);
        if(status == 5){
            RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
            start(rechargeOrderDetailFragment);
            return;
        }
        PopWindowUtils.initPopWindow_13(status,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 1:
                        RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
                        start(rechargeOrderDetailFragment);
                        break;
                    case 2:
                        Toast.makeText(mContext,"前往web端处理",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(mContext,orderCancelRemarks,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back) {
            getActivity().finish();
        }
    }
}
