package com.saiyu.foreground.ui.activitys;

import android.os.Bundle;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.RegistUnionIdFragment_;

import org.androidannotations.annotations.EActivity;
@EActivity(R.layout.frame_base_container_layout)
public class RegistUnionIdActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        loadRootFragment(R.id.fr_base_container, RegistUnionIdFragment_.newInstance(bundle));
    }

}
