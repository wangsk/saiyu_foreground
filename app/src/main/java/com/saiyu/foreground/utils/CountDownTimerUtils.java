package com.saiyu.foreground.utils;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;
    private boolean isClickable = true;
    private int bgDrawable = R.drawable.shape_bg_line,foreDrawable = R.drawable.shape_bg_transparent;
    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setEnabled(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s后重新获取");  //设置倒计时时间
        mTextView.setBackground(App.getApp().getResources().getDrawable(foreDrawable));
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，
         * start是起始位置,
         * 无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，
         * 所以处理的文字，
         * 包含开始位置，但不包含结束位置。
         */
    }

    @Override
    public void onFinish() {
        cancel();
        mTextView.setText("发送验证码");
        mTextView.setEnabled(isClickable);//重新获得点击
        if(isClickable){
            mTextView.setBackground(App.getApp().getResources().getDrawable(bgDrawable)); //还原背景色
        } else {
            mTextView.setBackground(App.getApp().getResources().getDrawable(foreDrawable)); //还原背景色
        }

    }

    //设置是否可点击
    public void setClickable(boolean flag){
        this.isClickable = flag;
        mTextView.setEnabled(isClickable);
        if(isClickable){
            mTextView.setBackground(App.getApp().getResources().getDrawable(bgDrawable)); //还原背景色
        } else {
            mTextView.setBackground(App.getApp().getResources().getDrawable(foreDrawable)); //还原背景色
        }
    }
}
