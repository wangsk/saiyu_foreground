package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MarketAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.GetTopOrderListRet;
import com.saiyu.foreground.https.response.StatisticsListRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.BarGraphView;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_market)
public class MarketFragment extends BaseFragment implements CallbackUtils.ResponseCallback,CallbackUtils.OnPositionListener{

    @ViewById
    ProgressBar pb_loading;
    @ViewById
    BarGraphView bar_view;
    @ViewById
    TextView tv_top_1,tv_top_2,tv_top_3;
    @ViewById
    LinearLayout ll_top_1,ll_top_2,ll_top_3;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    private MarketAdapter marketAdapter;

    private List<StatisticsListRet.DatasBean.ItemsBean> mItem = new ArrayList<>();//当天最高折扣列表
    private List<GetTopOrderListRet.DatasBean.ItemsBean> mItem_2 = new ArrayList<>();//行情列表

    private List<String> dateList;

    private boolean isRefresh = true;

    public static MarketFragment newInstance(Bundle bundle) {
        MarketFragment_ fragment = new MarketFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setOnPositionListener(this);
        CallbackUtils.setCallback(this);
        if(isRefresh){
            ApiRequest.statisticsList("MarketFragment_statisticsList",pb_loading);
        }
        isRefresh = true;

    }

    @AfterViews
    void afterView() {
        CallbackUtils.setCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));

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

            if(mItem == null){
                mItem = new ArrayList<>();
            }
            mItem.clear();
            mItem.addAll(ret.getData().getList());

            dateList = new ArrayList<>();//日期
            Map<String, Integer> value_max = new HashMap<>();//最高折扣列表
            Map<String, Integer> value_aver = new HashMap<>();//平均折扣列表

            if(mItem.size() >= 7){
                int index = mItem.size() - 7;
                //从后向前取
                while (index < mItem.size()){
                    dateList.add(mItem.get(index).getDate());
                    value_max.put(mItem.get(index).getDate(),mItem.get(index).getMaxDiscount());
                    value_aver.put(mItem.get(index).getDate(),mItem.get(index).getAverageDiscount());
                    index++;
                }
            } else {
                int index = 0;
                //从后向前取
                while (index < mItem.size()){
                    dateList.add(mItem.get(index).getDate());
                    value_max.put(mItem.get(index).getDate(),mItem.get(index).getMaxDiscount());
                    value_aver.put(mItem.get(index).getDate(),mItem.get(index).getAverageDiscount());
                    index++;
                }
            }

            if(dateList.size() > 0){
                if(!TextUtils.isEmpty(dateList.get((dateList.size() - 1)))){
                    ApiRequest.getTopOrderList(dateList.get((dateList.size() - 1)),"MarketFragment_getTopOrderList",pb_loading);
                }

                bar_view.setValue(value_aver,value_max, dateList, 150);
                bar_view.setCurrentMonth(dateList.size());
            }

            Collections.reverse(mItem); // 倒序排列

            //月折扣行情列表
            marketAdapter = new MarketAdapter(mItem);
            recyclerView.setAdapter(marketAdapter);
            marketAdapter.notifyDataSetChanged();

        } else if(method.equals("MarketFragment_getTopOrderList")){
            GetTopOrderListRet ret = (GetTopOrderListRet)baseRet;
            if(ret.getData() == null || ret.getData().getTopOrderList() == null || ret.getData().getTopOrderList().size() <= 0){
                LogUtils.print("当天没有TOP3");
                ll_top_1.setVisibility(View.GONE);
                ll_top_2.setVisibility(View.GONE);
                ll_top_3.setVisibility(View.GONE);
                return;
            }
            if(mItem_2 == null){
                mItem_2 = new ArrayList<>();
            }
            mItem_2.clear();
            mItem_2.addAll(ret.getData().getTopOrderList());
            if(mItem_2.size() == 1){
                tv_top_1.setText(mItem_2.get(0).getOrderTitle());
                ll_top_1.setVisibility(View.VISIBLE);
                ll_top_2.setVisibility(View.GONE);
                ll_top_3.setVisibility(View.GONE);
            } else if(mItem_2.size() == 2){
                tv_top_1.setText(mItem_2.get(0).getOrderTitle());
                ll_top_1.setVisibility(View.VISIBLE);
                tv_top_2.setText(mItem_2.get(1).getOrderTitle());
                ll_top_2.setVisibility(View.VISIBLE);
                ll_top_3.setVisibility(View.GONE);
            } else if(mItem_2.size() >= 3){
                tv_top_1.setText(mItem_2.get(0).getOrderTitle());
                ll_top_1.setVisibility(View.VISIBLE);
                tv_top_2.setText(mItem_2.get(1).getOrderTitle());
                ll_top_2.setVisibility(View.VISIBLE);
                tv_top_3.setText(mItem_2.get(2).getOrderTitle());
                ll_top_3.setVisibility(View.VISIBLE);
            }
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

                        isRefresh = false;

                        bundle.putString("orderId",mItem_2.get(0).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.ll_top_2:
                    if(mItem_2.size() > 1 && mItem_2.get(1) != null){

                        isRefresh = false;

                        bundle.putString("orderId",mItem_2.get(1).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.ll_top_3:
                    if(mItem_2.size() > 2 && mItem_2.get(2) != null){

                        isRefresh = false;

                        bundle.putString("orderId",mItem_2.get(2).getId());
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.HallOrderDetailFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }
    }

    @Override
    public void setOnPositionListener(int position) {
        LogUtils.print("position === " + position);
        if(dateList == null || dateList.size() <= position || TextUtils.isEmpty(dateList.get(position))){
            return;
        }
        LogUtils.print("日期 " + dateList.get(position));
        ApiRequest.getTopOrderList(dateList.get(position),"MarketFragment_getTopOrderList",pb_loading);

    }
}
