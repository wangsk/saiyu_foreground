package com.saiyu.foreground.ui.fragments.businessFragments.hallFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.https.response.IsCountDoRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.ProtocolFragment;
import com.saiyu.foreground.ui.views.MyViewPager;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.saiyu.foreground.utils.TimeParseUtils;
import com.saiyu.foreground.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EFragment(R.layout.fragment_hallorder_detail)
public class HallOrderDetailFragment extends BaseFragment implements CallbackUtils.ResponseCallback,OnItemClickListener,OnRefreshListener {

    @ViewById
    TextView tv_title_content, tv_orderTitle, tv_reserveTitle, tv_2_1, tv_2_2, tv_2_3, tv_2_4, tv_zc, tv_2_5, tv_2_6, tv_orderName, tv_orderProgress, tv_orderCount,
            tv_orderTime, tv_count_prompt, tv_orderPoint, tv_prompt, tv_protocol, tv_discount,tv_rechargePoint,tv_freePoint,tv_bottom_prompt;
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
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    Spinner sp_selector;
    @ViewById
    SmartRefreshLayout refreshLayout;
    private OrderCountSelectorAdapter orderCountSelectorAdapter;
    private OrderTitleAdapter orderTitleAdapter;
    private List<String> mItems = new ArrayList<>();
    private String orderId,receiveId,orderNum;

    private boolean isLogin, IsCustomerConfirmation, IsImgConfirmation, IsLessThanOriginalPrice, IsOrderPwd, IsFriendLimit;

    private final Timer timer = new Timer();
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

    public static HallOrderDetailFragment newInstance(Bundle bundle) {
        HallOrderDetailFragment_ fragment = new HallOrderDetailFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

        String accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
        boolean autologinflag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
        if(autologinflag && !TextUtils.isEmpty(accessToken)){

            if (!TextUtils.isEmpty(orderId)) {
                //登录状态下，先请求该订单的状态（正常状态 / 已经被自己接单，但是未充值状态）
                ApiRequest.hallDetail(orderId, "HallOrderDetailFragment_hallDetail", pb_loading);
            }

            isLogin = true;
            btn_confirm.setText("确认接单");
            Utils.setButtonClickable(btn_confirm,false);//确认接单按钮置为不可点击
        } else {

            if (!TextUtils.isEmpty(orderId)) {
                //未登录状态
                ApiRequest.hallDetailNoLogin(orderId, "HallOrderDetailFragment_hallDetailNoLogin", pb_loading);
            }

            isLogin = false;
            btn_confirm.setText("登录");
            Utils.setButtonClickable(btn_confirm,true);

            sendHandlerMsg(1);

        }

    }

    @AfterViews
    void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getString("orderId");
        }
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnRefreshListener(this);
        tv_title_content.setText("订单详情");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,8);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("HallOrderDetailFragment_usableQBCountList")){
            //输入的Q币数量是否可用
            IsCountDoRet ret = (IsCountDoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                tv_bottom_prompt.setVisibility(View.VISIBLE);
                tv_count_prompt.setText("必须使用一个QQ号码一次性充值完成，否则视为违约！将扣除5%违约金!");
                Utils.setButtonClickable(btn_confirm,true);
                recyclerView.setVisibility(View.GONE);
            } else {
                if(mItems == null){
                    mItems = new ArrayList<>();
                }
                mItems.clear();
                mItems.addAll(ret.getData().getNumList());
                orderCountSelectorAdapter = new OrderCountSelectorAdapter(mItems);
                orderCountSelectorAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(orderCountSelectorAdapter);
                tv_count_prompt.setText("数量 "+ rechargeNum+" 当日已被使用，你可以选择:");
                recyclerView.setVisibility(View.VISIBLE);
                tv_bottom_prompt.setVisibility(View.GONE);
                Utils.hideInput();
            }
        } else if(method.equals("HallOrderDetailFragment_hallDetailReceive")){
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
            //加载两个子fragment (OrderInfoChildFragment/OrderSubmitChildFragment)
            orderTitleAdapter = new OrderTitleAdapter(getChildFragmentManager(),ret);

            view_pager.setAdapter(orderTitleAdapter);
            layout_tab.setupWithViewPager(view_pager);

            receiveId = ret.getData().getReceiveId();

            String createTime = ret.getData().getCreateTime();
            setTime(createTime);

            sendHandlerMsg(2);

        } else if (method.equals("HallOrderDetailFragment_hallDetail") || method.equals("HallOrderDetailFragment_hallDetailNoLogin")) {
            HallDetailRet ret = (HallDetailRet) baseRet;
            if (ret.getData() == null) {
                return;
            }

            if (ret.getData().isReceive()) {
                //该订单已经被自己接过,但是未充值
                ApiRequest.hallDetailReceive(ret.getData().getId(),ret.getData().getReceiveQBCount(),"","HallOrderDetailFragment_hallDetailReceive", pb_loading);

            } else {
                //正常订单
                sendHandlerMsg(1);

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

                setLimit();

                isCountAble();

            }

           initYuanBaoList(ret);//初始化元宝输入框列表
        }
    }

    @Click({R.id.btn_title_back, R.id.tv_protocol, R.id.btn_confirm,R.id.btn_cancel,R.id.btn_prompt,R.id.tv_rechargePoint,R.id.tv_freePoint})
    void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, ContainerActivity_.class);
        switch (view.getId()) {
            case R.id.btn_prompt:
                bundle.putString("url", "https://www.saiyu.com/help/");
                bundle.putString("type","帮助文档");//
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.WebFragmentTag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;
            case R.id.btn_title_back:
                onBackPressedSupport();
                break;
            case R.id.tv_rechargePoint:
                bundle.putFloat("myPoint",maxOrderPoint);
                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.PointManagerFragmentTag);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
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
                if(!isLogin){
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    return;
                }
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
                            ApiRequest.hallDetailReceive(orderId,rechargeNum,callbackList.get(0),"HallOrderDetailFragment_hallDetailReceive", pb_loading);
                        }
                    });
                    return;
                }

                ApiRequest.hallDetailReceive(orderId,rechargeNum,"","HallOrderDetailFragment_hallDetailReceive", pb_loading);
                break;
            case R.id.btn_cancel:
                bundle.putString("receiveId",receiveId);
                OrderCancelFragment orderCancelFragment = OrderCancelFragment.newInstance(bundle);
                start(orderCancelFragment);
                break;
        }
    }

    @TextChange({R.id.et_count})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {

            rechargeNum = s.toString();

            isCountAble();

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
        long time = TimeParseUtils.TeamTimeStringToMills(createTime);
        long betweenTime_20 = 20 * 60 * 1000;//20分钟
        long betweenTime_40 = 40 * 60 * 1000;//40分钟
        surplusTime_20 = (betweenTime_20 - (System.currentTimeMillis() - time))/1000;
        surplusTime_40 = (betweenTime_40 - (System.currentTimeMillis() - time))/1000;
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
//                    LogUtils.print("还剩" + hh + "小时" + mm + "分钟" + ss + "秒");
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
                        int money = (20 - msg.arg1)*10;
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
    public boolean onBackPressedSupport() {
        try {
            if(timer != null){
                timer.cancel();
            }
        }catch (Exception e){

        }
        getActivity().finish();
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

        recyclerView.setVisibility(View.GONE);
    }

    //初始化输入框
    private void initYuanBaoList(HallDetailRet ret){
        type = ret.getData().getType();
        if(type == 0){
            //普通游戏
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

                    isCountAble();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            et_count.setVisibility(View.GONE);
            sp_selector.setVisibility(View.VISIBLE);

        }
    }

    //判断输入Q币数量是否可用
    private void isCountAble(){
        if(!isLogin){
            tv_count_prompt.setText("请登录");
            return;
        }

        LogUtils.print("rechargeNum === " + rechargeNum);

        if(TextUtils.isEmpty(rechargeNum) || "0".equals(rechargeNum)){
            tv_orderPoint.setText("0点");
            tv_bottom_prompt.setVisibility(View.GONE);
            return;
        }

        tv_orderPoint.setText(rechargeNum+"点");
        int qbCount = Integer.parseInt(rechargeNum);
        if(qbCount < OnceMinCount){
            //不可输入买家发布订单时所限制的单次充值最低数量
            tv_count_prompt.setText("该订单最低充值"+OnceMinCount+ "Q币，请修改出售数量！");
            Utils.setButtonClickable(btn_confirm,false);
            tv_bottom_prompt.setVisibility(View.GONE);
            return;
        } else {
            tv_count_prompt.setText("输入您要出售的Q币数量，最大不超过"+ maxQBcount + "Q币");
        }
        if(qbCount > maxQBcount){
            //不得输入超过该订单需求的最大剩余数量
            tv_count_prompt.setText("超过订单最大需求量，请输入"+OnceMinCount+"-"+maxQBcount+ "之间任意数值");
            Utils.setButtonClickable(btn_confirm,false);
            tv_bottom_prompt.setVisibility(View.GONE);
            return;
        } else {
            tv_count_prompt.setText("输入您要出售的Q币数量，最大不超过"+ maxQBcount + "Q币");
        }

        if(qbCount > maxOrderPoint){
            //不能大于接单点数
            tv_prompt.setText("接单点数不足，请更改数量或充值点数");
            ll_point.setVisibility(View.VISIBLE);
            Utils.setButtonClickable(btn_confirm,false);
            tv_bottom_prompt.setVisibility(View.GONE);
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

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        String accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
        boolean autologinflag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
        if(autologinflag && !TextUtils.isEmpty(accessToken)){

            if (!TextUtils.isEmpty(orderId)) {
                //登录状态下，先请求该订单的状态（正常状态 / 已经被自己接单，但是未充值状态）
                ApiRequest.hallDetail(orderId, "HallOrderDetailFragment_hallDetail", pb_loading);
            }

            isLogin = true;
            btn_confirm.setText("确认接单");
            Utils.setButtonClickable(btn_confirm,false);//确认接单按钮置为不可点击
        } else {

            if (!TextUtils.isEmpty(orderId)) {
                //未登录状态
                ApiRequest.hallDetailNoLogin(orderId, "HallOrderDetailFragment_hallDetailNoLogin", pb_loading);
            }

            isLogin = false;
            btn_confirm.setText("登录");
            Utils.setButtonClickable(btn_confirm,true);

            sendHandlerMsg(1);

        }

        if (refreshLayout != null) {
            refreshlayout.finishRefresh();
        }
    }
}
