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
import com.saiyu.foreground.adapters.PointRechargeHistoryAdapter;
import com.saiyu.foreground.adapters.SellerOrderHistoryAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.RechargePointHistoryRet;
import com.saiyu.foreground.https.response.SellerOrderHistoryRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
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
public class PointRechargeHistoryFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener {
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
    private PointRechargeHistoryAdapter pointRechargeHistoryAdapter;
    private List<RechargePointHistoryRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static PointRechargeHistoryFragment newInstance(Bundle bundle) {
        PointRechargeHistoryFragment_ fragment = new PointRechargeHistoryFragment_();
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
        tv_title_content.setText("点数充值历史");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        pointRechargeHistoryAdapter = new PointRechargeHistoryAdapter(mItems);
        recyclerView.setAdapter(pointRechargeHistoryAdapter);
        pointRechargeHistoryAdapter.notifyDataSetChanged();

        ApiRequest.receivePointHistory(page + "", pageSize + "", "PointRechargeHistoryFragment_receivePointHistory", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PointRechargeHistoryFragment_receivePointHistory")) {
            RechargePointHistoryRet ret = (RechargePointHistoryRet) baseRet;
            if (ret.getData() == null || ret.getData().getReceivePoints() == null) {
                return;
            }

            int totalCount = ret.getData().getReceivePoinCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getReceivePoints());

            pointRechargeHistoryAdapter = new PointRechargeHistoryAdapter(mItems);
            recyclerView.setAdapter(pointRechargeHistoryAdapter);
            pointRechargeHistoryAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.receivePointHistory(page + "", pageSize + "", "PointRechargeHistoryFragment_receivePointHistory", pb_loading);
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
        ApiRequest.receivePointHistory(page + "", pageSize + "", "PointRechargeHistoryFragment_receivePointHistory", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back) {
            getFragmentManager().popBackStack();
        }
    }
}
