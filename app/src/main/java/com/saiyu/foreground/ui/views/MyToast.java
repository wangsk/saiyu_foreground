package com.saiyu.foreground.ui.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;

public class MyToast extends Toast {

    private String text,text_2;
    private Activity activity;

    public MyToast(Activity activity,String... textArgs) {
        super(activity);
        this.activity = activity;
        if(textArgs.length == 1){
            this.text = textArgs[0];
        } else if(textArgs.length == 2){
            this.text = textArgs[0];
            this.text_2 = textArgs[1];
        }

        //根布局参数
        LinearLayout.LayoutParams layoutParamsRoot = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsRoot.gravity= Gravity.CENTER;

        LinearLayout linearLayout = new LinearLayout(App.getApp());
        linearLayout.setLayoutParams(layoutParamsRoot);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(App.getApp().getResources().getColor(R.color.line));

        if(!TextUtils.isEmpty(text_2)){
            TextView textView_2 = new TextView(App.getApp());
            textView_2.setGravity(Gravity.CENTER_HORIZONTAL);
            textView_2.setTextColor(Color.BLUE);
            textView_2.setText(text_2);
            textView_2.setPadding(70, 50, 70, 0);
            linearLayout.addView(textView_2);
        }
        TextView textView = new TextView(App.getApp());
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setPadding(70, 50, 70, 50);
        linearLayout.addView(textView);

        this.setGravity(Gravity.CENTER, 0, 0);
        this.setView(linearLayout);
        this.setDuration(Toast.LENGTH_SHORT);
        //end
    }

    public static MyToast newInstance(Activity activity, String... text) {
        MyToast fragment = new MyToast(activity,text);
        return fragment;
    }


    @Override
    public void show() {
        super.show();
        View view = this.getView();
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View v) {
                LogUtils.print("onViewDetachedFromWindow");
                Utils.backgroundAlpha(activity,1f);
            }

            @Override
            public void onViewAttachedToWindow(View v) {
                LogUtils.print("onViewAttachedToWindow");
                Utils.backgroundAlpha(activity,0.7f);
            }

        });
    }
}
