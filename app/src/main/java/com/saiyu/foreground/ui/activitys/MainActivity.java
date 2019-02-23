package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.HallFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.MarketFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.PersonalFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragment;
import com.saiyu.foreground.ui.views.BottomBar;
import com.saiyu.foreground.ui.views.BottomBarTab;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import me.yokeyword.fragmentation.SupportFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity  implements BottomBar.OnTabSelectedListener,CallbackUtils.ResponseCallback{

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIVE = 4;

    private BottomBar bottomBar;
    private SupportFragment[] mFragments = new SupportFragment[5];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportFragment firstFragment = findFragment(MarketFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = MarketFragment.newInstance(null);
            mFragments[SECOND] = HallFragment.newInstance(null);
            mFragments[THIRD] = BuyerFragment.newInstance(null);
            mFragments[FOURTH] = SellerFragment.newInstance(null);
            mFragments[FIVE] = PersonalFragment.newInstance(null);
            loadMultipleRootFragment(R.id.main_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH],
                    mFragments[FIVE]);

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(HallFragment.class);
            mFragments[THIRD] = findFragment(BuyerFragment.class);
            mFragments[FOURTH] = findFragment(SellerFragment.class);
            mFragments[FIVE] = findFragment(PersonalFragment.class);
        }
    }

    @AfterViews
    void afterViews(){
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.addItem(new BottomBarTab(this, R.mipmap.ic_launcher, "行情"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_launcher, "大厅"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_launcher, "买家"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_launcher, "买家"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_launcher, "我的"));
        bottomBar.setOnTabSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CallbackUtils.setCallback(this);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if(baseRet == null || TextUtils.isEmpty(method)){
            return;
        }
        if(method.equals("")){

        }
    }

    @Override
    public void onTabSelected(int position, int prePosition) {
        showHideFragment(mFragments[position], mFragments[prePosition]);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        SPUtils.putString("accessToken", "");
        SPUtils.putBoolean(ConstValue.AUTO_LOGIN_FLAG, false);

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

    }
}
