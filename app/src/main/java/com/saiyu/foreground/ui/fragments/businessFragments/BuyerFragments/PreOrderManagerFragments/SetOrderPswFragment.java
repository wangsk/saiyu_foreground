package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_set_orderpsw)
public class SetOrderPswFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    @ViewById
    EditText et_release_psw;

    public static SetOrderPswFragment newInstance(Bundle bundle) {
        SetOrderPswFragment_ fragment = new SetOrderPswFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("订单加密");
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_cancel:
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                String psw = et_release_psw.getText().toString();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("orderPsw",psw);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
