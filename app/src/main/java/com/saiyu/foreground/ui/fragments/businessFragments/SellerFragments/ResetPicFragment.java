package com.saiyu.foreground.ui.fragments.businessFragments.SellerFragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.adapters.MyArrayAdapter;
import com.saiyu.foreground.consts.ConstValue;
import com.saiyu.foreground.https.ApiRequest;
import com.saiyu.foreground.https.response.BaseRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.https.response.SellerOrderReceiveInfoRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.ui.fragments.BaseFragment;
import com.saiyu.foreground.ui.views.PhotoViewDialog;
import com.saiyu.foreground.ui.views.RxDialogChooseImage;
import com.saiyu.foreground.utils.CallbackUtils;
import com.saiyu.foreground.utils.LogUtils;
import com.saiyu.foreground.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxPhotoTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.saiyu.foreground.ui.views.RxDialogChooseImage.LayoutType.TITLE;

@EFragment(R.layout.fragment_resetpic)
public class ResetPicFragment extends BaseFragment implements CallbackUtils.ResponseCallback,CallbackUtils.OnActivityCallBack{
    @ViewById
    TextView tv_title_content,tv_recharge_prompt,tv_get,tv_record_upload,tv_info_upload,tv_success_upload;
    @ViewById
    Button btn_title_back,btn_confirm,btn_revise;
    @ViewById
    EditText et_count;
    @ViewById
    Spinner sp_selector;
    @ViewById
    ProgressBar pb_loading;
    @ViewById
    TextView tv_ordernum,tv_creattime,tv_completetime,tv_recharge_time,tv_autoconfirmtime,tv_ordercount,tv_successcount,
            tv_successprice,tv_money,iv_serviceprice,tv_total,tv_recharge_product,
            tv_product_type,tv_recharge_num,tv_once_limit,tv_pic,tv_confirm_type,tv_averagetime,
            tv_contacttype,tv_rechargeremark;
    @ViewById
    ImageView iv_recharge_success_show,iv_recharge_info_show,iv_record_show,iv_success_del,iv_info_del,iv_record_del;
    private String orderId;
    private String successUrl,infoUrl,recordUrl;
    private int uploadCode = 0;
    public static List<HallDetailRet.DataBean.YanBaoBean> selectorList = null;
    private MyArrayAdapter selectorAdapter;//元宝spinner下拉数据绑定adapter
    private int type;//区别普通商品 : 0 ，金元宝 : 1 ，银元宝 : 2

    private String rechargeNum;//出售Q币数量
    private String ReceiveQBCount,ReserveDiscount, ServiceRate,LiquidatedMoney;
    private float LessChargeDiscount,OnceMinCount;

    public static ResetPicFragment newInstance(Bundle bundle) {
        ResetPicFragment_ fragment = new ResetPicFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        CallbackUtils.setOnActivityCallBack(this);
        CallbackUtils.setCallback(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            orderId = bundle.getString("orderId");
        }
        if(!TextUtils.isEmpty(orderId)){
            ApiRequest.orderReceiveInfoSeller(orderId,"SellerOrderDetailFragment_orderReceiveInfoSeller",pb_loading);
        }
    }

    @AfterViews
    void afterViews() {
        tv_title_content.setText("重新传图");
    }

    @Override
    public void setOnResponseCallback(String method, BaseRet baseRet) {
        if (baseRet == null || TextUtils.isEmpty(method)) {
            return;
        }
        if (method.equals("SellerOrderDetailFragment_orderReceiveInfoSeller")) {
            SellerOrderReceiveInfoRet ret = (SellerOrderReceiveInfoRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            tv_ordernum.setText(ret.getData().getReceiveOrderNum());//充值订单号
            tv_creattime.setText(ret.getData().getCreateTime());//接单时间
            tv_completetime.setText(ret.getData().getFinishTime());//完成时间
            tv_recharge_time.setText(ret.getData().getRechargeTime());//充值时间
            tv_autoconfirmtime.setText(ret.getData().getAutoConfirmTime());//自动确认时间
            tv_ordercount.setText(ret.getData().getReserveQBCount());//接单数量
            tv_successcount.setText(ret.getData().getSuccQBCount());//成功数量
            tv_successprice.setText(ret.getData().getSuccMoney()+"元");//成功金额
            tv_money.setText(ret.getData().getLiquidatedMoney()+"元");//违约金
            iv_serviceprice.setText(ret.getData().getServiceMoney()+"元");//服务费
            tv_total.setText(ret.getData().getTotalMoney());//贷款金额
            tv_recharge_product.setText(ret.getData().getProductName());
            tv_product_type.setText(ret.getData().getProductTypeName());
            tv_recharge_num.setText(ret.getData().getReserveAccount());

            if(ret.getData().getIsPicConfirm() == 0){//支持验图确认
                tv_pic.setText("否");
            } else {
                tv_pic.setText("是");
            }
            tv_confirm_type.setText(ret.getData().getContactType());
            tv_averagetime.setText(ret.getData().getAverageConfirmTime());//平均确认时间
            tv_contacttype.setText("手机"+ret.getData().getContactMobile()+";qq"+ret.getData().getContactQQ()+";\n"+ret.getData().getIsAllowShowContactStr());//联系方式
            tv_rechargeremark.setText(ret.getData().getRemarks());//充值留言

            LessChargeDiscount = ret.getData().getLessChargeDiscount();
            OnceMinCount = ret.getData().getOnceMinCount();
            if(OnceMinCount == 0){
                tv_once_limit.setText("单次数量不限制");
            } else {
                if(LessChargeDiscount == 1){
                    tv_once_limit.setText(OnceMinCount+"Q币 少充按原价");
                } else {
                    tv_once_limit.setText(OnceMinCount+"Q币 少充则=" + LessChargeDiscount*100 + "%");
                }
            }

            ReceiveQBCount = ret.getData().getSuccQBCount();
            ReserveDiscount = ret.getData().getReserveDiscount();//折扣
            ServiceRate = ret.getData().getServiceRate();//服务费率
            LiquidatedMoney = ret.getData().getLiquidatedMoney();//违约金

            tv_get.setText(setFormula(ReceiveQBCount));

            if(!TextUtils.isEmpty(ret.getData().getPic_BillRecord())){
                recordUrl = ret.getData().getPic_BillRecord();
                LogUtils.print("recordUrl=== " + recordUrl);
                iv_record_del.setVisibility(View.VISIBLE);
                tv_record_upload.setVisibility(View.GONE);
                Glide.with(App.getApp())
                        .load(recordUrl)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_record_show);
            }
            if(!TextUtils.isEmpty(ret.getData().getPic_RechargeSucc())){
                successUrl = ret.getData().getPic_RechargeSucc();
                LogUtils.print("successUrl=== " + successUrl);
                iv_success_del.setVisibility(View.VISIBLE);
                tv_success_upload.setVisibility(View.GONE);
                Glide.with(App.getApp())
                        .load(successUrl)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_recharge_success_show);
            }
            if(!TextUtils.isEmpty(ret.getData().getPic_TradeInfo())){
                infoUrl = ret.getData().getPic_TradeInfo();
                LogUtils.print("infoUrl=== " + infoUrl);
                iv_info_del.setVisibility(View.VISIBLE);
                tv_info_upload.setVisibility(View.GONE);
                Glide.with(App.getApp())
                        .load(infoUrl)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_recharge_info_show);
            }

            initYuanBaoList(ret);

        } else  if(method.equals("SellerOrderDetailFragment_uploadIdentity")){
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
                } else if(uploadCode == 1){
                    infoUrl = ret.getData().getSrc();
                    LogUtils.print("infoUrl=== " + infoUrl);
                    iv_info_del.setVisibility(View.VISIBLE);
                    tv_info_upload.setVisibility(View.GONE);
                } else if(uploadCode == 2){
                    recordUrl = ret.getData().getSrc();
                    LogUtils.print("recordUrl=== " + recordUrl);
                    iv_record_del.setVisibility(View.VISIBLE);
                    tv_record_upload.setVisibility(View.GONE);
                }
            }
        } else if(method.equals("ResetPicFragment_orderReceiveReSubmit")){
            BooleanRet ret = (BooleanRet)baseRet;
            if(ret.getData() == null){
                return;
            }
            if(ret.getData().isResult()){
                Toast.makeText(mContext,"重新传图成功",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }
    }

    @Click({R.id.btn_title_back,R.id.btn_confirm,R.id.tv_record_upload, R.id.tv_info_upload, R.id.tv_success_upload,R.id.btn_revise,R.id.iv_success_del,R.id.iv_info_del,R.id.iv_record_del,R.id.iv_recharge_info_show,R.id.iv_recharge_success_show,R.id.iv_record_show})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_title_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(infoUrl)){
                    Toast.makeText(mContext,"请上传详情图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiRequest.orderReceiveReSubmit(orderId,rechargeNum,successUrl,infoUrl,recordUrl,"ResetPicFragment_orderReceiveReSubmit",pb_loading);
                break;
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
                    photoViewDialog.setmUrl(infoUrl);
                    photoViewDialog.show();
                } else {
                    uploadCode = 1;
                    upLoadPic();
                }
                break;
            case R.id.iv_recharge_success_show:
                if(!TextUtils.isEmpty(successUrl)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(successUrl);
                    photoViewDialog.show();
                }else {
                    uploadCode = 0;
                    upLoadPic();
                }
                break;
            case R.id.iv_record_show:
                if(!TextUtils.isEmpty(recordUrl)){
                    PhotoViewDialog photoViewDialog = new PhotoViewDialog(getActivity());
                    photoViewDialog.setmUrl(recordUrl);
                    photoViewDialog.show();
                }else {
                    uploadCode = 2;
                    upLoadPic();
                }
                break;
            case R.id.btn_revise:
                switch (type) {
                    case 0:
                        et_count.setText(ReceiveQBCount);
                        if (!TextUtils.isEmpty(ReceiveQBCount)) {
                            et_count.setSelection(ReceiveQBCount.length());
                        }
                        break;
                    case 1:
                    case 2:
                        for (int i = 0; i < selectorList.size(); i++) {
                            if (ReceiveQBCount.equals(selectorList.get(i).getValue())) {
                                sp_selector.setSelection(i);
                                break;
                            }
                        }
                        break;
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
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(outputUri));

                        if(uploadCode == 0){
                            iv_recharge_success_show.setImageBitmap(bitmap);
                        } else if(uploadCode == 1){
                            iv_recharge_info_show.setImageBitmap(bitmap);
                        } else if(uploadCode == 2){
                            iv_record_show.setImageBitmap(bitmap);
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
                        ApiRequest.uploadIdentity(file,"SellerOrderDetailFragment_uploadIdentity",pb_loading);
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

    //初始化输入框
    private void initYuanBaoList(SellerOrderReceiveInfoRet ret){
        type = ret.getData().getProductType();
        LogUtils.print("type === " + type);
        switch (type) {
            case 0:
                et_count.setText(ReceiveQBCount);
                et_count.setVisibility(View.VISIBLE);
                sp_selector.setVisibility(View.GONE);
                break;
            case 1://金元宝
                selectorList = ret.getData().getGoldList();
            case 2://银元宝
                selectorList = ret.getData().getSilverList();

                et_count.setVisibility(View.GONE);
                sp_selector.setVisibility(View.VISIBLE);
                selectorAdapter = new MyArrayAdapter(selectorList);
                sp_selector.setAdapter(selectorAdapter);

                for (int i = 0; i < selectorList.size(); i++) {
                    if (ReceiveQBCount.equals(selectorList.get(i).getValue())) {
                        sp_selector.setSelection(i);
                        break;
                    }
                }

                sp_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (selectorList == null || selectorList.size() <= position) {
                            return;
                        }
                        rechargeNum = selectorList.get(position).getValue();
                        isCountAble(rechargeNum);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;

        }

    }

    @FocusChange({R.id.et_count})
    void focusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            String text = et_count.getText().toString();
            if(!TextUtils.isEmpty(text)){
                et_count.setSelection(text.length());
            }
        }
    }

    @TextChange({R.id.et_count})
    void textChange(CharSequence s, TextView hello, int before, int start, int count) {
        rechargeNum = s.toString();
        isCountAble(rechargeNum);
    }

    //实际充值数量是否可用
    private void isCountAble(String count) {
        if (TextUtils.isEmpty(count)) {
            Utils.setButtonClickable(btn_confirm,false);
            return;
        }

        float qbCount = Float.parseFloat(count);
        if (qbCount < OnceMinCount) {
            tv_recharge_prompt.setText("最低成功数量为" + OnceMinCount + "Q币，请修改数量！");
            btn_revise.setVisibility(View.VISIBLE);
            btn_revise.setText("取消修改");
            Utils.setButtonClickable(btn_confirm,false);
            return;
        } else {
            tv_recharge_prompt.setText("");
            btn_revise.setVisibility(View.INVISIBLE);
        }

        float param = 0;

        if (!TextUtils.isEmpty(ReceiveQBCount)) {
            param = Float.parseFloat(ReceiveQBCount);
        }

        if (qbCount > param) {
            tv_recharge_prompt.setText("不允许输入大于接单数量，如果多充请另行接单！");
            btn_revise.setVisibility(View.VISIBLE);
            btn_revise.setText("新建此单");
            Utils.setButtonClickable(btn_confirm,false);
            return;
        } else {
            tv_recharge_prompt.setText("");
            btn_revise.setVisibility(View.INVISIBLE);
        }

        if (qbCount != param && LessChargeDiscount != 1) {
            tv_recharge_prompt.setText("低于接单数量视为违约，将扣除5%违约金，请慎重！");
        }

        tv_get.setText(setFormula(count));
        Utils.setButtonClickable(btn_confirm,true);


    }

    //计算公式
    private Spanned setFormula(String param_1) {
        BigDecimal bd_1 = new BigDecimal(param_1);//充值金额
        BigDecimal bd_2 = new BigDecimal(ReserveDiscount);//折扣
        BigDecimal bd_3 = new BigDecimal(ServiceRate);//服务费率
        BigDecimal bd_4 = bd_1.multiply(bd_2).setScale(2, BigDecimal.ROUND_HALF_UP);//产品金额
        BigDecimal bd_6 = bd_4.multiply(bd_3).setScale(2, BigDecimal.ROUND_HALF_UP);//服务费
        BigDecimal bd_7;
        if (LessChargeDiscount == 1) {
            //没有违约金
            bd_7 = new BigDecimal("0");
        } else {
            //有违约金
            BigDecimal bd_5 = new BigDecimal(LessChargeDiscount);
            bd_7 = bd_4.multiply(bd_5).setScale(2, BigDecimal.ROUND_HALF_UP);//违约金
        }
        BigDecimal bd_8 = bd_4.subtract(bd_6).subtract(bd_7);

        String text = "获得: " + "<font color = \"#5069d5\">" + param_1 + "</font>" + "Q币 * " + "<font color = \"#5069d5\">" + ReserveDiscount + "</font>" + "折 - " + "<font color = \"#5069d5\">" + bd_6.toString() + "</font>" + "元服务费 - " + "<font color = \"#5069d5\">" + bd_7.toString() + "</font>" + "元违约金 = " + "<font color = \"#5069d5\">" + bd_8.toString() + "</font>" + "元";
        return Html.fromHtml(text);
    }

}
