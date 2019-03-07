package com.saiyu.foreground.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.R;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.interfaces.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class DialogUtils {

    public static void showDialog(Activity activity, String title, String content, String cancel, String confirm, final OnClickListener mOnClickListener) {
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

    private static String wayId,account,remarks,accountId;
    public static void showCashChannelReviseDialog(final Activity activity, CashRet.DatasBean.ItemsBean itemsBean, final List<CashChannelRet.DatasBean.ItemsBean> mItems, final DialogClickCallbackListener dialogClickCallbackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_revise_cashchannel, null);
        Button btn_dialog_cancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btn_dialog_confirm = view.findViewById(R.id.btn_dialog_confirm);
        final EditText et_count = view.findViewById(R.id.et_count);
        final EditText et_remark = view.findViewById(R.id.et_remark);
        TextView tv_charge = view.findViewById(R.id.tv_charge);
        TextView tv_time = view.findViewById(R.id.tv_time);
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
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wayId = mItems.get(position).getId();
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
                if(dialogClickCallbackListener != null){
                    account = et_count.getText().toString();
                    remarks = et_remark.getText().toString();
                    if(TextUtils.isEmpty(account)){
                        Toast.makeText(activity,"请输入账号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogClickCallbackListener.setOnDialogClickCallbackListener(wayId,account,remarks,accountId);
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

    public interface DialogClickCallbackListener{
        void setOnDialogClickCallbackListener(String wayId,String account,String remarks,String accountId);
    }

    public interface DialogDismissCallbackListener{
        void setOnDialogDismissListener();
    }

}
