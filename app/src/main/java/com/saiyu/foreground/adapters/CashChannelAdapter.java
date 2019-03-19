package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class CashChannelAdapter extends RecyclerView.Adapter<CashChannelAdapter.MyHolder> {


    List<CashChannelRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public CashChannelAdapter(List<CashChannelRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public CashChannelAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cash_channel, viewGroup, false);
        return new CashChannelAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CashChannelAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }
        Glide.with(App.getApp())
                .load(mItems.get(i).getLogoPic())
                .error(R.mipmap.ic_launcher)
                .into(myHolder.iv_icon);

        myHolder.tv_name.setText(mItems.get(i).getName());
        myHolder.tv_charge.setText("手续费:"+mItems.get(i).getWithdrawWayConfigCharge()+"%");
        myHolder.rl_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = myHolder.getLayoutPosition();
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

    public void refreshData(List<CashChannelRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_name,tv_charge;
        public RelativeLayout rl_item_layout;
        public ImageView iv_icon;

        public MyHolder(View itemView) {
            super(itemView);
            rl_item_layout = itemView.findViewById(R.id.rl_item_layout);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_charge = itemView.findViewById(R.id.tv_charge);
            iv_icon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
