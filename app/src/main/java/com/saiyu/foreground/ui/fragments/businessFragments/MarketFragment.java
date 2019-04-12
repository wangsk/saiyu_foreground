package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MarketAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.StatisticsListRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.BarGraphView;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_market)
public class MarketFragment extends BaseFragment implements CallbackUtils.ResponseCallback {

    @ViewById
    ProgressBar pb_loading;
    @ViewById
    BarGraphView bar_view;
    @ViewById
    TextView tv_top_1,tv_top_2,tv_top_3;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private MarketAdapter marketAdapter;

    private List<StatisticsListRet.DatasBean.ItemsBean> mItem = new ArrayList<>();//当天最高折扣列表
    private List<StatisticsListRet.DatasBean.ItemsBean_2> mItem_2 = new ArrayList<>();//行情列表

    public static MarketFragment newInstance(Bundle bundle) {
        MarketFragment_ fragment = new MarketFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.statisticsList("MarketFragment_statisticsList",pb_loading);
    }

    @AfterViews
    void afterView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("MarketFragment_statisticsList")) {
            StatisticsListRet ret = (StatisticsListRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(mItem_2 == null){
                mItem_2 = new ArrayList<>();
            }
            mItem_2.clear();
            mItem_2.addAll(ret.getData().getTopOrderList());
            if(mItem_2.size() > 0){
                for(int i = 0; i < mItem_2.size(); i++){
                    switch (i){
                        case 0:
                            tv_top_1.setText(mItem_2.get(0).getOrderTitle());
                            break;
                        case 1:
                            tv_top_2.setText(mItem_2.get(1).getOrderTitle());
                            break;
                        case 2:
                            tv_top_3.setText(mItem_2.get(2).getOrderTitle());
                            break;
                    }
                }
            }

            if(mItem == null){
                mItem = new ArrayList<>();
            }
            mItem.clear();
            mItem.addAll(ret.getData().getList());

            List<String> dateList = new ArrayList<>();//日期
            Map<String, Integer> value_max = new HashMap<>();//最高折扣列表
            Map<String, Integer> value_aver = new HashMap<>();//平均折扣列表

            for (int i = 0; i < 7; i++) {//取折扣列表的前七个显示折线图
                if(mItem.size() <= i){//如果行情列表个数小于7，提前退出循环
                    break;
                } else {
                    dateList.add(mItem.get(i).getDate());
                    value_max.put(mItem.get(i).getDate(),mItem.get(i).getMaxDiscount());
                    value_aver.put(mItem.get(i).getDate(),mItem.get(i).getAverageDiscount());
                }
            }

            if(dateList.size() > 0){
                bar_view.setValue(value_aver,value_max, dateList, 250);
                bar_view.setCurrentMonth(dateList.size());
            }

            //月折扣行情列表
            marketAdapter = new MarketAdapter(mItem);
            recyclerView.setAdapter(marketAdapter);
            marketAdapter.notifyDataSetChanged();

        }
    }

    @Click({R.id.ll_top_1,R.id.ll_top_2,R.id.ll_top_3})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            Intent intent = new Intent(mContext,ContainerActivity_.class);
            Bundle bundle = new Bundle();
            switch (view.getId()) {
                case R.id.ll_top_1:
                    if(mItem_2.size() > 0 && mItem_2.get(0) != null){
                        bundle.putString("orderId",mItem_2.get(0).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.ll_top_2:
                    if(mItem_2.size() > 1 && mItem_2.get(1) != null){
                        bundle.putString("orderId",mItem_2.get(1).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.ll_top_3:
                    if(mItem_2.size() > 2 && mItem_2.get(2) != null){
                        bundle.putString("orderId",mItem_2.get(2).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }
    }

}
