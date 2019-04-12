package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_set_order_minrecharge)
public class SetMinRechargeFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    @ViewById
    EditText et_release_num;
    @ViewById
    RadioButton rb_1,rb_2;
    @ViewById
    RadioGroup rg_selector;
    private float onceMinCount,lessChargeDiscount;

    public static SetMinRechargeFragment newInstance(Bundle bundle) {
        SetMinRechargeFragment_ fragment = new SetMinRechargeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("单次充值最低");
        lessChargeDiscount = 1f;//原价
        rg_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_1:
                        lessChargeDiscount = 1f;
                        break;
                    case R.id.rb_2:
                        lessChargeDiscount = 0.05f;
                        break;
                }
                LogUtils.print("lessChargeDiscount === " + lessChargeDiscount);
            }
        });
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel})
    void onClick(View view) {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_cancel:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putFloat("onceMinCount",0f);
                bundle.putFloat("lessChargeDiscount",1f);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                intent = new Intent();
                bundle = new Bundle();
                if(TextUtils.isEmpty(et_release_num.getText().toString().trim())){
                    onceMinCount = 0f;
                } else {
                    onceMinCount = Float.parseFloat(et_release_num.getText().toString().trim());
                }
                bundle.putFloat("onceMinCount",onceMinCount);
                bundle.putFloat("lessChargeDiscount",lessChargeDiscount);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
