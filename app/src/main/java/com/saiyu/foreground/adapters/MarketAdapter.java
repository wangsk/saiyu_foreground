package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.StatisticsListRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.views.HorizontalBarView;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyHolder> {
    List<StatisticsListRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MarketAdapter(List<StatisticsListRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public MarketAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_market, viewGroup, false);
        return new MarketAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        myHolder.tv_date.setText(mItems.get(i).getDate());

        ArrayList<HorizontalBarView.HoBarEntity> hoBarEntities = new ArrayList<>();
        HorizontalBarView.HoBarEntity hoBarEntity = new HorizontalBarView.HoBarEntity();
        hoBarEntity.content = "";
        hoBarEntity.count = mItems.get(i).getAverageDiscount();
        hoBarEntities.add(hoBarEntity);
        HorizontalBarView.HoBarEntity hoBarEntity1 = new HorizontalBarView.HoBarEntity();
        hoBarEntity1.content = "";
        hoBarEntity1.count = mItems.get(i).getMaxDiscount();
        hoBarEntities.add(hoBarEntity1);
        myHolder.horizontalbar.setHoBarData(hoBarEntities);


    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void refreshData(List<StatisticsListRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;
        public HorizontalBarView horizontalbar;

        public MyHolder(View itemView) {
            super(itemView);
            horizontalbar = itemView.findViewById(R.id.horizontalbar);
            tv_date = itemView.findViewById(R.id.tv_date);
        }

    }
}
