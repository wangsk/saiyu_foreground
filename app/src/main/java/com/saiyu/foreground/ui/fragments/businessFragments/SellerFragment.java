package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.BuyerRechargeWebUrlAdapter;
import com.saiyu.foreground.adapters.SellerWebUrlAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BuyerWebListRet;
import com.saiyu.foreground.https.response.QBListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_seller)
public class SellerFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    Button btn_recharge_order,btn_sell_order_manager,btn_sell_order_history,btn_receive_right,btn_point_stream,btn_hongbao,btn_gohall;
    @ViewById
    TextView tv_orderpoint,tv_wait_recharge,tv_review_order,tv_wait_confirm,tv_total_order;

    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private SellerWebUrlAdapter sellerWebUrlAdapter;
    private List<QBListRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private float myPoint;
    private String OrderFreePointsUrl;

    public static SellerFragment newInstance(Bundle bundle) {
        SellerFragment_ fragment = new SellerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.qBJSNews("SellerFragment_qBJSNews",pb_loading);
    }

    @AfterViews
    void afterView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        sellerWebUrlAdapter = new SellerWebUrlAdapter(mItems);
        recyclerView.setAdapter(sellerWebUrlAdapter);
        sellerWebUrlAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SellerFragment_qBJSNews")) {
            QBListRet ret = (QBListRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            myPoint = ret.getData().getReceivePoint();
            tv_orderpoint.setText(myPoint + "");

            OrderFreePointsUrl = ret.getData().getOrderFreePointsUrl();

            tv_wait_recharge.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserSellerWaitRechargeOrdersCount() + "</font>" + "笔" + "<font color = \"#148cf1\">" + ret.getData().getUserSellerWaitRechargeOrdersMoney() + "</font>" + "元"));
            tv_review_order.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserSellerAuditOrdersCount() + "</font>" + "笔" + "<font color = \"#148cf1\">" + ret.getData().getUserSellerAuditOrdersMoney() + "</font>" + "元"));
            tv_wait_confirm.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserSellerWaitConfirmOrdersCount() + "</font>" + "笔" + "<font color = \"#148cf1\">" + ret.getData().getUserSellerWaitConfirmOrdersMoney() + "</font>" + "元"));
            tv_total_order.setText(Html.fromHtml("<font color = \"#148cf1\">" + ret.getData().getUserSellerOrderRSettleTotalCount() + "</font>" + "笔" + "<font color = \"#148cf1\">" + ret.getData().getUserSellerOrderRSettleTotalMoney() + "</font>" + "元"));

            if(mItems == null){
                mItems = new ArrayList<>();
            }
            mItems.clear();
            mItems.addAll(ret.getData().getQBJSNews());
            sellerWebUrlAdapter = new SellerWebUrlAdapter(mItems);
            recyclerView.setAdapter(sellerWebUrlAdapter);
            sellerWebUrlAdapter.notifyDataSetChanged();
            sellerWebUrlAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String url = mItems.get(position).getNewsDetailUrl();
                    if(!TextUtils.isEmpty(url)){
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(mContext,ContainerActivity_.class);
                        bundle.putString("url", url);
                        bundle.putString("type","Q币寄售");
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    @Click({R.id.btn_rechargepoint,R.id.btn_free,R.id.btn_recharge_order,R.id.btn_sell_order_manager,R.id.btn_sell_order_history,R.id.btn_receive_right,R.id.btn_point_stream,R.id.btn_hongbao,R.id.btn_gohall})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(mContext,ContainerActivity_.class);
            switch (view.getId()) {
                case R.id.btn_recharge_order:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WaitingRechargeOrderFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_sell_order_manager:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SellOrderManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_sell_order_history:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SellerOrderHistoryFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_receive_right:
                    Toast.makeText(mContext,"请往web端处理！",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_point_stream:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SellerOrderPointStreamFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_hongbao:
                    Toast.makeText(mContext,"请往web端处理！",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_gohall:
                    CallbackUtils.doBottomSelectCallback(1);
                    break;
                case R.id.btn_rechargepoint:
                    bundle.putFloat("myPoint",myPoint);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.PointManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_free:
                    if(!TextUtils.isEmpty(OrderFreePointsUrl)){
                        bundle.putString("url", OrderFreePointsUrl);
                        bundle.putString("type","免费点数");
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }
    }

}
