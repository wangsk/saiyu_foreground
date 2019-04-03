package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.SetOrderPswFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragments.SetOrderPswFragment_;
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
public class SetOrderReplaceConfirmFragment extends BaseFragment {
    @ViewById
    TextView tv_title_content;
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

    public static SetOrderReplaceConfirmFragment newInstance(Bundle bundle) {
        SetOrderReplaceConfirmFragment_ fragment = new SetOrderReplaceConfirmFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("客服代理确认");
        Bundle bundle = getArguments();
        if(bundle != null){
            ReserveAccount = bundle.getString("ReserveAccount");
            et_qq.setText(ReserveAccount);
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
                String qq = et_qq_psw.getText().toString();
                String psw = et_qq_psw.getText().toString();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if(!TextUtils.equals(qq,ReserveAccount)){
                    bundle.putString("ReserveAccount",qq);
                }

                bundle.putString("ReservePwd",psw);
                if(province != null){
                    bundle.putString("OftenLoginProvince",province.getName());
                }
                if(city != null){
                    bundle.putString("OftenLoginCity",city.getName());
                }

                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
