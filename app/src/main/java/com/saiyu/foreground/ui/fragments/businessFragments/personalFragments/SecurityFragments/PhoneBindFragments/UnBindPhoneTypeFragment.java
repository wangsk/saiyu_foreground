package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityFragments.PhoneBindFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.FaceIdentifyFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_unbind_phone_type)
public class UnBindPhoneTypeFragment extends BaseFragment {

    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_last,btn_next;
    @ViewById
    RadioButton rb_phone,rb_face;
    private String Mobile;
    private boolean isFaceIdentity;
    private String RealName,IDNum;

    public static UnBindPhoneTypeFragment newInstance(Bundle bundle) {
        UnBindPhoneTypeFragment_ fragment = new UnBindPhoneTypeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("绑定手机管理");
        Bundle bundle = getArguments();
        if (bundle != null) {
            isFaceIdentity = bundle.getBoolean("isFaceIdentity",false);
            Mobile = bundle.getString("Mobile");
            if(isFaceIdentity){
                rb_face.setVisibility(View.VISIBLE);
                RealName = bundle.getString("RealName","");
                IDNum = bundle.getString("IDNum","");
            } else {
                rb_face.setVisibility(View.GONE);
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_last,R.id.btn_next})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_last:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_next:
                Bundle bundle_3 = new Bundle();
                bundle_3.putString("Mobile",Mobile);
                if(rb_phone.isChecked()){
                    BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance(bundle_3);
                    start(bindPhoneFragment);
                } else {
                    bundle_3.putString("RealName",RealName);
                    bundle_3.putString("IDNum",IDNum);
                    bundle_3.putInt(FaceIdentifyFragment.FaceIdentifyFunctionType,1);
                    FaceIdentifyFragment faceIdentifyFragment = FaceIdentifyFragment.newInstance(bundle_3);
                    start(faceIdentifyFragment);
                }
                break;

        }

    }

}
