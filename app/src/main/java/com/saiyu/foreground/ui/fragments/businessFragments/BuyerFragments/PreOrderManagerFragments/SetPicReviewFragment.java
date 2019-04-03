package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_set_picreview)
public class SetPicReviewFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;

    public static SetPicReviewFragment newInstance(Bundle bundle) {
        SetPicReviewFragment_ fragment = new SetPicReviewFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("支持验图确认");
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel})
    void onClick(View view) {
        if(view.getId() == R.id.btn_title_back){
            getActivity().finish();
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.btn_cancel:
                bundle.putString("IsPicConfirm","0");
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                bundle.putString("IsPicConfirm","1");
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
