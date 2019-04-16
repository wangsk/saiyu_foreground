package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

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
import com.saiyu.foreground.adapters.RechargeStreamAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.RechargeStreamRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.saiyu.foreground.ui.fragments.BaseFragment;
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

@EFragment(R.layout.fragment_recharge_stream)
public class RechargeStreamFragment extends BaseFragment implements CallbackUtils.ResponseCallback,OnItemClickListener {
    @ViewById
    TextView tv_title_content,tv_ordernum,tv_game,tv_discount;
    @ViewById
    Button btn_title_back,btn_confirm,btn_blank;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading,pb_order;
    private RechargeStreamAdapter rechargeStreamAdapter;
    private List<RechargeStreamRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private String orderId;

    public static RechargeStreamFragment newInstance(Bundle bundle) {
        RechargeStreamFragment_ fragment = new RechargeStreamFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.orderReceiveList(orderId,"RechargeStreamFragment_orderReceiveList",pb_loading);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("充值流水");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableRefresh(false);
        rechargeStreamAdapter = new RechargeStreamAdapter(mItems);
        recyclerView.setAdapter(rechargeStreamAdapter);
        rechargeStreamAdapter.notifyDataSetChanged();

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
        if (method.equals("RechargeStreamFragment_orderReceiveList")) {
            RechargeStreamRet ret = (RechargeStreamRet) baseRet;
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
                btn_blank.setText("您当前没有充值流水");
                return;
            } else {
                btn_blank.setVisibility(View.GONE);
            }

            rechargeStreamAdapter = new RechargeStreamAdapter(mItems);
            rechargeStreamAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(rechargeStreamAdapter);
            rechargeStreamAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null && mItems.size() <= position){
            return;
        }
        initPopWindow_10(mItems.get(position));
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back || view.getId() == R.id.btn_confirm) {
            getFragmentManager().popBackStack();
        }
    }

    public void initPopWindow_10(RechargeStreamRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        TextView tv_1_1, tv_1_2, tv_2_1, tv_2_2, tv_3_1, tv_3_2, tv_4_1, tv_4_2, tv_5_1, tv_5_2, tv_6_1, tv_6_2, tv_7_1, tv_7_2, tv_8_1, tv_8_2;
        RelativeLayout rl_7,rl_8;
        LinearLayout ll_pop;

        if(getActivity() == null){
            return;
        }

        View mPopView = getActivity().getLayoutInflater().inflate(R.layout.popwindow_layout, null);
        ll_pop = (LinearLayout) mPopView.findViewById(R.id.ll_pop);
        rl_7 = (RelativeLayout)mPopView.findViewById(R.id.rl_7);
        rl_8 = (RelativeLayout)mPopView.findViewById(R.id.rl_8);
        tv_1_1 = (TextView) mPopView.findViewById(R.id.tv_1_1);
        tv_1_2 = (TextView) mPopView.findViewById(R.id.tv_1_2);
        tv_2_1 = (TextView) mPopView.findViewById(R.id.tv_2_1);
        tv_2_2 = (TextView) mPopView.findViewById(R.id.tv_2_2);
        tv_3_1 = (TextView) mPopView.findViewById(R.id.tv_3_1);
        tv_3_2 = (TextView) mPopView.findViewById(R.id.tv_3_2);
        tv_4_1 = (TextView) mPopView.findViewById(R.id.tv_4_1);
        tv_4_2 = (TextView) mPopView.findViewById(R.id.tv_4_2);
        tv_5_1 = (TextView) mPopView.findViewById(R.id.tv_5_1);
        tv_5_2 = (TextView) mPopView.findViewById(R.id.tv_5_2);
        tv_6_1 = (TextView) mPopView.findViewById(R.id.tv_6_1);
        tv_6_2 = (TextView) mPopView.findViewById(R.id.tv_6_2);
        tv_7_1 = (TextView) mPopView.findViewById(R.id.tv_7_1);
        tv_7_2 = (TextView) mPopView.findViewById(R.id.tv_7_2);
        tv_8_1 = (TextView) mPopView.findViewById(R.id.tv_8_1);
        tv_8_2 = (TextView) mPopView.findViewById(R.id.tv_8_2);

        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_6 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_6.setOutsideTouchable(true);
        mPopupWindow_6.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_6.setFocusable(true);
        mPopupWindow_6.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(getActivity(),1f);
            }
        });

        ll_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow_6.isShowing()) {
                    mPopupWindow_6.dismiss();
                }
            }
        });

        mPopupWindow_6.setAnimationStyle(R.style.pop_animation_up);

        mPopupWindow_6.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
        Utils.backgroundAlpha(getActivity(),0.7f);

        if(itemsBean == null){
            return;
        }

        tv_1_1.setText("订单状态");
        tv_1_2.setText(itemsBean.getReceiveOrderStatus());
        tv_2_1.setText("充值订单号");
        tv_2_2.setText(itemsBean.getReceiveOrderNum());
        tv_3_1.setText("接单时间");
        tv_3_2.setText(itemsBean.getCreateTime());
        tv_4_1.setText("完成时间");
        tv_4_2.setText(itemsBean.getOrderFinishTime());
        tv_5_1.setText("充值时间");
        tv_5_2.setText(itemsBean.getRechargeTime());
        tv_6_1.setText("接单数量");
        tv_6_2.setText(itemsBean.getReserveQBCount());
        tv_7_1.setText("成功数量");
        tv_7_2.setText(itemsBean.getSuccQBCount());
        tv_8_1.setText("成功金额");
        tv_8_2.setText(itemsBean.getSuccMoney()+"元");
    }

}
