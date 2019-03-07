package com.saiyu.foreground.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.saiyu.foreground.utils.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx3e154e50aa6775d4");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.print("onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            popRxDialog(resp.errCode);
        }
    }

    private void popRxDialog(int errCode) {
        if (errCode == 0) {
            LogUtils.print("成功, errCode = " + errCode);
//            Intent intent = new Intent();
//            intent.setAction("RewardSucc");
//            sendBroadcast(intent);
        } else if (errCode == -2) {
            LogUtils.print("取消, errCode = " + errCode);
        } else {
            LogUtils.print("失败, errCode = " + errCode);
        }

    }

}