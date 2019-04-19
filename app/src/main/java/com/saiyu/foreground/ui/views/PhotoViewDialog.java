package com.saiyu.foreground.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;

public class PhotoViewDialog extends Dialog {
    private Context mContext;
    private String mUrl;
    public PhotoViewDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PhotoViewDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected PhotoViewDialog(Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_photoview_layout, null);
        setContentView(view);

        setCanceledOnTouchOutside(true);

//        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
//        Point point = new Point();
//        display.getSize(point);
//
//        Window window = getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes(); //获取当前对话框的参数值
//        layoutParams.width = (int) (point.x * 1); //宽度设置为屏幕宽度的0.5
//        layoutParams.height = (int) (point.y * 0.9); //高度设置为屏幕高度的0.5
//        window.setAttributes(layoutParams);

        initView();


    }

    private void initView() {

        PhotoView photoView = (PhotoView)findViewById(R.id.pv_dialog);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowing()){
                    dismiss();
                }
            }
        });
        ImageView iv_close = (ImageView)findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowing()){
                    dismiss();
                }
            }
        });
        if(!TextUtils.isEmpty(mUrl)){
            Glide.with(App.getApp())
                    .load(mUrl)
                    .error(R.mipmap.ic_launcher)
                    .into(photoView);
        }
    }
}
