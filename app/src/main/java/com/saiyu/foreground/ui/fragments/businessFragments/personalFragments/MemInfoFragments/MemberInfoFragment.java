package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.MemInfoFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.saiyu.foreground.ui.fragments.FaceIdentifyFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;

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
    @ViewById
    ImageView iv_jiantou_realname,iv_jiantou_identity,iv_jiantou_identify,iv_jiantou_face;
    private String account,RealName,IDNum,Mobile;
    private int IsRealNameAuth = 0,IsFaceAuth = 0;
    private boolean IsModify;

    public static MemberInfoFragment newInstance(Bundle bundle) {
        MemberInfoFragment_ fragment = new MemberInfoFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        //页面显示时即时获取页面信息
        ApiRequest.getSmallAccountInfoLogin("MemberInfoFragment_getSmallAccountInfoLogin",pb_loading);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("会员信息");
        Bundle bundle = getArguments();
        if(bundle != null){
            account = bundle.getString("account");
            RealName = bundle.getString("RealName");
            IDNum = bundle.getString("IDNum");
            Mobile = bundle.getString("Mobile");
            IsFaceAuth = bundle.getInt("IsFaceAuth");
            IsRealNameAuth = bundle.getInt("IsRealNameAuth");
            IsModify = bundle.getBoolean("IsModify");

            tv_account.setText(account);
            if(!TextUtils.isEmpty(RealName)){
                tv_realname.setText(RealName);
                iv_jiantou_realname.setVisibility(View.GONE);
            } else {
                tv_realname.setText("补填资料");
                iv_jiantou_realname.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(IDNum)){
                tv_identity.setText(IDNum);
                iv_jiantou_identity.setVisibility(View.GONE);
            } else {
                tv_identity.setText("补填资料");
                iv_jiantou_identity.setVisibility(View.VISIBLE);
            }
            if(IsRealNameAuth == 0){
                tv_identify.setText("认证中");
                iv_jiantou_identify.setVisibility(View.GONE);
            } else if(IsRealNameAuth == 1){
                tv_identify.setText("已认证");
                iv_jiantou_identify.setVisibility(View.GONE);
            } else if(IsRealNameAuth == 2){
                tv_identify.setText("认证失败");
                iv_jiantou_identify.setVisibility(View.VISIBLE);
            } else if(IsRealNameAuth == 3){
                tv_identify.setText("未认证");
                iv_jiantou_identify.setVisibility(View.VISIBLE);
            }
            if(IsFaceAuth == 0){
                tv_face.setText("认证中");
                iv_jiantou_face.setVisibility(View.GONE);
            } else if(IsFaceAuth == 1){
                tv_face.setText("已认证");
                iv_jiantou_face.setVisibility(View.GONE);
            } else if(IsFaceAuth == 2){
                tv_face.setText("认证失败");
                iv_jiantou_face.setVisibility(View.VISIBLE);
            } else if(IsFaceAuth == 3){
                tv_face.setText("未认证");
                iv_jiantou_face.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("MemberInfoFragment_getSmallAccountInfoLogin")){
            AccountInfoLoginRet ret = (AccountInfoLoginRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            account = ret.getData().getAccount();
            RealName = ret.getData().getRealName();
            IDNum = ret.getData().getIDNum();
            Mobile = ret.getData().getMobile();
            IsRealNameAuth = ret.getData().getRealNameStatus();
            IsFaceAuth = ret.getData().getFaceAuthStatus();
            IsModify = ret.getData().isModify();
            tv_account.setText(account);
            if(!TextUtils.isEmpty(RealName)){
                tv_realname.setText(RealName);
                iv_jiantou_realname.setVisibility(View.GONE);
            } else {
                tv_realname.setText("补填资料");
                iv_jiantou_realname.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(IDNum)){
                tv_identity.setText(IDNum);
                iv_jiantou_identity.setVisibility(View.GONE);
            } else {
                tv_identity.setText("补填资料");
                iv_jiantou_identity.setVisibility(View.VISIBLE);
            }
            if(IsRealNameAuth == 0){
                tv_identify.setText("认证中");
                iv_jiantou_identify.setVisibility(View.GONE);
            } else if(IsRealNameAuth == 1){
                tv_identify.setText("已认证");
                iv_jiantou_identify.setVisibility(View.GONE);
            } else if(IsRealNameAuth == 2){
                tv_identify.setText("认证失败");
                iv_jiantou_identify.setVisibility(View.VISIBLE);
            } else if(IsRealNameAuth == 3){
                tv_identify.setText("未认证");
                iv_jiantou_identify.setVisibility(View.VISIBLE);
            }
            if(IsFaceAuth == 0){
                tv_face.setText("认证中");
                iv_jiantou_face.setVisibility(View.GONE);
            } else if(IsFaceAuth == 1){
                tv_face.setText("已认证");
                iv_jiantou_face.setVisibility(View.GONE);
            } else if(IsFaceAuth == 2){
                tv_face.setText("认证失败");
                iv_jiantou_face.setVisibility(View.VISIBLE);
            } else if(IsFaceAuth == 3){
                tv_face.setText("未认证");
                iv_jiantou_face.setVisibility(View.VISIBLE);
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
                    if(!TextUtils.isEmpty(RealName)){
                       Toast.makeText(mContext,"您的资料已补填完整",Toast.LENGTH_SHORT).show();
                       return;
                    }
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.rl_identity:
                    if(!TextUtils.isEmpty(IDNum)){
                        Toast.makeText(mContext,"您的资料已补填完整",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.rl_identify:
                    if(IsRealNameAuth == 1){
                        Toast.makeText(mContext,"您已经通过实名认证",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(IsRealNameAuth == 0){
                        Toast.makeText(mContext,"实名认证中",Toast.LENGTH_SHORT).show();
                        return;
                    }
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
                    bundle.putString("RealName",RealName);
                    bundle.putString("IDNum",IDNum);
                    bundle.putBoolean("IsModify",IsModify);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.UploadIdentityFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    break;
                case R.id.rl_face:
                    if(IsFaceAuth == 1){
                        Toast.makeText(mContext,"您已经通过刷脸认证",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(IsFaceAuth == 0){
                        Toast.makeText(mContext,"刷脸认证中",Toast.LENGTH_SHORT).show();
                        return;
                    }
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

                    bundle.putString("RealName",RealName);
                    bundle.putString("IDNum",IDNum);
                    bundle.putBoolean("IsModify",IsModify);
                    bundle.putInt(FaceIdentifyFragment.FaceIdentifyFunctionType,0);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.FaceIdentifyFragmentTag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                    break;
            }
        }
    }
}
