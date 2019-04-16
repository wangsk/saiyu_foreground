package com.saiyu.foreground.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.interfaces.OnClickListener;
import com.saiyu.foreground.interfaces.OnListCallbackListener;
import com.saiyu.foreground.zxinglibrary.encode.CodeCreator;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogUtils {

    public static void showDialog(Activity activity, String title, String content, String cancel, String confirm, final OnClickListener mOnClickListener) {
        if(activity == null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_bind, null);
        Button btn_dialog_cancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btn_dialog_confirm = view.findViewById(R.id.btn_dialog_confirm);
        TextView tv_dialog_title = view.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_content = view.findViewById(R.id.tv_dialog_content);
        tv_dialog_title.setText(title);
        if(TextUtils.isEmpty(content)){
            tv_dialog_content.setVisibility(View.GONE);
        } else {
            tv_dialog_content.setText(content);
            tv_dialog_content.setVisibility(View.VISIBLE);
        }

        btn_dialog_cancel.setText(cancel);
        btn_dialog_confirm.setText(confirm);

        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(dialogDismissCallbackListener != null){
//                    dialogDismissCallbackListener.setOnDialogDismissListener();
//                }
//            }
//        });
        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnClickListener != null){
                    mOnClickListener.onClick(view);
                }
                alertDialog.dismiss();
            }
        });
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showOrderSuccessDialog(Activity activity, String title, final OnClickListener mOnClickListener, final OnClickListener mOnClickListener_2) {
        if(activity == null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_ordersubmit_success, null);
        Button btn_dialog_cancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btn_dialog_confirm = view.findViewById(R.id.btn_dialog_confirm);
        TextView tv_dialog_title = view.findViewById(R.id.tv_content);
        tv_dialog_title.setText("您的出售订单"+title+"，已经提交完成，进入预审和确认流程，确认完毕后货款将转入你的赛鱼余额，请您耐心等待！");

        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(dialogDismissCallbackListener != null){
//                    dialogDismissCallbackListener.setOnDialogDismissListener();
//                }
//            }
//        });
        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnClickListener != null){
                    mOnClickListener.onClick(view);
                }
                alertDialog.dismiss();
            }
        });
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnClickListener_2 != null){
                    mOnClickListener_2.onClick(view);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showOrderPswDialog(Activity activity, String orderNum,final OnListCallbackListener listCallbackListener) {
        if(activity == null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_order_psw, null);
        Button btn_dialog_confirm = view.findViewById(R.id.btn_confirm);
        TextView tv_ordernum = view.findViewById(R.id.tv_ordernum);
        final EditText et_psw = view.findViewById(R.id.et_psw);
        tv_ordernum.setText("订单"+orderNum+"为加密订单，需要输入接单密码方可接单!");

        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(dialogDismissCallbackListener != null){
//                    dialogDismissCallbackListener.setOnDialogDismissListener();
//                }
//            }
//        });
        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listCallbackListener != null){
                    List<String> callbackList = new ArrayList<>();
                    callbackList.add(et_psw.getText().toString());
                    listCallbackListener.setOnListCallbackListener(callbackList);
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private  static Province province = null;
    private static City city = null;
    private static List<Province> list = new ArrayList<Province>();
    private static ArrayAdapter<Province> arrayAdapter1;
    private static ArrayAdapter<City> arrayAdapter2;
    public static void showOrderSubmitDialog(final Activity activity,final OnListCallbackListener listCallbackListener) {
        if(activity == null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_order_submit, null);
        final EditText et_qq = view.findViewById(R.id.et_qq);
        final EditText et_psw = view.findViewById(R.id.et_psw);
        Button btn_dialog_confirm = view.findViewById(R.id.btn_confirm);
        Spinner sp_province = view.findViewById(R.id.sp_province);
        final Spinner sp_city = view.findViewById(R.id.sp_city);

        list= Utils.parser(activity);

        arrayAdapter1 = new ArrayAdapter<Province>(activity,R.layout.item_spinner, R.id.tv_spinner,list);
        arrayAdapter2 = new ArrayAdapter<City>(activity,R.layout.item_spinner, R.id.tv_spinner,list.get(0).getCitys());

        sp_province.setAdapter(arrayAdapter1);
        sp_province.setSelection(0, true);
        sp_city.setAdapter(arrayAdapter2);
        sp_city.setSelection(0, true);

        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = list.get(position);
                arrayAdapter2 = new ArrayAdapter<City>(activity, R.layout.item_spinner, R.id.tv_spinner, list.get(position).getCitys());
                sp_city.setAdapter(arrayAdapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = province.getCitys().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if(dialogDismissCallbackListener != null){
//                    dialogDismissCallbackListener.setOnDialogDismissListener();
//                }
//            }
//        });
        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listCallbackListener != null){
                    String proName = "",cityName = "";
                    if(province != null){
                        proName = province.getName();
                    }
                    if(city != null){
                        cityName = city.getName();
                    }
                    LogUtils.print("qq " + et_qq.getText().toString() + " psw " + et_psw.getText().toString() + " 省份 " + proName + " 城市 " + cityName);
                    List<String> callbackList = new ArrayList<>();
                    callbackList.add(et_qq.getText().toString());
                    callbackList.add(et_psw.getText().toString());
                    callbackList.add(proName);
                    callbackList.add(cityName);
                    listCallbackListener.setOnListCallbackListener(callbackList);
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private static String wayId,account,remarks,accountId;
    private static int type;
    public static void showCashChannelReviseDialog(final Activity activity, CashRet.DatasBean.ItemsBean itemsBean, final List<CashChannelRet.DatasBean.ItemsBean> mItems, final OnListCallbackListener listCallbackListener) {
        if(activity == null){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_revise_cashchannel, null);
        final LinearLayout ll_remark = view.findViewById(R.id.ll_remark);
        final LinearLayout ll_account = view.findViewById(R.id.ll_account);
        final LinearLayout ll_wechat = view.findViewById(R.id.ll_wechat);
        final ImageView iv_wechat = view.findViewById(R.id.iv_wechat);
        Button btn_dialog_cancel = view.findViewById(R.id.btn_dialog_cancel);
        final Button btn_dialog_confirm = view.findViewById(R.id.btn_dialog_confirm);
        final EditText et_count = view.findViewById(R.id.et_count);
        final EditText et_remark = view.findViewById(R.id.et_remark);
        final TextView tv_charge = view.findViewById(R.id.tv_charge);
        final TextView tv_time = view.findViewById(R.id.tv_time);
        Spinner spinner = view.findViewById(R.id.spinner);
        if(itemsBean != null){
            tv_charge.setText(itemsBean.getCharge()+"%");
            tv_time.setText(itemsBean.getPayDateStr());

            accountId = itemsBean.getId();
        }

        if(mItems == null || mItems.size() <= 0){
            return;
        }

        List<String> list = new ArrayList<String>();
        for(CashChannelRet.DatasBean.ItemsBean item : mItems){
            list.add(item.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.item_spinner, R.id.tv_spinner,list);
        spinner.setAdapter(adapter);

        for(int i = 0; i < list.size(); i++){
            if(!TextUtils.isEmpty(mItems.get(i).getId()) && mItems.get(i).getId().equals(itemsBean.getWithdrawWayId())){
                spinner.setSelection(i);
                tv_charge.setText(mItems.get(i).getWithdrawWayConfigCharge()+"%");
                tv_time.setText(mItems.get(i).getPayDateStr());
                type = mItems.get(i).getType();
                switch (type){
                    case 0:
                        ll_wechat.setVisibility(View.GONE);
                        ll_remark.setVisibility(View.VISIBLE);
                        ll_account.setVisibility(View.VISIBLE);
                        et_count.setHint("请输入银行卡号");
                        break;
                    case 1:
                        ll_wechat.setVisibility(View.GONE);
                        ll_remark.setVisibility(View.VISIBLE);
                        ll_account.setVisibility(View.VISIBLE);
                        et_count.setHint("请输入支付宝账号");
                        break;
                    case 2:
                        ll_wechat.setVisibility(View.VISIBLE);
                        ll_remark.setVisibility(View.GONE);
                        ll_account.setVisibility(View.GONE);
                        String ImgUrl = mItems.get(i).getImgUrl();
                        if(!TextUtils.isEmpty(ImgUrl)){
                            final Bitmap bitmap = CodeCreator.createQRCode(ImgUrl, 400, 400, null);
                            if (bitmap != null) {
                                iv_wechat.setImageBitmap(bitmap);
                                iv_wechat.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        Utils.loadImage(activity,bitmap);
                                        return true;
                                    }
                                });
                            }
                        }
                        break;
                }
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_charge.setText(mItems.get(position).getWithdrawWayConfigCharge()+"%");
                tv_time.setText(mItems.get(position).getPayDateStr());
                wayId = mItems.get(position).getId();
                type = mItems.get(position).getType();
                switch (type){
                    case 0:
                        ll_wechat.setVisibility(View.GONE);
                        ll_remark.setVisibility(View.VISIBLE);
                        ll_account.setVisibility(View.VISIBLE);
                        et_count.setHint("请输入银行卡号");
                        break;
                    case 1:
                        ll_wechat.setVisibility(View.GONE);
                        ll_remark.setVisibility(View.VISIBLE);
                        ll_account.setVisibility(View.VISIBLE);
                        et_count.setHint("请输入支付宝账号");
                        break;
                    case 2:
                        ll_wechat.setVisibility(View.VISIBLE);
                        ll_remark.setVisibility(View.GONE);
                        ll_account.setVisibility(View.GONE);
                        String ImgUrl = mItems.get(position).getImgUrl();
                        if(!TextUtils.isEmpty(ImgUrl)){
                            final Bitmap bitmap = CodeCreator.createQRCode(ImgUrl, 400, 400, null);
                            if (bitmap != null) {
                                iv_wechat.setImageBitmap(bitmap);
                                iv_wechat.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        Utils.loadImage(activity,bitmap);
                                        return true;
                                    }
                                });
                            }
                        }
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();

        btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listCallbackListener != null){
                    account = et_count.getText().toString();
                    remarks = et_remark.getText().toString();
                    List<String> callbackList = new ArrayList<>();
                    callbackList.add(wayId);
                    callbackList.add(account);
                    callbackList.add(remarks);
                    callbackList.add(accountId);
                    callbackList.add(String.valueOf(type));
                    listCallbackListener.setOnListCallbackListener(callbackList);
                }
                alertDialog.dismiss();
            }
        });
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public interface DialogDismissCallbackListener{
        void setOnDialogDismissListener();
    }

}
