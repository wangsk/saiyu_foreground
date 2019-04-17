package com.saiyu.foreground.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.saiyu.foreground.utils.ButtonUtils;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class BaseActivity extends SupportActivity{

    protected Activity mContext = this;
    private boolean isForegroud = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        CacheActivity.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        CacheActivity.removeActivity(this);
        super.onDestroy();
    }

    //设置所有Fragment的转场动画
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // 设置自定义动画
        //        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForegroud = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForegroud = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForegroud = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isForegroud = true;
    }

    public boolean isForegroud() {
        return isForegroud;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (ButtonUtils.isFastDoubleClick()) { //防止连续点击事件
                return true;
            }
            View v = getCurrentFocus();
            boolean hideInputResult = isShouldHideInput(v, ev);
            if (hideInputResult) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (v != null) {
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 之前一直不成功的原因是,getX获取的是相对父视图的坐标,getRawX获取的才是相对屏幕原点的坐标！！！
//            KLog.i("leftTop[]", "zz--left:" + left + "--top:" + top + "--bottom:" + bottom + "--right:" + right);
//            KLog.i("event", "zz--getX():" + event.getRawX() + "--getY():" + event.getRawY());
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
