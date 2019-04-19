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
import com.saiyu.foreground.adapters.PreOrderManagerAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.PreOrderManagerRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.BaseActivity;
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
public class PreOrderManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private PreOrderManagerAdapter preOrderManagerAdapter;
    private List<PreOrderManagerRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 20;
    private int totalPage;

    public static PreOrderManagerFragment newInstance(Bundle bundle) {
        PreOrderManagerFragment_ fragment = new PreOrderManagerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderManage(page + "", pageSize + "", "PreOrderManagerFragment_orderManage", pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("预定订单管理");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        preOrderManagerAdapter = new PreOrderManagerAdapter(mItems);
        recyclerView.setAdapter(preOrderManagerAdapter);
        preOrderManagerAdapter.notifyDataSetChanged();

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

            if(totalCount == 0){
                recyclerView.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有预定订单");
                return;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
            }

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

        initPopWindow_8(status,status_2,new OnItemClickListener() {
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
                                bundle.putBoolean("isPass",false);
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
                                bundle.putBoolean("isPass",true);
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

    public void initPopWindow_8(int status,int status_2,final OnItemClickListener onItemClickListener) {
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
            case 0://未审核
                tv_1.setText("订单详情");
                tv_2.setText("取消订单");
                rl_3.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                rl_5.setVisibility(View.GONE);
                rl_6.setVisibility(View.GONE);
                break;
            case 1://审核通过
                tv_1.setText("订单详情");
                tv_2.setText("充值流水");
                tv_3.setText("结算订单");
                tv_4.setText("修改订单");
                tv_5.setText("订单日志");
                //订单状态 0未发布 1已发布 2暂停发布 3等待结算 4已结算 5手动取消
                if(status_2 == 2){
                    tv_6.setText("启动");
                } else if(status_2 == 1){
                    tv_6.setText("暂停");
                } else {
                    rl_6.setVisibility(View.GONE);
                }

                break;
            case 2://审核失败
                tv_1.setText("订单详情");
                tv_2.setText("修改订单");
                tv_3.setText("取消订单");
                tv_4.setText("失败原因");
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
