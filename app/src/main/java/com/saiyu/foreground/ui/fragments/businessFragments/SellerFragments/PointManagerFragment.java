package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_pointmanager)
public class PointManagerFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_mypoint, tv_title_content, tv_title_right;
    @ViewById
    Button btn_title_back;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    ImageView iv_1, iv_2, iv_3;
    private float myPoint;
    private String amount = "10";

    public static PointManagerFragment newInstance(Bundle bundle) {
        PointManagerFragment_ fragment = new PointManagerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("PointManagerFragment_rechargePoint")) {
            BooleanRet ret = (BooleanRet) baseRet;
            if (ret.getData() == null) {
                return;
            }
            if (ret.getData().isResult()) {
                Toast.makeText(mContext, "点数充值成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("接单点数管理");
        tv_title_right.setText("点数充值历史");
        tv_title_right.setTextSize(15);
        tv_title_right.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            myPoint = bundle.getFloat("myPoint");
            tv_mypoint.setText(myPoint + "");
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_confirm, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.tv_title_right})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                ApiRequest.rechargePoint(amount, "PointManagerFragment_rechargePoint", pb_loading);
                break;
            case R.id.tv_title_right:
                PointRechargeHistoryFragment pointRechargeHistoryFragment = PointRechargeHistoryFragment.newInstance(null);
                start(pointRechargeHistoryFragment);
                break;
            case R.id.ll_1:
                if (iv_1.getVisibility() == View.GONE) {
                    iv_1.setVisibility(View.VISIBLE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.GONE);
                    amount = "10";
                }
                break;
            case R.id.ll_2:
                if (iv_2.getVisibility() == View.GONE) {
                    iv_2.setVisibility(View.VISIBLE);
                    iv_1.setVisibility(View.GONE);
                    iv_3.setVisibility(View.GONE);
                    amount = "30";
                }
                break;
            case R.id.ll_3:
                if (iv_3.getVisibility() == View.GONE) {
                    iv_3.setVisibility(View.VISIBLE);
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.GONE);
                    amount = "50";
                }
                break;
        }
    }
}
