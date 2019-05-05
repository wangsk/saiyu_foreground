package com.saiyu.foreground.ui.fragments.businessFragments.hallFragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.calenderview.SelectDateDialog;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.activitys.ContainerActivity;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments.OrderRechargeFragment;
import com.saiyu.foreground.ui.views.PhotoViewDialog;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.saiyu.foreground.utils.BitmapUtils;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.DialogUtils;
import com.saiyu.foreground.utils.GetPathFromUri;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.saiyu.foreground.utils.TimeParseUtils;
import com.saiyu.foreground.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.RxSPTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.saiyu.foreground.ui.views.RxDialogChooseImage.LayoutType.TITLE;

@EFragment(R.layout.fragment_ordersubmit_child)
public class OrderSubmitChildFragment extends BaseFragment implements CallbackUtils.OnActivityCallBack,CallbackUtils.ResponseCallback ,CallbackUtils.OnContentListener{

    @ViewById
    TextView tv_time_show,tv_averagetime,tv_record_upload,tv_info_upload,tv_success_upload;
    @ViewById
    Button btn_submit,btn_time;

    @ViewById
    ImageView iv_recharge_success_show,iv_recharge_info_show,iv_record_show,iv_1,iv_2,iv_3,iv_success_del,iv_info_del,iv_record_del;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    LinearLayout ll_selector_3;
    private int uploadCode = 0;
    private String successUrl,infoUrl,recordUrl,ReceiveId,rechargeTime;
    private String BillQQNum,BillQQPwd,OftenLoginProvince,OftenLoginCity;
    private String ReceiveQBCount = "0";//0代表无效
    private String orderId,orderNum;
    private String ConfirmType = "0";
    private String OrderImgServerProcess;

    public static OrderSubmitChildFragment newInstance(Bundle bundle) {
        OrderSubmitChildFragment_ fragment = new OrderSubmitChildFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnContentListener(this);
        OrderImgServerProcess = SPUtils.getString(ConstValue.OrderImgServerProcess,"");
        LogUtils.print("OrderImgServerProcess === " + OrderImgServerProcess);
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("OrderSubmitChildFragment_uploadIdentity")){
            UploadIdentityRet ret = (UploadIdentityRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                LogUtils.print("uploadCode=== " + uploadCode);
                if(uploadCode == 0){
                    successUrl = ret.getData().getSrc();
                    LogUtils.print("successUrl=== " + successUrl);
                    iv_success_del.setVisibility(View.VISIBLE);
                    tv_success_upload.setVisibility(View.GONE);

                    Glide.with(App.getApp())
                            .load(successUrl + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_recharge_success_show);

                } else if(uploadCode == 1){
                    infoUrl = ret.getData().getSrc();
                    LogUtils.print("infoUrl=== " + infoUrl);
                    iv_info_del.setVisibility(View.VISIBLE);
                    tv_info_upload.setVisibility(View.GONE);

                    Glide.with(App.getApp())
                            .load(infoUrl + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_recharge_info_show);

                } else if(uploadCode == 2){
                    recordUrl = ret.getData().getSrc();
                    LogUtils.print("recordUrl=== " + recordUrl);
                    iv_record_del.setVisibility(View.VISIBLE);
                    tv_record_upload.setVisibility(View.GONE);

                    Glide.with(App.getApp())
                            .load(recordUrl + "?" + OrderImgServerProcess)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_record_show);

                }
            }
        }
    }

    @AfterViews
    void afterViews() {
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        CallbackUtils.setOnContentListener(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            String averagetime = "提交截图后，等待买家给我确认，平均确认时间: "+bundle.getString("AverageConfirmTime");
            tv_averagetime.setText(averagetime);
            ReceiveId = bundle.getString("ReceiveId");
            orderId = bundle.getString("orderId");
            orderNum = bundle.getString("orderNum");
            ReceiveQBCount = bundle.getString("ReceiveQBCount","0");
            if("0".equals(ReceiveQBCount)){
                //0代表无效
                Utils.setButtonClickable(btn_submit,false);
            } else {
                Utils.setButtonClickable(btn_submit,true);
            }
            if(bundle.getBoolean("IsCustomerConfirmation",false)){
                ll_selector_3.setVisibility(View.VISIBLE);
                iv_1.setVisibility(View.INVISIBLE);
                ConfirmType = "1";//客服代理确认
            } else {
                ll_selector_3.setVisibility(View.GONE);
                iv_1.setVisibility(View.VISIBLE);
                ConfirmType = "0";
            }
        }
    }

    @Click({R.id.btn_submit,R.id.ll_selector_3, R.id.ll_selector_1, R.id.ll_selector_2,R.id.btn_time, R.id.tv_time_show, R.id.tv_record_upload, R.id.tv_info_upload, R.id.tv_success_upload
    ,R.id.iv_recharge_info_show,R.id.iv_recharge_success_show,R.id.iv_record_show,R.id.iv_success_del,R.id.iv_info_del,R.id.iv_record_del})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_success_del:
                successUrl = "";
                iv_success_del.setVisibility(View.GONE);
                tv_success_upload.setVisibility(View.VISIBLE);
                iv_recharge_success_show.setImageDrawable(mContext.getDrawable(R.mipmap.paizhaoshangchuan));
                break;
            case R.id.iv_info_del:
                infoUrl = "";
                iv_info_del.setVisibility(View.GONE);
                tv_info_upload.setVisibility(View.VISIBLE);
                iv_recharge_info_show.setImageDrawable(mContext.getDrawable(R.mipmap.paizhaoshangchuan));
                break;
            case R.id.iv_record_del:
                recordUrl = "";
                iv_record_del.setVisibility(View.GONE);
                tv_record_upload.setVisibility(View.VISIBLE);
                iv_record_show.setImageDrawable(mContext.getDrawable(R.mipmap.paizhaoshangchuan));
                break;
            case R.id.btn_submit:
                if(TextUtils.isEmpty(ReceiveId)){
                    LogUtils.print("ReceiveId === " + ReceiveId);
                    return;
                }
                if(TextUtils.isEmpty(ReceiveQBCount)|| ReceiveQBCount.equals("0")){
                    LogUtils.print("ReceiveQBCount === " + ReceiveQBCount);
                    return;
                }
//                if(TextUtils.isEmpty(rechargeTime)){
//                    LogUtils.print("rechargeTime === " + rechargeTime);
//                    Toast.makeText(mContext,"请选择充值时间",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(TextUtils.isEmpty(infoUrl)){
                    LogUtils.print("infoUrl === " + infoUrl);
                    Toast.makeText(mContext,"请上传充值明细截图",Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("receiveId",ReceiveId);
                bundle.putString("SuccQBCount",ReceiveQBCount);
                bundle.putString("RechargeTime",rechargeTime);
                bundle.putString("PicRechargeSucc",successUrl);
                bundle.putString("PicTradeInfo",infoUrl);
                bundle.putString("PicBillRecord",recordUrl);
                bundle.putString("ConfirmType",ConfirmType);
                bundle.putString("BillQQNum",BillQQNum);
                bundle.putString("BillQQPwd",BillQQPwd);
                bundle.putString("OftenLoginProvince",OftenLoginProvince);
                bundle.putString("OftenLoginCity",OftenLoginCity);

                bundle.putString("orderId",orderId);
                bundle.putString("orderNum",orderNum);

                OrderConfirmFragment orderConfirmFragment = OrderConfirmFragment.newInstance(bundle);

                Bundle bundle_1 = ((ContainerActivity)getActivity()).getIntent().getExtras();
                if(bundle_1 != null){
                    int fragmentTag = bundle_1.getInt(ContainerActivity.FragmentTag,0);
                    if(fragmentTag == ContainerActivity.WaitingRechargeOrderFragmentTag){
                        ((OrderRechargeFragment)getParentFragment()).start(orderConfirmFragment);
                    } else if(fragmentTag == ContainerActivity.HallOrderDetailFragmentTag){
                        ((HallOrderDetailFragment)getParentFragment()).start(orderConfirmFragment);
                    }
                }

                break;
            case R.id.ll_selector_1:
                if(iv_1.getVisibility() == View.INVISIBLE){
                    iv_1.setVisibility(View.VISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.INVISIBLE);
                    BillQQNum = "";
                    BillQQPwd = "";
                    OftenLoginProvince = "";
                    OftenLoginCity = "";

                    ConfirmType = "0";//等待买家手动确认
                }
                break;
            case R.id.ll_selector_3:
                if(iv_3.getVisibility() == View.INVISIBLE){
                    iv_1.setVisibility(View.INVISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.VISIBLE);
                    BillQQNum = "";
                    BillQQPwd = "";
                    OftenLoginProvince = "";
                    OftenLoginCity = "";
                    ConfirmType = "1";//客服代理确认
                }
                break;
            case R.id.ll_selector_2:
                DialogUtils.showOrderSubmitDialog(getActivity(), new OnListCallbackListener() {
                    @Override
                    public void setOnListCallbackListener(List<String> callbackList) {
                        if(callbackList == null || callbackList.size() < 4){
                            return;
                        }
                        BillQQNum = callbackList.get(0);
                        BillQQPwd = callbackList.get(1);
                        OftenLoginProvince = callbackList.get(2);
                        OftenLoginCity = callbackList.get(3);

                        if(iv_2.getVisibility() == View.INVISIBLE){
                            iv_1.setVisibility(View.INVISIBLE);
                            iv_2.setVisibility(View.VISIBLE);
                            iv_3.setVisibility(View.INVISIBLE);
                            ConfirmType = "2";//客服验图确认
                        }
                    }
                });
                break;
            case R.id.btn_time:
                String time = getTime(new Date(System.currentTimeMillis()));
                LogUtils.print("time == " + time);
                tv_time_show.setText(time);
                rechargeTime = time;
                break;
            case R.id.tv_time_show:
                Calendar calendar1 = TimeParseUtils.DateToCalendar(new Date(System.currentTimeMillis()));
                showTimeSelectDialog(calendar1);
                break;
            case R.id.tv_record_upload:
                uploadCode = 2;
                upLoadPic();
                break;
            case R.id.tv_info_upload:
                uploadCode = 1;
                upLoadPic();
                break;
            case R.id.tv_success_upload:
                uploadCode = 0;
                upLoadPic();
                break;
            case R.id.iv_recharge_info_show:
                if(!TextUtils.isEmpty(infoUrl)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(infoUrl + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                } else {
                    uploadCode = 1;
                    upLoadPic();
                }
                break;
            case R.id.iv_recharge_success_show:
                if(!TextUtils.isEmpty(successUrl)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(successUrl + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                }else {
                    uploadCode = 0;
                    upLoadPic();
                }
                break;
            case R.id.iv_record_show:
                if(!TextUtils.isEmpty(recordUrl)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(recordUrl + "?" + OrderImgServerProcess);
                    photoViewDialog.show();
                }else {
                    uploadCode = 2;
                    upLoadPic();
                }
                break;
        }
    }
    private void showTimeSelectDialog(java.util.Calendar calendar) {
        SelectDateDialog selectDateDialog = new SelectDateDialog(getActivity(), calendar);
        selectDateDialog.setDialogOnClickListener(new SelectDateDialog.DialogOnClickListener() {

            @Override
            public void confirmedDo(Date date) {
                if (date == null) {
                    return;
                }

                //公历
                String time = getTime(date);
                LogUtils.print("time == " + time);
                tv_time_show.setText(time);
                rechargeTime = time;
            }
        });
        selectDateDialog.show();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(date);
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
                        ApiRequest.uploadIdentity(new File(path),"OrderSubmitChildFragment_uploadIdentity",pb_loading);
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

                        ApiRequest.uploadIdentity(output,"OrderSubmitChildFragment_uploadIdentity",pb_loading);
                    } else {
                        LogUtils.print("选择相册照片异常");
                    }

                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void setOnContentListener(String content) {
        ReceiveQBCount = content;
        LogUtils.print("ReceiveQBCount === " + ReceiveQBCount);
        if("0".equals(ReceiveQBCount)){
            //0代表无效
            Utils.setButtonClickable(btn_submit,false);
        } else {
            Utils.setButtonClickable(btn_submit,true);
        }
    }
}
