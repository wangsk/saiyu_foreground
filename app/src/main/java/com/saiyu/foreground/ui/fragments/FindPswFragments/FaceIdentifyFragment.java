package com.saiyu.foreground.ui.fragments.FindPswFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_face_identify_layout)
public class FaceIdentifyFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content, tv_account, tv_name, tv_identity;
    @ViewById
    Button btn_title_back, btn_next;
    private String account;
    private AccountInfoNoLoginRet accountInfoNoLoginRet;

    public static FaceIdentifyFragment newInstance(Bundle bundle) {
        FaceIdentifyFragment_ fragment = new FaceIdentifyFragment_();
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
        tv_title_content.setText("找回密码");
        Bundle bundle = getArguments();
        if (bundle != null) {
            account = bundle.getString("account");
            accountInfoNoLoginRet = (AccountInfoNoLoginRet) bundle.getSerializable("AccountInfoNoLoginRet");
        }
        tv_account.setText(account);
        if (accountInfoNoLoginRet != null && accountInfoNoLoginRet.getData() != null) {
            tv_name.setText(accountInfoNoLoginRet.getData().getRealName());
            String idNum = accountInfoNoLoginRet.getData().getIDNum();
            if (!TextUtils.isEmpty(idNum)) {
                try {
                    tv_identity.setText(idNum.substring(0, 3) + "***********" + idNum.substring(idNum.length() - 4));
                } catch (Exception e) {
                    LogUtils.print("身份证号码截取错误");
                }
            }
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
    }

    @Click({R.id.btn_title_back, R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getFragmentManager().popBackStack();
                    break;
                case R.id.btn_next:
                    String url = "";
                    doVerify(url);

//                RevisePswFragment revisePswFragment = RevisePswFragment.newInstance(null);
//                start(revisePswFragment);
                    break;

            }
        }
    }

    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (Utils.hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
//            builder.append(URLEncoder.encode(url));
            builder.append(url);
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            //处理没有安装支付宝的情况
            new AlertDialog.Builder(mContext)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

}
