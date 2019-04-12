package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.calenderview.SelectDateDialog;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.MyToast;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.TimeParseUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

@EFragment(R.layout.fragment_set_onlinetime)
public class SetOrderEndTimeFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content,tv_onlingtime,tv_time_prompt,tv_label_tima;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    private String orderEndTime;

    public static SetOrderEndTimeFragment newInstance(Bundle bundle) {
        SetOrderEndTimeFragment_ fragment = new SetOrderEndTimeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("到期时间");
        tv_label_tima.setText("设置到期时间:");
        tv_onlingtime.setText("请输入该订单到期时间");
        tv_time_prompt.setText("设置好订单到期时间，该订单到期前30分钟下架并退款，发生充值的本项自动失效！");

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
                bundle.putString("orderEndTime","");
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(orderEndTime)){
                    Toast.makeText(mContext,"请设置到期时间!",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("orderEndTime",orderEndTime);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.tv_onlingtime:
                SelectDateDialog selectDateDialog = new SelectDateDialog(getActivity(), TimeParseUtils.DateToCalendar(new Date(System.currentTimeMillis())));
                selectDateDialog.setDialogOnClickListener(new SelectDateDialog.DialogOnClickListener() {

                    @Override
                    public void confirmedDo(Date date) {
                        if (date == null) {
                            return;
                        }

                        //公历
                        orderEndTime = TimeParseUtils.getTime(date);
                        LogUtils.print("orderEndTime == " + orderEndTime);
                        tv_onlingtime.setText(orderEndTime);
                    }
                });
                selectDateDialog.show();

                break;
        }
    }
}
