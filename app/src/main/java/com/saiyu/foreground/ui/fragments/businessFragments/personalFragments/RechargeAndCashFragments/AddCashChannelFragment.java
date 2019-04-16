package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.Utils;
import com.saiyu.foreground.zxinglibrary.encode.CodeCreator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_add_cashchannel)
public class AddCashChannelFragment extends BaseFragment  implements CallbackUtils.ResponseCallback{
    @ViewById
    TextView tv_title_content,tv_realname,tv_id,tv_channel,tv_charge,tv_time,tv_charge_prompt;
    @ViewById
    Button btn_title_back,btn_confirm;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    EditText et_count;
    @ViewById
    LinearLayout ll_wechat,ll_add;
    @ViewById
    ImageView iv_wechat;
    private String RealName,Id,Name,Mobile,ImgUrl;
    private int Type,RiskLevel;

    public static AddCashChannelFragment newInstance(Bundle bundle) {
        AddCashChannelFragment_ fragment = new AddCashChannelFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            RiskLevel = bundle.getInt("RiskLevel");
            Mobile = bundle.getString("Mobile");
            RealName = bundle.getString("RealName");
            Id = bundle.getString("Id");
            Name = bundle.getString("Name");
            Type = bundle.getInt("Type");
            ImgUrl = bundle.getString("ImgUrl");
            String withdrawWayConfigCharge = bundle.getString("withdrawWayConfigCharge");
            String PayDateStr = bundle.getString("PayDateStr");
            String StartMoney = bundle.getString("StartMoney");
            String TopMoney = bundle.getString("TopMoney");
            tv_charge.setText(withdrawWayConfigCharge + "%");
            tv_time.setText(PayDateStr);
            tv_charge_prompt.setText("最低手续费"+StartMoney+"元，封顶手续费"+TopMoney+"元");
            switch (Type){
                case 0://;其他
                    tv_title_content.setText("添加银行卡");
                    tv_channel.setText("卡号");
                    et_count.setHint("请输入银行卡号");
                    ll_wechat.setVisibility(View.GONE);
                    ll_add.setVisibility(View.VISIBLE);
                    break;
                case 1://支付宝
                    tv_title_content.setText("添加支付宝");
                    tv_channel.setText("支付宝账号");
                    et_count.setHint("请输入支付宝账号");
                    ll_wechat.setVisibility(View.GONE);
                    ll_add.setVisibility(View.VISIBLE);
                    break;
                case 2://微信
                    tv_title_content.setText("添加微信");
                    btn_confirm.setText("我已绑定完成");
                    ll_wechat.setVisibility(View.VISIBLE);
                    ll_add.setVisibility(View.GONE);

                    if(!TextUtils.isEmpty(ImgUrl)){
                        final Bitmap bitmap = CodeCreator.createQRCode(ImgUrl, 400, 400, null);
                        if (bitmap != null) {
                            iv_wechat.setImageBitmap(bitmap);
                            iv_wechat.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Utils.loadImage(getActivity(),bitmap);
                                    return true;
                                }
                            });
                        }
                    }

                    break;
            }

            tv_realname.setText(RealName);
            tv_id.setText(Name);
        }

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("AddCashChannelFragment_addCashAccount")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                if(Type == 2){
                    getFragmentManager().popBackStack();
                    return;
                }

                String account = et_count.getText().toString();
                if(TextUtils.isEmpty(account)){
                    Toast.makeText(mContext,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(RiskLevel > 3){
                    //风控等级为4级/5级，随便添加/修改提现渠道
                    ApiRequest.addCashAccount(Id,account,"","","0","AddCashChannelFragment_addCashAccount",pb_loading);
                } else {
                    //风控等级为1 2 3级，修改/添加渠道需要发送一次手机验证码
                    Bundle bundle = new Bundle();
                    bundle.putString("Mobile",Mobile);
                    bundle.putString("account",account);
                    bundle.putString("Id",Id);
                    if(Type == 0){
                        bundle.putString("title","添加银行卡");
                    } else if(Type == 1){
                        bundle.putString("title","添加支付宝");
                    }

                    AddCashChannelWithSendMsgFragment addCashChannelWithSendMsgFragment = AddCashChannelWithSendMsgFragment.newInstance(bundle);
                    start(addCashChannelWithSendMsgFragment);
                }

                break;
        }
    }

}


