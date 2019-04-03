package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.GameSelectorAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.ProductListRet;
import com.saiyu.foreground.https.response.ProductPropertyRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_base_record)
public class GameSelectorFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnItemClickListener {

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
    private GameSelectorAdapter gameSelectorAdapter;
    private List<ProductListRet.DatasBean.ProductItemsBean> productItemsBeans = new ArrayList<>();
    private ProductListRet.DatasBean.ProductItemsBean mProductItemsBean;

    public static GameSelectorFragment newInstance(Bundle bundle) {
        GameSelectorFragment_ fragment = new GameSelectorFragment_();
        fragment.setArguments(bundle);
        CallbackUtils.setCallback(fragment);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("充值游戏选择");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        gameSelectorAdapter = new GameSelectorAdapter(productItemsBeans);
        recyclerView.setAdapter(gameSelectorAdapter);
        gameSelectorAdapter.notifyDataSetChanged();

        ApiRequest.getProductList("1", "GameSelectorFragment_getProductList", pb_loading);

    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("GameSelectorFragment_getProductList")) {
            ProductListRet ret = (ProductListRet) baseRet;
            if (ret.getData() == null) {
                return;
            }

            productItemsBeans.clear();
            productItemsBeans.addAll(ret.getData().getProList());

            gameSelectorAdapter = new GameSelectorAdapter(productItemsBeans);
            gameSelectorAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(gameSelectorAdapter);
            gameSelectorAdapter.notifyDataSetChanged();

        } else if(method.equals("GameSelectorFragment_getProductProperty1")){
            ProductPropertyRet ret = (ProductPropertyRet)baseRet;
            if (ret.getData() == null || ret.getData().size() <= 0) {
                //没有区服
                Bundle bundle = new Bundle();
                bundle.putString("name",mProductItemsBean.getName());
                bundle.putString("ProductId",mProductItemsBean.getpId());
                bundle.putString("qbCount",mProductItemsBean.getQbCount());
                bundle.putString("unitName",mProductItemsBean.getUnitName());
                bundle.putString("convertCount",mProductItemsBean.getConvertCount());
                bundle.putInt("isRole",mProductItemsBean.getIsRole());
                ReleaseOrderFragment releaseOrderFragment = ReleaseOrderFragment.newInstance(bundle);
                start(releaseOrderFragment);

            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("gamePlaceList",ret.getData());
                bundle.putString("name",mProductItemsBean.getName());
                bundle.putString("ProductId",mProductItemsBean.getpId());
                bundle.putString("qbCount",mProductItemsBean.getQbCount());
                bundle.putString("unitName",mProductItemsBean.getUnitName());
                bundle.putString("convertCount",mProductItemsBean.getConvertCount());
                bundle.putInt("isRole",mProductItemsBean.getIsRole());
                bundle.putInt("index",0);
                GamePlaceSelectorFragment gamePlaceSelectorFragment = GamePlaceSelectorFragment.newInstance(bundle);
                start(gamePlaceSelectorFragment);
            }

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(productItemsBeans == null || productItemsBeans.size() <= position || productItemsBeans.get(position) == null || TextUtils.isEmpty(productItemsBeans.get(position).getName())){
            return;
        }
        mProductItemsBean = productItemsBeans.get(position);
        ApiRequest.getProductProperty1(productItemsBeans.get(position).getName(),"GameSelectorFragment_getProductProperty1",pb_loading);

        LogUtils.print(productItemsBeans.get(position).getName());

    }

    @Click(R.id.btn_title_back)
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
        }
    }
}
