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
import com.saiyu.foreground.adapters.PreOrderManagerAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.PreOrderManagerRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.BalanceOrderFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.BuyerOrderDetailFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.CancelOrderFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.OrderFailReasonFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.OrderRecordFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.PauseOrderFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.RechargeStreamFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.ReviseOrderFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments.StartOrderFragment;
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
public class PreOrderManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private PreOrderManagerAdapter preOrderManagerAdapter;
    private List<PreOrderManagerRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 20;
    private int totalPage;

    public static PreOrderManagerFragment newInstance(Bundle bundle) {
        PreOrderManagerFragment_ fragment = new PreOrderManagerFragment_();
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
        tv_title_content.setText("预定订单管理");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        preOrderManagerAdapter = new PreOrderManagerAdapter(mItems);
        recyclerView.setAdapter(preOrderManagerAdapter);
        preOrderManagerAdapter.notifyDataSetChanged();

        ApiRequest.orderManage(page + "", pageSize + "", "PreOrderManagerFragment_orderManage", pb_loading);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PreOrderManagerFragment_orderManage")) {
            PreOrderManagerRet ret = (PreOrderManagerRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderList() == null) {
                return;
            }

            int totalCount = ret.getData().getOrderCount();

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderList());

            preOrderManagerAdapter = new PreOrderManagerAdapter(mItems);
            preOrderManagerAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(preOrderManagerAdapter);
            preOrderManagerAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderManage(page + "", pageSize + "", "PreOrderManagerFragment_orderManage", pb_loading);
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
        ApiRequest.orderManage(page + "", pageSize + "", "PreOrderManagerFragment_orderManage", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        final int status = mItems.get(position).getAuditStatus();//审核状态
        final int status_2 = mItems.get(position).getOrderStatus();//发布状态
        final String orderId = mItems.get(position).getId();
        final String reservePrice = mItems.get(position).getReservePrice();

        PopWindowUtils.initPopWindow_8(status,status_2,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                switch (position){
                    case 1:
                        //订单详情
                        bundle.putString("orderId",orderId);
                        BuyerOrderDetailFragment buyerOrderDetailFragment = BuyerOrderDetailFragment.newInstance(bundle);
                        start(buyerOrderDetailFragment);
                        break;
                    case 2:
                        switch (status){
                            case 0://未审核
                                //取消订单
                                bundle.putString("orderId",orderId);
                                bundle.putString("ReservePrice",reservePrice);
                                CancelOrderFragment cancelOrderFragment = CancelOrderFragment.newInstance(bundle);
                                start(cancelOrderFragment);
                                break;
                            case 1://审核通过
                                //订单流水
                                bundle.putString("orderId",orderId);
                                RechargeStreamFragment rechargeStreamFragment = RechargeStreamFragment.newInstance(bundle);
                                start(rechargeStreamFragment);
                                break;
                            case 2://审核失败
                                //修改订单
                                bundle.putString("orderId",orderId);
                                ReviseOrderFragment reviseOrderFragment = ReviseOrderFragment.newInstance(bundle);
                                start(reviseOrderFragment);
                                break;
                        }

                        break;
                    case 3:
                        switch (status){
                            case 1://审核通过
                                //结算订单
                                bundle.putString("orderId",orderId);
                                BalanceOrderFragment balanceOrderFragment = BalanceOrderFragment.newInstance(bundle);
                                start(balanceOrderFragment);
                                break;
                            case 2://审核失败
                                //取消订单
                                bundle.putString("orderId",orderId);
                                bundle.putString("ReservePrice",reservePrice);
                                CancelOrderFragment cancelOrderFragment = CancelOrderFragment.newInstance(bundle);
                                start(cancelOrderFragment);
                                break;
                        }

                        break;
                    case 4:
                        switch (status){
                            case 1://审核通过
                                //修改订单
                                bundle.putString("orderId",orderId);
                                ReviseOrderFragment reviseOrderFragment = ReviseOrderFragment.newInstance(bundle);
                                start(reviseOrderFragment);
                                break;
                            case 2://审核失败
                                //订单失败原因
                                bundle.putString("orderId",orderId);
                                OrderFailReasonFragment orderFailReasonFragment = OrderFailReasonFragment.newInstance(bundle);
                                start(orderFailReasonFragment);
                                break;
                        }

                        break;
                    case 5:
                        //订单日志
                        bundle.putString("orderId",orderId);
                        OrderRecordFragment orderRecordFragment = OrderRecordFragment.newInstance(bundle);
                        start(orderRecordFragment);
                        break;
                    case 6:
                        //订单状态 0未发布 1已发布 2暂停发布 3等待结算 4已结算 5手动取消
                        if(status_2 == 2){//暂停状态
                            //启动订单
                            bundle.putString("orderId",orderId);
                            StartOrderFragment startOrderFragment = StartOrderFragment.newInstance(bundle);
                            start(startOrderFragment);
                        } else if(status_2 == 1){//正常发布状态
                            //暂停订单
                            bundle.putString("orderId",orderId);
                            PauseOrderFragment pauseOrderFragment = PauseOrderFragment.newInstance(bundle);
                            start(pauseOrderFragment);
                        } else {
                            //其他状态不予处理
                        }

                        break;
                }
            }
        });
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
        }
    }
}
