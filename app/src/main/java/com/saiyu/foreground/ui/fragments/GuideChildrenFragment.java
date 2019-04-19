package com.saiyu.foreground.ui.fragments;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_guide_children)
public class GuideChildrenFragment extends BaseFragment{

    @ViewById
    ImageView iv_guide;
    @ViewById
    TextView tv_guide;

    public static GuideChildrenFragment newInstance(Bundle bundle) {
        GuideChildrenFragment_ fragment = new GuideChildrenFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void afterViews(){
        Bundle bundle = getArguments();
        if(bundle != null){
            int index = bundle.getInt("index");
            switch (index){
                case 0:
                    iv_guide.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.guide_1));
                    tv_guide.setText("赛鱼交易平台\n让交易更便捷");
                    break;
                case 1:
                    iv_guide.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.guide_2));
                    tv_guide.setText("赛鱼\n轻松交易 省心省力");
                    break;
                case 2:
                    iv_guide.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.guide_3));
                    tv_guide.setText("赛鱼\n品牌担保 严格风控");
                    break;
            }
        }

    }
}
