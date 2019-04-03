package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.GamePlaceSelectorAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.ProductPropertyRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.DashlineItemDivider;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_base_record)
public class GamePlaceSelectorFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnItemClickListener  {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView;
    @ViewById
    ProgressBar pb_loading;
    private GamePlaceSelectorAdapter gamePlaceSelectorAdapter;
    private ArrayList<String> mItems;
    private String name,qbCount,unitName,convertCount,productId,place,ProductProperty1,ProductProperty2,ProductProperty3;
    private int isRole;
    private int index;

    public static GamePlaceSelectorFragment newInstance(Bundle bundle) {
        GamePlaceSelectorFragment_ fragment = new GamePlaceSelectorFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("充值大区选择");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //分割线的颜色
        recyclerView.addItemDecoration(new DashlineItemDivider(2));
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);


        Bundle bundle = getArguments();
        if(bundle != null){

            name = bundle.getString("name");
            productId = bundle.getString("ProductId");
            qbCount = bundle.getString("qbCount");
            unitName = bundle.getString("unitName");
            convertCount = bundle.getString("convertCount");
            isRole = bundle.getInt("isRole");

            ProductProperty2 = bundle.getString("ProductProperty2");
            ProductProperty3 = bundle.getString("ProductProperty3");

            index = bundle.getInt("index");
            ProductProperty1 = bundle.getString("ProductProperty1");
            ProductProperty2 = bundle.getString("ProductProperty2");
            ProductProperty3 = bundle.getString("ProductProperty3");

            mItems = bundle.getStringArrayList("gamePlaceList");
            if(mItems != null && mItems.size() > 0){
                gamePlaceSelectorAdapter = new GamePlaceSelectorAdapter(mItems);
                gamePlaceSelectorAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(gamePlaceSelectorAdapter);
                gamePlaceSelectorAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("GamePlaceSelectorFragment_getProductProperty2")) {
            ProductPropertyRet ret = (ProductPropertyRet)baseRet;
            if (ret.getData() == null || ret.getData().size() <= 0) {
                //没有区服
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("ProductId",productId);
                bundle.putString("qbCount",qbCount);
                bundle.putString("unitName",unitName);
                bundle.putString("convertCount",convertCount);
                bundle.putInt("isRole",isRole);
                switch (index){
                    case 0:
                        bundle.putString("ProductProperty1",place);
                        break;
                    case 1:
                        bundle.putString("ProductProperty1",ProductProperty1);
                        bundle.putString("ProductProperty2",place);
                        break;
                    case 2:
                        bundle.putString("ProductProperty1",ProductProperty1);
                        bundle.putString("ProductProperty2",ProductProperty2);
                        bundle.putString("ProductProperty3",place);
                        break;
                }
                ReleaseOrderFragment releaseOrderFragment = ReleaseOrderFragment.newInstance(bundle);
                start(releaseOrderFragment);

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("ProductId",productId);
                bundle.putString("qbCount",qbCount);
                bundle.putString("unitName",unitName);
                bundle.putString("convertCount",convertCount);
                bundle.putInt("isRole",isRole);
                bundle.putStringArrayList("gamePlaceList",ret.getData());
                switch (index){
                    case 0:
                        bundle.putString("ProductProperty1",place);
                        break;
                    case 1:
                        bundle.putString("ProductProperty1",ProductProperty1);
                        bundle.putString("ProductProperty2",place);
                        break;
                    case 2:
                        bundle.putString("ProductProperty1",ProductProperty1);
                        bundle.putString("ProductProperty2",ProductProperty2);
                        bundle.putString("ProductProperty3",place);
                        break;
                }

                bundle.putInt("index",(index+1));
                GamePlaceSelectorFragment gamePlaceSelectorFragment = GamePlaceSelectorFragment.newInstance(bundle);
                start(gamePlaceSelectorFragment);
            }

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mItems == null || mItems.size() <= position || TextUtils.isEmpty(mItems.get(position))){
            return;
        }
        place = mItems.get(position);
        ApiRequest.getProductProperty2(mItems.get(position),"GamePlaceSelectorFragment_getProductProperty2",pb_loading);
    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
