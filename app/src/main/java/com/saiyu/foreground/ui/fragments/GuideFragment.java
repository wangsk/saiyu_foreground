package com.saiyu.foreground.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.ViewPagerAdapter;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.ui.activitys.MainActivity_;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_guide)
public class GuideFragment extends BaseFragment{

    @ViewById
    Button button;
    @ViewById
    ViewPager view_pager;
    @ViewById
    ImageView iv_1,iv_2,iv_3;


    public static GuideFragment newInstance(Bundle bundle) {
        GuideFragment_ fragment = new GuideFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        setupViewPager(view_pager);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        iv_1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide));
                        iv_2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        iv_3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        button.setVisibility(View.GONE);
                        break;
                    case 1:
                        iv_1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        iv_2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide));
                        iv_3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        button.setVisibility(View.GONE);
                        break;
                    case 2:
                        iv_1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        iv_2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide_unfocus));
                        iv_3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yuan_guide));
                        button.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Click({R.id.button})
    void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                // 设置 sp 中 isFirstRunning 为false
                SPUtils.putBoolean(ConstValue.ISFIRSTRUNNING, false);
                // 跳至登陆页面，
                Intent intent = new Intent(mContext, MainActivity_.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),3);
        viewPager.setAdapter(adapter);
    }

}
