package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.BuyerWebListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.utils.LogUtils;

import java.util.List;

public class BuyerRechargeWebUrlAdapter extends RecyclerView.Adapter<BuyerRechargeWebUrlAdapter.MyHolder> {
    List<BuyerWebListRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public BuyerRechargeWebUrlAdapter(List<BuyerWebListRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public BuyerRechargeWebUrlAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_record, viewGroup, false);
        return new BuyerRechargeWebUrlAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerRechargeWebUrlAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_deal_1.setText(mItems.get(i).getTitle());

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

    public void refreshData(List<BuyerWebListRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_deal_1,tv_deal_2,tv_deal_3,tv_deal_4;
        public LinearLayout ll_item_layout;
        public ImageView iv_jiantou;

        public MyHolder(View itemView) {
            super(itemView);
            ll_item_layout = itemView.findViewById(R.id.ll_item_layout);
            tv_deal_1 = itemView.findViewById(R.id.tv_deal_1);
            tv_deal_1.setSingleLine(true);
            tv_deal_1.setEllipsize(TextUtils.TruncateAt.END);
            tv_deal_2 = itemView.findViewById(R.id.tv_deal_2);
            tv_deal_3 = itemView.findViewById(R.id.tv_deal_3);
            tv_deal_4 = itemView.findViewById(R.id.tv_deal_4);
            iv_jiantou = itemView.findViewById(R.id.iv_jiantou);
            iv_jiantou.setVisibility(View.GONE);
            tv_deal_3.setVisibility(View.GONE);
            tv_deal_2.setVisibility(View.GONE);
            tv_deal_4.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams layoutParams=
//                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            layoutParams.setMargins(5,15,5,15);
//            tv_deal_1.setLayoutParams(layoutParams);
        }

    }
}
