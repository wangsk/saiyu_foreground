package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class OrderCountSelectorAdapter extends RecyclerView.Adapter<OrderCountSelectorAdapter.MyHolder> {

    List<String> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public OrderCountSelectorAdapter(List<String> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public OrderCountSelectorAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ordercount_selector, viewGroup, false);
        return new OrderCountSelectorAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCountSelectorAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_count.setText(mItems.get(i));

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

    public void refreshData(List<String> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_count;
        public LinearLayout ll_item_layout;

        public MyHolder(View itemView) {
            super(itemView);
            ll_item_layout = itemView.findViewById(R.id.ll_item_layout);
            tv_count = itemView.findViewById(R.id.tv_count);
        }
    }

}
