package com.saiyu.foreground.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.SPUtils;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class BaseActivity extends SupportActivity implements CallbackUtils.OnExitListener {

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
        CallbackUtils.setOnExitListener(this);
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

    @Override
    public void setOnExitListener(String code) {
        if(TextUtils.isEmpty(code)){
            return;
        }
        if("411.0".equals(code)){//账号在其他端退出登录，需要将用户退出登录
            SPUtils.putString(ConstValue.ACCESS_TOKEN, "");
            SPUtils.putInt(ConstValue.MainBottomVisibleType,0);//卖家、买家激活状态清空
            SPUtils.putInt(ConstValue.IdentifyInfo,0);//补填身份信息清空
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessageDelayed(2,2000);
        } else if("413.0".equals(code)){//服务器维护(数据出错什么的异常，需要用户暂时不能登录操作)
            SPUtils.putString(ConstValue.ACCESS_TOKEN, "");
            SPUtils.putInt(ConstValue.MainBottomVisibleType,0);//卖家、买家激活状态清空
            SPUtils.putInt(ConstValue.IdentifyInfo,0);//补填身份信息清空
            handler.sendEmptyMessage(3);

        }

    }

    public void restartApplication(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(BaseActivity.this,"您的账号已退出，请重新登录",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(BaseActivity.this,"系统维护，给您带来不便，敬请谅解",Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.SystemMaintenanceFragmentTag);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                    break;
                case 2:
                    restartApplication(BaseActivity.this);
                    break;
            }
        }
    };

}
