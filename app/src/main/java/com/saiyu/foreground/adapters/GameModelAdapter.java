package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.ProductListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class GameModelAdapter extends RecyclerView.Adapter<GameModelAdapter.MyHolder> {

    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    List<ProductListRet.DatasBean.ProductItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public GameModelAdapter(List<ProductListRet.DatasBean.ProductItemsBean> list){
        this.mItems = list;
        this.flag = false;
    }

    @NonNull
    @Override
    public GameModelAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game_model, viewGroup, false);
        return new GameModelAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameModelAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.item_hall_tv.setText(mItems.get(i).getName());
        if(flag){
            myHolder.iv_del.setVisibility(View.VISIBLE);
        } else {
            myHolder.iv_del.setVisibility(View.INVISIBLE);
        }

        myHolder.rl_game_model.setOnClickListener(new View.OnClickListener() {
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
        public RelativeLayout rl_game_model;
        public ImageView iv_del;

        public MyHolder(View itemView) {
            super(itemView);
            item_hall_tv = itemView.findViewById(R.id.item_hall_tv);
            rl_game_model = itemView.findViewById(R.id.rl_game_model);
            iv_del = itemView.findViewById(R.id.iv_del);
        }

    }
}
