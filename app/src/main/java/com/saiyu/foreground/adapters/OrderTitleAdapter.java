package com.saiyu.foreground.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.ui.fragments.businessFragments.hallFragments.OrderInfoChildFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.hallFragments.OrderSubmitChildFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderTitleAdapter extends FragmentStatePagerAdapter {
    private FragmentManager fm;
    private List<String> tablist;
    private HallDetailReceiveRet hallDetailRet;
    private String orderId;
    private String orderNum;
    private int type;

    public OrderTitleAdapter(FragmentManager fm,HallDetailReceiveRet ret,String orderId,String orderNum,int type){
        super(fm);
        this.fm = fm;
        this.hallDetailRet = ret;
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.type = type;

        tablist = new ArrayList<>();
        tablist.add("订单信息");
        tablist.add("提交订单");
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (i){
            case 0:
                bundle.putString("ReserveAccount",hallDetailRet.getData().getReserveAccount());
                bundle.putString("ProductName",hallDetailRet.getData().getProductName());
                bundle.putString("OfficialRatio",hallDetailRet.getData().getOfficialRatio());
                bundle.putString("ProductPropertyStr",hallDetailRet.getData().getProductPropertyStr());
                bundle.putBoolean("IsFriendLimit",hallDetailRet.getData().isFriendLimit());
                bundle.putString("Remarks",hallDetailRet.getData().getRemarks());
                bundle.putString("RechargeNum",hallDetailRet.getData().getRechargeNum());
                bundle.putString("RechargeUrl",hallDetailRet.getData().getRechargeUrl());
                bundle.putString("ReserveDiscount",hallDetailRet.getData().getReserveDiscount());
                bundle.putString("ServiceMoney",hallDetailRet.getData().getServiceMoney());
                bundle.putString("LiquidatedMoney",hallDetailRet.getData().getLiquidatedMoney());
                bundle.putString("TotalMoney",hallDetailRet.getData().getTotalMoney());
                bundle.putInt("OnceMinCount",hallDetailRet.getData().getOnceMinCount());
                bundle.putString("ReceiveQBCount",hallDetailRet.getData().getReceiveQBCount());
                bundle.putFloat("LessChargeDiscount",hallDetailRet.getData().getLessChargeDiscount());
                bundle.putInt("type",type);
                bundle.putString("ServiceRate",hallDetailRet.getData().getServiceRate());

                fragment = OrderInfoChildFragment.newInstance(bundle);
                break;
            case 1:
                bundle.putBoolean("IsCustomerConfirmation",hallDetailRet.getData().isCustomerConfirmation());
                bundle.putString("orderNum",orderNum);
                bundle.putString("orderId",orderId);
                bundle.putString("ReceiveId",hallDetailRet.getData().getReceiveId());
                bundle.putString("AverageConfirmTime",hallDetailRet.getData().getAverageConfirmTime());
                bundle.putString("ReceiveQBCount",hallDetailRet.getData().getReceiveQBCount());
                fragment = OrderSubmitChildFragment.newInstance(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tablist.get(position);
    }

    @Override
    public int getCount() {
        if (tablist != null && tablist.size() > 0) {
            return tablist.size();
        } else {
            return 0;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


    /**
     * 在调用notifyDataSetChanged()方法后，随之会触发该方法，根据该方法返回的值来确定是否更新
     * object对象为Fragment，具体是当前显示的Fragment和它的前一个以及后一个
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;   // 返回发生改变，让系统重新加载
        // 系统默认返回的是     POSITION_UNCHANGED，未改变
        // return super.getItemPosition(object);
    }
}
