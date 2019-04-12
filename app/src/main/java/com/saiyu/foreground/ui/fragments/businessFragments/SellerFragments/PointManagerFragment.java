package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    TextView tv_mypoint, tv_title_content;
    @ViewById
    Button btn_title_back;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_1_1, tv_2_1, tv_1_2,tv_2_2,tv_1_3,tv_2_3;
    @ViewById
    LinearLayout ll_1,ll_2,ll_3;
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            myPoint = bundle.getFloat("myPoint");
            tv_mypoint.setText(myPoint + "");
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_confirm, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_point})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                ApiRequest.rechargePoint(amount, "PointManagerFragment_rechargePoint", pb_loading);
                break;
            case R.id.ll_point:
                PointRechargeHistoryFragment pointRechargeHistoryFragment = PointRechargeHistoryFragment.newInstance(null);
                start(pointRechargeHistoryFragment);
                break;
            case R.id.ll_1:
                if (!"10".equals(amount)) {
                    ll_1.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_blue));
                    ll_2.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    ll_3.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    tv_1_1.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_2_1.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_1_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_2.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    tv_1_3.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_3.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    amount = "10";
                }
                break;
            case R.id.ll_2:
                if (!"30".equals(amount)) {
                    ll_1.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    ll_2.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_blue));
                    ll_3.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    tv_1_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_1.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    tv_1_2.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_2_2.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_1_3.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_3.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    amount = "30";
                }
                break;
            case R.id.ll_3:
                if (!"50".equals(amount)) {
                    ll_1.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    ll_2.setBackground(mContext.getResources().getDrawable(R.drawable.border_colorline_bluelight_2));
                    ll_3.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_blue));
                    tv_1_1.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_1.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    tv_1_2.setTextColor(mContext.getResources().getColor(R.color.blue));
                    tv_2_2.setTextColor(mContext.getResources().getColor(R.color.black_light));
                    tv_1_3.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_2_3.setTextColor(mContext.getResources().getColor(R.color.white));
                    amount = "50";
                }
                break;
        }
    }
}
