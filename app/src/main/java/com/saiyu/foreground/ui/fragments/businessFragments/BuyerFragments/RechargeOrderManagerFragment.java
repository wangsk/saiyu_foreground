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
import com.saiyu.foreground.adapters.RechargeOrderManagerAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.RechargeOrderManageRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.BuyerOrderDetailFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments.ConfirmReceiverFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments.RechargeOrderDetailFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments.RightFragment;
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
public class RechargeOrderManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private RechargeOrderManagerAdapter rechargeOrderManagerAdapter;
    private List<RechargeOrderManageRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static RechargeOrderManagerFragment newInstance(Bundle bundle) {
        RechargeOrderManagerFragment_ fragment = new RechargeOrderManagerFragment_();
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
        tv_title_content.setText("充值订单管理");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        rechargeOrderManagerAdapter = new RechargeOrderManagerAdapter(mItems);
        recyclerView.setAdapter(rechargeOrderManagerAdapter);
        rechargeOrderManagerAdapter.notifyDataSetChanged();

        ApiRequest.orderReceiveManage(page + "", pageSize + "", "RechargeOrderManagerFragment_orderReceiveManage", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("RechargeOrderManagerFragment_orderReceiveManage")) {
            RechargeOrderManageRet ret = (RechargeOrderManageRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceiveList() == null) {
                return;
            }

            int totalCount = ret.getData().getDataCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceiveList());

            rechargeOrderManagerAdapter = new RechargeOrderManagerAdapter(mItems);
            rechargeOrderManagerAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(rechargeOrderManagerAdapter);
            rechargeOrderManagerAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderReceiveManage(page + "", pageSize + "", "RechargeOrderManagerFragment_orderReceiveManage", pb_loading);
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
        ApiRequest.orderReceiveManage(page + "", pageSize + "", "RechargeOrderManagerFragment_orderReceiveManage", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        //待充值 = 0,审核中 = 1,审核失败 = 2,已审核 = 3,等待确认 = 4,确认完结 = 5, 维权中 = 6,维权完结 = 7,已取消 = 8
        final int status = mItems.get(position).getReceiveOrderStatus();
        final String orderReceiveId = mItems.get(position).getId();
        final Bundle bundle = new Bundle();
        bundle.putString("orderReceiveId",orderReceiveId);
        if(status == 0 || status == 1){
            RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
            start(rechargeOrderDetailFragment);
            return;
        }

        PopWindowUtils.initPopWindow_12(status,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 1://订单详情
                        RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
                        start(rechargeOrderDetailFragment);
                        break;
                    case 2://确认收货
                        ConfirmReceiverFragment confirmReceiverFragment = ConfirmReceiverFragment.newInstance(bundle);
                        start(confirmReceiverFragment);
                        break;
                    case 3://发起维权
                        RightFragment rightFragment = RightFragment.newInstance(bundle);
                        start(rightFragment);
                        break;
                    case 4://响应维权
                        Toast.makeText(mContext,"请往web端处理！",Toast.LENGTH_SHORT).show();
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
