package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.utils.LogUtils;

import java.util.List;

public class MyArrayAdapter extends BaseAdapter {

    private List<HallDetailRet.DataBean.YanBaoBean> mItem;

    public MyArrayAdapter(List<HallDetailRet.DataBean.YanBaoBean> item){
        this.mItem = item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mItem == null ? 0 : mItem.size();
    }

    @Nullable
    @Override
    public HallDetailRet.DataBean.YanBaoBean getItem(int position) {
        return mItem.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = null;
        LayoutInflater mInflater = LayoutInflater.from(App.getApp());
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_spinner, null);
            mViewHolder = new ViewHolder();

            mViewHolder.tv_spinner = (TextView) convertView
                    .findViewById(R.id.tv_spinner);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if(mItem != null && mItem.size() > position){
            mViewHolder.tv_spinner.setText(mItem.get(position).getProName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_spinner;
    }
}
