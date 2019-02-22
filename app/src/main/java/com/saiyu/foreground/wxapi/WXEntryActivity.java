package com.saiyu.foreground.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiService;
import com.saiyu.foreground.https.RequestHeaderInterceptor;
import com.saiyu.foreground.https.SecretJsonConverterFactory;
import com.saiyu.foreground.utils.LogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by jishu on 2017/11/7.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxapi = WXAPIFactory.createWXAPI(this, ConstValue.WECHAT_APP_ID, false);
        wxapi.registerApp(ConstValue.WECHAT_APP_ID);
        wxapi.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
        finish();
    }
    //微信授权成功回调
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode){
            //成功
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if(sendResp!=null){
                    //拿着code换取access_token
                    String code = sendResp.code;
                    LogUtils.print("WXEntryActivity code =================== " + code);
                    getAccessToken(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    //拿着code去微信官方换取access_token(code只能用一次)
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ConstValue.WECHAT_APP_ID+"&secret="+ConstValue.WECHAT_APP_SECRET+"&code="+code+"&grant_type=authorization_code";

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.print(" WECHATresponse===================" + response);
                if(TextUtils.isEmpty(response)){
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    String wechat_access_token = (String) obj.get("access_token");
                    String wechat_openid = (String) obj.get("openid");
                    String wechat_unionid = (String) obj.get("unionid");
                    LogUtils.print("WXEntryActivity_wechat_access_token ===================" + wechat_access_token);

                    getUserInfo(wechat_access_token, wechat_openid,wechat_unionid);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.print(" Exception===================" + e.toString());
                }
            }
        });
    }

    //因为access_token是有时效性的，所以需要用access_token去微信官方获取wechat_unionid以及其他用户信息
    private void getUserInfo(String wechat_access_token, String wechat_openid,final String wechat_unionid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+wechat_access_token+"&openid="+wechat_openid;

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.print(" WECHATresponse===================" + response);
                if(TextUtils.isEmpty(response)){
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    String wechat_img = (String) obj.get("headimgurl");
                    String wechat_nickname = (String) obj.get("nickname");

                    Bundle bundle = new Bundle();
                    bundle.putString("wechat_unionid",wechat_unionid);
                    bundle.putString("wechat_img",wechat_img);
                    bundle.putString("wechat_nickname",wechat_nickname);

                    Intent intent = new Intent();
                    intent.setAction("weChat_loginFragment");
                    intent.putExtras(bundle);
                    sendBroadcast(intent);

                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.print(" Exception===================" + e.toString());
                }
            }
        });

    }

    //用于访问其他网络地址(比如微信)
    private static ApiService initRetrofit(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient apiClientClient = builder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new RequestHeaderInterceptor())
                .build();

        Retrofit apiRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SecretJsonConverterFactory.create())
                .client(apiClientClient)
                .build();

        ApiService mApiService = apiRetrofit.create(ApiService.class);
        return mApiService;
    }

}
