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

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.PreOrderManagerRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;

import java.util.List;

public class PreOrderManagerAdapter extends RecyclerView.Adapter<PreOrderManagerAdapter.MyHolder> {
    List<PreOrderManagerRet.DatasBean.ItemsBean> mItems;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public PreOrderManagerAdapter(List<PreOrderManagerRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public PreOrderManagerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_record, viewGroup, false);
        return new PreOrderManagerAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreOrderManagerAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        String deal_1 = mItems.get(i).getTitle() + "<font color = \"#fe8f62\">" +"(已完成"+ mItems.get(i).getSuccQBCount()+")" + "</font>";

        myHolder.tv_deal_1.setText(Html.fromHtml(deal_1));
        myHolder.tv_deal_2.setText(mItems.get(i).getOperatorType());
        myHolder.tv_deal_3.setText(mItems.get(i).getProductTypeName());
        myHolder.tv_deal_4.setText(mItems.get(i).getAuditStatusStr());

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

    public void refreshData(List<PreOrderManagerRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_deal_1,tv_deal_2,tv_deal_3,tv_deal_4;
        public RelativeLayout rl_item_layout;

        public MyHolder(View itemView) {
            super(itemView);
            rl_item_layout = itemView.findViewById(R.id.rl_item_layout);
            tv_deal_1 = itemView.findViewById(R.id.tv_deal_1);
            tv_deal_2 = itemView.findViewById(R.id.tv_deal_2);
            tv_deal_3 = itemView.findViewById(R.id.tv_deal_3);
            tv_deal_4 = itemView.findViewById(R.id.tv_deal_4);
        }

    }
}
