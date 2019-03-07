package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.SellerInfoRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_seller_info)
public class SellerInfoFragment  extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back;

    public static SellerInfoFragment newInstance(Bundle bundle) {
        SellerInfoFragment_ fragment = new SellerInfoFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.sellerInfo("SellerInfoFragment_sellerInfo",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("卖家信息");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("SellerInfoFragment_sellerInfo")){
            SellerInfoRet ret = (SellerInfoRet)baseRet;
            if(ret == null || ret.getData() == null){
                return;
            }

        }
    }

    @Click({R.id.btn_title_back})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
            }
        }
    }
}
