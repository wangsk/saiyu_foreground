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

    private static TagFlowLayout tfl_recharge_game;
    private static ProgressBar pb_loading;
    private static String gameSelected = "";
    public static void initPopWindow_2(final Activity activity,final List<HallRet.DatasBean.ProductItemsBean> productItemsBeans, View view, final OnListCallbackListener listCallbackListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(activity == null){
            return;
        }
        View mPopView = activity.getLayoutInflater().inflate(R.layout.pop_recharge_game, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_2 = new PopupWindow(mPopView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // 点击popuwindow外让其消失
        mPopupWindow_2.setOutsideTouchable(true);
        mPopupWindow_2.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_2.setFocusable(true);
        mPopupWindow_2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(activity, 1f);
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
        Utils.backgroundAlpha(activity, 0.7f);

    }

    private static String extend = "",rQBCount = "",rDiscount = "";
    private static TagFlowLayout tfl_1,tfl_2,tfl_3;
    public static void initPopWindow_3(final Activity activity,View view,final OnListCallbackListener listCallbackListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(activity == null){
            return;
        }
        View mPopView = activity.getLayoutInflater().inflate(R.layout.pop_selector, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_3 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_3.setOutsideTouchable(true);
        mPopupWindow_3.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_3.setFocusable(true);
        mPopupWindow_3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                List<String> callbackList = new ArrayList<>();
                callbackList.add(rQBCount);
                callbackList.add(rDiscount);
                callbackList.add(extend);
                listCallbackListener.setOnListCallbackListener(callbackList);
                Utils.backgroundAlpha(activity, 1f);
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
        Utils.backgroundAlpha(activity, 0.7f);

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


    public static void initPopWindow_4(final Activity activity,View view, final OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub
        // 将布局文件转换成View对象，popupview 内容视图
        if(activity == null){
            return;
        }
        View mPopView = activity.getLayoutInflater().inflate(R.layout.pop_sort, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        final PopupWindow mPopupWindow_4 = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow_4.setOutsideTouchable(true);
        mPopupWindow_4.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow_4.setFocusable(true);
        mPopupWindow_4.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(activity, 1f);
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
        Utils.backgroundAlpha(activity, 0.7f);

    }















}
