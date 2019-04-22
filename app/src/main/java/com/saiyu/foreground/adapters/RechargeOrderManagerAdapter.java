package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.PreOrderHistoryRet;
import com.saiyu.foreground.https.response.RechargeOrderManageRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class RechargeOrderManagerAdapter extends RecyclerView.Adapter<RechargeOrderManagerAdapter.MyHolder> {
    List<RechargeOrderManageRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public RechargeOrderManagerAdapter(List<RechargeOrderManageRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public RechargeOrderManagerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_record, viewGroup, false);
        return new RechargeOrderManagerAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeOrderManagerAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_deal_1.setText(mItems.get(i).getProductName());

        myHolder.tv_deal_2.setText(mItems.get(i).getReceiveOrderStatusStr());
        myHolder.tv_deal_3.setText(mItems.get(i).getCreateTime());
        myHolder.tv_deal_4.setText(mItems.get(i).getReserveAccount());

        myHolder.ll_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v,i);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void refreshData(List<RechargeOrderManageRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_deal_1,tv_deal_2,tv_deal_3,tv_deal_4;
        public LinearLayout ll_item_layout;

        public MyHolder(View itemView) {
            super(itemView);
            ll_item_layout = itemView.findViewById(R.id.ll_item_layout);
            tv_deal_1 = itemView.findViewById(R.id.tv_deal_1);
            tv_deal_1.setTextColor(App.getApp().getResources().getColor(R.color.black));
            tv_deal_1.setTextSize(16);
            tv_deal_2 = itemView.findViewById(R.id.tv_deal_2);
            tv_deal_2.setTextSize(16);
            tv_deal_3 = itemView.findViewById(R.id.tv_deal_3);
            tv_deal_4 = itemView.findViewById(R.id.tv_deal_4);
        }

    }
}
