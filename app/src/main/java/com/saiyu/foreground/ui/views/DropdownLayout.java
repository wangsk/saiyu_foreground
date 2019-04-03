package com.saiyu.foreground.ui.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.saiyu.foreground.R;
import com.saiyu.foreground.utils.LogUtils;


/**
 * -----------------------------------------------------
 * 项目： MyAndroidDemo
 * 作者： wd_zhu
 * 日期： 2016/12/7 13:52
 * 邮箱： zhushengping@wdit.com.cn
 * 描述：
 * ------------------------------------------------------
 */
public class DropdownLayout extends LinearLayout {
    private static final String TAG = "DropdownLayout";
    private int mDropSpeed ;

    private View mDropView;
    private int mDropHeight;

    public DropdownLayout(Context context) {
        this(context,null);
        this.setOrientation(VERTICAL);
    }

    public DropdownLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        this.setOrientation(VERTICAL);
    }

    public DropdownLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropdownLayout);
        mDropSpeed = array.getInteger(R.styleable.DropdownLayout_dropSpeed,300);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(null != this.getChildAt(0)){
//            mDropView = (View)this.getChildAt(0);
//            mDropHeight = mDropView.getMeasuredHeight();
//            mDropView.setY(-mDropHeight);
//            LogUtils.print("mDropHeight === " + mDropHeight);
//        }

    }

    public void toggle(View view,boolean flag){
        if (flag){
            Message msg =new Message();
            msg.what = 1;
            msg.obj = view;
            handler.sendMessage(msg);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -416, 0);
            translationY.setDuration(mDropSpeed);
            translationY.start();
        }else{
            ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, -416);
            translationY.setDuration(mDropSpeed);
            translationY.start();
            Message msg =new Message();
            msg.what = 2;
            msg.obj = view;
            handler.sendMessageDelayed(msg,mDropSpeed);

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View view = (View)msg.obj;
            switch (msg.what){
                case 1:
                    view.setVisibility(VISIBLE);
                    break;
                case 2:
                    view.setVisibility(GONE);
                    break;
            }
        }
    };

    public void setDropSpeed(int speed){
        this.mDropSpeed = speed;
    }
}
