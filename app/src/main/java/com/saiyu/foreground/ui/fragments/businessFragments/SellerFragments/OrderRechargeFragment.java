package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MyArrayAdapter;
import com.saiyu.foreground.adapters.OrderCountSelectorAdapter;
import com.saiyu.foreground.adapters.OrderTitleAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.ProtocolFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.hallFragments.OrderCancelFragment;
import com.saiyu.foreground.ui.views.MyViewPager;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.TimeParseUtils;
import com.saiyu.foreground.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EFragment(R.layout.fragment_hallorder_detail)
public class OrderRechargeFragment extends BaseFragment implements CallbackUtils.ResponseCallback,OnItemClickListener {
    @ViewById
    TextView tv_title_content, tv_orderTitle, tv_reserveTitle, tv_2_1, tv_2_2, tv_2_3, tv_2_4, tv_zc, tv_2_5, tv_2_6, tv_orderName, tv_orderProgress, tv_orderCount,
            tv_orderTime, tv_count_prompt, tv_orderPoint, tv_prompt, tv_protocol, tv_discount,tv_rechargePoint,tv_freePoint;
    @ViewById
    TextView tv_time,tv_prompt_1,tv_prompt_2,tv_prompt_3,tv_prompt_4;
    @ViewById
    Button btn_title_back, btn_confirm, btn_cancel,btn_prompt;
    @ViewById
    ProgressBar pb_loading, pb_order;
    @ViewById
    LinearLayout hall_order_2,ll_point;
    @ViewById
    RelativeLayout hall_order_1;
    @ViewById
    RatingBar rb_mark;
    @ViewById
    EditText et_count;
    @ViewById
    TabLayout layout_tab;
    @ViewById
    MyViewPager view_pager;
    @ViewById
    Spinner sp_selector;
    @ViewById
    SmartRefreshLayout refreshLayout;
    private OrderCountSelectorAdapter orderCountSelectorAdapter;
    private OrderTitleAdapter orderTitleAdapter;
    private List<String> mItems = new ArrayList<>();
    private String orderId,receiveId,orderNum;

    private boolean IsCustomerConfirmation, IsImgConfirmation, IsLessThanOriginalPrice, IsOrderPwd, IsFriendLimit;

    private Timer timer = null;
    private long surplusTime_20,surplusTime_40;

    private int isLock;
    private float maxOrderPoint;//卖家剩余的最大接单点数
    private float maxQBcount;//卖家能接的最大QB数量
    private int OnceMinCount = 0;//最小充值QB数量
    private float ReserveQBCount;//该订单最大QB数量
    private float RechargeQBCount;//该订单被接过的QB数量

    private String OrderRechargePointsUrl,OrderFreePointsUrl;
    public static List<HallDetailRet.DataBean.YanBaoBean> selectorList = null;

    //该方法是用于OrderSubmitChildFragment类中 修改实际Q币数量时spinner控件加载调用
    public static List<HallDetailRet.DataBean.YanBaoBean> getSelectorList() {
        return selectorList;
    }

    private MyArrayAdapter yuanbaoAdapter;//元宝spinner下拉数据绑定adapter
    private int type;//区别普通商品 : 0 ，金元宝 : 1 ，银元宝 : 2

    private String rechargeNum;//出售Q币数量

    private boolean isDetail;

    private int tabPosition = 0;

    public static OrderRechargeFragment newInstance(Bundle bundle) {
        OrderRechargeFragment_ fragment = new OrderRechargeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getString("orderId");
            isDetail = bundle.getBoolean("isDetail",true);
            if(isDetail){
                hall_order_1.setVisibility(View.VISIBLE);
                hall_order_2.setVisibility(View.GONE);
                ApiRequest.hallDetail(orderId, "OrderRechargeFragment_hallDetail", pb_loading);
                btn_confirm.setText("已接单");
                Utils.setButtonClickable(btn_confirm,false);//确认接单按钮置为不可点击
                et_count.setFocusable(false);
                et_count.setClickable(false);
                sp_selector.setEnabled(false);

            } else {
                hall_order_1.setVisibility(View.GONE);
                hall_order_2.setVisibility(View.VISIBLE);
                ApiRequest.hallDetailReceive(orderId,bundle.getString("ReserveQBCount"),"","OrderRechargeFragment_hallDetailReceive", pb_loading);

            }
        }

    }

    @AfterViews
    void afterViews() {

        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        tv_title_content.setText("订单详情");

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("OrderRechargeFragment_hallDetailReceive")){
            //该订单已经被自己接过，但是未充值
            HallDetailReceiveRet ret = (HallDetailReceiveRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(!ret.getData().isResult()){
                LogUtils.print("确认接单失败！");
                Toast.makeText(mContext,"确认接单失败！",Toast.LENGTH_SHORT).show();
                return;
            }

            type = ret.getData().getType();
            if(type != 0){
                if(selectorList == null){
                    selectorList = new ArrayList<>();
                }
                selectorList.clear();
                HallDetailRet.DataBean.YanBaoBean yanBaoBean = new HallDetailRet.DataBean.YanBaoBean();
                yanBaoBean.setProName("请选择数量");
                yanBaoBean.setValue("0");
                selectorList.add(yanBaoBean);

                if(type == 1){
                    //金元宝
                    selectorList.addAll(ret.getData().getGoldList());
                } else if(type == 2){
                    //银元宝
                    selectorList.addAll(ret.getData().getSilverList());
                }
            }

            //加载两个子fragment (OrderInfoChildFragment/OrderSubmitChildFragment)
            orderTitleAdapter = new OrderTitleAdapter(getChildFragmentManager(),ret);

            view_pager.setAdapter(orderTitleAdapter);
            layout_tab.setupWithViewPager(view_pager);
            layout_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tabPosition = tab.getPosition();
                    LogUtils.print("tabPosition ==== " + tabPosition);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            receiveId = ret.getData().getReceiveId();

            String createTime = ret.getData().getCreateTime();
            setTime(createTime);

        } else if (method.equals("OrderRechargeFragment_hallDetail")) {
            HallDetailRet ret = (HallDetailRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            //订单详情
            OrderFreePointsUrl = ret.getData().getOrderFreePointsUrl();//获取免费点数的链接
            OrderRechargePointsUrl = ret.getData().getOrderRechargePointsUrl();//充值点数链接

            maxOrderPoint = ret.getData().getReceivePoint();//最大接单点数，超过最大接单点数则不允许接单

            isLock = ret.getData().getIsLock();//该字段表示订单是否被别人抢单，（截止3月20号版本该字段服务端保持false，也就是说服务端暂时允许多人接同一单,未来可能会启用作为一条订单只能同时被一个人接单）

            tv_orderTitle.setText(ret.getData().getOrderTitle());//头标题
            tv_reserveTitle.setText(ret.getData().getReserveTitle());//副标题

            orderNum = ret.getData().getOrderNum();
            tv_orderName.setText(orderNum);

            if(!TextUtils.isEmpty(ret.getData().getReserveQBCount()) && !TextUtils.isEmpty(ret.getData().getRechargeQBCount())){
                ReserveQBCount = Float.parseFloat(ret.getData().getReserveQBCount());//买家发布的总QB数量
                RechargeQBCount = Float.parseFloat(ret.getData().getRechargeQBCount());//已经被接单的QB数量
            }

            pb_order.setProgress((int)(RechargeQBCount/ReserveQBCount*100));
            tv_orderProgress.setText(ret.getData().getRechargeQBCount() + "/" + ret.getData().getReserveQBCount() + "Q币");

            maxQBcount = ReserveQBCount - RechargeQBCount;
            tv_count_prompt.setText("输入您要出售的Q币数量，最大不超过"+ maxQBcount + "Q币");

            rb_mark.setRating(ret.getData().getVipLevel()/2);
            tv_orderCount.setText("成交" + ret.getData().getOrderRSettleTotalCount() + "笔" + ret.getData().getOrderRSettleTotalMoney() + "元");
            tv_orderTime.setText(ret.getData().getAverageConfirmTime());
            tv_discount.setText(ret.getData().getReserveDiscount() + "折");

            OnceMinCount = ret.getData().getOnceMinCount();//最小充值Q币数，小于该数值不允许接单
            IsImgConfirmation = ret.getData().isImgConfirmation();
            IsCustomerConfirmation = ret.getData().isCustomerConfirmation();
            IsLessThanOriginalPrice = ret.getData().isLessThanOriginalPrice();
            IsOrderPwd = ret.getData().isOrderPwd();
            IsFriendLimit = ret.getData().isFriendLimit();


            tv_orderPoint.setText(ret.getData().getReceiveQBCount()+"点");

            setLimit();

            isCountAble();

            initYuanBaoList(ret);//初始化元宝输入框列表
        }
    }

    @Click({R.id.btn_title_back, R.id.tv_protocol, R.id.btn_confirm,R.id.btn_cancel,R.id.btn_prompt,R.id.tv_rechargePoint,R.id.tv_freePoint})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, ContainerActivity_.class);
        switch (view.getId()) {
            case R.id.btn_prompt:
                bundle.putString("url", "https://www.saiyu.com/news-142.html");
                bundle.putString("type","帮助文档");//
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;
            case R.id.btn_title_back:
                onBackPressedSupport();
                break;
            case R.id.tv_rechargePoint:
                if(!TextUtils.isEmpty(OrderRechargePointsUrl)){
                    bundle.putFloat("myPoint",maxOrderPoint);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.PointManagerFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.tv_freePoint:
                if(!TextUtils.isEmpty(OrderFreePointsUrl)){
                    bundle.putString("url", OrderFreePointsUrl);
                    bundle.putString("type","获取免费点数");//
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.tv_protocol:
                ProtocolFragment protocolFragment = ProtocolFragment.newInstance(null);
                start(protocolFragment);
                break;
            case R.id.btn_confirm:
                if(isLock == 1){
                    Toast.makeText(mContext,"该订单目前有他人在充值，请稍等操作",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(IsOrderPwd){
                    DialogUtils.showOrderPswDialog(getActivity(), orderNum, new OnListCallbackListener() {
                        @Override
                        public void setOnListCallbackListener(List<String> callbackList) {
                            if(callbackList == null || callbackList.size() < 1){
                                return;
                            }
                            if(TextUtils.isEmpty(callbackList.get(0))){
                                Toast.makeText(mContext,"请输入接单密码",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ApiRequest.hallDetailReceive(orderId,rechargeNum,callbackList.get(0),"OrderRechargeFragment_hallDetailReceive", pb_loading);
                        }
                    });
                    return;
                }

                ApiRequest.hallDetailReceive(orderId,rechargeNum,"","OrderRechargeFragment_hallDetailReceive", pb_loading);
                break;
            case R.id.btn_cancel:
                bundle.putString("receiveId",receiveId);
                OrderCancelFragment orderCancelFragment = OrderCancelFragment.newInstance(bundle);
                start(orderCancelFragment);
                break;
        }
    }

    //设置附加条件
    private void setLimit() {
        if (OnceMinCount > 0 ) {
            tv_2_1.setText("单次最低" + OnceMinCount + "Q币");
        } else {
            tv_2_1.setText("单次数量不限制");
        }
        tv_2_1.setVisibility(View.VISIBLE);

        int index = 0;
        if(IsImgConfirmation){
            tv_2_2.setVisibility(View.VISIBLE);
            tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
            tv_2_2.setTextColor(App.getApp().getResources().getColor(R.color.yellow));
            tv_2_2.setText("验图代确认");
            index++;
        }
        if(IsCustomerConfirmation){
            switch (index){
                case 0:
                    tv_2_2.setVisibility(View.VISIBLE);
                    tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zilight));
                    tv_2_2.setTextColor(App.getApp().getResources().getColor(R.color.zi_light));
                    tv_2_2.setText("客服代确认");
                    break;
                case 1:
                    tv_2_3.setVisibility(View.VISIBLE);
                    tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zilight));
                    tv_2_3.setTextColor(App.getApp().getResources().getColor(R.color.zi_light));
                    tv_2_3.setText("客服代确认");
                    break;
            }
            index++;
        }
        if(IsLessThanOriginalPrice){
            switch (index){
                case 0:
                    tv_2_2.setVisibility(View.VISIBLE);
                    tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_green));
                    tv_2_2.setTextColor(App.getApp().getResources().getColor(R.color.green));
                    tv_2_2.setText("少充按原价");
                    break;
                case 1:
                    tv_2_3.setVisibility(View.VISIBLE);
                    tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_green));
                    tv_2_3.setTextColor(App.getApp().getResources().getColor(R.color.green));
                    tv_2_3.setText("少充按原价");
                    break;
                case 2:
                    tv_2_4.setVisibility(View.VISIBLE);
                    tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_green));
                    tv_2_4.setTextColor(App.getApp().getResources().getColor(R.color.green));
                    tv_2_4.setText("少充按原价");
                    break;
            }
            index++;
        }
        if(IsOrderPwd){
            switch (index){
                case 0:
                    tv_2_2.setVisibility(View.VISIBLE);
                    tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zi));
                    tv_2_2.setTextColor(App.getApp().getResources().getColor(R.color.zi));
                    tv_2_2.setText("私密订单");
                    break;
                case 1:
                    tv_2_3.setVisibility(View.VISIBLE);
                    tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zi));
                    tv_2_3.setTextColor(App.getApp().getResources().getColor(R.color.zi));
                    tv_2_3.setText("私密订单");
                    break;
                case 2:
                    tv_2_4.setVisibility(View.VISIBLE);
                    tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zi));
                    tv_2_4.setTextColor(App.getApp().getResources().getColor(R.color.zi));
                    tv_2_4.setText("私密订单");
                    break;
                case 3:
                    tv_2_5.setVisibility(View.VISIBLE);
                    tv_2_5.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_zi));
                    tv_2_5.setTextColor(App.getApp().getResources().getColor(R.color.zi));
                    tv_2_5.setText("私密订单");
                    break;
            }
            index++;
        }
        if(IsFriendLimit){
            switch (index){
                case 0:
                    tv_2_2.setVisibility(View.VISIBLE);
                    tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_greenlight));
                    tv_2_2.setTextColor(App.getApp().getResources().getColor(R.color.green_light));
                    tv_2_2.setText("需加好友");
                    break;
                case 1:
                    tv_2_3.setVisibility(View.VISIBLE);
                    tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_greenlight));
                    tv_2_3.setTextColor(App.getApp().getResources().getColor(R.color.green_light));
                    tv_2_3.setText("需加好友");
                    break;
                case 2:
                    tv_2_4.setVisibility(View.VISIBLE);
                    tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_greenlight));
                    tv_2_4.setTextColor(App.getApp().getResources().getColor(R.color.green_light));
                    tv_2_4.setText("需加好友");
                    break;
                case 3:
                    tv_2_5.setVisibility(View.VISIBLE);
                    tv_2_5.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_greenlight));
                    tv_2_5.setTextColor(App.getApp().getResources().getColor(R.color.green_light));
                    tv_2_5.setText("需加好友");
                    break;
                case 4:
                    tv_2_6.setVisibility(View.VISIBLE);
                    tv_2_6.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_greenlight));
                    tv_2_6.setTextColor(App.getApp().getResources().getColor(R.color.green_light));
                    tv_2_6.setText("需加好友");
                    break;
            }
            index++;
        }
    }

    //计时器
    private void setTime(String createTime) {
        LogUtils.print("createTime？ ==== " + createTime);
        long time = TimeParseUtils.TeamTimeStringToMills(createTime);
        long betweenTime_20 = 20 * 60 * 1000;//20分钟
        long betweenTime_40 = 40 * 60 * 1000;//40分钟
        surplusTime_20 = (betweenTime_20 - (System.currentTimeMillis() - time))/1000;
        surplusTime_40 = (betweenTime_40 - (System.currentTimeMillis() - time))/1000;
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            public void run() {
                if(surplusTime_20 > 0){
                    surplusTime_20--;
                    surplusTime_40--;
                    long hh = surplusTime_20 / 60 / 60 % 60;
                    int mm = (int)(surplusTime_20 / 60 % 60);
                    int ss = (int)(surplusTime_20 % 60);
                    Message msg = new Message();
                    msg.what =1;
                    msg.arg1 = mm;
                    msg.arg2 = ss;
                    handler.sendMessage(msg);
                } else {
                    if(surplusTime_40 > 0 ){
                        surplusTime_40--;
                        long hh = surplusTime_40 / 60 / 60 % 60;
                        int mm = (int)(surplusTime_40 / 60 % 60);
                        int ss = (int)(surplusTime_40 % 60);
                        Message msg = new Message();
                        msg.what =2;
                        msg.arg1 = mm;
                        msg.arg2 = ss;
                        handler.sendMessage(msg);
                    }
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
                    if(tv_time != null){
                        tv_time.setText(msg.arg1 + "分" + msg.arg2 + "秒");
                    }

                    break;
                case 2:
                    if(tv_time != null && tv_prompt_1 != null && tv_prompt_2 != null && tv_prompt_3 != null && tv_prompt_4 != null){
                        tv_time.setText(msg.arg1 + "分" + msg.arg2 + "秒");
                        tv_prompt_1.setText("后将强制取消订单");
                        tv_prompt_2.setText("已扣除");
                        int money = (19 - msg.arg1)*10;
                        tv_prompt_3.setText(money+"");
                        tv_prompt_4.setText("接单点数,");
                    }
                    break;
                case 3:
                    if(msg.arg1 == 1){
                        hall_order_1.setVisibility(View.VISIBLE);
                        hall_order_2.setVisibility(View.GONE);
                    } else if(msg.arg1 == 2){
                        hall_order_1.setVisibility(View.GONE);
                        hall_order_2.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if(tabPosition == 0){
            try {
                if(timer != null){
                    timer.cancel();
                    timer = null;
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        try {
            if(timer != null){
                timer.cancel();
                timer = null;
            }
        }catch (Exception e){

        }
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position){
            LogUtils.print("备选数字 ：" + mItems);
            return;
        }

        switch (type){
            case 0://普通游戏
                et_count.setText(mItems.get(position));
                et_count.setSelection(mItems.get(position).length());
                break;
            case 1://金元宝
            case 2://银元宝
                if(TextUtils.isEmpty(mItems.get(position))){
                    return;
                }
                for(int i = 0; i < selectorList.size(); i++){
                    if(mItems.get(position).equals(selectorList.get(i).getValue())){
                        sp_selector.setSelection(i);
                        break;
                    }
                }

                break;
        }

    }

    //初始化输入框
    private void initYuanBaoList(HallDetailRet ret){
        tv_orderPoint.setText(ret.getData().getReceiveQBCount()+"点");
        type = ret.getData().getType();
        if(type == 0){
            //普通游戏
            et_count.setText(ret.getData().getReceiveQBCount());
            et_count.setVisibility(View.VISIBLE);
            sp_selector.setVisibility(View.GONE);
        } else {
            if(selectorList == null){
                selectorList = new ArrayList<>();
            }
            selectorList.clear();
            HallDetailRet.DataBean.YanBaoBean yanBaoBean = new HallDetailRet.DataBean.YanBaoBean();
            yanBaoBean.setProName("请选择数量");
            yanBaoBean.setValue("0");
            selectorList.add(yanBaoBean);

            if(type == 1){
                //金元宝
                selectorList.addAll(ret.getData().getGoldList());
            } else if(type == 2){
                //银元宝
                selectorList.addAll(ret.getData().getSilverList());
            }

            yuanbaoAdapter = new MyArrayAdapter(selectorList);
            sp_selector.setAdapter(yuanbaoAdapter);
            sp_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(selectorList == null || selectorList.size() <= position){
                        return;
                    }
                    LogUtils.print("ProName : " + selectorList.get(position).getProName() + "  Value : " + selectorList.get(position).getValue());
                    rechargeNum = selectorList.get(position).getValue();

//                    isCountAble();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for(int i = 0; i < selectorList.size(); i++){
                if(selectorList.get(i).getValue().equals(ret.getData().getReceiveQBCount())){
                    sp_selector.setSelection(i);
                    tv_orderPoint.setText(selectorList.get(i).getValue()+"点");
                    break;
                }
            }

            et_count.setVisibility(View.GONE);
            sp_selector.setVisibility(View.VISIBLE);

        }
    }

    //判断输入Q币数量是否可用
    private void isCountAble(){

        LogUtils.print("rechargeNum === " + rechargeNum);

        if(TextUtils.isEmpty(rechargeNum) || "0".equals(rechargeNum)){
            tv_orderPoint.setText("0点");
            return;
        }

        tv_orderPoint.setText(rechargeNum+"点");
        int qbCount = Integer.parseInt(rechargeNum);
        if(qbCount < OnceMinCount){
            //不可输入买家发布订单时所限制的单次充值最低数量
            tv_count_prompt.setText("该订单最低充值"+OnceMinCount+ "Q币，请修改出售数量！");
            Utils.setButtonClickable(btn_confirm,false);
            return;
        } else {
            tv_count_prompt.setText("输入您要出售的Q币数量，最大不超过"+ maxQBcount + "Q币");
        }
        if(qbCount > maxQBcount){
            //不得输入超过该订单需求的最大剩余数量
            tv_count_prompt.setText("超过订单最大需求量，请输入"+OnceMinCount+"-"+maxQBcount+ "之间任意数值");
            Utils.setButtonClickable(btn_confirm,false);
            return;
        } else {
            tv_count_prompt.setText("输入您要出售的Q币数量，最大不超过"+ maxQBcount + "Q币");
        }

        if(qbCount > maxOrderPoint){
            //不能大于接单点数
            tv_prompt.setText("接单点数不足，请更改数量或充值点数");
            ll_point.setVisibility(View.VISIBLE);
            Utils.setButtonClickable(btn_confirm,false);
            return;
        } else {
            tv_prompt.setText("接单时按1:1扣除接单点数，交易成功双倍返还，失败不退");
            ll_point.setVisibility(View.GONE);
        }

        ApiRequest.usableQBCountList(orderId,rechargeNum,"HallOrderDetailFragment_usableQBCountList",pb_loading);
    }

    //控制页面UI显示
    private void sendHandlerMsg(int arg1){
        Message message = new Message();
        message.what = 3;
        message.arg1 = arg1;
        handler.sendMessage(message);
    }
}
