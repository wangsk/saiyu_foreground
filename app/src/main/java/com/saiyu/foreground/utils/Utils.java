package com.saiyu.foreground.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.ui.activitys.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {
    public static boolean isWeixinAvilible() {
        final PackageManager packageManager = App.getApp().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    public static boolean hasApplication() {
        PackageManager manager = App.getApp().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    public static int getintVerName(String verName) {
        verName = verName.replaceAll("\\.", "");
        if (TextUtils.isEmpty(verName)) {
            return 0;
        } else {
            return Integer.parseInt(verName);
        }
    }

    /**
     * 隐藏键盘
     */
    public static void hideInput() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.getBaseActivity().getSystemService(INPUT_METHOD_SERVICE);
        View v = BaseActivity.getBaseActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void loadImage(final Bitmap bitmap) {
        RxPermissions rxPermissions = new RxPermissions(BaseActivity.getBaseActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(!aBoolean){
                            Toast.makeText(App.getApp(),"请开启读写权限",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        saveBitmap(bitmap);

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static String saveBitmap(Bitmap bitmap) {
        try {
            // 获取内置SD卡路径
            String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            // 图片文件路径
            File file = new File(sdCardPath);
            File[] files = file.listFiles();
            if(files != null){
                for(File file1 : files){
                    String name = file1.getName();
                    if(name.endsWith("twocode.png")){
                        boolean flag = file1.delete();
                        LogUtils.print("删除 + " + flag);
                    }
                }
            }
            String filePath = sdCardPath + "/twocode.png";
            file = new File(filePath);
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(App.getApp().getContentResolver(),
                    file.getAbsolutePath(), "twocode.png", null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            App.getApp().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            Toast.makeText(App.getApp(),"二维码保存成功",Toast.LENGTH_SHORT).show();

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<Province> parser(final Activity activity){
        List<Province>list =null;
        Province province = null;

        List<City>cities = null;
        City city = null;

        List<District>districts = null;
        District district = null;

        // 创建解析器，并制定解析的xml文件
        XmlResourceParser parser = activity.getResources().getXml(R.xml.cities);
        try{
            int type = parser.getEventType();
            while(type!=1) {
                String tag = parser.getName();//获得标签名
                switch (type) {
                    case XmlResourceParser.START_DOCUMENT:
                        list = new ArrayList<Province>();
                        break;
                    case XmlResourceParser.START_TAG:
                        if ("p".equals(tag)) {
                            province=new Province();
                            cities = new ArrayList<City>();
                            int n =parser.getAttributeCount();
                            for(int i=0 ;i<n;i++){
                                //获得属性的名和值
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if("p_id".equals(name)){
                                    province.setId(value);
                                }
                            }
                        }
                        if ("pn".equals(tag)){//省名字
                            province.setName(parser.nextText());
                        }
                        if ("c".equals(tag)){//城市
                            city = new City();
                            districts = new ArrayList<District>();
                            int n =parser.getAttributeCount();
                            for(int i=0 ;i<n;i++){
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if("c_id".equals(name)){
                                    city.setId(value);
                                }
                            }
                        }
                        if ("cn".equals(tag)){
                            city.setName(parser.nextText());
                        }
                        if ("d".equals(tag)){
                            district = new District();
                            int n =parser.getAttributeCount();
                            for(int i=0 ;i<n;i++){
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if("d_id".equals(name)){
                                    district.setId(value);
                                }
                            }
                            district.setName(parser.nextText());
                            districts.add(district);
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if ("c".equals(tag)){
                            city.setDistricts(districts);
                            cities.add(city);
                        }
                        if("p".equals(tag)){
                            province.setCitys(cities);
                            list.add(province);
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        }catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */
        catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public static void copyContent(String content){
        if (!TextUtils.isEmpty(content)) {
            ClipboardManager cm = (ClipboardManager) App.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(content);
            Toast.makeText(App.getApp(), "复制成功", Toast.LENGTH_SHORT).show();
        } else return;
    }

    public static void setExtraView(List<String>extraList,TextView tv_2_2,TextView tv_2_3,TextView tv_2_4,TextView tv_2_5,TextView tv_2_6){
        switch (extraList.size()){
            case 1:
                tv_2_2.setText(extraList.get(0));
                tv_2_2.setVisibility(View.VISIBLE);
                tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                break;
            case 2:
                tv_2_2.setText(extraList.get(0));
                tv_2_2.setVisibility(View.VISIBLE);
                tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_3.setText(extraList.get(1));
                tv_2_3.setVisibility(View.VISIBLE);
                tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                break;
            case 3:
                tv_2_2.setText(extraList.get(0));
                tv_2_2.setVisibility(View.VISIBLE);
                tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_3.setText(extraList.get(1));
                tv_2_3.setVisibility(View.VISIBLE);
                tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_4.setText(extraList.get(2));
                tv_2_4.setVisibility(View.VISIBLE);
                tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                break;
            case 4:
                tv_2_2.setText(extraList.get(0));
                tv_2_2.setVisibility(View.VISIBLE);
                tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_3.setText(extraList.get(1));
                tv_2_3.setVisibility(View.VISIBLE);
                tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_4.setText(extraList.get(2));
                tv_2_4.setVisibility(View.VISIBLE);
                tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_5.setText(extraList.get(3));
                tv_2_5.setVisibility(View.VISIBLE);
                tv_2_5.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                break;
            case 5:
                tv_2_2.setText(extraList.get(0));
                tv_2_2.setVisibility(View.VISIBLE);
                tv_2_2.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_3.setText(extraList.get(1));
                tv_2_3.setVisibility(View.VISIBLE);
                tv_2_3.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_4.setText(extraList.get(2));
                tv_2_4.setVisibility(View.VISIBLE);
                tv_2_4.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_5.setText(extraList.get(3));
                tv_2_5.setVisibility(View.VISIBLE);
                tv_2_5.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                tv_2_6.setText(extraList.get(4));
                tv_2_6.setVisibility(View.VISIBLE);
                tv_2_6.setBackground(App.getApp().getResources().getDrawable(R.drawable.border_colorline_yellow));
                break;
        }
    }


    public static void setButtonClickable(Button button, boolean flag){
        if(button == null){
            return;
        }
        if(flag){
            button.setClickable(true);
            button.setFocusable(true);
            button.setBackground(App.getApp().getResources().getDrawable(R.drawable.shape_bg_blue));
        } else {
            button.setClickable(false);
            button.setFocusable(false);
            button.setBackground(App.getApp().getResources().getDrawable(R.drawable.shape_bg_grey));
        }
    }

}
