package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

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
import com.saiyu.foreground.adapters.CashDetailAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashDetailRet;
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

//资金明细列表页面
@EFragment(R.layout.fragment_base_record)
public class CashDetailFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnRefreshListener, OnLoadmoreListener, OnItemClickListener {
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
    private CashDetailAdapter cashDetailAdapter;
    private List<CashDetailRet.DatasBean.ItemsBean> mItems = new ArrayList<>();
    private int page = 1;
    private int pageSize = 30;
    private int totalPage;

    public static CashDetailFragment newInstance(Bundle bundle) {
        CashDetailFragment_ fragment = new CashDetailFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.financeRecord(page + "", pageSize + "", "CashDetailFragment_financeRecord", pb_loading);

    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("资金明细");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(1));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        cashDetailAdapter = new CashDetailAdapter(mItems);
        recyclerView.setAdapter(cashDetailAdapter);
        cashDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("CashDetailFragment_financeRecord")) {
            CashDetailRet ret = (CashDetailRet) baseRet;
            if (ret.getData() == null || ret.getData().getRecordLogs() == null) {
                return;
            }

            int totalCount = ret.getData().getDataCount();

            if(totalCount == 0){
                recyclerView.setVisibility(View.GONE);
                btn_blank.setVisibility(View.VISIBLE);
                btn_blank.setText("您当前没有资金明细记录");
                return;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btn_blank.setVisibility(View.GONE);
            }

            totalPage = totalCount / pageSize + 1;
            LogUtils.print("总数:" + totalCount + " ;总页码：" + totalPage);

            mItems.clear();
            mItems.addAll(ret.getData().getRecordLogs());

            cashDetailAdapter = new CashDetailAdapter(mItems);
            cashDetailAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(cashDetailAdapter);
            cashDetailAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (page < totalPage) {
            page++;
            ApiRequest.financeRecord(page + "", pageSize + "", "CashDetailFragment_financeRecord", pb_loading);
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
            ApiRequest.financeRecord(page + "", pageSize + "", "CashDetailFragment_financeRecord", pb_loading);
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
        initPopWindow_5(mItems.get(position));
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        if (view.getId() == R.id.btn_title_back) {
            getActivity().finish();
        }
    }

    public void initPopWindow_5(CashDetailRet.DatasBean.ItemsBean itemsBean) {
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
        final PopupWindow mPopupWindow_5 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_5.setOutsideTouchable(true);
        mPopupWindow_5.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_5.setFocusable(true);
        mPopupWindow_5.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(getActivity(),1f);
            }
        });

        ll_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow_5.isShowing()) {
                    mPopupWindow_5.dismiss();
                }
            }
        });

        mPopupWindow_5.setAnimationStyle(R.style.pop_animation_up);

        mPopupWindow_5.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
        Utils.backgroundAlpha(getActivity(),0.7f);

        if(itemsBean == null){
            return;
        }

        tv_1_1.setText("业务类型");
        tv_1_2.setText(itemsBean.getBizNote());
        tv_2_1.setText("渠道");
        tv_2_2.setText(itemsBean.getWithdrawName());
        tv_3_1.setText("时间");
        tv_3_2.setText(itemsBean.getCreateTime());
        tv_4_1.setText("相关单号");
        tv_4_2.setText(itemsBean.getOrderNum());
        tv_5_1.setText("金额");
        int status = itemsBean.getType();//0 收入； 1 支出
        String money = "";
        if(status == 1){
            money = "<font color = \"#fe8f62\">" +"-"+ itemsBean.getMoney() + "元" + "</font>";
        } else if(status == 0){
            money = "<font color = \"#5069d5\">" +"+"+ itemsBean.getMoney()  + "元"+ "</font>";
        }
        tv_5_2.setText(Html.fromHtml(money));
        tv_6_1.setText("当前金额");
        tv_6_2.setText(itemsBean.getCurrentMoney() + "元");

        rl_7.setVisibility(View.GONE);
        rl_8.setVisibility(View.GONE);
    }

}
