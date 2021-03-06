package com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.RechargeOrderFragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.calenderview.SelectTimeDialog;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.RechargeOrderInfoRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.activitys.ContainerActivity_;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.BuyerFragments.PreOrderManagerFragment;
import com.saiyu.foreground.ui.views.PhotoViewDialog;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.saiyu.foreground.utils.BitmapUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.GetPathFromUri;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.saiyu.foreground.utils.TimeParseUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxPhotoTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.saiyu.foreground.ui.views.RxDialogChooseImage.LayoutType.TITLE;

@EFragment(R.layout.fragment_right)
public class RightFragment extends BaseFragment implements CallbackUtils.ResponseCallback,CallbackUtils.OnActivityCallBack{
    @ViewById
    TextView tv_title_content,tv_order,tv_recharge_product,tv_producttype,tv_rechargenum,tv_ordernum,tv_successnum,tv_money,tv_time;
    @ViewById
    Button btn_title_back,btn_confirm,btn_cancel;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    ImageView iv_1,iv_2,iv_3,iv_4,iv_back_2,iv_back_1;
    @ViewById
    RelativeLayout rl_back_1,rl_back_2;
    @ViewById
    EditText et_mobile,et_qq,et_backnum,et_reason;

    private int uploadCode = 4;
    private String Pic = "";
    private Bitmap bitmap = null;

    private String orderReceiveId,BuyerContactMobile,BuyerContactQQ,BuyerContacTimeBegin,BuyerContacTimeEnd,RefundQBCount,Remarks;
    private String SuccQBCount;
    private boolean isEndActivity;
    private String url_1,url_2,url_3,url_4;
    private String OrderImgServerProcess;

    public static RightFragment newInstance(Bundle bundle) {
        RightFragment_ fragment = new RightFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        Bundle bundle = getArguments();
        if(bundle != null){
            orderReceiveId = bundle.getString("orderReceiveId");
            isEndActivity = bundle.getBoolean("isEndActivity",false);
        }
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        ApiRequest.orderReceiveInfo(orderReceiveId,"RightFragment_orderReceiveInfo",pb_loading);

        OrderImgServerProcess = SPUtils.getString(ConstValue.OrderImgServerProcess,"");
        LogUtils.print("OrderImgServerProcess === " + OrderImgServerProcess);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("RightFragment_startAppeal")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"申请维权成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        } else if(method.equals("RightFragment_orderReceiveInfo")){
            RechargeOrderInfoRet ret = (RechargeOrderInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_order.setText(ret.getData().getReceiveOrderNum());
            tv_recharge_product.setText(ret.getData().getProductName());
            tv_producttype.setText(ret.getData().getProductTypeName());
            tv_rechargenum.setText(ret.getData().getReserveQBCount());
            tv_ordernum.setText(ret.getData().getReserveAccount());
            SuccQBCount = ret.getData().getSuccQBCount();
            RefundQBCount = SuccQBCount;
            tv_successnum.setText(SuccQBCount);
            tv_money.setText(ret.getData().getSuccMoney() + "元");
        } else if(method.equals("RightFragment_uploadIdentity")){
            UploadIdentityRet ret = (UploadIdentityRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                LogUtils.print("uploadCode=== " + uploadCode);

                if(uploadCode == 4){
                    url_1 = ret.getData().getSrc();
                    iv_2.setVisibility(View.VISIBLE);

                    Glide.with(App.getApp())
                            .load(url_1 + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_1);

                    uploadCode--;
                } else if(uploadCode == 3){
                    url_2 = ret.getData().getSrc();
                    iv_3.setVisibility(View.VISIBLE);

                    Glide.with(App.getApp())
                            .load(url_2 + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_2);

                    uploadCode--;
                } else if(uploadCode == 2){
                    url_3 = ret.getData().getSrc();
                    iv_4.setVisibility(View.VISIBLE);

                    Glide.with(App.getApp())
                            .load(url_3 + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_3);

                    uploadCode--;
                } else if(uploadCode == 1){
                    url_4 = ret.getData().getSrc();

                    Glide.with(App.getApp())
                            .load(url_4 + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_4);

                    uploadCode--;
                }

            }
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("发起维权");
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.btn_cancel,R.id.iv_1,R.id.iv_2,R.id.iv_3,R.id.iv_4,R.id.tv_time,R.id.rl_back_1,R.id.rl_back_2})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_cancel:
                if(isEndActivity){
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }

                break;
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(orderReceiveId)){
                    return;
                }
                BuyerContactMobile = et_mobile.getText().toString();
                if(TextUtils.isEmpty(BuyerContactMobile)){
                    Toast.makeText(mContext,"请输入联系手机",Toast.LENGTH_SHORT).show();
                    return;
                }
                BuyerContactQQ = et_qq.getText().toString();
                if(TextUtils.isEmpty(BuyerContactQQ)){
                    Toast.makeText(mContext,"请输入联系QQ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(BuyerContacTimeBegin) || TextUtils.isEmpty(BuyerContacTimeEnd)){
                    Toast.makeText(mContext,"请输入联系时间",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(RefundQBCount) && TextUtils.isEmpty(et_backnum.getText().toString())){
                    Toast.makeText(mContext,"请输入退回Q币数量",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(TextUtils.isEmpty(RefundQBCount)){
                        RefundQBCount = et_backnum.getText().toString();
                    }
                }

                Remarks = et_reason.getText().toString();
                if(TextUtils.isEmpty(Remarks)){
                    Toast.makeText(mContext,"请输入维权说明",Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (uploadCode){
                    case 3:
                        Pic = url_1;
                        break;
                    case 2:
                        Pic = url_1 + "," + url_2;
                        break;
                    case 1:
                        Pic = url_1 + "," + url_2 + "," + url_3;
                        break;
                    case 0:
                        Pic = url_1 + "," + url_2 + "," + url_3 + "," + url_4;
                        break;
                }
                LogUtils.print("上传的图片 ==== " + Pic);

                RequestParams requestParams = new RequestParams();
                requestParams.put("orderReceiveId",orderReceiveId);
                requestParams.put("BuyerContactMobile",BuyerContactMobile);
                requestParams.put("BuyerContactQQ",BuyerContactQQ);
                requestParams.put("BuyerContacTimeBegin",BuyerContacTimeBegin);
                requestParams.put("BuyerContacTimeEnd",BuyerContacTimeEnd);
                requestParams.put("RefundQBCount",RefundQBCount);
                requestParams.put("Remarks",Remarks);
                requestParams.put("Pic",Pic);

                ApiRequest.startAppeal(requestParams,"RightFragment_startAppeal",pb_loading);
                break;
            case R.id.rl_back_1:
                if(iv_back_1.getVisibility() == View.GONE){
                    iv_back_1.setVisibility(View.VISIBLE);
                    RefundQBCount = SuccQBCount;
                    et_backnum.setText("");
                    et_backnum.setVisibility(View.GONE);
                    iv_back_2.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_back_2:
                if(iv_back_2.getVisibility() == View.GONE){
                    iv_back_1.setVisibility(View.GONE);
                    et_backnum.setVisibility(View.VISIBLE);
                    iv_back_2.setVisibility(View.VISIBLE);
                    RefundQBCount = "";
                }
                break;
            case R.id.tv_time:
                SelectTimeDialog selectTimeDialog = new SelectTimeDialog(mContext,TimeParseUtils.DateToCalendar(new Date(System.currentTimeMillis())));
                selectTimeDialog.setDialogOnClickListener(new OnListCallbackListener() {
                    @Override
                    public void setOnListCallbackListener(List<String> callbackList) {
                        if(callbackList == null){
                            tv_time.setText("请输入在线时间");
                            BuyerContacTimeBegin = "";
                            BuyerContacTimeEnd = "";
                            return;
                        }
                        if(callbackList.size() == 4){
                            LogUtils.print(callbackList.get(0)+":"+callbackList.get(1)+callbackList.get(2)+":"+callbackList.get(3));
                            BuyerContacTimeBegin = callbackList.get(0)+":"+callbackList.get(1) + ":00";
                            BuyerContacTimeEnd = callbackList.get(2)+":"+callbackList.get(3) + ":59";
                            tv_time.setText(BuyerContacTimeBegin + "-" + BuyerContacTimeEnd);
                        }
                    }
                });
                selectTimeDialog.show();
                break;
            case R.id.iv_1:
                if(uploadCode == 4){
                    upLoadPic();
                } else {
                    if(!TextUtils.isEmpty(url_1)){
                        PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                        photoViewDialog.setmUrl(url_1 + "?" + OrderImgServerProcess);
                        photoViewDialog.show();
                    }
                }
                break;
            case R.id.iv_2:
                if(uploadCode == 3){
                    upLoadPic();
                }else {
                    if(!TextUtils.isEmpty(url_2)){
                        PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                        photoViewDialog.setmUrl(url_2 + "?" + OrderImgServerProcess);
                        photoViewDialog.show();
                    }
                }
                break;
            case R.id.iv_3:
                if(uploadCode == 2){
                    upLoadPic();
                }else {
                    if(!TextUtils.isEmpty(url_3)){
                        PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                        photoViewDialog.setmUrl(url_3 + "?" + OrderImgServerProcess);
                        photoViewDialog.show();
                    }
                }
                break;
            case R.id.iv_4:
                if(uploadCode == 1){
                    upLoadPic();
                }else {
                    if(!TextUtils.isEmpty(url_4)){
                        PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                        photoViewDialog.setmUrl(url_4 + "?" + OrderImgServerProcess);
                        photoViewDialog.show();
                    }
                }
                break;
        }
    }

    private void upLoadPic() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            initDialogChooseImage();

                        } else {
                            Toast.makeText(mContext,"请开启存储与相机的权限",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void initDialogChooseImage() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mContext, TITLE);
        dialogChooseImage.getFromCameraView().setTextColor(getResources().getColor(R.color.blue));
        dialogChooseImage.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.print("requestCode === " + requestCode);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                    LogUtils.print("选择相册之后的处理");
                    String path = GetPathFromUri.getPath(mContext,data.getData());
                    if(!TextUtils.isEmpty(path)){
                        ApiRequest.uploadIdentity(new File(path),"RightFragment_uploadIdentity",pb_loading);
                    } else {
                        LogUtils.print("选择相册照片异常");
                    }
                    break;
                case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                    LogUtils.print("选择照相机之后的处理");
                    String pathFromCamera = GetPathFromUri.getPath(mContext,RxPhotoTool.imageUriFromCamera);
                    if(!TextUtils.isEmpty(pathFromCamera)){

                        File output = new File(getActivity().getExternalCacheDir(), "/" + System.currentTimeMillis() + ".png");
                        try {
                            if (output.exists()) {
                                output.delete();
                            }
                            output.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        BitmapUtils.compressBitmap(pathFromCamera,output.getPath(),2000);

                        ApiRequest.uploadIdentity(output,"RightFragment_uploadIdentity",pb_loading);
                    } else {
                        LogUtils.print("选择相册照片异常");
                    }
                    break;

                default:
                    break;
            }
        }
    }

}
