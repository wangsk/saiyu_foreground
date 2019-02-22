package com.saiyu.foreground.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.saiyu.foreground.cashe.CacheActivity;

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
}
