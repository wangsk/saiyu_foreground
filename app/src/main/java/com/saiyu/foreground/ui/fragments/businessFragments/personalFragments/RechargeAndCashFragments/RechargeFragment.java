package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeAndCashFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.saiyu.foreground.R;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.PayResult;
import com.saiyu.foreground.https.response.RechargeRateRet;
import com.saiyu.foreground.https.response.RewardRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.ButtonUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.Map;

@EFragment(R.layout.fragment_recharge)
public class RechargeFragment extends BaseFragment implements CallbackUtils.ResponseCallback  {
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_title_content,tv_zhifubao,tv_wechat;
    @ViewById
    Button btn_title_back,btn_next;
    @ViewById
    EditText et_count;
    @ViewById
    ImageView iv_wechat,iv_zhifubao;
    @ViewById
    LinearLayout ll_wechat,ll_zhifubao;

    private IWXAPI api;

    private String WechatServiceRate,AliServiceRate;

    public static RechargeFragment newInstance(Bundle bundle) {
        RechargeFragment_ fragment = new RechargeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getRechargeRate("RechargeFragment_getRechargeRate",pb_loading);

    }

    @AfterViews
    void afterView() {
        tv_title_content.setText("我要充值");
        et_count.requestFocus();
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("RechargeFragment_getRechargeRate")){
            RechargeRateRet ret = (RechargeRateRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                AliServiceRate = ret.getData().getAliServiceRate();
                WechatServiceRate = ret.getData().getWechatServiceRate();
            }
        } else if(method.equals("RechargeFragment_submitRecharge")){
            RewardRet ret = (RewardRet)baseRet;
            if(ret.getData() == null){
                return;
            }

            if(iv_wechat.getVisibility() == View.VISIBLE){
                toWXPay(ret.getData());
            } else if(iv_zhifubao.getVisibility() == View.VISIBLE){
                toAliPay(ret.getData());
            }

        }
    }

    @Click({R.id.btn_title_back,R.id.ll_wechat,R.id.ll_zhifubao,R.id.btn_next})
    void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_title_back:
                    getActivity().finish();
                    break;
                case R.id.btn_next:
                    String money = et_count.getText().toString();
                    if(TextUtils.isEmpty(money)){
                        Toast.makeText(mContext,"请输入充值金额",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String payType = "";

                    RewardRet.DataBean dataBean = new RewardRet.DataBean();
                    if(iv_wechat.getVisibility() == View.VISIBLE){
                        payType = "2";
                    } else if(iv_zhifubao.getVisibility() == View.VISIBLE){
                        payType = "1";
                    }
                    ApiRequest.submitRecharge(payType,money,"RechargeFragment_submitRecharge",pb_loading);
                    break;
                case R.id.ll_wechat:
                    if(iv_wechat.getVisibility() == View.VISIBLE){
                        return;
                    } else {
                        iv_wechat.setVisibility(View.VISIBLE);
                        iv_zhifubao.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ll_zhifubao:
                    if(iv_zhifubao.getVisibility() == View.VISIBLE){
                        return;
                    } else {
                        iv_zhifubao.setVisibility(View.VISIBLE);
                        iv_wechat.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    /**
     * 调起微信支付的方法     //这里注意要放在子线程
     **/
    @Background
    void toWXPay(RewardRet.DataBean ret) {
        api = WXAPIFactory.createWXAPI(mContext, ret.getAppid()); //初始化微信api
        api.registerApp(ret.getAppid());
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext,"请先安装微信应用",Toast.LENGTH_SHORT).show();
            return;
        }
        PayReq request = new PayReq();
        //调起微信APP的对象 //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
        request.appId = ret.getAppid();
        request.partnerId = ret.getPartnerid();
        request.prepayId = ret.getPrepayid();
        request.packageValue = ret.getPackage_1();
        request.nonceStr = ret.getNoncestr();
        request.timeStamp = ret.getTimestamp();
        request.sign = ret.getSign();
        api.sendReq(request);//发送调起微信的请求
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    /**
     * 调用支付宝
     **/
    void toAliPay(final RewardRet.DataBean ret) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(ret.getOrderInfo(), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    LogUtils.print("resultInfo================" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
//                     判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @TextChange({R.id.et_count})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        String money = s.toString();
        if(!TextUtils.isEmpty(money)){
            BigDecimal bigDecimal = new BigDecimal(money);
            BigDecimal decimal_wechat = new BigDecimal(WechatServiceRate);
            BigDecimal decimal_w = bigDecimal.multiply(decimal_wechat).setScale(2,BigDecimal.ROUND_HALF_UP);//四舍五入，2.35变成2.4
            tv_wechat.setText(decimal_w+"");
            BigDecimal decimal_zhifubao = new BigDecimal(AliServiceRate);
            BigDecimal decimal_z = bigDecimal.multiply(decimal_zhifubao).setScale(2,BigDecimal.ROUND_HALF_UP);//四舍五入，2.35变成2.4
            tv_zhifubao.setText(decimal_z+"");
        } else {
            tv_wechat.setText("手续费");
            tv_zhifubao.setText("手续费");
        }

    }
}
