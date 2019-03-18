package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.CashRecordAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashRecordRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
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
public class CashRecordFragment extends BaseFragment implements CallbackUtils.ResponseCallback ,OnRefreshListener,OnLoadmoreListener , OnItemClickListener {
    @ViewById
    TextView tv_title_content,tv_title_right;
    @ViewById
    Button btn_title_back;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private CashRecordAdapter cashRecordAdapter;
    private List<CashRecordRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static CashRecordFragment newInstance(Bundle bundle) {
        CashRecordFragment_ fragment = new CashRecordFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.financeWithdrawLog(page + "",pageSize + "","CashRecordFragment_financeWithdrawLog",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("提现记录");
        tv_title_right.setText("资金明细");
        tv_title_right.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        cashRecordAdapter = new CashRecordAdapter(mItems);
        recyclerView.setAdapter(cashRecordAdapter);
        cashRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("CashRecordFragment_financeWithdrawLog")){
            CashRecordRet ret = (CashRecordRet)baseRet;
            if(ret.getData() == null || ret.getData().getWithdrawLog() == null){
                return;
            }

            int totalCount = ret.getData().getDataCount();

            totalPage = totalCount/pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getWithdrawLog());

            cashRecordAdapter = new CashRecordAdapter(mItems);
            cashRecordAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(cashRecordAdapter);
            cashRecordAdapter.notifyDataSetChanged();

        }
    }

    @Click({R.id.btn_title_back,R.id.tv_title_right})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.tv_title_right:
                    CashDetailFragment cashDetailFragment = CashDetailFragment.newInstance(null);
                    start(cashDetailFragment);
                    break;
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(page < totalPage){
            page++;
            ApiRequest.financeWithdrawLog(page + "",pageSize + "","CashRecordFragment_financeWithdrawLog",pb_loading);
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
            ApiRequest.financeWithdrawLog(page + "",pageSize + "","CashRecordFragment_financeWithdrawLog",pb_loading);
        } else {
            Toast.makeText(mContext,"已经是第一页",Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        PopWindowUtils.initPopWindow_6(mItems.get(position));
    }

}
