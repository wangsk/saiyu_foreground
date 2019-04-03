package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;

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
    LinearLayout ll_recharge_1,ll_recharge_2;
    @ViewById
    ImageView iv_recharge_1,iv_recharge_2;
    private String onceMinCount,lessChargeDiscount;

    public static SetMinRechargeFragment newInstance(Bundle bundle) {
        SetMinRechargeFragment_ fragment = new SetMinRechargeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("单次充值最低");
        lessChargeDiscount = "1";//原价

    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.ll_recharge_1,R.id.ll_recharge_2,R.id.btn_cancel})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_cancel:
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                onceMinCount = et_release_num.getText().toString();
                bundle.putString("onceMinCount",onceMinCount);
                bundle.putString("lessChargeDiscount",lessChargeDiscount);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.ll_recharge_1:
                if(iv_recharge_1.getVisibility() == View.GONE){
                    iv_recharge_1.setVisibility(View.VISIBLE);
                    iv_recharge_2.setVisibility(View.GONE);
                    lessChargeDiscount = "1";
                }
                break;
            case R.id.ll_recharge_2:
                if(iv_recharge_2.getVisibility() == View.GONE){
                    iv_recharge_1.setVisibility(View.GONE);
                    iv_recharge_2.setVisibility(View.VISIBLE);
                    lessChargeDiscount = "0.05";
                }
                break;
        }
    }
}
