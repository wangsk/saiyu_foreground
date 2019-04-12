package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.LoginRecordRet;
import com.saiyu.foreground.utils.LogUtils;

import java.util.List;

public class LoginRecordAdapter extends RecyclerView.Adapter<LoginRecordAdapter.MyHolder> {
    List<LoginRecordRet.DatasBean.ItemsBean> mItems;

    public LoginRecordAdapter(List<LoginRecordRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public LoginRecordAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_record, viewGroup, false);
        return new LoginRecordAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginRecordAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_deal_1.setText(mItems.get(i).getOperationType());
        myHolder.tv_deal_2.setText(mItems.get(i).getCreateTime());
        myHolder.tv_deal_3.setText(mItems.get(i).getEquipmentType());
        myHolder.tv_deal_4.setText(mItems.get(i).getiPDistrict());
        LogUtils.print(mItems.get(i).getOperationType() + "  " + mItems.get(i).getCreateTime()+"  "+mItems.get(i).getOperationIP()+"  "+mItems.get(i).getiPDistrict());


    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void refreshData(List<LoginRecordRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_deal_1,tv_deal_2,tv_deal_3,tv_deal_4;
        public ImageView iv_jiantou;

        public MyHolder(View itemView) {
            super(itemView);
            tv_deal_1 = itemView.findViewById(R.id.tv_deal_1);
            tv_deal_2 = itemView.findViewById(R.id.tv_deal_2);
            tv_deal_3 = itemView.findViewById(R.id.tv_deal_3);
            tv_deal_4 = itemView.findViewById(R.id.tv_deal_4);
            iv_jiantou = itemView.findViewById(R.id.iv_jiantou);
            iv_jiantou.setVisibility(View.GONE);
        }

    }
}
