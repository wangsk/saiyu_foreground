package com.saiyu.foreground.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

import java.util.List;

public class CashAdapter extends RecyclerView.Adapter<CashAdapter.MyHolder> {

    List<CashRet.DatasBean.ItemsBean> mItems;

    public OnItemClickListener mOnItemClickListener;
    public OnSwipeListener mOnSwipeListener;
    public OnItemLongClickListener mOnItemLongClickListener;

    private int defaultPosition;

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public CashAdapter(List<CashRet.DatasBean.ItemsBean> list){
        this.mItems = list;
    }

    @NonNull
    @Override
    public CashAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cash, viewGroup, false);
        return new CashAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CashAdapter.MyHolder myHolder, final int i) {
        if(mItems == null || mItems.size() <= i || mItems.get(i) == null){
            return;
        }

        Glide.with(App.getApp())
                .load(mItems.get(i).getLogoPic())
                .error(R.mipmap.ic_launcher)
                .into(myHolder.iv_icon);

        int status = mItems.get(i).getIsDefault();
        if(status == 1){
            myHolder.iv_default.setVisibility(View.VISIBLE);
            defaultPosition = i;
            CallbackUtils.doPositionCallback(defaultPosition);
        } else {
            myHolder.iv_default.setVisibility(View.GONE);
        }

        myHolder.tv_bankname.setText(mItems.get(i).getName());

        String text = "";
        if(!TextUtils.isEmpty(mItems.get(i).getName())){
            if(mItems.get(i).getName().contains("支付宝")){
                text = "免手续费 到账时间" + mItems.get(i).getPayDateStr();
            } else if(mItems.get(i).getName().contains("微信")){
                text = "手续费 " + mItems.get(i).getCharge() + "% 到账时间" + mItems.get(i).getPayDateStr();
            } else {
                if(!TextUtils.isEmpty(mItems.get(i).getAccount())){
                    if(mItems.get(i).getAccount().length() > 4){
                        text = "尾号" + mItems.get(i).getAccount().substring(mItems.get(i).getAccount().length() -4)+"储蓄卡";
                    } else {
                        text = "尾号" +mItems.get(i).getAccount()+"储蓄卡";
                    }
                }
                text = text + " 手续费" + mItems.get(i).getCharge() + "% 到账时间" + mItems.get(i).getPayDateStr();
            }
        }

        myHolder.tv_text.setText(text);

        myHolder.rl_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = myHolder.getLayoutPosition();
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(CashAdapter.this,myHolder.itemView,layoutPosition);
                    try {
                        mItems.get(defaultPosition).setIsDefault(0);
                        mItems.get(i).setIsDefault(1);
                        notifyDataSetChanged();
                    }catch (Exception e){
                        LogUtils.print("切换默认 Exception ==  " + e.toString());
                    }
                }
            }
        });
        myHolder.rl_item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int layoutPosition = myHolder.getLayoutPosition();
                if(mOnItemLongClickListener != null){
                    mOnItemLongClickListener.onItemLongClick(CashAdapter.this,myHolder.itemView,layoutPosition);
                }
                return false;
            }
        });
        myHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = myHolder.getLayoutPosition();
                if(null != mOnSwipeListener){
                    mOnSwipeListener.onDelete(CashAdapter.this,layoutPosition);
                    ((SwipeMenuLayout) myHolder.itemView).quickClose();
                }
            }
        });
        myHolder.btn_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = myHolder.getLayoutPosition();
                if(null != mOnSwipeListener){
                    mOnSwipeListener.onRevise(CashAdapter.this,layoutPosition);
                    ((SwipeMenuLayout) myHolder.itemView).quickClose();
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

    public void refreshData(List<CashRet.DatasBean.ItemsBean> list) {
        if (list != null) {
            mItems.clear();
            mItems.addAll(list);
            notifyDataSetChanged();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_bankname,tv_text;
        public RelativeLayout rl_item_layout;
        public Button btn_revise,btn_delete;
        public ImageView iv_icon,iv_default;

        public MyHolder(View itemView) {
            super(itemView);
            rl_item_layout = itemView.findViewById(R.id.rl_item_layout);
            tv_bankname = itemView.findViewById(R.id.tv_bankname);
            tv_text = itemView.findViewById(R.id.tv_text);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_default = itemView.findViewById(R.id.iv_default);
            btn_revise = itemView.findViewById(R.id.btn_revise);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(CashAdapter cashAdapter, View view, int position);
    }

    public interface OnItemLongClickListener {

        void onItemLongClick(CashAdapter cashAdapter, View view, int position);
    }

    public interface OnSwipeListener {
        void onRevise(CashAdapter cashAdapter, int pos);
        void onDelete(CashAdapter cashAdapter, int pos);
    }

    public void setOnDelListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClick) {
        this.mOnItemLongClickListener = onItemLongClick;
    }

}
