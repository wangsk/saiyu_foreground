package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.app.Activity;
import android.content.Intent;
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
import com.saiyu.foreground.adapters.SellerOrderManagerAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.SellerOrderManagerRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
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
public class SellOrderManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private SellerOrderManagerAdapter sellerOrderManagerAdapter;
    private List<SellerOrderManagerRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static SellOrderManagerFragment newInstance(Bundle bundle) {
        SellOrderManagerFragment_ fragment = new SellOrderManagerFragment_();
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
        tv_title_content.setText("出售订单管理");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        sellerOrderManagerAdapter = new SellerOrderManagerAdapter(mItems);
        recyclerView.setAdapter(sellerOrderManagerAdapter);
        sellerOrderManagerAdapter.notifyDataSetChanged();

        ApiRequest.orderReceiveManageSeller(page + "", pageSize + "", "SellOrderManagerFragment_orderReceiveManageSeller", pb_loading);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SellOrderManagerFragment_orderReceiveManageSeller")) {
            SellerOrderManagerRet ret = (SellerOrderManagerRet) baseRet;
            if (ret.getData() == null || ret.getData().getOrderReceives() == null) {
                return;
            }

            int totalCount = ret.getData().getOrderReceivesCount();

            if(totalCount == 0){
                recyclerView.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有出售订单");
                return;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
            }

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getOrderReceives());

            sellerOrderManagerAdapter = new SellerOrderManagerAdapter(mItems);
            sellerOrderManagerAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(sellerOrderManagerAdapter);
            sellerOrderManagerAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
            ApiRequest.orderReceiveManageSeller(page + "", pageSize + "", "SellOrderManagerFragment_orderReceiveManageSeller", pb_loading);
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
            ApiRequest.orderReceiveManageSeller(page + "", pageSize + "", "SellOrderManagerFragment_orderReceiveManageSeller", pb_loading);
        } else {
            Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
        }
        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            return;
        }
        final Intent intent = new Intent(mContext, ContainerActivity_.class);
        final String orderId = mItems.get(position).getId();
        final int status = mItems.get(position).getReceiveOrderStatus();
        final Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        //子订单状态
        //待充值 = 0,审核中 = 1,审核失败 = 2,已审核 = 3,等待确认 = 4,确认完结 = 5,维权中 = 6,维权完结 = 7,已取消 = 8,
        if(status == 1){
            SellerOrderDetailFragment sellerOrderDetailFragment = SellerOrderDetailFragment.newInstance(bundle);
            start(sellerOrderDetailFragment);
            return;
        }
        LogUtils.print("status == " + status + " mItems.get(position).getConfirmType() == " + mItems.get(position).getConfirmType() + " mItems.get(position).getIsPicConfirm() == " + mItems.get(position).getIsPicConfirm());
        if(status == 4 && (mItems.get(position).getConfirmType() != 0 || mItems.get(position).getIsPicConfirm() != 1)){
            SellerOrderDetailFragment sellerOrderDetailFragment = SellerOrderDetailFragment.newInstance(bundle);
            start(sellerOrderDetailFragment);
            return;
        }
        initPopWindow_14(getActivity(),status,new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 1:
                        //订单详情
                        SellerOrderDetailFragment sellerOrderDetailFragment = SellerOrderDetailFragment.newInstance(bundle);
                        start(sellerOrderDetailFragment);
                        break;
                    case 2:
                        //重新传图
                        ResetPicFragment resetPicFragment = ResetPicFragment.newInstance(bundle);
                        start(resetPicFragment);
                        break;
                    case 3:
                        //取消订单
                        bundle.putString("receiveId",orderId);
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.OrderCancelFragmentTag);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        break;
                    case 4:
                        //响应维权
                        MyToast.newInstance(getActivity(),"请前往web端操作","APP暂不支持").show();
                        break;
                    case 5:
                        //申请验图确认
                        bundle.putString("receiveId",orderId);
                        bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SubmitPicReplaceConfirmFragmentTag);
                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent, ContainerActivity.SubmitPicReplaceConfirmFragmentTag);
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

    public void initPopWindow_14(final Activity activity, int status, final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(activity == null){
            return;
        }
        View mPopView = activity.getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
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
        mPopupWindow_8.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(activity,1f);
            }
        });

        switch (status){
            case 4://等待确认
                tv_1.setText("订单详情");
                tv_5.setText("申请验图确认");
                rl_2.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                rl_3.setVisibility(View.GONE);
                rl_6.setVisibility(View.GONE);
                break;
            case 2://审核失败
                tv_1.setText("订单详情");
                tv_2.setText("重新传图");
                tv_3.setText("取消订单");
                rl_4.setVisibility(View.GONE);
                rl_5.setVisibility(View.GONE);
                rl_6.setVisibility(View.GONE);
                break;
            case 6://维权中
                tv_1.setText("订单详情");
                tv_4.setText("响应维权");
                rl_2.setVisibility(View.GONE);
                rl_3.setVisibility(View.GONE);
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
        Utils.backgroundAlpha(activity,0.7f);

    }
}
