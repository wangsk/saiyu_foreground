package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.SellerOrderHistoryAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.SellerOrderHistoryRet;
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
public class SellerOrderHistoryFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private SellerOrderHistoryAdapter sellerOrderHistoryAdapter;
    private List<SellerOrderHistoryRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static SellerOrderHistoryFragment newInstance(Bundle bundle) {
        SellerOrderHistoryFragment_ fragment = new SellerOrderHistoryFragment_();
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
        tv_title_content.setText("出售订单历史");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        sellerOrderHistoryAdapter = new SellerOrderHistoryAdapter(mItems);
        recyclerView.setAdapter(sellerOrderHistoryAdapter);
        sellerOrderHistoryAdapter.notifyDataSetChanged();

        ApiRequest.orderReceiveHistorySeller(page + "", pageSize + "", "SellerOrderHistoryFragment_orderReceiveHistorySeller", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SellerOrderHistoryFragment_orderReceiveHistorySeller")) {
            SellerOrderHistoryRet ret = (SellerOrderHistoryRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceives() == null) {
                return;
            }

            int totalCount = ret.getData().getOrderReceivesCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceives());

            sellerOrderHistoryAdapter = new SellerOrderHistoryAdapter(mItems);
            sellerOrderHistoryAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(sellerOrderHistoryAdapter);
            sellerOrderHistoryAdapter.notifyDataSetChanged();

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
        final int status = mItems.get(position).getReceiveOrderStatus();
        final String orderCancelRemarks = mItems.get(position).getOrderCancelRemarks();
        final Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        //订单状态 0待充值 1审核中 2审核失败 3已审核 4等待确认 5确认完结 6维权中 7维权完结 8已取消
        if(status != 7 && status != 8){
            SellerOrderDetailFragment sellerOrderDetailFragment = SellerOrderDetailFragment.newInstance(bundle);
            start(sellerOrderDetailFragment);
            return;
        }

        PopWindowUtils.initPopWindow_16(getActivity(),status,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 1:
                        //订单详情
                        SellerOrderDetailFragment sellerOrderDetailFragment = SellerOrderDetailFragment.newInstance(bundle);
                        start(sellerOrderDetailFragment);
                        break;
                    case 2:
                        //查看维权
                       Toast.makeText(mContext,"请往web端查看",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //取消原因
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
