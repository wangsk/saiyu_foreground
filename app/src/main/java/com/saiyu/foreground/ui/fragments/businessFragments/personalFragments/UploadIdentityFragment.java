package com.saiyu.foreground.ui.fragments.businessFragments.personalFragments;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.vondear.rxtool.RxPhotoTool;

import static com.saiyu.foreground.ui.views.RxDialogChooseImage.LayoutType.TITLE;

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

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


@EFragment(R.layout.fragment_upload_identity)
public class UploadIdentityFragment extends BaseFragment implements CallbackUtils.OnActivityCallBack,CallbackUtils.ResponseCallback {
    @ViewById
    TextView tv_title_content;
    @ViewById
    Button btn_title_back,btn_next,btn_last;
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
    private int uploadCode = -1;
    private String identity_front,identity_bg;

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

    }

    @AfterViews
    void afterViews(){
        tv_title_content.setText("实名认证");
        Bundle bundle = getArguments();
        if (bundle != null) {
            String RealName = bundle.getString("RealName","");
            String IDNum = bundle.getString("IDNum","");
            et_realname.setText(RealName);
            et_identity.setText(IDNum);
        }
    }

    @Click({R.id.btn_title_back,R.id.fl_identity_front,R.id.fl_identity_bg,R.id.btn_last,R.id.btn_next})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_title_back:
            case R.id.btn_last:
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
                } else if(uploadCode == 1){
                    identity_bg = ret.getData().getSrc();
                    LogUtils.print("identity_bg=== " + identity_bg);
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
                    trimImage(data.getData(), ConstValue.ACTION_TRIM_IMAGE);
                    break;
                case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                    LogUtils.print("选择照相机之后的处理");
                    trimImage(RxPhotoTool.imageUriFromCamera, ConstValue.ACTION_TRIM_IMAGE);
                    break;
                case ConstValue.ACTION_TRIM_IMAGE:
                    LogUtils.print("普通裁剪后的处理");
                    Bundle bundle = data.getExtras();
                    if(bundle == null){
                        return;
                    }
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(outputUri));

                        if(uploadCode == 0){
                            iv_identity_front.setImageBitmap(bitmap);
                            ll_identity_front.setVisibility(View.GONE);
                        } else if(uploadCode == 1){
                            iv_identity_bg.setImageBitmap(bitmap);
                            ll_identity_bg.setVisibility(View.GONE);
                        }

                        File file = new File(getActivity().getExternalCacheDir() + syUser_crop);
                        File oldFile = new File(getActivity().getExternalCacheDir() + syUser_crop);
                        File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                String.valueOf(System.currentTimeMillis()) + ".png");
                        try {
                            copyFileUsingFileChannels(oldFile, newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogUtils.print("file === " + file.length());
                        ApiRequest.uploadIdentity(file,"UploadIdentityFragment_uploadIdentity",pb_loading);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    }

    private Uri outputUri;
    String syUser_crop = "/" + System.currentTimeMillis() + ".png";

    // 修剪图片
    public void trimImage(Uri uri, int requestCode) {
        File output = new File(getActivity().getExternalCacheDir(), syUser_crop);
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputUri = Uri.fromFile(output);

        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        //裁剪框比例
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 280);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        mContext.startActivityForResult(intent, requestCode);
    }

    private static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }


}
