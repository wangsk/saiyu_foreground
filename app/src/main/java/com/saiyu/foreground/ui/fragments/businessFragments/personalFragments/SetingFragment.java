package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.ProtocolFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CacheUtil;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_setting)
public class SetingFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content,tv_account;
    @ViewById
    Button btn_title_back;
    @ViewById
    RelativeLayout rl_account,rl_clearcache,rl_protocal,rl_copyright;

    public static SetingFragment newInstance(Bundle bundle) {
        SetingFragment_ fragment = new SetingFragment_();
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
        tv_title_content.setText("设置");
        Bundle bundle = getArguments();
        if(bundle != null){
            String account = bundle.getString("account");
            tv_account.setText(account);
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_unlogin,R.id.rl_clearcache,R.id.rl_protocal,R.id.rl_copyright})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            if(view.getId() == R.id.btn_title_back){
                getActivity().finish();
                return;
            }
            Bundle bundle_3 = new Bundle();
            switch (view.getId()) {
                case R.id.btn_unlogin:
                    ApiRequest.unLogin(getActivity());
                    break;
                case R.id.rl_clearcache:
                    String totalCacheSize = "";
                    try {
                        totalCacheSize = CacheUtil.getTotalCacheSize(mContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (totalCacheSize.equals("0K")) {
                        Toast.makeText(mContext, "您当前没有缓存", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtils.showDialog(getActivity(),"提示", "确定要清理:" + totalCacheSize + "缓存吗?", "取消", "清理", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CacheUtil.clearAllCache(mContext);
                                Toast.makeText(mContext, "清理成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case R.id.rl_protocal:
                    ProtocolFragment protocolFragment =  ProtocolFragment.newInstance(bundle_3);
                    start(protocolFragment);
                    break;
                case R.id.rl_copyright:
                    CopyRightFragment copyRightFragment = CopyRightFragment.newInstance(bundle_3);
                    start(copyRightFragment);
                    break;
            }
        }
    }
}
