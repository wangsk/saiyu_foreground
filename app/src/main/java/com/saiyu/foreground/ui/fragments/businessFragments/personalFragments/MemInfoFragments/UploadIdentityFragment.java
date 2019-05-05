package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments.MemInfoFragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.RxSPTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.saiyu.foreground.ui.views.RxDialogChooseImage.LayoutType.TITLE;


@EFragment(R.layout.fragment_upload_identity)
public class UploadIdentityFragment extends BaseFragment implements CallbackUtils.OnActivityCallBack,CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_next;
    @ViewById
    FrameLayout fl_identity_front,fl_identity_bg;
    @ViewById
    LinearLayout ll_identity_front,ll_identity_bg;
    @ViewById
    ImageView iv_identity_front,iv_identity_bg;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    EditText et_realname,et_identity;
    private int uploadCode = -1;//区别身份证正反面
    private String identity_front,identity_bg;
    private boolean IsModify;
    private String UserImgServerProcess;

    public static UploadIdentityFragment newInstance(Bundle bundle) {
        UploadIdentityFragment_ fragment = new UploadIdentityFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        UserImgServerProcess = SPUtils.getString(ConstValue.UserImgServerProcess,"");
        LogUtils.print("UserImgServerProcess === " + UserImgServerProcess);

    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("实名认证");
        Bundle bundle = getArguments();
        if (bundle != null) {
            IsModify = bundle.getBoolean("IsModify",true);
            String RealName = bundle.getString("RealName","");
            String IDNum = bundle.getString("IDNum","");
            et_realname.setText(RealName);
            et_identity.setText(IDNum);
            if(!IsModify){//如果该为true说明用户已经通过刷脸认证，修改过一次姓名和身份证号码了
                et_realname.setClickable(false);
                et_realname.setFocusable(false);
                et_identity.setClickable(false);
                et_identity.setFocusable(false);
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.fl_identity_front,R.id.fl_identity_bg,R.id.btn_next})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
                getActivity().finish();
                break;
            case R.id.btn_next:
                String RealName = et_realname.getText().toString();
                String IDNum = et_identity.getText().toString();
                if(TextUtils.isEmpty(RealName)){
                    Toast.makeText(mContext,"姓名不能为空",Toast.LENGTH_SHORT).show();;
                    return;
                }
                if(TextUtils.isEmpty(IDNum)){
                    Toast.makeText(mContext,"身份证号码不能为空",Toast.LENGTH_SHORT).show();;
                    return;
                }
                if(TextUtils.isEmpty(identity_front)){
                    Toast.makeText(mContext,"请上传身份证正面照片",Toast.LENGTH_SHORT).show();;
                    return;
                }
                if(TextUtils.isEmpty(identity_bg)){
                    Toast.makeText(mContext,"请上传身份证反面照片",Toast.LENGTH_SHORT).show();;
                    return;
                }
                ApiRequest.realNameSubmit(RealName,IDNum,identity_front,identity_bg,"UploadIdentityFragment_realNameSubmit",pb_loading);
                break;
            case R.id.fl_identity_front:
                uploadCode = 0;
                upLoadPic();
                break;
            case R.id.fl_identity_bg:
                uploadCode = 1;
                upLoadPic();
                break;
        }
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("UploadIdentityFragment_uploadIdentity")){
            UploadIdentityRet ret = (UploadIdentityRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                LogUtils.print("uploadCode=== " + uploadCode);
                if(uploadCode == 0){
                    identity_front = ret.getData().getSrc();
                    LogUtils.print("identity_front=== " + identity_front);
                    if(!TextUtils.isEmpty(identity_front)){
                        Glide.with(App.getApp())
                                .load(identity_front + "?" + UserImgServerProcess)
                                .error(R.mipmap.ic_launcher)
                                .into(iv_identity_front);
                        ll_identity_front.setVisibility(View.GONE);
                    }
                } else if(uploadCode == 1){
                    identity_bg = ret.getData().getSrc();
                    LogUtils.print("identity_bg=== " + identity_bg);
                    if(!TextUtils.isEmpty(identity_bg)){
                        Glide.with(App.getApp())
                                .load(identity_bg + "?" + UserImgServerProcess)
                                .error(R.mipmap.ic_launcher)
                                .into(iv_identity_bg);
                        ll_identity_bg.setVisibility(View.GONE);
                    }
                }
            }
        } else if(method.equals("UploadIdentityFragment_realNameSubmit")){
            BooleanRet ret =(BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"实名认证提交成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
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
                    initUCrop(data.getData());
                    break;
                case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                    LogUtils.print("选择照相机之后的处理");
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                    break;
                case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                    LogUtils.print("UCrop裁剪之后的处理");
                    Uri resultUri = UCrop.getOutput(data);
                    File file = new File(RxPhotoTool.getImageAbsolutePath(mContext, resultUri));
                    RxSPTool.putContent(mContext, "AVATAR", resultUri.toString());
                    ApiRequest.uploadIdentity(file,"UploadIdentityFragment_uploadIdentity",pb_loading);
                    break;
                default:
                    break;
            }
        }
    }

    private void initUCrop(Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(mContext.getCacheDir(), imageName + ".png"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.NONE, UCropActivity.NONE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mContext, R.color.blue));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mContext, R.color.blue));
        options.setHideBottomControls(true);
        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        options.setShowCropFrame(true);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        options.setCropGridColumnCount(2);
        //设置横线的数量
        options.setCropGridRowCount(2);
        options.setFreeStyleCropEnabled(true);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(mContext);
    }


}
