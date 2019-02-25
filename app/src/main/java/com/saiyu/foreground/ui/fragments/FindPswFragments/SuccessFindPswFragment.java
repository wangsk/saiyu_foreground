package com.saiyu.foreground.ui.fragments.FindPswFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_suc_findpsw_layout)
public class SuccessFindPswFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content, tv_success;
    @ViewById
    Button btn_title_back, btn_next;

    public static SuccessFindPswFragment newInstance(Bundle bundle) {
        SuccessFindPswFragment_ fragment = new SuccessFindPswFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean isRegist = bundle.getBoolean("isRegist", false);
            boolean bind = bundle.getBoolean("bind", false);
            boolean isReset = bundle.getBoolean("isReset", false);
            if (isRegist) {
                tv_title_content.setText("注册成功");
                tv_success.setText("恭喜您注册成功");
            }
            if (bind) {
                tv_title_content.setText("绑定成功");
                tv_success.setText("恭喜您绑定成功");
            }
            if (isReset) {
                tv_title_content.setText("找回密码");
            }
        }

        btn_title_back.setVisibility(View.GONE);

    }

    @Click({R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_next:
                    getActivity().finish();
                    break;
            }
        }
    }
}
