package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_security_set)
public class SecuritySetFragment extends BaseFragment implements CallbackUtils.ResponseCallback{

    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_save;
    @ViewById
    EditText et_count_1,et_count_2;

    @ViewById
    ProgressBar pb_loading;
    private String Mobile;

    public static SecuritySetFragment newInstance(Bundle bundle) {
        SecuritySetFragment_ fragment = new SecuritySetFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("安全配置");
        Bundle bundle = getArguments();
        if (bundle != null) {
            Mobile = bundle.getString("Mobile");
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if(baseRet == null || TextUtils.isEmpty(method)){
            return;
        }
        if(method.equals("SecuritySetFragment_securitySet")){
            BooleanRet ret = (BooleanRet) baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"安全配置成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

        }
    }

    @Click({R.id.btn_title_back,R.id.btn_save})
    void onClick(View view){
        if(view.getId() == R.id.btn_title_back){
            getFragmentManager().popBackStack();
        } else if(view.getId() == R.id.btn_save){

            if(TextUtils.isEmpty(Mobile)){
                //未绑定手机
                Toast.makeText(mContext,"未绑定手机，无法进行安全设置操作",Toast.LENGTH_LONG).show();
                return;
            }

            String limit_1 = et_count_1.getText().toString();
            String limit_2 = et_count_2.getText().toString();
            if(TextUtils.isEmpty(limit_1)){
                Toast.makeText(mContext,"请输入每日下单金额",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(limit_2)){
                Toast.makeText(mContext,"请输入每日下单累计笔数",Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("Mobile",Mobile);
            bundle.putString("orderMoney",limit_1);
            bundle.putString("orderCount",limit_2);
            SecurityCommitFragment securityCommitFragment = SecurityCommitFragment.newInstance(bundle);
            start(securityCommitFragment);

        }
    }

}
