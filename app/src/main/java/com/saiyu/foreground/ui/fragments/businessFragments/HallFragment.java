package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.HallAdapter;
import com.saiyu.foreground.adapters.MyTagAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
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

@EFragment(R.layout.fragment_hall)
public class HallFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_total_order, tv_total_qcount;
    @ViewById
    LinearLayout ll_recharge_game, ll_selector;
    @ViewById
    ImageView iv_hall_selector,iv_hall_question;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private HallAdapter hallAdapter;
    private List<HallRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private List<HallRet.DatasBean.ProductItemsBean> productItemsBeans = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;
    private String rQBCount = "",rDiscount = "",pId = "",extend = "",sort = "";

    public static HallFragment newInstance(Bundle bundle) {
        HallFragment_ fragment = new HallFragment_();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        hallAdapter = new HallAdapter(mItems);
        recyclerView.setAdapter(hallAdapter);

        CallbackUtils.setCallback(this);
        ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("HallFragment_hallIndex")) {
            HallRet ret = (HallRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            tv_total_order.setText(ret.getData().getOrderCount() + "");
            tv_total_qcount.setText(ret.getData().getTotalCount() + "");

            int totalCount = ret.getData().getOrderCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderList());

            hallAdapter = new HallAdapter(mItems);
            hallAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(hallAdapter);
            hallAdapter.notifyDataSetChanged();

            //游戏产品列表
            productItemsBeans.clear();

            HallRet.DatasBean.ProductItemsBean productItemsBean = new HallRet.DatasBean.ProductItemsBean();
            productItemsBean.setName("不限");
            productItemsBeans.add(productItemsBean);

            productItemsBeans.addAll(ret.getData().getProductList());

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position || TextUtils.isEmpty(mItems.get(position).getOrderNum())){
            LogUtils.print("点击异常");
            return;
        }

        Intent intent = new Intent(mContext,ContainerActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId",mItems.get(position).getId());
        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
            ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishLoadmore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (page > 1) {
            page--;
            ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
        } else {
            ApiRequest.hallIndex("g", "1", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
            Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Click({R.id.iv_hall_selector, R.id.ll_recharge_game, R.id.ll_selector,R.id.iv_hall_question})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hall_question:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(mContext,ContainerActivity_.class);
                bundle.putString("url", "https://www.saiyu.com/help/");
                bundle.putString("type","帮助文档");//
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;
            case R.id.ll_recharge_game:
                MyTagAdapter myTagAdapter = new MyTagAdapter(productItemsBeans);
                PopWindowUtils.initPopWindow_2(myTagAdapter, ll_recharge_game, new OnListCallbackListener() {
                    @Override
                    public void setOnListCallbackListener(List<String> callbackList) {
                        if(callbackList == null || callbackList.size() < 1){
                            return;
                        }
                        LogUtils.print("current _pId ==== " + pId + " newId === " + callbackList.get(0));
                        if(TextUtils.isEmpty(callbackList.get(0)) && TextUtils.isEmpty(pId)){
                            return;
                        }
                        pId = callbackList.get(0);
                        ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
                    }
                });
                break;
            case R.id.ll_selector:

                PopWindowUtils.initPopWindow_3(ll_selector, new OnListCallbackListener() {
                    @Override
                    public void setOnListCallbackListener(List<String> callbackList) {
                        if(callbackList == null || callbackList.size() < 3){
                            return;
                        }
                        LogUtils.print("old rQBCount ==== " + rQBCount + " new rQBCount === " + callbackList.get(0) + " old rDiscount ==== " + rDiscount + " new rDiscount === " + callbackList.get(1) + " old extend ==== " + extend + " new extend === " + callbackList.get(2));
                        if(TextUtils.isEmpty(rQBCount) && TextUtils.isEmpty(callbackList.get(0)) && TextUtils.isEmpty(rDiscount) && TextUtils.isEmpty(callbackList.get(1)) && TextUtils.isEmpty(extend) && TextUtils.isEmpty(callbackList.get(2))){
                            return;
                        }
                        rQBCount = callbackList.get(0);
                        rDiscount = callbackList.get(1);
                        extend = callbackList.get(2);
                        ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
                    }
                });

                break;
            case R.id.iv_hall_selector:
                PopWindowUtils.initPopWindow_4(iv_hall_selector, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        sort = position+"";
                        ApiRequest.hallIndex("g", page + "", pageSize + "", rQBCount,rDiscount,pId,extend,sort,"HallFragment_hallIndex", pb_loading);
                        LogUtils.print("你点击了 ：" + position);
                    }
                });

                break;
        }
    }

}
