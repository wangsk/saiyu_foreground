package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;

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
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import me.yokeyword.fragmentation.SupportFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity  implements BottomBar.OnTabSelectedListener,CallbackUtils.ResponseCallback,CallbackUtils.OnBottomSelectListener{

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
            mFragments[THIRD] = SellerFragment.newInstance(null);
            mFragments[FOURTH] = BuyerFragment.newInstance(null);
            mFragments[FIVE] = PersonalFragment.newInstance(null);
            loadMultipleRootFragment(R.id.main_container, SECOND,
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
            mFragments[THIRD] = findFragment(SellerFragment.class);
            mFragments[FOURTH] = findFragment(BuyerFragment.class);
            mFragments[FIVE] = findFragment(PersonalFragment.class);
        }
    }

    @AfterViews
    void afterViews(){
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.addItem(new BottomBarTab(this, R.mipmap.mem_info_icon, "行情"))
                .addItem(new BottomBarTab(this, R.mipmap.mem_info_icon, "大厅"))
                .addItem(new BottomBarTab(this, R.mipmap.mem_info_icon, "卖家"))
                .addItem(new BottomBarTab(this, R.mipmap.mem_info_icon, "买家"))
                .addItem(new BottomBarTab(this, R.mipmap.mem_info_icon, "我的"));
        bottomBar.setOnTabSelectedListener(this);
        bottomBar.setCurrentItem(1);//默认选择大厅

        //产品要求，一旦买家或者卖家有一个被激活，主页面不再显示卖家或者买家（产品的意思是一个用户可能很长时间只是买家或者卖家，那么就把他另外一个身份页面（买家/卖家）隐藏）
        int type = SPUtils.getInt(ConstValue.MainBottomVisibleType,0);
        switch (type){
            case 0://全部显示
                break;
            case 1://不显示买家
                bottomBar.hide(3);
                break;
            case 2://不显示卖家
                bottomBar.hide(2);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnBottomSelectListener(this);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if(baseRet == null || TextUtils.isEmpty(method)){
            return;
        }
        if(method.equals("")){
            //该页面暂时没有网络请求，所以这个接口实现暂时没用
        }
    }

    @Override
    public void onTabSelected(int position, int prePosition) {
        //用户点击买家/卖家/我的页面的时候，必须是登录状态，否则弹出登录页面
        switch (position){
            case 2://买家
            case 3://卖家
            case 4://我的
                String accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
                boolean autologinflag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
                if(autologinflag && !TextUtils.isEmpty(accessToken)){
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                } else {
                    bottomBar.setCurrentItem(prePosition);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
                    intent.putExtras(bundle);
                    MainActivity.this.startActivity(intent);
                    //设置activity从底部弹出
                    //overridePendingTransition(R.anim.from_bottom_to_top, R.anim.from_top_to_bottom);
                }
                break;
                default:
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                    break;
        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void setOnBottomSelectListener(int position) {
        bottomBar.setCurrentItem(position);
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    //按返回键不销毁activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
