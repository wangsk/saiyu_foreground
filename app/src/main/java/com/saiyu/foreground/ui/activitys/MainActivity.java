package com.saiyu.foreground.ui.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        SPUtils.putString("accessToken", "");
        SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, false);
    }
}
