package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.BuyerRechargeWebUrlAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BuyerWebListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.ui.views.MyToast;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_buyer)
public class BuyerFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    RatingBar rb_mark;
    @ViewById
    TextView tv_time,tv_last_order,tv_wait_order,tv_total_order;
    @ViewById
    Button btn_release_order,btn_last_order_manager,btn_release_order_history,btn_recharge_order_manager,btn_recharge_order_history,btn_right;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private BuyerRechargeWebUrlAdapter buyerRechargeWebUrlAdapter;
    private List<BuyerWebListRet.DatasBean.ItemsBean> mItems = new ArrayList<>();

    public static BuyerFragment newInstance(Bundle bundle) {
        BuyerFragment_ fragment = new BuyerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.dQCZNews("BuyerFragment_dQCZNews",pb_loading);
    }

    @AfterViews
    void afterView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("BuyerFragment_dQCZNews")) {
            BuyerWebListRet ret = (BuyerWebListRet)baseRet;
            if(ret.getData() == null || ret.getData().getNewsList() == null){
                return;
            }

            rb_mark.setRating(ret.getData().getVipLevel());
            if(!TextUtils.isEmpty(ret.getData().getAverageConfirmTime())){
                long time = Long.parseLong(ret.getData().getAverageConfirmTime());
                long day = time/60/24;
                long hour = time/60;
                long minute = time - day*24*60 - hour*60;
                tv_time.setText(Html.fromHtml("<font color = \"#ffa800\">" + day + "</font>" + "天" + "<font color = \"#ffa800\">" + hour + "</font>" + "小时"+ "<font color = \"#ffa800\">" + minute + "</font>" + "分钟"));
            } else {
                tv_time.setText(Html.fromHtml("<font color = \"#ffa800\">" + 0 + "</font>" + "天" + "<font color = \"#ffa800\">" + 0 + "</font>" + "小时"+ "<font color = \"#ffa800\">" + 0 + "</font>" + "分钟"));
            }

            tv_last_order.setText(Html.fromHtml("<font color = \"#ffa800\">" + ret.getData().getUserBuyerCount() + "</font>" + "单" + "<font color = \"#ffa800\">" + ret.getData().getUserBuyerMoney() + "</font>" + "元"));
            tv_wait_order.setText(Html.fromHtml("<font color = \"#ffa800\">" + ret.getData().getUserBuyerWaitConfirmOrdersCount() + "</font>" + "单" + "<font color = \"#ffa800\">" + ret.getData().getUserBuyerWaitConfirmOrdersMoney() + "</font>" + "元"));
            tv_total_order.setText(Html.fromHtml("<font color = \"#ffa800\">" + ret.getData().getUserBuyerOrderRSettleTotalCount() + "</font>" + "单" + "<font color = \"#ffa800\">" + ret.getData().getUserBuyerOrderRSettleTotalMoney() + "</font>" + "元"));

            if(mItems == null){
                mItems = new ArrayList<>();
            }
            mItems.clear();
            mItems.addAll(ret.getData().getNewsList());
            buyerRechargeWebUrlAdapter = new BuyerRechargeWebUrlAdapter(mItems);
            recyclerView.setAdapter(buyerRechargeWebUrlAdapter);
            buyerRechargeWebUrlAdapter.notifyDataSetChanged();
            buyerRechargeWebUrlAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String url = mItems.get(position).getNewsDetailUrl();
                    if(!TextUtils.isEmpty(url)){
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(mContext,ContainerActivity_.class);
                        bundle.putString("url", url);
                        bundle.putString("type","点券充值");
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    @Click({R.id.btn_release_order,R.id.btn_last_order_manager,R.id.btn_release_order_history,R.id.btn_recharge_order_manager,R.id.btn_recharge_order_history,R.id.btn_right})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(mContext,ContainerActivity_.class);
            switch (view.getId()) {
                case R.id.btn_release_order:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.GameSelectorFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_last_order_manager:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.PreOrderManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_release_order_history:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.PreOrderHistoryFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_recharge_order_manager:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RechargeOrderManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_recharge_order_history:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.RechargeOrderHistoryFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_right:
                    MyToast.newInstance(getActivity(),"请前往web端操作","APP暂不支持").show();
                    break;
            }
        }
    }

}
