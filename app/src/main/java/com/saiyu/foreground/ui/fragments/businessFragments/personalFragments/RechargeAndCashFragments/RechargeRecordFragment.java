package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
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
import com.saiyu.foreground.adapters.RechargeRecordAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.RechargeRecordRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.ButtonUtils;
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

//充值记录列表页面
@EFragment(R.layout.fragment_base_record)
public class RechargeRecordFragment extends BaseFragment implements CallbackUtils.ResponseCallback ,OnRefreshListener,OnLoadmoreListener , OnItemClickListener {
    @ViewById
    TextView tv_title_content,tv_title_right;
    @ViewById
    Button btn_title_back,btn_blank;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private RechargeRecordAdapter rechargeRecordAdapter;
    private List<RechargeRecordRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static RechargeRecordFragment newInstance(Bundle bundle) {
        RechargeRecordFragment_ fragment = new RechargeRecordFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.financeRechargeLog(page + "",pageSize + "","RechargeRecordFragment_financeRechargeLog",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("充值记录");
        tv_title_right.setText("资金明细");
        tv_title_right.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        rechargeRecordAdapter = new RechargeRecordAdapter(mItems);
        recyclerView.setAdapter(rechargeRecordAdapter);
        rechargeRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("RechargeRecordFragment_financeRechargeLog")){
            RechargeRecordRet ret = (RechargeRecordRet)baseRet;
            if(ret.getData() == null || ret.getData().getRechargeLogs() == null){
                return;
            }

            int totalCount = ret.getData().getDataCount();

            if(totalCount == 0){
                recyclerView.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有充值记录");
                return;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
            }

            totalPage = totalCount/pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getRechargeLogs());

            rechargeRecordAdapter = new RechargeRecordAdapter(mItems);
            rechargeRecordAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(rechargeRecordAdapter);
            rechargeRecordAdapter.notifyDataSetChanged();

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
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.CashDetailFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(page < totalPage){
            page++;
            ApiRequest.financeRechargeLog(page + "",pageSize + "","RechargeRecordFragment_financeRechargeLog",pb_loading);
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
            ApiRequest.financeRechargeLog(page + "",pageSize + "","RechargeRecordFragment_financeRechargeLog",pb_loading);
        } else {
            Toast.makeText(mContext,"已经是第一页",Toast.LENGTH_SHORT).show();
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
        initPopWindow_7(mItems.get(position));
    }

    public void initPopWindow_7(RechargeRecordRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(getActivity() == null){
            return;
        }
        TextView tv_1_1, tv_1_2, tv_2_1, tv_2_2, tv_3_1, tv_3_2, tv_4_1, tv_4_2, tv_5_1, tv_5_2, tv_6_1, tv_6_2, tv_7_1, tv_7_2, tv_8_1, tv_8_2;
        RelativeLayout rl_7,rl_8;
        LinearLayout ll_pop;
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
        final PopupWindow mPopupWindow_7 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_7.setOutsideTouchable(true);
        mPopupWindow_7.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_7.setFocusable(true);
        mPopupWindow_7.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(getActivity(),1f);
            }
        });

        ll_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow_7.isShowing()) {
                    mPopupWindow_7.dismiss();
                }
            }
        });

        mPopupWindow_7.setAnimationStyle(R.style.pop_animation_up);

        mPopupWindow_7.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
        Utils.backgroundAlpha(getActivity(),0.7f);

        if(itemsBean == null){
            return;
        }

        tv_1_1.setText("当前状态");

        tv_2_1.setText("渠道");
        tv_2_2.setText(itemsBean.getPayTypeName());
        tv_3_1.setText("时间");
        tv_3_2.setText(itemsBean.getCreateTime());
        tv_4_1.setText("相关单号");
        tv_4_2.setText(itemsBean.getOrderNum());
        tv_5_1.setText("金额");
        int status_2 = itemsBean.getStatus();//0充值中 1成功 2失败
        String money_2 = "";
        switch (status_2){
            case 0:
                tv_1_2.setText("充值中");
                money_2 = "<font color = \"#666666\">" + itemsBean.getApplyMoney() + "元" + "</font>";
                break;
            case 1:
                tv_1_2.setText("充值成功");
                money_2 = "<font color = \"#5069d5\">" +"+"+ itemsBean.getSuccMoney() + "元" + "</font>";
                break;
            case 2:
                tv_1_2.setText("充值失败");
                money_2 = "<font color = \"#fe8f62\">" + itemsBean.getApplyMoney() + "元" + "</font>";
                break;
        }

        tv_5_2.setText(Html.fromHtml(money_2));
        tv_6_1.setText("当前金额");
        tv_6_2.setText(itemsBean.getCurrentMoney() + "元");

        rl_7.setVisibility(View.GONE);
        rl_8.setVisibility(View.GONE);
    }

}
