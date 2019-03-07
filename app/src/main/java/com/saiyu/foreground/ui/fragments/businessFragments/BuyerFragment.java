package com.saiyu.foreground.ui.fragments.businessFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_buyer)
public class BuyerFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    ProgressBar pb_loading;

    public static BuyerFragment newInstance(Bundle bundle) {
        BuyerFragment_ fragment = new BuyerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.buyerInfo("",pb_loading);
    }

    @AfterViews
    void afterView() {

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("")) {

        }
    }

//    @Click({})
//    void onClick(View view) {
//        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
//            switch (view.getId()) {
//
//            }
//        }
//    }

}
