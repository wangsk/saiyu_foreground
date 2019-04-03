package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.ProductListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class GameSelectorAdapter extends RecyclerView.Adapter<GameSelectorAdapter.MyHolder> {
    List<ProductListRet.DatasBean.ProductItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public GameSelectorAdapter(List<ProductListRet.DatasBean.ProductItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public GameSelectorAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hall_tv, viewGroup, false);
        return new GameSelectorAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameSelectorAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.item_hall_tv.setText(mItems.get(i).getName());

        myHolder.item_hall_tv.setOnClickListener(new View.OnClickListener() {
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

    public void refreshData(List<ProductListRet.DatasBean.ProductItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView item_hall_tv;

        public MyHolder(View itemView) {
            super(itemView);
            item_hall_tv = itemView.findViewById(R.id.item_hall_tv);
        }

    }
}
