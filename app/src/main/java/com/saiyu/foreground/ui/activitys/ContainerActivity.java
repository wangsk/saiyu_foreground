package com.saiyu.foreground.ui.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.FindPswFragments.ForgotPswFragment;
import com.saiyu.foreground.ui.fragments.LoginFragment;
import com.saiyu.foreground.ui.fragments.ProtocolFragment;
import com.saiyu.foreground.ui.fragments.RegistFragments.RegistFragment;
import com.saiyu.foreground.ui.fragments.RegistFragments.RegistUnionIdFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.ActiveBuyerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.ActiveSellerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.BuyerInfoFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.CashFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.CashRecordFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.InfoWadFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.MemberInfoFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.RechargeRecordFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SecurityManagerFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SellerInfoFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.SetingFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.UnionLoginFragment;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.frame_base_container_layout)
public class ContainerActivity extends BaseActivity{

    private Bundle bundle = null;
    public static final String FragmentTag = "FragmentTag";

    @AfterViews
    void afterViews(){
        bundle = getIntent().getExtras();
        if(bundle != null){
            Message msg = new Message();
            msg.what = 1;
            msg.arg1 = bundle.getInt(FragmentTag,0);
            handler.sendMessage(msg);
        }
    }

    public static final int SetingFragmentTag = 1;//设置
    public static final int ActiveBuyerFragmentTag = 2;//激活买家
    public static final int ActiveSellerFragmentTag = 3;//激活卖家
    public static final int BuyerInfoFragmentTag = 4;//买家资料
    public static final int CashFragmentTag = 5;//我要提现
    public static final int CashRecordFragmentTag = 6;//提现记录
    public static final int InfoWadFragmentTag = 7;//资料补填
    public static final int MemberInfoFragmentTag = 8;//会员资料
    public static final int RechargeFragmentTag = 9;//我要充值
    public static final int RechargeRecordFragmentTag = 10;//充值记录
    public static final int SecurityManagerFragmentTag = 11;//安全管理
    public static final int SellerInfoFragmentTag = 12;//卖家资料
    public static final int UnionLoginFragmentTag = 13;//互联登陆
    public static final int LoginFragmentTag = 14;
    public static final int ForgotPswFragmentTag = 15;
    public static final int RegistUnionIdFragmentTag = 16;
    public static final int RegistFragmentTag = 17;
    public static final int ProtocolFragmentTag = 18;

    private BaseFragment getFragment(int tag){
        switch (tag){
            case SetingFragmentTag:
                return SetingFragment.newInstance(bundle);
            case ActiveBuyerFragmentTag:
                return ActiveBuyerFragment.newInstance(bundle);
            case ActiveSellerFragmentTag:
                return ActiveSellerFragment.newInstance(bundle);
            case BuyerInfoFragmentTag:
                return BuyerInfoFragment.newInstance(bundle);
            case CashFragmentTag:
                return CashFragment.newInstance(bundle);
            case CashRecordFragmentTag:
                return CashRecordFragment.newInstance(bundle);
            case InfoWadFragmentTag:
                return InfoWadFragment.newInstance(bundle);
            case MemberInfoFragmentTag:
                return MemberInfoFragment.newInstance(bundle);
            case RechargeFragmentTag:
                return RechargeFragment.newInstance(bundle);
            case RechargeRecordFragmentTag:
                return RechargeRecordFragment.newInstance(bundle);
            case SecurityManagerFragmentTag:
                return SecurityManagerFragment.newInstance(bundle);
            case SellerInfoFragmentTag:
                return SellerInfoFragment.newInstance(bundle);
            case UnionLoginFragmentTag:
                return UnionLoginFragment.newInstance(bundle);
            case LoginFragmentTag:
                return LoginFragment.newInstance(bundle);
            case ForgotPswFragmentTag:
                return ForgotPswFragment.newInstance(bundle);
            case RegistUnionIdFragmentTag:
                return RegistUnionIdFragment.newInstance(bundle);
            case RegistFragmentTag:
                return RegistFragment.newInstance(bundle);
            case ProtocolFragmentTag:
                return ProtocolFragment.newInstance(bundle);
                default:
                    return null;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        loadRootFragment(R.id.fr_base_container, getFragment(msg.arg1));
                    }catch (Exception e){
                        LogUtils.print("页面加载异常 ： " + e.toString());
                    }

                    break;
            }
        }
    };


}
