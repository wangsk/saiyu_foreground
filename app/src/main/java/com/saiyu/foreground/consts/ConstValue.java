package com.saiyu.foreground.consts;

public class ConstValue {

    public static boolean flag = true;//是否打开log打印

    public static final String SERVR_URL = "http://192.168.1.147:66/";//测试地址
//    public static final String SERVR_URL = "http://work.saiyu.com/manageApi/";//线上地址
    public static String MetaTypeJson = "application/json; charset=utf-8";
    public static final String APPID = "SaiyuAndroidApp";
    public static final String APPSecret = "XdEUpQjvih5KC4ZiMkXwFvNfRvvMED38";
    public static String UserSecret;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER_KEY = "userKey";
    //自动登录的标记
    public static final String AUTO_LOGIN_FLAG = "autologinflag";

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
    public static final String nameCheck = "[\\u4e00-\\u9fa5]+";
    //身份证验证
    public static final String identityCheck = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

}
