package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.frame_base_container_layout)
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.print("token == " + SPUtils.getString(ConstValue.ACCESS_TOKEN,""));
//        if(!TextUtils.isEmpty(SPUtils.getString(ConstValue.AUTO_LOGIN_FLAG,"")) && !TextUtils.isEmpty(SPUtils.getString(ConstValue.ACCESS_TOKEN,""))){
//            Intent intentlogin = new Intent(SplashActivity.this,
//                    MainActivity.class);
//            SplashActivity.this.startActivity(intentlogin);
//            SplashActivity.this.finish();
//
//        } else {
            Intent intent = new Intent(SplashActivity.this,LoginActivity_.class);
            startActivity(intent);
            SplashActivity.this.finish();
//        }
    }
}
