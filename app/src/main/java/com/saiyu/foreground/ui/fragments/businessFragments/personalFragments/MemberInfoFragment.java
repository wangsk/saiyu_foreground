package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.FaceStatusRet;
import com.saiyu.foreground.https.response.RealNameStatusRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_mem_info)
public class MemberInfoFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content,tv_account,tv_realname,tv_identity,tv_identify,tv_face;
    @ViewById
    Button btn_title_back;
    @ViewById
    RelativeLayout rl_realname,rl_identity,rl_identify,rl_face;
    private String account,RealName,IDNum,Mobile;

    public static MemberInfoFragment newInstance(Bundle bundle) {
        MemberInfoFragment_ fragment = new MemberInfoFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getSmallAccountInfoLogin("MemberInfoFragment_getSmallAccountInfoLogin",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("会员信息");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("MemberInfoFragment_faceStatus")){
            FaceStatusRet ret = (FaceStatusRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            int status = ret.getData().getFaceAuthStatus();
            switch (status){
                case 0:
                    Toast.makeText(mContext,"您有刷脸认证任务正在处理中，请稍后",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    tv_face.setText("已认证");
                    rl_face.setClickable(false);
                    Toast.makeText(mContext,"您已经刷脸认证通过",Toast.LENGTH_LONG).show();
                    break;
                case 2://认证失败
                case 3://没有认证申请记录
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle.putString("RealName",RealName);
                    bundle.putString("IDNum",IDNum);
                    bundle.putBoolean("isFaceIdentify",true);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.FaceIdentifyFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
            }
        } else if(method.equals("MemberInfoFragment_realNameStatus")){
            RealNameStatusRet ret = (RealNameStatusRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            int status = ret.getData().getRealNameStatus();
            switch (status){
                case 0://正在申请中
                    Toast.makeText(mContext,"您有实名认证任务正在处理中，请稍后",Toast.LENGTH_LONG).show();
                    break;
                case 1://通过
                    tv_identify.setText("已认证");
                    rl_identify.setClickable(false);
                    Toast.makeText(mContext,"您已经刷脸认证通过",Toast.LENGTH_LONG).show();
                    break;
                case 2://失败
                case 3://未申请
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext,ContainerActivity_.class);
                    bundle.putString("RealName",RealName);
                    bundle.putString("IDNum",IDNum);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.UploadIdentityFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
            }
        } else if(method.equals("MemberInfoFragment_getSmallAccountInfoLogin")){
            AccountInfoLoginRet ret = (AccountInfoLoginRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            account = ret.getData().getAccount();
            RealName = ret.getData().getRealName();
            IDNum = ret.getData().getIDNum();
            Mobile = ret.getData().getMobile();
            int IsRealNameAuth = ret.getData().getIsRealNameAuth();
            int IsFaceAuth = ret.getData().getIsFaceAuth();
            tv_account.setText(account);
            if(!TextUtils.isEmpty(RealName)){
                tv_realname.setText(RealName);
                rl_realname.setClickable(false);
            } else {
                tv_realname.setText("补填资料>");
            }
            if(!TextUtils.isEmpty(IDNum)){
                tv_identity.setText(IDNum);
                rl_identity.setClickable(false);
            } else {
                tv_identity.setText("补填资料>");
            }
            if(IsRealNameAuth == 0){
                //未实名
                tv_identify.setText("未认证>");
            } else if(IsRealNameAuth == 1){
                tv_identify.setText("已认证");
                rl_identify.setClickable(false);
            }
            if(IsFaceAuth == 0){
                //未刷脸
                tv_face.setText("未认证>");
            } else if(IsFaceAuth == 1){
                tv_face.setText("已认证");
                rl_face.setClickable(false);
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.rl_realname,R.id.rl_identity,R.id.rl_identify,R.id.rl_face})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            final Bundle bundle = new Bundle();
            final Intent intent = new Intent(mContext,ContainerActivity_.class);
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.rl_realname:
                case R.id.rl_identity:
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.rl_identify:
                    //查询实名认证状态
                    if(TextUtils.isEmpty(RealName) || TextUtils.isEmpty(IDNum)){
                        DialogUtils.showDialog(getActivity(), "提示", "实名认证需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }
                    if(TextUtils.isEmpty(Mobile)){
                        DialogUtils.showDialog(getActivity(), "提示", "实名认证需要绑定手机，是否绑定手机", "取消", "绑定手机", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }
                    ApiRequest.realNameStatus("MemberInfoFragment_realNameStatus",pb_loading);
                    break;
                case R.id.rl_face:
                    if(TextUtils.isEmpty(RealName) || TextUtils.isEmpty(IDNum)){
                        DialogUtils.showDialog(getActivity(), "提示", "刷脸认证需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }
                    if(TextUtils.isEmpty(Mobile)){
                        DialogUtils.showDialog(getActivity(), "提示", "刷脸认证需要绑定手机，是否绑定手机", "取消", "绑定手机", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.BindPhoneStatusFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        });
                        return;
                    }
                    //查询刷脸认证状态
                    ApiRequest.faceStatus("MemberInfoFragment_faceStatus",pb_loading);

                    break;
            }
        }
    }
}
