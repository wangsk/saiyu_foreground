package com.saiyu.foreground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_protocol_layout)
public class ProtocolFragment extends BaseFragment{
    @ViewById
    TextView tv_protocol_detail,tv_title_content;
    @ViewById
    Button btn_title_back,btn_back;

    public static ProtocolFragment newInstance(Bundle bundle) {
        ProtocolFragment_ fragment = new ProtocolFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("赛鱼网服务协议");
    }

    @Click({R.id.btn_title_back,R.id.btn_back})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
