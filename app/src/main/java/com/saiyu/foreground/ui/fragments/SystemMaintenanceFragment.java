package com.saiyu.foreground.ui.fragments;

import android.os.Bundle;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.activitys.CacheActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_system_maintenance)
public class SystemMaintenanceFragment extends BaseFragment{

    public static SystemMaintenanceFragment newInstance(Bundle bundle) {
        SystemMaintenanceFragment_ fragment = new SystemMaintenanceFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){

    }

    @Override
    public boolean onBackPressedSupport() {
        CacheActivity.finishActivity();
        return true;
    }
}
