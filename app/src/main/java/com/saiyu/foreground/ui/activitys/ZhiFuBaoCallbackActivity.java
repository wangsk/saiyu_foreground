package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.utils.GsonUtils;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.EActivity;

import java.util.Map;

@EActivity(R.layout.activity_zhifubao_callback_layout)
public class ZhiFuBaoCallbackActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if(uri != null){
            String sycallbackid = uri.getQueryParameter("sycallbackid");
            //sycallbackid 返回成功的结构：biz_content={"biz_no":"ZM201902283000000242400358519054","passed":"true"}
            //返回失败的结构:biz_content={"biz_no":"ZM201902283000000464600357976834","failed_reason":"请返回重新认证","passed":"false"}
            LogUtils.print("sycallbackid === " + sycallbackid);
            String[] array = sycallbackid.split("=");
            if(array != null && array.length >= 2 && !TextUtils.isEmpty(array[1])){
                LogUtils.print("array[1] ===  " + array[1]);
                Map resultMap = GsonUtils.changeGsonToMaps(array[1]);
                try {
                    Bundle bundle = new Bundle();
                    String flag = (String) resultMap.get("passed");
                    if(!TextUtils.isEmpty(flag)){
                        if(flag.equals("false")){
                            String failed_reason = (String)resultMap.get("failed_reason");
                            bundle.putString("failed_reason",failed_reason);
                        }
                        bundle.putString("Action_Identify_Callback",flag);
                        Intent intent = new Intent();
                        //这个ACTION在FaceIdentifyFragment类里接收
                        intent.setAction("Action_Identify_Callback");
                        intent.putExtras(bundle);
                        sendBroadcast(intent);

                        finish();
                    }

                }catch (Exception e){
                    LogUtils.print("ZhiFuBaoCallbackActivity Exception : " + e.toString());
                }
            }
        }

    }
}
