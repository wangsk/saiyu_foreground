package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.utils.LogUtils;

import java.util.List;

public class HallAdapter extends RecyclerView.Adapter<HallAdapter.MyHolder> {

    List<HallRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public HallAdapter(List<HallRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public HallAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hall, viewGroup, false);
        return new HallAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_line_1.setText(mItems.get(i).getOrderTitle());
        myHolder.tv_line_1_2.setText(mItems.get(i).getReserveTitle());

        if(mItems.get(i).isOnceMinCount()){
            myHolder.tv_2_1.setText("单次最低"+mItems.get(i).getOnceMinCount()+"Q币");
        } else {
            myHolder.tv_2_1.setText("单次数量不限制");
        }
        int index = 0;
        if(mItems.get(i).isImgConfirmation()){
            myHolder.tv_2_2.setText("验图代确认");
            myHolder.tv_2_2.setVisibility(View.VISIBLE);
            index++;
        }

        if(mItems.get(i).isCustomerConfirmation()){
            if(index == 0){
                myHolder.tv_2_2.setText("客服代确认");
                myHolder.tv_2_2.setVisibility(View.VISIBLE);
            } else if(index == 1){
                myHolder.tv_2_3.setText("客服代确认");
                myHolder.tv_2_3.setVisibility(View.VISIBLE);
            }
            index++;
        }

        if(mItems.get(i).isLessThanOriginalPrice()){
            if(index == 0){
                myHolder.tv_2_2.setText("少充按原价");
                myHolder.tv_2_2.setVisibility(View.VISIBLE);
            } else if(index == 1){
                myHolder.tv_2_3.setText("少充按原价");
                myHolder.tv_2_3.setVisibility(View.VISIBLE);
            } else if(index == 2){
                myHolder.tv_2_4.setText("少充按原价");
                myHolder.tv_2_4.setVisibility(View.VISIBLE);
            }
            index++;
        }
        if(mItems.get(i).isOrderPwd()){
            if(index == 0){
                myHolder.tv_2_2.setText("私密订单");
                myHolder.tv_2_2.setVisibility(View.VISIBLE);
            } else if(index == 1){
                myHolder.tv_2_3.setText("私密订单");
                myHolder.tv_2_3.setVisibility(View.VISIBLE);
            } else if(index == 2){
                myHolder.tv_2_4.setText("私密订单");
                myHolder.tv_2_4.setVisibility(View.VISIBLE);
            }else if(index == 3){
                myHolder.tv_2_5.setText("私密订单");
                myHolder.tv_2_5.setVisibility(View.VISIBLE);
            }
            index++;
        }
        if(mItems.get(i).isFriendLimit()){
            if(index == 0){
                myHolder.tv_2_2.setText("需加好友");
                myHolder.tv_2_2.setVisibility(View.VISIBLE);
            } else if(index == 1){
                myHolder.tv_2_3.setText("需加好友");
                myHolder.tv_2_3.setVisibility(View.VISIBLE);
            } else if(index == 2){
                myHolder.tv_2_4.setText("需加好友");
                myHolder.tv_2_4.setVisibility(View.VISIBLE);
            }else if(index == 3){
                myHolder.tv_2_5.setText("需加好友");
                myHolder.tv_2_5.setVisibility(View.VISIBLE);
            }
            else if(index == 4){
                myHolder.tv_2_6.setText("需加好友");
                myHolder.tv_2_6.setVisibility(View.VISIBLE);
            }
            index++;
        }
        myHolder.tv_line_3_1.setText(mItems.get(i).getRemainingAmount()+"Q币");
        myHolder.tv_line_3_2.setText("成交"+mItems.get(i).getOrderRSettleTotalCount()+"笔"+mItems.get(i).getOrderRSettleTotalMoney()+"元");
        myHolder.tv_zc.setText(mItems.get(i).getReserveDiscount()+"折");
        myHolder.rb_mark.setRating(mItems.get(i).getVipLevel());

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

    public void refreshData(List<HallRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_line_1,tv_line_1_2,tv_2_1,tv_2_2,tv_2_3,tv_2_4,tv_zc,tv_2_5,tv_2_6,tv_line_3_1,tv_line_3_2;
        public LinearLayout ll_item_layout;
        public RatingBar rb_mark;

        public MyHolder(View itemView) {
            super(itemView);
            ll_item_layout = itemView.findViewById(R.id.ll_item_layout);
            tv_line_1 = itemView.findViewById(R.id.tv_line_1);
            tv_line_1_2 = itemView.findViewById(R.id.tv_line_1_2);
            tv_2_1 = itemView.findViewById(R.id.tv_2_1);
            tv_2_2 = itemView.findViewById(R.id.tv_2_2);
            tv_2_3 = itemView.findViewById(R.id.tv_2_3);
            tv_2_4 = itemView.findViewById(R.id.tv_2_4);
            tv_zc = itemView.findViewById(R.id.tv_zc);
            tv_2_5 = itemView.findViewById(R.id.tv_2_5);
            tv_2_6 = itemView.findViewById(R.id.tv_2_6);
            tv_line_3_1 = itemView.findViewById(R.id.tv_line_3_1);
            tv_line_3_2 = itemView.findViewById(R.id.tv_line_3_2);
            rb_mark = (RatingBar)itemView.findViewById(R.id.rb_mark);

        }

    }
}
