package com.saiyu.foreground.utils;

import android.app.Activity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.CashRecordRet;
import com.saiyu.foreground.https.response.RechargeRecordRet;

public class PopWindowUtils {

    private static PopupWindow mPopupWindow;

    public static void initPopWindow(final Activity activity,int type,CashDetailRet.DatasBean.ItemsBean data,RechargeRecordRet.DatasBean.ItemsBean data_2,CashRecordRet.DatasBean.ItemsBean data_3) {
        // TODO Auto-generated method stub
        TextView tv_1_1, tv_1_2, tv_2_1, tv_2_2, tv_3_1, tv_3_2, tv_4_1, tv_4_2, tv_5_1, tv_5_2, tv_6_1, tv_6_2, tv_7_1, tv_7_2, tv_8_1, tv_8_2;
        RelativeLayout rl_7,rl_8;
        View mPopView;
        // 将布局文件转换成View对象，popupview 内容视图
        mPopView = activity.getLayoutInflater().inflate(R.layout.popwindow_layout, null);
        // 将转换的View放置到 新建一个popuwindow对象中
        mPopupWindow = new PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击popuwindow外让其消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_bg_white));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity,1f);
            }
        });
        LinearLayout ll_pop = (LinearLayout) mPopView.findViewById(R.id.ll_pop);
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

        ll_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 30);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
        backgroundAlpha(activity,0.7f);

        switch (type){
            case 0:
                if(data == null){
                    return;
                }

                tv_1_1.setText("业务类型");
                tv_1_2.setText(data.getBizNote());
                tv_2_1.setText("渠道");
                tv_2_2.setText(data.getWithdrawName());
                tv_3_1.setText("时间");
                tv_3_2.setText(data.getCreateTime());
                tv_4_1.setText("相关单号");
                tv_4_2.setText(data.getOrderNum());
                tv_5_1.setText("金额");
                int status = data.getType();//0 收入； 1 支出
                String money = "";
                if(status == 1){
                    money = "<font color = \"#fe8f62\">" +"-"+ data.getMoney() + "</font>";
                } else if(status == 0){
                    money = "<font color = \"#148cf1\">" +"+"+ data.getMoney() + "</font>";
                }
                tv_5_2.setText(Html.fromHtml(money));
                tv_6_1.setText("当前金额");
                tv_6_2.setText(data.getCurrentMoney());

                rl_7.setVisibility(View.GONE);
                rl_8.setVisibility(View.GONE);
                break;
            case 1:
                if(data_2 == null){
                    return;
                }

                tv_1_1.setText("当前状态");

                tv_2_1.setText("渠道");
                tv_2_2.setText(data_2.getPayTypeName());
                tv_3_1.setText("时间");
                tv_3_2.setText(data_2.getCreateTime());
                tv_4_1.setText("相关单号");
                tv_4_2.setText(data_2.getOrderNum());
                tv_5_1.setText("金额");
                int status_2 = data_2.getStatus();//0充值中 1成功 2失败
                String money_2 = "";
                switch (status_2){
                    case 0:
                        tv_1_2.setText("充值中");
                        money_2 = "<font color = \"#8C8C8C\">" + data_2.getApplyMoney() + "</font>";
                        break;
                    case 1:
                        tv_1_2.setText("充值成功");
                        money_2 = "<font color = \"#148cf1\">" +"+"+ data_2.getSuccMoney() + "</font>";
                        break;
                    case 2:
                        tv_1_2.setText("充值失败");
                        money_2 = "<font color = \"#fe8f62\">" + data_2.getApplyMoney() + "</font>";
                        break;
                }

                tv_5_2.setText(Html.fromHtml(money_2));
                tv_6_1.setText("当前金额");
                tv_6_2.setText(data_2.getCurrentMoney());

                rl_7.setVisibility(View.GONE);
                rl_8.setVisibility(View.GONE);
                break;
            case 2:
                if(data_3 == null){
                    return;
                }

                tv_1_1.setText("当前状态");

                tv_2_1.setText("渠道");
                tv_2_2.setText(data_3.getWithdrawWayName());
                tv_3_1.setText("时间");
                tv_3_2.setText(data_3.getCreateTime());
                tv_4_1.setText("相关单号");
                tv_4_2.setText(data_3.getOrderNum());
                tv_5_1.setText("申请金额");
                int status_3 = data_3.getStatus();//0提现中 1成功 2失败
                String money_3 = "";
                switch (status_3){
                    case 0:
                        tv_1_2.setText("提现中");
                        money_3 = "<font color = \"#8C8C8C\">" + data_3.getApplyMoney() + "</font>";
                        break;
                    case 1:
                        tv_1_2.setText("提现成功");
                        money_3 = "<font color = \"#148cf1\">" +"-"+ data_3.getApplyMoney() + "</font>";
                        break;
                    case 2:
                        tv_1_2.setText("提现失败");
                        money_3 = "<font color = \"#fe8f62\">"+ data_3.getApplyMoney() + "</font>";
                        break;
                }

                tv_5_2.setText(Html.fromHtml(money_3));
                tv_6_1.setText("手续费");
                tv_6_2.setText(data_3.getChargeMoeny());
                tv_7_1.setText("到账金额");
                tv_7_2.setText(data_3.getSuccMoney());
                tv_8_1.setText("当前余额");
                tv_8_2.setText(data_3.getCurrentMoney());
                break;
        }

    }

    public static boolean isShowing(){
        if(mPopupWindow == null){
            return false;
        }
        if(mPopupWindow.isShowing()){
            return true;
        }
        return false;
    }

    public static void hide(Activity activity){
        if(mPopupWindow == null){
            return;
        }
        if(mPopupWindow.isShowing()){
            backgroundAlpha(activity,1f);
            mPopupWindow.dismiss();
        }
    }

    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
