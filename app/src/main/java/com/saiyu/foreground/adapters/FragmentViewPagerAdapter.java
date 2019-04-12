package com.saiyu.foreground.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.saiyu.foreground.https.response.WaitingRechargeOrderRet;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments.WaitingRechargeOrderChildFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jiushubu on 2017/6/30.
 */

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<WaitingRechargeOrderRet.DatasBean.ItemsBean> mItem = new ArrayList<>();

    public FragmentViewPagerAdapter(FragmentManager fm, List<WaitingRechargeOrderRet.DatasBean.ItemsBean> mItem){
        super(fm);
        this.mItem = mItem;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId",mItem.get(i).getOrderID());
        bundle.putString("ReserveQBCount",mItem.get(i).getReserveQBCount());
        bundle.putString("ReceiveOrderNum",mItem.get(i).getReceiveOrderNum());
        bundle.putString("RechargeProduct",mItem.get(i).getRechargeProduct());
        bundle.putString("ReserveAccount",mItem.get(i).getReserveAccount());
        bundle.putString("ConfirmationTime",mItem.get(i).getConfirmationTime());
        bundle.putString("CreateTime",mItem.get(i).getCreateTime());

        Fragment fragment = WaitingRechargeOrderChildFragment.newInstance(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (mItem != null && mItem.size() > 0) {
            return mItem.size();
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
