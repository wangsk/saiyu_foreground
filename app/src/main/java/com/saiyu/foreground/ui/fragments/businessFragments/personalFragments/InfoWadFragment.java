package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_wad_info)
public class InfoWadFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    EditText et_identity,et_realname;
    private String realName,identity;
    private boolean flag = true;

    public static InfoWadFragment newInstance(Bundle bundle) {
        InfoWadFragment_ fragment = new InfoWadFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);

    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("资料补填");
        Bundle bundle = getArguments();
        if(bundle != null){
            realName = bundle.getString("RealName");
            identity = bundle.getString("IDNum");
            if(!TextUtils.isEmpty(realName)&& !TextUtils.isEmpty(identity)){
                et_identity.setText(identity);
                et_realname.setText(realName);
                et_realname.setClickable(false);
                et_realname.setFocusable(false);
                et_identity.setClickable(false);
                et_identity.setFocusable(false);
                flag = false;
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("InfoWadFragment_wadInfo")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"资料补填成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();

                    break;
                case R.id.btn_confirm:
                    if(!flag){
                        Toast.makeText(mContext,"信息已补填",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    realName = et_realname.getText().toString();
                    identity = et_identity.getText().toString();
                    if(TextUtils.isEmpty(realName)){
                        Toast.makeText(mContext,"请输入真实姓名",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(identity)){
                        Toast.makeText(mContext,"请输入身份证号",Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (!realName.matches(ConstValue.nameCheck)) {
//                        Toast.makeText(mContext,"请输入汉字",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (!identity.matches(ConstValue.identityCheck)) {
//                        Toast.makeText(mContext,"请输入正确的身份证号",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    ApiRequest.wadInfo(realName,identity,"InfoWadFragment_wadInfo",pb_loading);
                    break;
            }
        }
    }
}
