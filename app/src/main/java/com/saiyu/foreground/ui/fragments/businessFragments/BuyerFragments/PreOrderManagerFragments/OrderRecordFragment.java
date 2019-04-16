package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.OrderRecordAdapter;
import com.saiyu.foreground.adapters.RechargeStreamAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.OrderLogRet;
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

@EFragment(R.layout.fragment_recharge_stream)
public class OrderRecordFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnItemClickListener {
    @ViewById
    TextView tv_title_content,tv_game,tv_ordernum,tv_discount;
    @ViewById
    Button btn_title_back,btn_confirm,btn_blank;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading,pb_order;
    private OrderRecordAdapter orderRecordAdapter;
    private List<OrderLogRet.DatasBean.ItemsBean> mItems = new ArrayList<>();

    private String orderId;

    public static OrderRecordFragment newInstance(Bundle bundle) {
        OrderRecordFragment_ fragment = new OrderRecordFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderLog(orderId,"OrderRecordFragment_orderLog",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("订单日志");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableRefresh(false);
        orderRecordAdapter = new OrderRecordAdapter(mItems);
        recyclerView.setAdapter(orderRecordAdapter);
        orderRecordAdapter.notifyDataSetChanged();

        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("OrderRecordFragment_orderLog")) {
            OrderLogRet ret = (OrderLogRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceiveList() == null) {
                return;
            }

            String succQBCount = ret.getData().getSuccQBCount();
            String reserveQBCount = ret.getData().getReserveQBCount();
            tv_discount.setText(succQBCount+"/"+reserveQBCount +"Q币");
            if(!TextUtils.isEmpty(succQBCount)&& !TextUtils.isEmpty(reserveQBCount)){
                pb_order.setProgress((int)(Float.parseFloat(succQBCount)/Float.parseFloat(reserveQBCount)*100));
            }

            tv_ordernum.setText(ret.getData().getOrderEntity());
            tv_game.setText(ret.getData().getProductName());

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceiveList());

            if(mItems.size() <= 0){
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有订单日志");
                return;
            } else {
                btn_blank.setVisibility(View.GONE);
            }

            orderRecordAdapter = new OrderRecordAdapter(mItems);
            orderRecordAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(orderRecordAdapter);
            orderRecordAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back || view.getId() == R.id.btn_confirm) {
            getFragmentManager().popBackStack();
        }
    }
}
