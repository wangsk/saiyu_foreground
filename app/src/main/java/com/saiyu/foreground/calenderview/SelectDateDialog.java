package com.saiyu.foreground.calenderview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.saiyu.foreground.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectDateDialog extends Dialog implements
        CalendarView.OnCalendarSelectListener{

    private TextView mTextSolar,mTextMonthDay,tp_queren, tp_quxiao;
    private CalendarView mCalendarView;
    private Context mContext;
    private java.util.Calendar mCalendar;

    private PickerView hour_pv, minute_pv;
    private List<String> hourList = new ArrayList<String>();
    private List<String> minuteList = new ArrayList<String>();

    private DialogOnClickListener dialogOnClickListener;

    private int mYear,mMonth,mDate,mHour ,mMinute;

    private int[] lunar;


    public SelectDateDialog(@NonNull Context context, java.util.Calendar calendar) {
        super(context);
        mContext = context;
        mCalendar = calendar;
    }

    public SelectDateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected SelectDateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dialogstyle, null);
        setContentView(view);

        setCanceledOnTouchOutside(false);

//        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
//        Point point = new Point();
//        display.getSize(point);
//
//        Window window = getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes(); //获取当前对话框的参数值
//        layoutParams.width = (int) (point.x * 0.9); //宽度设置为屏幕宽度的0.5
//        layoutParams.height = (int) (point.y * 0.8); //高度设置为屏幕高度的0.5
////        layoutParams.width = (int) (display.getWidth() * 0.5);
////        layoutParams.height = (int) (display.getHeight() * 0.5);
//        window.setAttributes(layoutParams);

        initView();


    }

    private void initView() {

        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextSolar = (TextView) findViewById(R.id.tv_solar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarSelectListener(this);

        mYear = mCalendar.get(java.util.Calendar.YEAR);
        mMonth = mCalendar.get(java.util.Calendar.MONTH) + 1;
        mDate = mCalendar.get(java.util.Calendar.DATE);

        mHour = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(java.util.Calendar.MINUTE);

        mCalendarView.scrollToCalendar(mYear,mMonth,mDate);

        mCalendarView.setWeekBar(EnglishWeekBar.class);

        if(mCalendar != null){
            mTextMonthDay.setText(mMonth + "月");

            lunar = LunarUtil.solarToLunar(mYear,mMonth,mDate);

            mTextSolar.setText(mYear + "年" + mMonth + "月" + mDate + "日");

        }

        hour_pv = (PickerView) findViewById(R.id.hour);
        minute_pv = (PickerView) findViewById(R.id.minute);
        tp_queren = (TextView) findViewById(R.id.tp_queren);
        tp_quxiao = (TextView) findViewById(R.id.tp_quxiao);
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
        hour_pv.setSelected(mHour);

        minute_pv.setData(minuteList);
        minute_pv.setSelected(mMinute);

        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mHour = Integer.parseInt(text);
            }
        });
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mMinute = Integer.parseInt(text);
            }
        });
        /*
         *设置按钮点击事件
         */
        tp_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
                try{
                    SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy/MM/dd HH:mm" );
                    date = sdf.parse(mYear + "/" + mMonth + "/"  + mDate + " "  + mHour + ":"  + mMinute);
                }catch(Exception e){
                    e.printStackTrace();
                }

                if(dialogOnClickListener != null){
                    dialogOnClickListener.confirmedDo(date);
                }
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

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextMonthDay.setText(calendar.getMonth() + "月");

        if(isClick){
            mYear = calendar.getYear();
            mMonth = calendar.getMonth();
            mDate = calendar.getDay();

            lunar = LunarUtil.solarToLunar(mYear, mMonth, mDate);

            mTextSolar.setText(mYear + "年" + mMonth + "月" + mDate + "日");

//            mTextLunar.setText(calendar.getLunar());

        }

    }

    //按钮点击事件接口，用于在activity中完成功能，传入的是timepicker的hour和minute
    public interface DialogOnClickListener {
        public void confirmedDo(Date date);

    }

    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener){
        this.dialogOnClickListener = dialogOnClickListener;
    }

}
