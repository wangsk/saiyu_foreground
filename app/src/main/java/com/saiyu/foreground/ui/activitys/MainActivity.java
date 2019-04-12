package com.saiyu.foreground.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.ActiveStatusRet;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragment_;
import com.saiyu.foreground.ui.fragments.businessFragments.HallFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.HallFragment_;
import com.saiyu.foreground.ui.fragments.businessFragments.MarketFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.MarketFragment_;
import com.saiyu.foreground.ui.fragments.businessFragments.PersonalFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.PersonalFragment_;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragment_;
import com.saiyu.foreground.ui.views.BottomBar;
import com.saiyu.foreground.ui.views.BottomBarTab;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import me.yokeyword.fragmentation.SupportFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements BottomBar.OnTabSelectedListener,CallbackUtils.OnBottomSelectListener{

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIVE = 4;

    private BottomBar bottomBar;
    private SupportFragment[] mFragments = new SupportFragment[5];

    @ViewById
    ProgressBar pb_loading;
    private int curPosition;
    private String accessToken;
    private boolean autologinflag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 补充说明:
         * 我们需要判断:该意图是打开一个新的任务,还是将后台的应用给提到前台来.
         * 若是要将应用提到前台来直接将这个Activity结束掉,然后显示出来的Activity就是之前被最小化的Activity.
         * 因为点击图标的意图会将新启动的Activity置于顶端,而顶端的下面的Activity就是之前被最小化的Activity.
         * 此时结束掉新启动的Activity,就可以让之前被最小化的Activity 显示出来了.
         *
         * https://blog.csdn.net/busjb/article/details/40891239  感谢博主分享！
         * */

        //添加这个if语句
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SupportFragment firstFragment = findFragment(MarketFragment_.class);
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
            mFragments[SECOND] = findFragment(HallFragment_.class);
            mFragments[THIRD] = findFragment(SellerFragment_.class);
            mFragments[FOURTH] = findFragment(BuyerFragment_.class);
            mFragments[FIVE] = findFragment(PersonalFragment_.class);
        }
    }

    @AfterViews
    void afterViews(){
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.addItem(new BottomBarTab(this, R.mipmap.icon_hq, "行情"))
                .addItem(new BottomBarTab(this, R.mipmap.index, "大厅"))
                .addItem(new BottomBarTab(this, R.mipmap.icon_maij, "卖家"))
                .addItem(new BottomBarTab(this, R.mipmap.icon_mj, "买家"))
                .addItem(new BottomBarTab(this, R.mipmap.icon_w, "我的"));
        bottomBar.setOnTabSelectedListener(this);
        bottomBar.setCurrentItem(1);//默认选择大厅

    }

    @Override
    protected void onResume() {
        super.onResume();
        CallbackUtils.setOnBottomSelectListener(this);

        accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
        autologinflag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
        if(autologinflag && !TextUtils.isEmpty(accessToken)){

            //产品要求，一旦买家或者卖家有一个被激活，主页面不再显示卖家或者买家（产品的意思是一个用户可能很长时间只是买家或者卖家，那么就把他另外一个身份页面（买家/卖家）隐藏）
            int type = SPUtils.getInt(ConstValue.MainBottomVisibleType,0);
            switch (type){
                case 0://此账号未在该设备上登陆过，默认先全部显示，然后请求买卖家激活状态
                case 3://当前账号买卖家都激活，不做处理
                    if(getBottomBar().checkStatus(3) == View.GONE){
                        getBottomBar().show(3);
                    }
                    if(getBottomBar().checkStatus(2) == View.GONE){
                        getBottomBar().show(2);
                    }
                    break;
                case 1://当前账号买家未激活，请求买卖家激活状态
                    bottomBar.hide(3);
                    if(getBottomBar().checkStatus(2) == View.GONE){
                        getBottomBar().show(2);
                    }
                    break;
                case 2://当前账号卖家未激活，请求买卖家激活状态
                    bottomBar.hide(2);
                    if(getBottomBar().checkStatus(3) == View.GONE){
                        getBottomBar().show(3);
                    }
                    break;
            }

        } else {
            if(getBottomBar().checkStatus(3) == View.GONE){
                getBottomBar().show(3);
            }
            if(getBottomBar().checkStatus(2) == View.GONE){
                getBottomBar().show(2);
            }
        }

    }

    @Override
    public void onTabSelected(int position, int prePosition) {
        //用户点击买家/卖家/我的页面的时候，必须是登录状态，否则弹出登录页面
        switch (position){
            case 2://卖家
            case 3://买家
            case 4://我的
                accessToken = SPUtils.getString(ConstValue.ACCESS_TOKEN,"");
                autologinflag = SPUtils.getBoolean(ConstValue.AUTO_LOGIN_FLAG,false);
                if(autologinflag && !TextUtils.isEmpty(accessToken)){
                    int type = SPUtils.getInt(ConstValue.MainBottomVisibleType,0);
                    LogUtils.print("type === " + type);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this,ContainerActivity_.class);
                    switch (type){
                        case 0://此账号未在该设备上登陆过，默认先全部显示，然后请求买卖家激活状态
                            if(position == 2){//点击卖家
                                bottomBar.setCurrentItem(prePosition);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveSellerFragmentTag);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivityForResult(intent,ContainerActivity.ActiveSellerFragmentTag);
                            } else if(position == 3){//点击买家
                                bottomBar.setCurrentItem(prePosition);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveBuyerFragmentTag);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivityForResult(intent,ContainerActivity.ActiveBuyerFragmentTag);
                            } else {
                                showHideFragment(mFragments[position], mFragments[prePosition]);
                            }
                            break;
                        case 1://当前账号买家未激活，请求买卖家激活状态
                            if(position == 3){//点击买家
                                bottomBar.setCurrentItem(prePosition);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveBuyerFragmentTag);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivityForResult(intent,ContainerActivity.ActiveBuyerFragmentTag);
                            } else {
                                showHideFragment(mFragments[position], mFragments[prePosition]);
                            }
                            break;
                        case 2://当前账号卖家未激活，请求买卖家激活状态
                            if(position == 2){//点击卖家
                                bottomBar.setCurrentItem(prePosition);
                                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ActiveSellerFragmentTag);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivityForResult(intent,ContainerActivity.ActiveSellerFragmentTag);
                            } else {
                                showHideFragment(mFragments[position], mFragments[prePosition]);
                            }
                            break;
                        case 3://当前账号买卖家都激活，不做处理
                            showHideFragment(mFragments[position], mFragments[prePosition]);
                            break;
                        default:
                            break;
                    }
                } else {
                    curPosition = position;
                    LogUtils.print("curPosition === " + curPosition);
                    bottomBar.setCurrentItem(prePosition);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this,ContainerActivity_.class);
                    bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.LoginFragmentTag);
                    intent.putExtras(bundle);
                    MainActivity.this.startActivityForResult(intent,ContainerActivity.LoginFragmentTag);
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
        LogUtils.print("setOnBottomSelectListener === " + position);
        bottomBar.setCurrentItem(position);
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    @Override
    public void onBackPressedSupport() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("requestCode === " + requestCode);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            int status;
            switch (requestCode){
                case ContainerActivity.ActiveBuyerFragmentTag:
                    if(bundle == null){
                        return;
                    }
                    status = bundle.getInt(ConstValue.MainBottomVisibleType,0);
                    if(status == 1){
                        bottomBar.setCurrentItem(3);
                    }

                    break;
                case ContainerActivity.ActiveSellerFragmentTag:
                    if(bundle == null){
                        return;
                    }
                    status = bundle.getInt(ConstValue.MainBottomVisibleType,0);
                    if(status == 1){
                        bottomBar.setCurrentItem(2);
                    }
                    break;
                case ContainerActivity.LoginFragmentTag:
                    if(bundle == null){
                        return;
                    }
                    boolean isLogin = bundle.getBoolean("isLogin",false);
                    if(isLogin){
                        LogUtils.print("curPosition === " + curPosition);
                        bottomBar.setCurrentItem(curPosition);
                    }
                    break;
                default:
                    break;
            }
        }

    }

}
