package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.calenderview.SelectTimeDialog;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.TimeParseUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_set_onlinetime)
public class SetOnlineTimeFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content,tv_onlingtime;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    private String onLineStartTime,onLineStopTime;

    public static SetOnlineTimeFragment newInstance(Bundle bundle) {
        SetOnlineTimeFragment_ fragment = new SetOnlineTimeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("设置在线时间");
        Bundle bundle = getArguments();
        if(bundle != null){
            onLineStartTime = bundle.getString("onLineStartTime");
            onLineStopTime = bundle.getString("onLineStopTime");
            if (!TextUtils.isEmpty(onLineStartTime) && !TextUtils.isEmpty(onLineStopTime)) {
                tv_onlingtime.setText(onLineStartTime + "-" + onLineStopTime);
            }

        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.tv_onlingtime,R.id.btn_cancel})
    void onClick(View view) {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_cancel:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("onLineStartTime","");
                bundle.putString("onLineStopTime","");
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("onLineStartTime",onLineStartTime);
                bundle.putString("onLineStopTime",onLineStopTime);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.tv_onlingtime:
                SelectTimeDialog selectTimeDialog = new SelectTimeDialog(mContext,TimeParseUtils.DateToCalendar(new Date(System.currentTimeMillis())));
                selectTimeDialog.setDialogOnClickListener(new OnListCallbackListener() {
                    @Override
                    public void setOnListCallbackListener(List<String> callbackList) {
                        if(callbackList == null){
                            tv_onlingtime.setText("请输入在线时间");
                            return;
                        }
                        if(callbackList.size() == 4){
                            LogUtils.print(callbackList.get(0)+":"+callbackList.get(1)+callbackList.get(2)+":"+callbackList.get(3));
                            onLineStartTime = callbackList.get(0)+":"+callbackList.get(1);
                            onLineStopTime = callbackList.get(2)+":"+callbackList.get(3);
                            tv_onlingtime.setText(onLineStartTime + "-" + onLineStopTime);
                        }
                    }
                });
                selectTimeDialog.show();

                break;
        }
    }
}
