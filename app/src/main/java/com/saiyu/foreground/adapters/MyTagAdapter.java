package com.saiyu.foreground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.HallRet;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.HashSet;
import java.util.List;

public class MyTagAdapter extends TagAdapter<HallRet.DatasBean.ProductItemsBean> {

    private List<HallRet.DatasBean.ProductItemsBean> mItems;

    public MyTagAdapter(List<HallRet.DatasBean.ProductItemsBean> datas) {
        super(datas);
        this.mItems = datas;
    }

    @Override
    public View getView(FlowLayout parent, int position, HallRet.DatasBean.ProductItemsBean productItemsBean) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hall_tv, parent, false);
        tv.setText(productItemsBean.getName());
        return tv;
    }

    @Override
    public HallRet.DatasBean.ProductItemsBean getItem(int position) {
        return mItems.get(position);
    }

    public void clear(){
        HashSet<Integer> set = new HashSet<Integer>();
        setSelectedList(set);
        notifyDataChanged();
    }
}
