package com.saiyu.foreground.consts;

public class ConstValue {

    public static boolean flag = false;//是否打开log打印

//    public static final String SERVR_URL = "http://192.168.1.147:66/";//测试地址
//    public static final String SERVR_URL = "http://192.168.1.147:88/";//测试断点地址
    public static final String SERVR_URL = "https://api.saiyu.com/";//线上地址
    public static String MetaTypeJson = "application/json; charset=utf-8";
    public static final String APPID = "SaiyuAndroidApp";
    public static final String APPSecret = "ammLYNXrC822OoEDqWyIt8Twf9czIqsl";
    public static String UserSecret;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String OrderImgServerProcess = "OrderImgServerProcess";
    public static final String UserImgServerProcess = "UserImgServerProcess";

    public static final String ISFIRSTRUNNING = "isFirstRunning";

    public static final int ACTION_PHOTOGRAPH = 201;//拍照的请求码
    public static final int ACTION_TRIM_IMAGE = 202;//相册的请求码
    public static final int ACTION_ALBUM = 203;//裁剪照片的请求码

    public static final String MainBottomVisibleType = "mainBottomVisibleType";//主界面下面显示的item个数，（0：全部显示:1：不显示买家:2：不显示卖家）
    public static final String IdentifyInfo = "identifyInfo";//身份信息

    /**
     * ==========================第三方登录=================================
     **/
    public static final String QQ_APP_ID = "101474081";
    public static final String QQ_APP_KEY = "8cff2cc1e9454a13746239fa104af37e";

    public static final String WECHAT_APP_ID = "wxf5a0ce95a89ddf50";
    public static final String WECHAT_APP_SECRET = "b61a3de76c3da2b6995f2d0c13932456";

    public static final String QQ_UNIONID = "qqunionid";
    public static final String WECHAT_UNIONID = "wechatunionid";

    //    手机正则
    public static final String REGEX_PHONE = "^((1[0-9]{1})+\\d{9})$";
    //    邮箱正则
    public static final String REGEX_EMAIL = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    //检测注册账号合法性
    public static final String checkAccout = "^[a-zA-Z0-9]{5,20}$";
    public static final String checkAccout_2 = "^[0-9]{5,20}$";
    public static final String checkAccout_3 = "^[a-zA-Z]{5,20}$";
    public static final String checkAccout_4 = "^[0-9]+";
    //姓名验证
   // public static final String nameCheck = "[\\u4e00-\\u9fa5]+";
    //身份证验证
   // public static final String identityCheck = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

}
