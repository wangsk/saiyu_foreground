package com.saiyu.foreground.ui.fragments;

import android.app.Activity;

import java.io.Serializable;

import me.yokeyword.fragmentation.SupportFragment;

public class BaseFragment extends SupportFragment{
    protected static Activity mContext;

    @Override
    public void onAttach(Activity context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

}
