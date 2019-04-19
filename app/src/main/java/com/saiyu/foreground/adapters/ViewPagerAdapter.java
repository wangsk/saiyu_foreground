package com.saiyu.foreground.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.saiyu.foreground.ui.fragments.GuideChildrenFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int count;

    public ViewPagerAdapter(FragmentManager manager,int count) {
        super(manager);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position){
            case 0:
                bundle.putInt("index",0);
                fragment = GuideChildrenFragment.newInstance(bundle);
                break;
            case 1:
                bundle.putInt("index",1);
                fragment = GuideChildrenFragment.newInstance(bundle);
                break;
            case 2:
                bundle.putInt("index",2);
                fragment = GuideChildrenFragment.newInstance(bundle);
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }


}
