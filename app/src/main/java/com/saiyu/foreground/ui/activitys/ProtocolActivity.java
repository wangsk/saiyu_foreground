package com.saiyu.foreground.ui.activitys;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_protocol_layout)
public class ProtocolActivity extends BaseActivity{
    @ViewById
    TextView tv_protocol_detail,tv_title_content;
    @ViewById
    Button btn_title_back;

    @AfterViews
    void afterViews(){
        tv_title_content.setText("赛鱼网服务协议");
    }

    @Click(R.id.btn_title_back)
    void onClick(View view){
        if(view.getId() == R.id.btn_title_back){
            onBackPressedSupport();
        }
    }


}
