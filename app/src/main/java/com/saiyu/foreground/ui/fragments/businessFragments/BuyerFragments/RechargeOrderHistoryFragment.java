package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.RechargeOrderHistoryAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.OrderReceiveHistoryRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments.RechargeOrderDetailFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.ui.views.MyToast;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.PopWindowUtils;
import com.saiyu.foreground.utils.Utils;
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
public class RechargeOrderHistoryFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_blank;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private RechargeOrderHistoryAdapter rechargeOrderHistoryAdapter;
    private List<OrderReceiveHistoryRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static RechargeOrderHistoryFragment newInstance(Bundle bundle) {
        RechargeOrderHistoryFragment_ fragment = new RechargeOrderHistoryFragment_();
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
        tv_title_content.setText("充值订单历史");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        rechargeOrderHistoryAdapter = new RechargeOrderHistoryAdapter(mItems);
        recyclerView.setAdapter(rechargeOrderHistoryAdapter);
        rechargeOrderHistoryAdapter.notifyDataSetChanged();

        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("RechargeOrderHistoryFragment_orderReceiveHistory")) {
            OrderReceiveHistoryRet ret = (OrderReceiveHistoryRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceiveList() == null) {
                return;
            }

            int totalCount = ret.getData().getDataCount();

            if(totalCount == 0){
                recyclerView.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有充值订单记录");
                return;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
            }

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceiveList());

            rechargeOrderHistoryAdapter = new RechargeOrderHistoryAdapter(mItems);
            rechargeOrderHistoryAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(rechargeOrderHistoryAdapter);
            rechargeOrderHistoryAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
        } else {
            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
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
        ApiRequest.orderReceiveHistory(page + "", pageSize + "", "RechargeOrderHistoryFragment_orderReceiveHistory", pb_loading);
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        //确认完结 = 5,
        //    维权完结 = 7,
        //    已取消 = 8,
        final int status = mItems.get(position).getReceiveOrderStatus();
        final String orderReceiveId = mItems.get(position).getId();
        final String orderCancelRemarks = mItems.get(position).getOrderCancelRemarks();
        final Bundle bundle = new Bundle();
        bundle.putString("orderReceiveId",orderReceiveId);
        if(status == 5){
            RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
            start(rechargeOrderDetailFragment);
            return;
        }
        initPopWindow_13(status,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 1:
                        RechargeOrderDetailFragment rechargeOrderDetailFragment = RechargeOrderDetailFragment.newInstance(bundle);
                        start(rechargeOrderDetailFragment);
                        break;
                    case 2:
                        MyToast.newInstance(getActivity(),"请前往web端操作","APP暂不支持").show();
                        break;
                    case 3:
                        Toast.makeText(mContext,orderCancelRemarks,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back) {
            getActivity().finish();
        }
    }

    public void initPopWindow_13(int status,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(getActivity() == null){
            return;
        }
        View mPopView = getActivity().getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
        RelativeLayout rl_1 = (RelativeLayout)mPopView.findViewById(R.id.rl_1);
        RelativeLayout rl_2 = (RelativeLayout)mPopView.findViewById(R.id.rl_2);
        RelativeLayout rl_3 = (RelativeLayout)mPopView.findViewById(R.id.rl_3);
        RelativeLayout rl_4 = (RelativeLayout)mPopView.findViewById(R.id.rl_4);
        RelativeLayout rl_5 = (RelativeLayout)mPopView.findViewById(R.id.rl_5);
        RelativeLayout rl_6 = (RelativeLayout)mPopView.findViewById(R.id.rl_6);
        TextView tv_1 = (TextView) mPopView.findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) mPopView.findViewById(R.id.tv_2);
        TextView tv_3 = (TextView) mPopView.findViewById(R.id.tv_3);
        TextView tv_4 = (TextView) mPopView.findViewById(R.id.tv_4);
        TextView tv_5 = (TextView) mPopView.findViewById(R.id.tv_5);
        TextView tv_6 = (TextView) mPopView.findViewById(R.id.tv_6);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_8 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_8.setOutsideTouchable(true);
        mPopupWindow_8.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(getActivity(),1f);
            }
        });

        switch (status){
            case 7://维权完结
                tv_1.setText("订单详情");
                tv_2.setText("查看维权");
                rl_3.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                rl_5.setVisibility(View.GONE);
                rl_6.setVisibility(View.GONE);
                break;
            case 8://已取消
                tv_1.setText("订单详情");
                tv_3.setText("取消原因");
                rl_2.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                rl_5.setVisibility(View.GONE);
                rl_6.setVisibility(View.GONE);
                break;
        }

        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,1);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });
        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,2);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });
        rl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,3);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });
        rl_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,4);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });
        rl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,5);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });
        rl_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,6);
                }
                if (mPopupWindow_8.isShowing()) {
                    mPopupWindow_8.dismiss();
                }
            }
        });

        mPopupWindow_8.setAnimationStyle(R.style.pop_animation_up);

        mPopupWindow_8.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
        Utils.backgroundAlpha(getActivity(),0.7f);

    }
}
