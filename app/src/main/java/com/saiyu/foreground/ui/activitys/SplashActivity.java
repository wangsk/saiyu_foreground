package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity{
    private final int SPLASH_DISPLAY_LENGHT = 500; // 延迟500毫秒

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    @AfterViews
    void AfterViews() {
//        ((TextView) findViewById(R.id.tv_version)).setText(getResources().getString(R.string.app_versionName));
        final boolean isFirstRunning = SPUtils.getBoolean(ConstValue.ISFIRSTRUNNING, true);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isFirstRunning) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(SplashActivity.this,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.GuideFragmentTag);
                    intent.putExtras(bundle);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();

                } else {
                    Intent mainIntent = new Intent(SplashActivity.this,
                            MainActivity_.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
