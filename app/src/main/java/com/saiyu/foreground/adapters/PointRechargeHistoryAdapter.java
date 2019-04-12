package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.RechargePointHistoryRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class PointRechargeHistoryAdapter extends RecyclerView.Adapter<PointRechargeHistoryAdapter.MyHolder> {
    List<RechargePointHistoryRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public PointRechargeHistoryAdapter(List<RechargePointHistoryRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public PointRechargeHistoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_record, viewGroup, false);
        return new PointRechargeHistoryAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointRechargeHistoryAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_deal_1.setText("+"+mItems.get(i).getPoint());

        myHolder.tv_deal_5.setText(mItems.get(i).getCurrentPoint());
        myHolder.tv_deal_3.setText(mItems.get(i).getCreateTime());

        myHolder.rl_item_layout.setOnClickListener(new View.OnClickListener() {
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

    public void refreshData(List<RechargePointHistoryRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_deal_1,tv_deal_2,tv_deal_3,tv_deal_4,tv_deal_5;
        public RelativeLayout rl_item_layout;
        public ImageView iv_jiantou;

        public MyHolder(View itemView) {
            super(itemView);
            rl_item_layout = itemView.findViewById(R.id.rl_item_layout);
            tv_deal_1 = itemView.findViewById(R.id.tv_deal_1);
            tv_deal_2 = itemView.findViewById(R.id.tv_deal_2);
            tv_deal_3 = itemView.findViewById(R.id.tv_deal_3);
            tv_deal_4 = itemView.findViewById(R.id.tv_deal_4);
            tv_deal_5 = itemView.findViewById(R.id.tv_deal_5);
            iv_jiantou = itemView.findViewById(R.id.iv_jiantou);
            iv_jiantou.setVisibility(View.INVISIBLE);
        }

    }
}
