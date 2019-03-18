package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.ProgressWebView;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.activity_baseweb)
public class WebFragment extends BaseFragment {

    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back;
    @ViewById
    ProgressWebView baseweb_webview;

    public static WebFragment newInstance(Bundle bundle) {
        WebFragment_ fragment = new WebFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        Bundle bundle = getArguments();
        if(bundle != null){
            String type = bundle.getString("type");
            tv_title_content.setText(type);

            String url = bundle.getString("url");

            baseweb_webview.getSettings().setJavaScriptEnabled(true);

            //设置可以访问文件
            baseweb_webview.getSettings().setAllowFileAccess(true);
            //设置支持缩放
            baseweb_webview.getSettings().setBuiltInZoomControls(true);

            baseweb_webview.getSettings().setAppCacheEnabled(true);
            baseweb_webview.getSettings().setDomStorageEnabled(true);
            baseweb_webview.getSettings().supportMultipleWindows();
            baseweb_webview.getSettings().setAllowContentAccess(true);
            baseweb_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            baseweb_webview.getSettings().setUseWideViewPort(true);
            baseweb_webview.getSettings().setLoadWithOverviewMode(true);
            baseweb_webview.getSettings().setSaveFormData(true);
            baseweb_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            baseweb_webview.getSettings().setLoadsImagesAutomatically(true);

            baseweb_webview.loadUrl(url);
            baseweb_webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    //证书错误
                    handler.proceed();
                    //1.用户选择继续加载
                    // handler.proceed();
                    //2.用户取消
                    //handler.cancel()
                    super.toString();
                }
            });

        }
    }

    @Click(R.id.btn_title_back)
    void onClick(View view){
        if(view.getId() == R.id.btn_title_back){
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baseweb_webview = null;
    }

}
