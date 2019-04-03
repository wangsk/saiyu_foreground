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

import com.saiyu.foreground.R;
import com.saiyu.foreground.calenderview.SelectTimeDialog;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.request.RequestParams;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.StartAppealInfoRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
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
        }
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        ApiRequest.startAppealInfo(orderReceiveId,"RightFragment_startAppealInfo",pb_loading);
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
                getFragmentManager().popBackStack();
            }
        } else if(method.equals("RightFragment_startAppealInfo")){
            StartAppealInfoRet ret = (StartAppealInfoRet)baseRet;
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
                if(TextUtils.isEmpty(Pic)){
                    Pic = ret.getData().getSrc();
                } else {
                    Pic = Pic+"," + ret.getData().getSrc();
                }

                if(uploadCode == 4){
                    iv_2.setVisibility(View.VISIBLE);
                    iv_1.setImageBitmap(bitmap);
                    uploadCode--;
                } else if(uploadCode == 3){
                    iv_3.setVisibility(View.VISIBLE);
                    iv_2.setImageBitmap(bitmap);
                    uploadCode--;
                } else if(uploadCode == 2){
                    iv_4.setVisibility(View.VISIBLE);
                    iv_3.setImageBitmap(bitmap);
                    uploadCode--;
                } else if(uploadCode == 1){
                    iv_4.setImageBitmap(bitmap);
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
                getFragmentManager().popBackStack();
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
                }
                break;
            case R.id.iv_2:
                if(uploadCode == 3){
                    upLoadPic();
                }
                break;
            case R.id.iv_3:
                if(uploadCode == 2){
                    upLoadPic();
                }
                break;
            case R.id.iv_4:
                if(uploadCode == 1){
                    upLoadPic();
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
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(outputUri));

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
                        ApiRequest.uploadIdentity(file,"RightFragment_uploadIdentity",pb_loading);
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
