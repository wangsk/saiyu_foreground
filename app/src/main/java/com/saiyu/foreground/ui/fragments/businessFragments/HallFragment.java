package com.saiyu.foreground.ui.fragments.businessFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.HallAdapter;
import com.saiyu.foreground.adapters.RechargeRecordAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
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

@EFragment(R.layout.fragment_hall)
public class HallFragment extends BaseFragment implements CallbackUtils.ResponseCallback ,OnRefreshListener,OnLoadmoreListener, OnItemClickListener {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_total_order,tv_total_qcount;
    @ViewById
    LinearLayout ll_recharge_game,ll_selector;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private HallAdapter hallAdapter;
    private List<HallRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 10;
    private int totalPage;

    public static HallFragment newInstance(Bundle bundle) {
        HallFragment_ fragment = new HallFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.hallIndex("g",page+"",pageSize+"","HallFragment_hallIndex",pb_loading);

    }

    @AfterViews
    void afterView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        hallAdapter = new HallAdapter(mItems);
        recyclerView.setAdapter(hallAdapter);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("HallFragment_hallIndex")) {
            HallRet ret = (HallRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_total_order.setText(ret.getData().getOrderCount()+"");
            tv_total_qcount.setText(ret.getData().getTotalCount()+"");

            int totalCount = ret.getData().getTotalCount();

            totalPage = totalCount/pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderList());

            hallAdapter = new HallAdapter(mItems);
            hallAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(hallAdapter);
            hallAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(page < totalPage){
            page++;
            ApiRequest.hallIndex("g",page+"",pageSize+"","HallFragment_hallIndex",pb_loading);
        } else {
            Toast.makeText(mContext,"已经是最后一页",Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishLoadmore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(page > 1){
            page--;
            ApiRequest.hallIndex("g",page+"",pageSize+"","HallFragment_hallIndex",pb_loading);
        } else {
            Toast.makeText(mContext,"已经是第一页",Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    //    @Click({})
//    void onClick(View view) {
//        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
//            switch (view.getId()) {
//
//            }
//        }
//    }

}
