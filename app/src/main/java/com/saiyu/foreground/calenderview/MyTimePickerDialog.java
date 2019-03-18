package com.saiyu.foreground.calenderview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.saiyu.foreground.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimePickerDialog extends Dialog {

    Context context;
    PickerView hour_pv, minute_pv;
    List<String> hourList = new ArrayList<String>();
    List<String> minuteList = new ArrayList<String>();
    Button tp_queren, tp_quxiao;
    public static String hourText ="12";
    public static String minuteText = "30";
    DialogOnClickListener dialogOnClickListener;
    Dialog dialog;

    public MyTimePickerDialog(Context context,int hour,int minute) {
        super(context);
        this.context = context;
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.timepickerdialog);
        hour_pv = (PickerView) dialog.findViewById(R.id.hour);
        minute_pv = (PickerView) dialog.findViewById(R.id.minute);
        tp_queren = (Button) dialog.findViewById(R.id.tp_queren);
        tp_quxiao = (Button) dialog.findViewById(R.id.tp_quxiao);
        //设置自定义timepicker的数据
        for (int i = 0; i < 10; i++) {
            hourList.add("0" + i);
        }
        for (int i1 = 10; i1 < 24; i1++) {
            hourList.add(Integer.toString(i1));
        }
        for (int i = 0; i < 60; i++) {
            minuteList.add(i < 10 ? "0" + i : "" + i);
        }

        hour_pv.setData(hourList);
        hour_pv.setSelected(hour);
        hourText = hour + "";

        minute_pv.setData(minuteList);
        minute_pv.setSelected(minute);
        minuteText = minute + "";

        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                hourText = text;
            }
        });
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                minuteText = text;
            }
        });
        /*
         *设置按钮点击事件
         */
        tp_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOnClickListener.confirmedDo(hourText, minuteText);
                dismiss();
            }
        });
        tp_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    //按钮点击事件接口，用于在activity中完成功能，传入的是timepicker的hour和minute
    public interface DialogOnClickListener {
        public void confirmedDo(String hourText, String minuteText);

    }

    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener){
        this.dialogOnClickListener = dialogOnClickListener;
    }
    //以下是一些常用方法
    public String getHourText(){
        return hourText;

    }

    public String getMinuteText(){
        return minuteText;
    }
    public void show() {
        dialog.show();
    }
    public void hide(){
        dialog.hide();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
