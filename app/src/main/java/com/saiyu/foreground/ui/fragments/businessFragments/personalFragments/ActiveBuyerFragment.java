package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_active_buyer)
public class ActiveBuyerFragment extends BaseFragment implements CallbackUtils.ResponseCallback,CallbackUtils.OnActivityCallBack{
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_agree,btn_close;

    public static ActiveBuyerFragment newInstance(Bundle bundle) {
        ActiveBuyerFragment_ fragment = new ActiveBuyerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnActivityCallBack(this);
    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("点券充值(买家)服务协议");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("ActiveBuyerFragment_activeSeller")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                int type = SPUtils.getInt(ConstValue.MainBottomVisibleType,0);
                switch (type){
                    case 0://此账号未在该设备上登陆过，默认先全部显示，然后请求买卖家激活状态
                        SPUtils.putInt(ConstValue.MainBottomVisibleType,2);//不显示卖家
                        break;
                    case 1://当前账号买家未激活，请求买卖家激活状态
                        SPUtils.putInt(ConstValue.MainBottomVisibleType,3);//买卖家都激活
                        break;
                    case 2://当前账号卖家未激活，请求买卖家激活状态
                        break;
                    case 3://当前账号买卖家都激活，不做处理
                        break;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(ConstValue.MainBottomVisibleType,1);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);

                Toast.makeText(mContext,"激活成功",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_close,R.id.btn_agree})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                case R.id.btn_close:
                    getActivity().finish();
                    break;
                case R.id.btn_agree:
                    int status = SPUtils.getInt(ConstValue.IdentifyInfo,0);
                    if(status == 0){
                        final Bundle bundle = new Bundle();
                        final Intent intent = new Intent(mContext,ContainerActivity_.class);
                        DialogUtils.showDialog(getActivity(), "提示", "激活买家需要补填信息，是否补填信息", "取消", "补填信息", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.InfoWadFragmentTag);
                                intent.putExtras(bundle);
                                mContext.startActivityForResult(intent,ContainerActivity.ActiveBuyerFragmentTag);
                            }
                        });
                    } else if(status == 1){
                        ApiRequest.activeBuyer("ActiveBuyerFragment_activeSeller",pb_loading);
                    }

                    break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("requestCode === " + requestCode);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ContainerActivity.ActiveBuyerFragmentTag:
                    Bundle bundle = data.getExtras();
                    if(bundle == null){
                        return;
                    }
                    int status = bundle.getInt(ConstValue.IdentifyInfo,0);
                    if(status == 1){
                        ApiRequest.activeBuyer("ActiveBuyerFragment_activeSeller",pb_loading);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
