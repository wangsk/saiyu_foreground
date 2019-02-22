package com.saiyu.foreground.ui.activitys;

import android.os.Bundle;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.ForgotPswFragment_;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.frame_base_container_layout)
public class ForgotPswActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRootFragment(R.id.fr_base_container, ForgotPswFragment_.newInstance(null));
    }
}
