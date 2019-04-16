package com.saiyu.foreground.ui.fragments.FindPswFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.FaceIdentifyFragment;
import com.saiyu.foreground.utils.ButtonUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

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

    private String account,RealName,IDNum,mobile,RegTime,totalMoney;
    private int riskLevel;

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
            RealName = bundle.getString("RealName","");
            IDNum = bundle.getString("IDNum","");
            mobile = bundle.getString("mobile");
            RegTime = bundle.getString("RegTime");
            riskLevel = bundle.getInt("riskLevel");
            totalMoney = bundle.getString("totalMoney");
        }

        if (!TextUtils.isEmpty(account)) {
            switch (riskLevel) {
                case 1:
                case 2:
                    //一级，二级 手机，刷脸二选一
                    if(TextUtils.isEmpty(mobile)){
                        rb_face.setVisibility(View.VISIBLE);
                        rb_face.setChecked(true);
                        rb_face.setClickable(false);
                    } else {
                        rb_mobile.setVisibility(View.VISIBLE);
                        rb_face.setVisibility(View.VISIBLE);
                        rb_mobile.setChecked(true);
                    }

                    break;
                case 3:
                    //三级  手机验证
                    rb_mobile.setVisibility(View.VISIBLE);
                    rb_mobile.setChecked(true);
                    rb_mobile.setClickable(false);
                    break;
                case 4:
                    if(TextUtils.isEmpty(totalMoney)){
                        return;
                    }
                    BigDecimal bigDecimal = new BigDecimal(totalMoney);
                    BigDecimal bigDecimal_100 = new BigDecimal(100);
                    switch (bigDecimal.compareTo(bigDecimal_100)){
                        case -1://小于
                        case 0://等于
                            //四级 小于等于100 身份验证
                            rb_identify.setVisibility(View.VISIBLE);
                            rb_identify.setChecked(true);
                            rb_identify.setClickable(false);
                            break;
                        case 1://大于
                            //四级 大于100 刷脸验证
                            rb_face.setVisibility(View.VISIBLE);
                            rb_face.setChecked(true);
                            rb_face.setClickable(false);
                            break;
                    }
                    break;
                case 5:
                    //五级  无法验证找回
                    ll_level_top4.setVisibility(View.GONE);
                    ll_level_5.setVisibility(View.VISIBLE);
                    tv_level_5_prompt.setText("抱歉,您的赛鱼账号"+account+"未绑定手机\n未补填资料，无法找回!");
                    tv_level_5_time.setText("注册时间: " + RegTime);
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
                    if (rb_identify.isChecked()) {
                        bundle.putString("RealName",RealName);
                        bundle.putString("IDNum",IDNum);
                        IdentityIdentifyFragment identityIdentifyFragment = IdentityIdentifyFragment.newInstance(bundle);
                        start(identityIdentifyFragment);
                    }
                    if (rb_face.isChecked()) {
                        bundle.putString("RealName",RealName);
                        bundle.putString("IDNum",IDNum);
                        bundle.putInt(FaceIdentifyFragment.FaceIdentifyFunctionType,2);
                        FaceIdentifyFragment faceIdentifyFragment = FaceIdentifyFragment.newInstance(bundle);
                        start(faceIdentifyFragment);
                    }
                    if (rb_mobile.isChecked()) {
                        bundle.putString("mobile",mobile);
                        PhoneIdentifyFragment phoneIdentifyFragment = PhoneIdentifyFragment.newInstance(bundle);
                        start(phoneIdentifyFragment);
                    }

                    break;
                case R.id.btn_level_5_regist:
                    Bundle bundle_1 = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle_1.putInt(ContainerActivity.FragmentTag, ContainerActivity.RegistFragmentTag);
                    intent.putExtras(bundle_1);
                    mContext.startActivity(intent);
                    getActivity().finish();

                    break;

            }
        }
    }

}
