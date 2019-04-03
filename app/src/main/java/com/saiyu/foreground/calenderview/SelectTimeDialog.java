package com.saiyu.foreground.calenderview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private TextView tp_queren, tp_quxiao;
    private Context mContext;
    private java.util.Calendar mCalendar;

    private PickerView pv_hour_start, pv_minute_start,pv_hour_end,pv_minute_end;
    private List<String> hourList = new ArrayList<String>();
    private List<String> minuteList = new ArrayList<String>();

    private OnListCallbackListener onListCallbackListener;

    private int mHourStart ,mMinuteStart,mHourEnd,mMinuteEnd;

    public SelectTimeDialog(@NonNull Context context, java.util.Calendar calendar) {
        super(context);
        mContext = context;
        mCalendar = calendar;

        View view = View.inflate(mContext, R.layout.dialog_selecttime, null);
        setContentView(view);

        setCanceledOnTouchOutside(false);

        initView();
    }

//    protected SelectTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//        mContext = context;
//
//    }


    private void initView() {
        mHourStart = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        mMinuteStart = mCalendar.get(java.util.Calendar.MINUTE);
        mHourEnd = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        mMinuteEnd = mCalendar.get(java.util.Calendar.MINUTE);

        pv_hour_start = (PickerView) findViewById(R.id.pv_hour_start);
        pv_minute_start = (PickerView) findViewById(R.id.pv_minute_start);
        pv_hour_end = (PickerView) findViewById(R.id.pv_hour_end);
        pv_minute_end = (PickerView) findViewById(R.id.pv_minute_end);

        tp_queren = (TextView) findViewById(R.id.tp_queren);
        tp_quxiao = (TextView) findViewById(R.id.tp_quxiao);

        //设置自定义timepicker的数据
//        for (int i = 0; i < 10; i++) {
//            hourList.add("0" + i);
//        }
//        for (int i1 = 10; i1 < 24; i1++) {
//            hourList.add(Integer.toString(i1));
//        }
//        for (int i = 0; i < 60; i++) {
//            minuteList.add(i < 10 ? "0" + i : "" + i);
//        }

        for (int i = 0; i < 24; i++) {
            hourList.add(Integer.toString(i));
        }

        for (int i = 0; i < 60; i++) {
            minuteList.add(Integer.toString(i));
        }

        pv_hour_start.setData(hourList);
        pv_hour_end.setData(hourList);
        pv_minute_start.setData(minuteList);
        pv_minute_end.setData(minuteList);

        pv_hour_start.setSelected(mHourStart+"");
        pv_minute_start.setSelected(mMinuteStart+"");
        pv_hour_end.setSelected(mHourEnd+"");
        pv_minute_end.setSelected(mMinuteEnd+"");
        LogUtils.print(mHourStart+","+mMinuteStart+","+mHourEnd+","+mMinuteEnd);

        pv_hour_start.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mHourStart = Integer.parseInt(text);
            }
        });
        pv_minute_start.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mMinuteStart = Integer.parseInt(text);
            }
        });

        pv_hour_end.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mHourEnd = Integer.parseInt(text);
            }
        });
        pv_minute_end.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mMinuteEnd = Integer.parseInt(text);
            }
        });

        tp_queren.setOnClickListener(this);
        tp_quxiao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tp_queren:
                if(onListCallbackListener != null){
                    List<String> list = new ArrayList<>();
                    list.add(mHourStart+"");
                    list.add(mMinuteStart+"");
                    list.add(mHourEnd+"");
                    list.add(mMinuteEnd+"");
                    onListCallbackListener.setOnListCallbackListener(list);
                }
                dismiss();
                break;
            case R.id.tp_quxiao:
                if(onListCallbackListener != null){
                    onListCallbackListener.setOnListCallbackListener(null);
                }
                dismiss();
                break;
        }
    }

    public void setDialogOnClickListener(OnListCallbackListener onListCallbackListener){
        this.onListCallbackListener = onListCallbackListener;
    }
}
