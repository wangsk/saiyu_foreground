package com.saiyu.foreground.ui.activitys;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.saiyu.foreground.R;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_zhifubao_callback_layout)
public class ZhiFuBaoCallbackActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        String sycallbackid = data.getQueryParameter("sycallbackid");
        LogUtils.print("sycallbackid === " + sycallbackid);
    }
}
