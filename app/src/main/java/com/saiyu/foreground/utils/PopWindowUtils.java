package com.saiyu.foreground.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MyTagAdapter;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.CashRecordRet;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.https.response.ReceivePointRet;
import com.saiyu.foreground.https.response.RechargeRecordRet;
import com.saiyu.foreground.https.response.RechargeStreamRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.saiyu.foreground.ui.views.ReboundScrollView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopWindowUtils {

    private static TextView tv_1_1, tv_1_2, tv_2_1, tv_2_2, tv_3_1, tv_3_2, tv_4_1, tv_4_2, tv_5_1, tv_5_2, tv_6_1, tv_6_2, tv_7_1, tv_7_2, tv_8_1, tv_8_2;
    private static RelativeLayout rl_7,rl_8;
    private static LinearLayout ll_pop;
    private static View getPopView(){
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.popwindow_layout, null);
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

        return mPopView;
    }

    public static void initPopWindow_5(CashDetailRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = getPopView();
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_5 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_5.setOutsideTouchable(true);
        mPopupWindow_5.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_5.setFocusable(true);
        mPopupWindow_5.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

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
            money = "<font color = \"#fe8f62\">" +"-"+ itemsBean.getMoney() + "</font>";
        } else if(status == 0){
            money = "<font color = \"#148cf1\">" +"+"+ itemsBean.getMoney() + "</font>";
        }
        tv_5_2.setText(Html.fromHtml(money));
        tv_6_1.setText("当前金额");
        tv_6_2.setText(itemsBean.getCurrentMoney());

        rl_7.setVisibility(View.GONE);
        rl_8.setVisibility(View.GONE);
    }

    public static void initPopWindow_6(CashRecordRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = getPopView();
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_6 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_6.setOutsideTouchable(true);
        mPopupWindow_6.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_6.setFocusable(true);
        mPopupWindow_6.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

        if(itemsBean == null){
            return;
        }

        tv_1_1.setText("当前状态");

        tv_2_1.setText("渠道");
        tv_2_2.setText(itemsBean.getWithdrawWayName());
        tv_3_1.setText("时间");
        tv_3_2.setText(itemsBean.getCreateTime());
        tv_4_1.setText("相关单号");
        tv_4_2.setText(itemsBean.getOrderNum());
        tv_5_1.setText("申请金额");
        int status_3 = itemsBean.getStatus();//0提现中 1成功 2失败
        String money_3 = "";
        switch (status_3){
            case 0:
                tv_1_2.setText("提现中");
                money_3 = "<font color = \"#8C8C8C\">" + itemsBean.getApplyMoney() + "</font>";
                break;
            case 1:
                tv_1_2.setText("提现成功");
                money_3 = "<font color = \"#148cf1\">" +"-"+ itemsBean.getApplyMoney() + "</font>";
                break;
            case 2:
                tv_1_2.setText("提现失败");
                money_3 = "<font color = \"#fe8f62\">"+ itemsBean.getApplyMoney() + "</font>";
                break;
        }

        tv_5_2.setText(Html.fromHtml(money_3));
        tv_6_1.setText("手续费");
        tv_6_2.setText(itemsBean.getChargeMoeny());
        tv_7_1.setText("到账金额");
        tv_7_2.setText(itemsBean.getSuccMoney());
        tv_8_1.setText("当前余额");
        tv_8_2.setText(itemsBean.getCurrentMoney());
    }

    public static void initPopWindow_7(RechargeRecordRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = getPopView();
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_7 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_7.setOutsideTouchable(true);
        mPopupWindow_7.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_7.setFocusable(true);
        mPopupWindow_7.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

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
                money_2 = "<font color = \"#8C8C8C\">" + itemsBean.getApplyMoney() + "</font>";
                break;
            case 1:
                tv_1_2.setText("充值成功");
                money_2 = "<font color = \"#148cf1\">" +"+"+ itemsBean.getSuccMoney() + "</font>";
                break;
            case 2:
                tv_1_2.setText("充值失败");
                money_2 = "<font color = \"#fe8f62\">" + itemsBean.getApplyMoney() + "</font>";
                break;
        }

        tv_5_2.setText(Html.fromHtml(money_2));
        tv_6_1.setText("当前金额");
        tv_6_2.setText(itemsBean.getCurrentMoney());

        rl_7.setVisibility(View.GONE);
        rl_8.setVisibility(View.GONE);
    }

    private static TagFlowLayout tfl_recharge_game;
    private static ProgressBar pb_loading;
    private static String gameSelected = "";
    public static void initPopWindow_2(final List<HallRet.DatasBean.ProductItemsBean> productItemsBeans, View view, final OnListCallbackListener listCallbackListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.pop_recharge_game, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_2 = new PopupWindow(mPopView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_2.setOutsideTouchable(true);
        mPopupWindow_2.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_2.setFocusable(true);
        mPopupWindow_2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(), 1f);
            }
        });

        Button btn_confirm = (Button)mPopView.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listCallbackListener != null){
                    List<String> callbackList = new ArrayList<>();
                    callbackList.add(gameSelected);
                    listCallbackListener.setOnListCallbackListener(callbackList);
                }
                if(mPopupWindow_2.isShowing()){
                    mPopupWindow_2.dismiss();
                }
            }
        });

        pb_loading = (ProgressBar)mPopView.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        tfl_recharge_game = (TagFlowLayout) mPopView.findViewById(R.id.tfl_recharge_game);

        Message message = new Message();
        message.obj = productItemsBeans;
        message.what = 2;
        handler.sendMessageDelayed(message,100);

        mPopupWindow_2.setAnimationStyle(R.style.pop_animation);
        // 作为下拉视图显示
        mPopupWindow_2.showAsDropDown(view, Gravity.TOP, 0, 300);
        backgroundAlpha(BaseActivity.getBaseActivity(), 0.7f);

    }


    private static String extend = "",rQBCount = "",rDiscount = "";
    private static TagFlowLayout tfl_1,tfl_2,tfl_3;
    public static void initPopWindow_3( View view,final OnListCallbackListener listCallbackListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.pop_selector, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_3 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_3.setOutsideTouchable(true);
        mPopupWindow_3.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_3.setFocusable(true);
        mPopupWindow_3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                List<String> callbackList = new ArrayList<>();
                callbackList.add(rQBCount);
                callbackList.add(rDiscount);
                callbackList.add(extend);
                listCallbackListener.setOnListCallbackListener(callbackList);
                backgroundAlpha(BaseActivity.getBaseActivity(), 1f);
            }
        });

        final Button btn_reset = (Button)mPopView.findViewById(R.id.btn_reset);
        final Button btn_confirm = (Button)mPopView.findViewById(R.id.btn_confirm);

        final EditText et_count_1 = (EditText)mPopView.findViewById(R.id.et_count_1);
        final EditText et_count_2 = (EditText)mPopView.findViewById(R.id.et_count_2);
        final EditText et_count_3 = (EditText)mPopView.findViewById(R.id.et_count_3);
        final EditText et_count_4 = (EditText)mPopView.findViewById(R.id.et_count_4);

        pb_loading = (ProgressBar)mPopView.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        tfl_1 = (TagFlowLayout) mPopView.findViewById(R.id.tfl_1);
        tfl_2 = (TagFlowLayout) mPopView.findViewById(R.id.tfl_2);
        tfl_3 = (TagFlowLayout) mPopView.findViewById(R.id.tfl_3);

        Message message = new Message();
        message.what = 3;
        handler.sendMessageDelayed(message,100);

        tfl_1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                et_count_1.setText("");
                et_count_2.setText("");
                return false;
            }
        });

        tfl_1.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if(selectPosSet == null || selectPosSet.size() == 0){
                    rQBCount = "";
                    return;
                }
                for (Integer in : selectPosSet) {
                    switch (in){
                        case 0:
                            rQBCount = "";
                            break;
                        case 1:
                            rQBCount = "50";
                            break;
                        case 2:
                            rQBCount = "100";
                            break;
                        case 3:
                            rQBCount = "300";
                            break;
                        case 4:
                            rQBCount = "500";
                            break;
                        case 5:
                            rQBCount = "1000";
                            break;
                    }
                }
                LogUtils.print("出售数量 === "+rQBCount);
            }
        });

        et_count_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Set<Integer> set = new HashSet<>();
                    tfl_1.getAdapter().setSelectedList(set);
                    rQBCount = "";
                }
                return false;
            }
        });

        et_count_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Set<Integer> set = new HashSet<>();
                    tfl_1.getAdapter().setSelectedList(set);
                    rQBCount = "";
                }
                return false;
            }
        });

        tfl_2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                et_count_3.setText("");
                et_count_4.setText("");
                return false;
            }
        });

        tfl_2.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if(selectPosSet == null || selectPosSet.size() == 0){
                    rDiscount = "";
                    return;
                }
                for (Integer in : selectPosSet) {
                    switch (in){
                        case 0:
                            rDiscount = "";
                            break;
                        case 1:
                            rDiscount = "70";
                            break;
                        case 2:
                            rDiscount = "75";
                            break;
                        case 3:
                            rDiscount = "80";
                            break;
                        case 4:
                            rDiscount = "83";
                            break;
                        case 5:
                            rDiscount = "86";
                            break;
                        case 6:
                            rDiscount = "88";
                            break;
                        case 7:
                            rDiscount = "90+";
                            break;
                    }
                }
                LogUtils.print("出售折扣 === "+rDiscount);
            }
        });

        et_count_3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Set<Integer> set = new HashSet<>();
                    tfl_2.getAdapter().setSelectedList(set);
                    rDiscount = "";

                }
                return false;
            }
        });

        et_count_4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Set<Integer> set = new HashSet<>();
                    tfl_2.getAdapter().setSelectedList(set);
                    rDiscount = "";
                }
                return false;
            }
        });

        tfl_3.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(position == 0){
                    extend = "";
                    tfl_3.getAdapter().setSelectedList(0);
                    return true;
                }
                return false;
            }
        });

        tfl_3.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if(selectPosSet == null || selectPosSet.size() == 0){
                    extend = "";
                    return;
                }
                if(!TextUtils.isEmpty(extend)){
                    extend = "";
                }
                for (Integer in : selectPosSet) {
                    if(in == 0){
                        if(selectPosSet.size() == 2){
                            selectPosSet.remove(0);
                            for(Integer in_2 : selectPosSet){
                                extend = in_2+"";
                            }
                            tfl_3.getAdapter().setSelectedList(selectPosSet);
                        }
                        break;
                    } else {
                        if(TextUtils.isEmpty(extend)){
                            extend = in+"";
                        } else {
                            extend = extend + "o" + in+"";
                        }
                    }
                }
                LogUtils.print("extend === "+extend);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_count_1.setText("");
                et_count_2.setText("");
                et_count_3.setText("");
                et_count_4.setText("");
                tfl_1.getAdapter().setSelectedList(0);
                tfl_2.getAdapter().setSelectedList(0);
                tfl_3.getAdapter().setSelectedList(0);

                extend = "";
                rQBCount = "";
                rDiscount = "";

                if (mPopupWindow_3.isShowing()) {
                    mPopupWindow_3.dismiss();
                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listCallbackListener != null){
                    String tv_1 = et_count_1.getText().toString();
                    String tv_2 = et_count_2.getText().toString();
                    String tv_3 = et_count_3.getText().toString();
                    String tv_4 = et_count_4.getText().toString();
                    if(TextUtils.isEmpty(tv_1) && TextUtils.isEmpty(tv_2)){

                    } else {
                        if(TextUtils.isEmpty(tv_1)){
                            rQBCount = tv_2;
                        } else if(TextUtils.isEmpty(tv_2)){
                            rQBCount = tv_1;
                        } else {
                            rQBCount = tv_1 + "o" + tv_2;
                        }
                    }
                    if(TextUtils.isEmpty(tv_3) && TextUtils.isEmpty(tv_4)){

                    } else {
                        if(TextUtils.isEmpty(tv_3)){
                            rDiscount = tv_4;
                        } else if(TextUtils.isEmpty(tv_4)){
                            rDiscount = tv_3;
                        } else {
                            rDiscount = tv_3 + "o" + tv_4;
                        }
                    }

                    if (mPopupWindow_3.isShowing()) {
                        mPopupWindow_3.dismiss();
                    }
                }
            }
        });

        mPopupWindow_3.setAnimationStyle(R.style.pop_animation);
        // 作为下拉视图显示
        mPopupWindow_3.showAsDropDown(view, Gravity.TOP, 0, 300);
        backgroundAlpha(BaseActivity.getBaseActivity(), 0.7f);

    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    List<HallRet.DatasBean.ProductItemsBean> productItemsBeans = (List<HallRet.DatasBean.ProductItemsBean>)msg.obj;

                    final MyTagAdapter myTagAdapter = new MyTagAdapter(productItemsBeans);
                    tfl_recharge_game.setAdapter(myTagAdapter);

                    tfl_recharge_game.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            if(position == 0){
                                gameSelected = "";
                                myTagAdapter.setSelectedList(0);
                                return true;
                            }
                            return false;
                        }
                    });

                    tfl_recharge_game.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                        @Override
                        public void onSelected(Set<Integer> selectPosSet) {
                            if(selectPosSet == null || selectPosSet.size() == 0){
                                gameSelected = "";
                                return;
                            }
                            if(!TextUtils.isEmpty(gameSelected)){
                                gameSelected = "";
                            }
                            for (Integer in : selectPosSet) {
                                if(in == 0){
                                    if(selectPosSet.size() == 2){
                                        selectPosSet.remove(0);
                                        for(Integer in_2 : selectPosSet){
                                            gameSelected = myTagAdapter.getItem(in_2).getId();
                                        }
                                        myTagAdapter.setSelectedList(selectPosSet);
                                    }
                                    break;
                                } else {
                                    if(TextUtils.isEmpty(gameSelected)){
                                        gameSelected = myTagAdapter.getItem(in).getId();
                                    } else {
                                        gameSelected = gameSelected + "o" + myTagAdapter.getItem(in).getId();
                                    }
                                }
                            }
                            LogUtils.print("gameSelected === "+gameSelected);
                        }
                    });

                    gameSelected = "";
                    myTagAdapter.setSelectedList(0);

                    pb_loading.setVisibility(View.GONE);
                    break;
                case 3:

                    final String[] arg_1 = {"不限", "50Q币", "100Q币", "300Q币", "500Q币", "1000Q币"};
                    final String[] arg_2 = {"不限", "70折", "75折", "80折", "83折", "86折", "88折", "90折+"};
                    final String[] arg_3 = {"不限", "私密订单", "少冲按原价", "客服代确认", "验图确认", "次数不限制", "无需加好友"};

                    tfl_1.setAdapter(new TagAdapter<String>(arg_1) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hall_tv, tfl_1, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                    tfl_1.setMaxSelectCount(1);
                    tfl_1.getAdapter().setSelectedList(0);

                    tfl_2.setAdapter(new TagAdapter<String>(arg_2) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hall_tv, tfl_2, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                    tfl_2.setMaxSelectCount(1);
                    tfl_2.getAdapter().setSelectedList(0);

                    tfl_3.setAdapter(new TagAdapter<String>(arg_3) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hall_tv, tfl_3, false);
                            tv.setText(s);
                            return tv;
                        }
                    });

                    tfl_3.getAdapter().setSelectedList(0);
                    extend = "";
                    rQBCount = "";
                    rDiscount = "";

                    pb_loading.setVisibility(View.GONE);

                    break;
            }
        }
    };


    public static void initPopWindow_4(View view, final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.pop_sort, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_4 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_4.setOutsideTouchable(true);
        mPopupWindow_4.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_4.setFocusable(true);
        mPopupWindow_4.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(), 1f);
            }
        });

        LinearLayout ll_1,ll_2,ll_3,ll_4,ll_5,ll_6,ll_7,ll_8;
        ll_1 = (LinearLayout)mPopView.findViewById(R.id.ll_1);
        ll_2 = (LinearLayout)mPopView.findViewById(R.id.ll_2);
        ll_3 = (LinearLayout)mPopView.findViewById(R.id.ll_3);
        ll_4 = (LinearLayout)mPopView.findViewById(R.id.ll_4);
        ll_5 = (LinearLayout)mPopView.findViewById(R.id.ll_5);
        ll_6 = (LinearLayout)mPopView.findViewById(R.id.ll_6);
        ll_7 = (LinearLayout)mPopView.findViewById(R.id.ll_7);
        ll_8 = (LinearLayout)mPopView.findViewById(R.id.ll_8);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,1);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,2);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,3);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,4);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,5);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,6);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,7);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });
        ll_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,8);
                }
                if (mPopupWindow_4.isShowing()) {
                    mPopupWindow_4.dismiss();
                }
            }
        });

        mPopupWindow_4.setAnimationStyle(R.style.pop_animation);
        // 作为下拉视图显示
        mPopupWindow_4.showAsDropDown(view, Gravity.TOP, 0, 300);
        backgroundAlpha(BaseActivity.getBaseActivity(), 0.7f);

    }

    public static void initPopWindow_8(int status,int status_2,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
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
        mPopupWindow_8.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

    }


    public static void initPopWindow_9(final Activity activity,String gameName,final OnClickListener onClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = activity.getLayoutInflater().inflate(R.layout.pop_orderrelease, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_9 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_9.setOutsideTouchable(true);
        mPopupWindow_9.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_9.setFocusable(true);
        mPopupWindow_9.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity, 1f);
            }
        });

        final CheckBox cb_1 = (CheckBox)mPopView.findViewById(R.id.cb_1);
        cb_1.setText("我今天没有充值相同数量的"+gameName+"点券");
        final CheckBox cb_2 = (CheckBox)mPopView.findViewById(R.id.cb_2);
        final CheckBox cb_3 = (CheckBox)mPopView.findViewById(R.id.cb_3);
        final Button btn_confirm = (Button)mPopView.findViewById(R.id.btn_confirm);
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_2.isChecked() && cb_3.isChecked()){
                        Utils.setButtonClickable(btn_confirm,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm,false);
                }
            }
        });
        cb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_1.isChecked() && cb_3.isChecked()){
                        Utils.setButtonClickable(btn_confirm,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm,false);
                }
            }
        });
        cb_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cb_1.isChecked() && cb_2.isChecked()){
                        Utils.setButtonClickable(btn_confirm,true);
                    } else {
                        Utils.setButtonClickable(btn_confirm,false);
                    }
                } else {
                    Utils.setButtonClickable(btn_confirm,false);
                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
                if (mPopupWindow_9.isShowing()) {
                    mPopupWindow_9.dismiss();
                }
            }
        });

        mPopupWindow_9.setAnimationStyle(R.style.pop_animation_up);
        // 作为下拉视图显示
        mPopupWindow_9.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(activity, 0.7f);

    }

    public static void initPopWindow_10(RechargeStreamRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = getPopView();
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_6 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_6.setOutsideTouchable(true);
        mPopupWindow_6.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_6.setFocusable(true);
        mPopupWindow_6.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

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

    public static void initPopWindow_11(final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
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
        mPopupWindow_8.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
            }
        });

        rl_4.setVisibility(View.GONE);
        rl_5.setVisibility(View.GONE);
        rl_6.setVisibility(View.GONE);
        tv_1.setText("订单详情");
        tv_2.setText("充值流水");
        tv_3.setText("订单日志");

        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,0);
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
                    onItemClickListener.onItemClick(v,1);
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
                    onItemClickListener.onItemClick(v,2);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

    }

    public static void initPopWindow_12(int status,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
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
        mPopupWindow_8.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
            }
        });

        switch (status){
            case 4://等待确认
                tv_1.setText("订单详情");
                tv_2.setText("确认收货");
                tv_3.setText("发起维权");
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

    }

    public static void initPopWindow_13(int status,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = BaseActivity.getBaseActivity().getLayoutInflater().inflate(R.layout.popwindow_8_layout, null);
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
        mPopupWindow_8.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_8.setFocusable(true);
        mPopupWindow_8.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

    }

    public static void initPopWindow_14(final Activity activity,int status,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
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
                backgroundAlpha(activity,1f);
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
        backgroundAlpha(activity,0.7f);

    }

    public static void initPopWindow_15(ReceivePointRet.DatasBean.ItemsBean itemsBean) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        View mPopView = getPopView();
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_5 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_5.setOutsideTouchable(true);
        mPopupWindow_5.setBackgroundDrawable(BaseActivity.getBaseActivity().getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_5.setFocusable(true);
        mPopupWindow_5.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(BaseActivity.getBaseActivity(),1f);
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
        backgroundAlpha(BaseActivity.getBaseActivity(),0.7f);

        if(itemsBean == null){
            return;
        }

        tv_1_1.setText("业务类型");
        tv_1_2.setText(itemsBean.getBizNote());
        tv_2_1.setText("项目类型");
        tv_2_2.setText(itemsBean.getBizTypeStr());
        tv_3_1.setText("时间");
        tv_3_2.setText(itemsBean.getCreateTime());
        tv_4_1.setText("相关单号");
        tv_4_2.setText(itemsBean.getOrderNum());
        tv_5_1.setText("点数");
        int status = itemsBean.getType();//0 收入； 1 支出
        String money = "";
        if(status == 1){
            money = "<font color = \"#fe8f62\">" +"-"+ itemsBean.getPoint() + "</font>";
        } else if(status == 0){
            money = "<font color = \"#148cf1\">" +"+"+ itemsBean.getPoint() + "</font>";
        }
        tv_5_2.setText(Html.fromHtml(money));
        tv_6_1.setText("当前点数");
        tv_6_2.setText(itemsBean.getCurrentPoint());

        rl_7.setVisibility(View.GONE);
        rl_8.setVisibility(View.GONE);
    }

    public static void initPopWindow_16(final Activity activity,int status,final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
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
                backgroundAlpha(activity,1f);
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
        backgroundAlpha(activity,0.7f);

    }


    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
