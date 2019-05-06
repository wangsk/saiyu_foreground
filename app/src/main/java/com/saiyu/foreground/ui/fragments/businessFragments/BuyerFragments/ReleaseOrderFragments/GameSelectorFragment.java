package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.ReleaseOrderFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.GameModelAdapter;
import com.saiyu.foreground.adapters.GameSelectorAdapter;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.ProductListRet;
import com.saiyu.foreground.https.response.ProductPropertyRet;
import com.saiyu.foreground.interfaces.OnItemClickListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
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

@EFragment(R.layout.fragment_game_selector)
public class GameSelectorFragment extends BaseFragment implements CallbackUtils.ResponseCallback, OnItemClickListener {

    @ViewById
    TextView tv_title_content,tv_manage;
    @ViewById
    Button btn_title_back;
    @ViewById
    LinearLayout ll_model;
    @ViewById
    RelativeLayout rl_game_select;
    @ViewById
    SmartRefreshLayout refreshLayout;
    @ViewById
    SwipeMenuRecyclerView recyclerView,rv_model;
    @ViewById
    ProgressBar pb_loading;
    private GameModelAdapter gameModelAdapter;
    private GameSelectorAdapter gameSelectorAdapter;
    private List<ProductListRet.DatasBean.ProductItemsBean> productItemsBeans = new ArrayList<>();
    private ProductListRet.DatasBean.ProductItemsBean mProductItemsBean;

    public static GameSelectorFragment newInstance(Bundle bundle) {
        GameSelectorFragment_ fragment = new GameSelectorFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setCallback(this);
        ApiRequest.getProductList("1", "GameSelectorFragment_getProductList", pb_loading);

        tv_manage.setText("删除模板");
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("充值游戏选择");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        GridLayoutManager gridLayoutManager_2 = new GridLayoutManager(mContext,3);
        rv_model.setLayoutManager(gridLayoutManager_2);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        gameSelectorAdapter = new GameSelectorAdapter(productItemsBeans);
        recyclerView.setAdapter(gameSelectorAdapter);
        gameSelectorAdapter.notifyDataSetChanged();



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

//            gameModelAdapter = new GameModelAdapter(productItemsBeans);
//            gameModelAdapter.setOnItemClickListener(this);
//            rv_model.setAdapter(gameModelAdapter);
//            gameModelAdapter.notifyDataSetChanged();
//            ll_model.setVisibility(View.VISIBLE);

        } else if(method.equals("GameSelectorFragment_getProductProperty1")){
            ProductPropertyRet ret = (ProductPropertyRet)baseRet;

            Intent intent = new Intent(mContext, ContainerActivity_.class);
            Bundle bundle = new Bundle();

            if (ret.getData() == null || ret.getData().size() <= 0) {
                //没有区服
                bundle.putString("name",mProductItemsBean.getName());
                bundle.putString("ProductId",mProductItemsBean.getpId());
                bundle.putString("qbCount",mProductItemsBean.getQbCount());
                bundle.putString("unitName",mProductItemsBean.getUnitName());
                bundle.putString("convertCount",mProductItemsBean.getConvertCount());
                bundle.putInt("isRole",mProductItemsBean.getIsRole());

                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.ReleaseOrderFragmentTag);

            } else {
                bundle.putStringArrayList("gamePlaceList",ret.getData());
                bundle.putString("name",mProductItemsBean.getName());
                bundle.putString("ProductId",mProductItemsBean.getpId());
                bundle.putString("qbCount",mProductItemsBean.getQbCount());
                bundle.putString("unitName",mProductItemsBean.getUnitName());
                bundle.putString("convertCount",mProductItemsBean.getConvertCount());
                bundle.putInt("isRole",mProductItemsBean.getIsRole());
                bundle.putInt("index",0);

                bundle.putInt(ContainerActivity.FragmentTag, ContainerActivity.GamePlaceSelectorFragmentTag);

            }
            intent.putExtras(bundle);
            getActivity().startActivity(intent);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()){
            case R.id.item_hall_tv:
                LogUtils.print("选择游戏");
                if(productItemsBeans == null || productItemsBeans.size() <= position || productItemsBeans.get(position) == null || TextUtils.isEmpty(productItemsBeans.get(position).getName())){
                    return;
                }
                mProductItemsBean = productItemsBeans.get(position);
                ApiRequest.getProductProperty1(productItemsBeans.get(position).getName(),"GameSelectorFragment_getProductProperty1",pb_loading);
                break;
            case R.id.rl_game_model:
                boolean flag = gameModelAdapter.isFlag();
                if(flag){
                    LogUtils.print("删除模板");
                } else {
                    LogUtils.print("选择模板");
                }

                break;
        }
    }

    @Click({R.id.btn_title_back,R.id.tv_manage})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_manage:
                String text = tv_manage.getText().toString();
                if(TextUtils.equals("删除模板",text)){
                    tv_manage.setText("取消");
                    gameModelAdapter.setFlag(true);
                    gameModelAdapter.notifyDataSetChanged();
                } else {
                    tv_manage.setText("删除模板");
                    gameModelAdapter.setFlag(false);
                    gameModelAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
