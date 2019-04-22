package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments.SetOrderReplaceConfirmFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments.SetOrderReplaceConfirmFragment_;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.City;
import com.saiyu.foreground.utils.Province;
import com.saiyu.foreground.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_order_replace_confirm)
public class SubmitPicReplaceConfirmFragment extends BaseFragment implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    @ViewById
    EditText et_qq,et_qq_psw;
    @ViewById
    Spinner sp_province,sp_city;
    private  static Province province = null;
    private static City city = null;
    private static List<Province> list = new ArrayList<Province>();
    private static ArrayAdapter<Province> arrayAdapter1;
    private static ArrayAdapter<City> arrayAdapter2;

    private String ReserveAccount,ReservePwd,OftenLoginProvince,OftenLoginCity;
    private String receiveId;

    public static SubmitPicReplaceConfirmFragment newInstance(Bundle bundle) {
        SubmitPicReplaceConfirmFragment_ fragment = new SubmitPicReplaceConfirmFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SubmitPicReplaceConfirmFragment_applyPicConfirm")) {
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"申请成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("申请验图确认");
        Bundle bundle = getArguments();
        if(bundle != null){
            receiveId = bundle.getString("receiveId");
        }

        list= Utils.parser(getActivity());

        arrayAdapter1 = new ArrayAdapter<Province>(getActivity(),R.layout.item_spinner, R.id.tv_spinner,list);
        arrayAdapter2 = new ArrayAdapter<City>(getActivity(),R.layout.item_spinner, R.id.tv_spinner,list.get(0).getCitys());

        sp_province.setAdapter(arrayAdapter1);
        sp_province.setSelection(0, true);
        sp_city.setAdapter(arrayAdapter2);
        sp_city.setSelection(0, true);

        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = list.get(position);
                arrayAdapter2 = new ArrayAdapter<City>(getActivity(), R.layout.item_spinner, R.id.tv_spinner, list.get(position).getCitys());
                sp_city.setAdapter(arrayAdapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = province.getCitys().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_cancel:
                getActivity().finish();
                break;
            case R.id.btn_confirm:
                String qq = et_qq.getText().toString();
                String psw = et_qq_psw.getText().toString();
                if(TextUtils.isEmpty(qq)){
                    Toast.makeText(mContext,"请输入QQ号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(psw)){
                    Toast.makeText(mContext,"请输入QQ密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(province == null){
                    Toast.makeText(mContext,"请输入常用登陆地",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(city == null){
                    Toast.makeText(mContext,"请输入常用登陆地",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(province.getName())){
                    Toast.makeText(mContext,"请输入常用登陆地",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(city.getName())){
                    Toast.makeText(mContext,"请输入常用登陆地",Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiRequest.applyPicConfirm(receiveId,qq,psw,province.getName(),city.getName(),"SubmitPicReplaceConfirmFragment_applyPicConfirm",pb_loading);

                break;
        }
    }
}
