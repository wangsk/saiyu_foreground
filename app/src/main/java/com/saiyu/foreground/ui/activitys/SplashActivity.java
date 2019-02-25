package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.frame_base_container_layout)
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean autoLoginFlag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
        String accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
        LogUtils.print("accessToken == " + accessToken);
        if(autoLoginFlag && !TextUtils.isEmpty(accessToken)){
            Intent intentlogin = new Intent(SplashActivity.this,
                    MainActivity_.class);
            SplashActivity.this.startActivity(intentlogin);
            SplashActivity.this.finish();

        } else {
            Bundle bundle_1 = new Bundle();
            Intent intent = new Intent(mContext,ContainerActivity_.class);
            bundle_1.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
            intent.putExtras(bundle_1);
            mContext.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
