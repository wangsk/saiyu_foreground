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
import com.saiyu.foreground.adapters.PreOrderHistoryAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.PreOrderHistoryRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.BuyerOrderDetailFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.OrderRecordFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.RechargeStreamFragment;
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
public class PreOrderHistoryFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private PreOrderHistoryAdapter preOrderHistoryAdapter;
    private List<PreOrderHistoryRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static PreOrderHistoryFragment newInstance(Bundle bundle) {
        PreOrderHistoryFragment_ fragment = new PreOrderHistoryFragment_();
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
        tv_title_content.setText("预定订单历史");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        preOrderHistoryAdapter = new PreOrderHistoryAdapter(mItems);
        recyclerView.setAdapter(preOrderHistoryAdapter);
        preOrderHistoryAdapter.notifyDataSetChanged();

        ApiRequest.orderHistory(page + "", pageSize + "", "PreOrderHistoryFragment_orderHistory", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PreOrderHistoryFragment_orderHistory")) {
            PreOrderHistoryRet ret = (PreOrderHistoryRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderList() == null) {
                return;
            }

            int totalCount = ret.getData().getDataCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderList());

            preOrderHistoryAdapter = new PreOrderHistoryAdapter(mItems);
            preOrderHistoryAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(preOrderHistoryAdapter);
            preOrderHistoryAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderHistory(page + "", pageSize + "", "PreOrderHistoryFragment_orderHistory", pb_loading);
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
        ApiRequest.orderHistory(page + "", pageSize + "", "PreOrderHistoryFragment_orderHistory", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        final String orderId = mItems.get(position).getId();
        final Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        PopWindowUtils.initPopWindow_11(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){
                    case 0:
                        //订单详情
                        BuyerOrderDetailFragment buyerOrderDetailFragment = BuyerOrderDetailFragment.newInstance(bundle);
                        start(buyerOrderDetailFragment);
                        break;
                    case 1:
                        //订单流水
                        RechargeStreamFragment rechargeStreamFragment = RechargeStreamFragment.newInstance(bundle);
                        start(rechargeStreamFragment);
                        break;
                    case 2:
                        //订单日志
                        OrderRecordFragment orderRecordFragment = OrderRecordFragment.newInstance(bundle);
                        start(orderRecordFragment);
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
