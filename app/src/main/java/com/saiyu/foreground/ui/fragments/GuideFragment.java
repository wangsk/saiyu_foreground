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
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.ui.activitys.MainActivity_;
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

    private List<ImageView> imageList;
    private int[] imageIds;

    public static GuideFragment newInstance(Bundle bundle) {
        GuideFragment_ fragment = new GuideFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        imageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        assignData();
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

    private void assignData() {
        imageList = new ArrayList<ImageView>();

        for (int i = 0; i < imageIds.length; i++) {

            ImageView image = new ImageView(mContext);
            image.setBackgroundResource(imageIds[i]);

            imageList.add(image);
        }

        adapter = new MyAdapter();
        view_pager.setAdapter(adapter);

        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            /**
             * 当页面选择发生改变时调用
             * position 新选择的页面的下标
             */
            public void onPageSelected(int position) {
                // 如果选择的是最后一个页面，那么，显示button按钮
                if (position == imageList.size() - 1) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }

            }

            @Override
            // 页面滑动时，不断调用
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            // 当页面的滑动状态发生改变时
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private MyAdapter adapter;

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        /**
         * 实例化某个条目时，调用此方法
         * 我们二件事要做：
         * 1- 根据 position 获得一个对应的view 并添加至 container
         * 2- 返回一个和view相关的对象
         */
        public Object instantiateItem(ViewGroup container, int position) {

            // 1- 根据 position 获得一个对应的view 并添加至 container
            View view = imageList.get(position);
            container.addView(view);

            // 2- 返回一个和view相关的对象
            return view;
        }

        @Override
        /**
         * 判断view与object之间的对应关系
         * @param view 就是 instantiateItem 方法 中，添加至 container 的view
         * @param object 就是 instantiateItem 方法的返回值
         */
        public boolean isViewFromObject(View view, Object object) {
            if (view == object) {
                return true;
            }
            return false;
        }

        @Override
        /**
         * 当需要销毁某个条目的时候，调用此方法
         * position 要销毁的条目的下标
         * object 是该条目对应的对象 ，即 instantiateItem 方法的返回值
         */
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 下面这句话，一定要注掉,否则会崩，原因，你懂的。
//			super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
