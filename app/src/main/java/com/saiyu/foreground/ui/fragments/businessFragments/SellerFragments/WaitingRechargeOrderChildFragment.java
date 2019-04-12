package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.TimeParseUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

@EFragment(R.layout.fragment_order_waitingrecharge)
public class WaitingRechargeOrderChildFragment extends BaseFragment {

    @ViewById
    TextView tv_recharge_account,tv_creattime,tv_recharge_product,tv_recharge_num,tv_order_num,tv_cancle_time,tv_surplus_time;

    private String orderId,ReserveQBCount;

    public static WaitingRechargeOrderChildFragment newInstance(Bundle bundle) {
        WaitingRechargeOrderChildFragment_ fragment = new WaitingRechargeOrderChildFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
            ReserveQBCount = bundle.getString("ReserveQBCount");

            tv_recharge_account.setText(bundle.getString("ReceiveOrderNum"));
            tv_recharge_product.setText(bundle.getString("RechargeProduct"));
            tv_recharge_num.setText(bundle.getString("ReserveAccount"));
            tv_order_num.setText(ReserveQBCount);
            tv_cancle_time.setText(bundle.getString("ConfirmationTime"));
//            tv_surplus_time.setText(mItem.get(position).getSurplusTime());

            String createTime = bundle.getString("CreateTime");
            tv_creattime.setText(createTime);
            setTime(createTime);

        }
    }


    @Click({R.id.btn_order_detail,R.id.btn_order_recharge})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            Bundle bundle = new Bundle();
            switch (view.getId()) {
                case R.id.btn_order_detail:
                    bundle.putBoolean("isDetail",true);
                    bundle.putString("orderId",orderId);
                    break;
                case R.id.btn_order_recharge:
                    bundle.putBoolean("isDetail",false);
                    bundle.putString("orderId",orderId);
                    bundle.putString("ReserveQBCount",ReserveQBCount);
                    break;
            }
            OrderRechargeFragment orderRechargeFragment = OrderRechargeFragment.newInstance(bundle);
            ((WaitingRechargeOrderFragment)getParentFragment()).start(orderRechargeFragment);
        }
    }

    private Timer timer;
    private long surplusTime_20;

    //计时器
    private void setTime(String createTime) {
        long time = TimeParseUtils.TeamTimeStringToMills(createTime);
        long betweenTime_20 = 20 * 60 * 1000;//20分钟
        surplusTime_20 = (betweenTime_20 - (System.currentTimeMillis() - time))/1000;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(surplusTime_20 > 0){
                    surplusTime_20--;
                    long hh = surplusTime_20 / 60 / 60 % 60;
                    int mm = (int)(surplusTime_20 / 60 % 60);
                    int ss = (int)(surplusTime_20 % 60);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = mm;
                    msg.arg2 = ss;
                    handler.sendMessage(msg);
//                    LogUtils.print("还剩" + hh + "小时" + mm + "分钟" + ss + "秒");
                }
            }
        }, 0, 1000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(tv_surplus_time != null){
                        tv_surplus_time.setText(msg.arg1 + "分" + msg.arg2 + "秒");
                    }

                    break;
            }
        }
    };

}
