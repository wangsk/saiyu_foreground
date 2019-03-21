package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SetFragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.AppVersionRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SdLocal;
import com.saiyu.foreground.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Request;

@EFragment(R.layout.fragment_copy_right)
public class CopyRightFragment extends BaseFragment implements CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content,tv_version;
    @ViewById
    Button btn_title_back;
    @ViewById
    RelativeLayout rl_checkupdate;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    ImageView iv_newversion;
    private String downloadUrl;
    private int serverVersion,curVersion;

    public static CopyRightFragment newInstance(Bundle bundle) {
        CopyRightFragment_ fragment = new CopyRightFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getAppVersion("CopyRightFragment_getAppVersion",pb_loading);

    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("版权信息");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("CopyRightFragment_getAppVersion")){
            final AppVersionRet ret = (AppVersionRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult() && ret.getData().getSysTerminalEntity() != null && !TextUtils.isEmpty(ret.getData().getSysTerminalEntity().getVersion())){

                serverVersion = Utils.getintVerName(ret.getData().getSysTerminalEntity().getVersion());
                curVersion = Utils.getintVerName(mContext.getResources().getString(R.string.app_versionName));
                LogUtils.print("服务器版本号 ： " + serverVersion + "  本地版本号 : " + curVersion);
                if(serverVersion > curVersion){
                    tv_version.setText("有新版本更新");
                    iv_newversion.setVisibility(View.VISIBLE);
                    downloadUrl = ret.getData().getSysTerminalEntity().getLinkUrl();
                } else {
                    iv_newversion.setVisibility(View.GONE);
                    tv_version.setText("已是最新版");
                }
            }
        }
    }

    private static void checkPermission(final String url) {
        RxPermissions rxPermissions = new RxPermissions(mContext);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            downloadAndInstallAPK(url);
                        } else {
                            Toast.makeText(mContext,"请开启存储权限",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    protected static void downloadAndInstallAPK(String versonurl) {
        //下载的apk的文件
        if (TextUtils.isEmpty(versonurl)) {
            Toast.makeText(mContext,"服务器忙请稍后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpUtils.get().url(versonurl).build().execute(new FileCallBack(SdLocal.getDownloadFolder(mContext), "syforeground.apk") {
            @Override
            public void onError(Call call, Exception e, int i) {
                Toast.makeText(mContext,"服务器忙请稍后重试",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // 下载失败时，
//                if (dialogProgress.isShowing()) {
//                    dialogProgress.dismiss();
//                }
            }

            @Override
            public void onResponse(File file, int i) {
//                dialogProgress.dismiss();
                File newApk = file;
                LogUtils.print("apkqqwer" + "下载的地址=?????" + file.toString());
                installApk(newApk);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                String number = progress * 100 + "";
                number = number.substring(0, number.indexOf("."));
                LogUtils.print("下载进度 ： " + Integer.valueOf(number));
//                bnp.setProgress(Integer.valueOf(number));
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
            }
        });
    }

    public static void installApk(File newApk) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.saiyu.foreground.fileprovider", newApk);//在AndroidManifest中的android:authorities值
            LogUtils.print("contentUri=================" + apkUri.toString());
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(newApk), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(install);
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.print("killProcess----------------------------");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }, 500);
        } catch (Exception e) {
        }
    }

    @Click({R.id.btn_title_back,R.id.rl_checkupdate})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.rl_checkupdate:
                if(serverVersion <= curVersion){
                    return;
                }
                DialogUtils.showDialog(getActivity(), "提示", "有新版本更新，请下载体验", "取消", "体验", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.print("下载地址 ： " + downloadUrl);
                        if (Patterns.WEB_URL.matcher(downloadUrl).matches()) {
                            //符合标准
                            checkPermission(downloadUrl);
                        } else{
                            //不符合标准
                            Toast.makeText(mContext,"下载地址不合法",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
        }
    }
}
