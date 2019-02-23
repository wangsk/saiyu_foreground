package com.saiyu.foreground.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.activitys.RegistActivity_;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_findpsw_bylevel_layout)
public class FindPswByLevelFragment extends BaseFragment {

    @ViewById
    TextView tv_title_content, tv_level_5_time, tv_level_5_count, tv_level_5_prompt;
    @ViewById
    Button btn_title_back, btn_level_5_back, btn_level_5_regist, btn_next;
    @ViewById
    LinearLayout ll_level_5, ll_level_top4;
    @ViewById
    RadioButton rb_mobile, rb_face, rb_identify;
    @ViewById
    RadioGroup rg_selector;

    private String account;
    private AccountInfoNoLoginRet accountInfoNoLoginRet;

    public static FindPswByLevelFragment newInstance(Bundle bundle) {
        FindPswByLevelFragment_ fragment = new FindPswByLevelFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("找回密码");
        Bundle bundle = getArguments();
        if (bundle != null) {
            account = bundle.getString("account");
            accountInfoNoLoginRet = (AccountInfoNoLoginRet)bundle.getSerializable("AccountInfoNoLoginRet");
        }

        if (!TextUtils.isEmpty(account) && accountInfoNoLoginRet != null) {
            int riskLevel = accountInfoNoLoginRet.getData().getRiskLevel();
            int totalMoney = accountInfoNoLoginRet.getData().getTotalMoeney();
            switch (riskLevel) {
                case 1:
                case 2:
                    rb_mobile.setVisibility(View.VISIBLE);
                    rb_face.setVisibility(View.VISIBLE);
                    rb_mobile.setChecked(true);
                    break;
                case 3:
                    rb_mobile.setVisibility(View.VISIBLE);
                    rb_mobile.setChecked(true);
                    rb_mobile.setClickable(false);
                    break;
                case 4:
                    if (totalMoney > 100) {
                        rb_face.setVisibility(View.VISIBLE);
                        rb_face.setChecked(true);
                        rb_face.setClickable(false);
                    } else {
                        rb_identify.setVisibility(View.VISIBLE);
                        rb_identify.setChecked(true);
                        rb_identify.setClickable(false);
                    }
                    break;
                case 5:
                    ll_level_top4.setVisibility(View.GONE);
                    ll_level_5.setVisibility(View.VISIBLE);
                    tv_level_5_time.setText("注册时间: " + accountInfoNoLoginRet.getData().getRegTime());
                    break;
            }

        }

    }

    @Click({R.id.btn_title_back, R.id.btn_level_5_back, R.id.btn_next, R.id.btn_level_5_regist})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                case R.id.btn_level_5_back:
                    getFragmentManager().popBackStack();
                    break;
                case R.id.btn_next:
                    Bundle bundle = new Bundle();
                    bundle.putString("account",account);
                    bundle.putSerializable("AccountInfoNoLoginRet",accountInfoNoLoginRet);
                    if (rb_identify.isChecked()) {
                        IdentityIdentifyFragment identityIdentifyFragment = IdentityIdentifyFragment.newInstance(bundle);
                        start(identityIdentifyFragment);
                    }
                    if (rb_face.isChecked()) {
                        FaceIdentifyFragment faceIdentifyFragment = FaceIdentifyFragment.newInstance(bundle);
                        start(faceIdentifyFragment);
                    }
                    if (rb_mobile.isChecked()) {
                        PhoneIdentifyFragment phoneIdentifyFragment = PhoneIdentifyFragment.newInstance(bundle);
                        start(phoneIdentifyFragment);
                    }

                    break;
                case R.id.btn_level_5_regist:
                    mContext.startActivity(new Intent(mContext,RegistActivity_.class));
                    getActivity().finish();

                    break;

            }
        }
    }

}
