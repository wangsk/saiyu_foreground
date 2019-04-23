package com.saiyu.foreground.https;

import com.saiyu.foreground.https.response.AccountInfoLoginRet;
import com.saiyu.foreground.https.response.AccountInfoNoLoginRet;
import com.saiyu.foreground.https.response.ActiveStatusRet;
import com.saiyu.foreground.https.response.AppVersionRet;
import com.saiyu.foreground.https.response.BooleanRet;
import com.saiyu.foreground.https.response.BuyerInfoRet;
import com.saiyu.foreground.https.response.BuyerOrderInfoRet;
import com.saiyu.foreground.https.response.BuyerWebListRet;
import com.saiyu.foreground.https.response.CancelOrderInfoRet;
import com.saiyu.foreground.https.response.CashChannelRet;
import com.saiyu.foreground.https.response.CashDetailRet;
import com.saiyu.foreground.https.response.CashRecordRet;
import com.saiyu.foreground.https.response.CashRet;
import com.saiyu.foreground.https.response.FaceStatusRet;
import com.saiyu.foreground.https.response.GetImgProcessRet;
import com.saiyu.foreground.https.response.HallDetailReceiveRet;
import com.saiyu.foreground.https.response.HallDetailRet;
import com.saiyu.foreground.https.response.HallRet;
import com.saiyu.foreground.https.response.IsAccountExistRet;
import com.saiyu.foreground.https.response.IsCountDoRet;
import com.saiyu.foreground.https.response.LoginRecordRet;
import com.saiyu.foreground.https.response.LoginRet;
import com.saiyu.foreground.https.response.OrderFailReasonRet;
import com.saiyu.foreground.https.response.OrderLogRet;
import com.saiyu.foreground.https.response.OrderNumRet;
import com.saiyu.foreground.https.response.OrderPauseRet;
import com.saiyu.foreground.https.response.OrderReceiveConfirmRet;
import com.saiyu.foreground.https.response.OrderReceiveHistoryRet;
import com.saiyu.foreground.https.response.OrderSettlementRet;
import com.saiyu.foreground.https.response.PreOrderHistoryRet;
import com.saiyu.foreground.https.response.PreOrderManagerRet;
import com.saiyu.foreground.https.response.ProductListRet;
import com.saiyu.foreground.https.response.ProductPropertyRet;
import com.saiyu.foreground.https.response.QBListRet;
import com.saiyu.foreground.https.response.RealNameStatusRet;
import com.saiyu.foreground.https.response.ReceivePointRet;
import com.saiyu.foreground.https.response.RechargeOrderInfoRet;
import com.saiyu.foreground.https.response.RechargeOrderManageRet;
import com.saiyu.foreground.https.response.RechargePointHistoryRet;
import com.saiyu.foreground.https.response.RechargeRateRet;
import com.saiyu.foreground.https.response.RechargeRecordRet;
import com.saiyu.foreground.https.response.RechargeStreamRet;
import com.saiyu.foreground.https.response.RegistRet;
import com.saiyu.foreground.https.response.RewardRet;
import com.saiyu.foreground.https.response.SellerInfoRet;
import com.saiyu.foreground.https.response.FaceRet;
import com.saiyu.foreground.https.response.SellerOrderHistoryRet;
import com.saiyu.foreground.https.response.SellerOrderManagerRet;
import com.saiyu.foreground.https.response.SellerOrderReceiveInfoRet;
import com.saiyu.foreground.https.response.StatisticsListRet;
import com.saiyu.foreground.https.response.UploadIdentityRet;
import com.saiyu.foreground.https.response.WaitingRechargeOrderRet;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface ApiService {

    //登录
    @POST("Login/accout")
    Observable<LoginRet> login(@Body RequestBody body);
    //退出登录
    @POST("Home/SignOut")
    Observable<BooleanRet> unLogin(@Body RequestBody body);

    //买家/卖家激活状态
    @POST("my/GetUserStatus")
    Observable<ActiveStatusRet> getUserStatus(@Body RequestBody body);

    //手机验证码登录
    @POST("Login/Mobile")
    Observable<LoginRet> loginMobile(@Body RequestBody body);

    //第三方登录
    @POST("Authorize/OauthLogin")
    Observable<LoginRet> unionIDLogin(@Body RequestBody body);

    //用户注册
    @POST("reg/register")
    Observable<RegistRet> regist(@Body RequestBody body);

    //第三方注册
    @POST("Authorize/register")
    Observable<RegistRet> unionIDRegist(@Body RequestBody body);
    //第三方绑定
    @POST("Authorize/OldMemberBind")
    Observable<RegistRet> oldMemberBind(@Body RequestBody body);

    //发送验证码
    @POST("Tool/SendMobileCode")
    Observable<BooleanRet> sendVCode(@Body RequestBody body);

    //check验证码
    @POST("Tool/CheckMobileCode")
    Observable<BooleanRet> checkVCode(@Body RequestBody body);

    @POST("Tool/isExist")
    Observable<IsAccountExistRet> isAccountExist(@Body RequestBody body);

    // 判断唯一标示unionID判断是否此号已经绑定
    @POST("Authorize/IsExistOAuth")
    Observable<IsAccountExistRet> isUnionIDExist(@Body RequestBody body);
    //不登录获取账号信息
    @POST("tool/nologinuserinfo")
    Observable<AccountInfoNoLoginRet> getAccountInfoNoLogin(@Body RequestBody body);

    //登录状态获取账号信息
    @POST("Home/Index")
    Observable<AccountInfoLoginRet> getAccountInfoLogin(@Body RequestBody body);

    //登录状态获取个别账号信息
    @POST("Home/SmallIndex")
    Observable<AccountInfoLoginRet> getSmallAccountInfoLogin(@Body RequestBody body);

    //身份证找回密码
    @POST("SearchPwd/IDCardCodeSearch")
    Observable<BooleanRet> searchPswIdCard(@Body RequestBody body);

    //手机号找回密码
    @POST("SearchPwd/CheckCodeSearch")
    Observable<BooleanRet> searchPswMobile(@Body RequestBody body);
    //重置密码
    @POST("SearchPwd/LoginPwdResetApi")
    Observable<BooleanRet> retsetPsw(@Body RequestBody body);

    //修改密码，在原密码知道的情况下
    @POST("Safety/ModifyPwd")
    Observable<LoginRet> revisePsw(@Body RequestBody body);

    //信息补填
    @POST("My/InfoFill")
    Observable<BooleanRet> wadInfo(@Body RequestBody body);

    //解绑定
    @POST("My/UnbindThirdLogin")
    Observable<BooleanRet> unBind(@Body RequestBody body);
    //绑定
    @POST("My/BindThirdLogin")
    Observable<BooleanRet> bind(@Body RequestBody body);

    //买家信息
    @POST("My/BuyerInfo")
    Observable<BuyerInfoRet> buyerInfo(@Body RequestBody body);
    //卖家信息
    @POST("My/SellerInfo")
    Observable<SellerInfoRet> sellerInfo(@Body RequestBody body);

    //绑定手机
    @POST("Safety/MobileBind")
    Observable<BooleanRet> bindMobile(@Body RequestBody body);
    //解绑手机
    @POST("Safety/MobileUnBind")
    Observable<BooleanRet> unBindMobile(@Body RequestBody body);
    //更换手机
    @POST("Safety/MobileChangeBind")
    Observable<BooleanRet> changeMobile(@Body RequestBody body);

    //安全配置
    @POST("Safety/ConfigureSave")
    Observable<BooleanRet> securitySet(@Body RequestBody body);
    //用户日志
    @POST("Safety/UserLog")
    Observable<LoginRecordRet> userRecord(@Body RequestBody body);

    //刷脸认证
    @POST("Authentication/FaceSubmit")
    Observable<FaceRet> faceIdentify(@Body RequestBody body);
    //刷脸认证状态
    @POST("Authentication/Face")
    Observable<FaceStatusRet> faceStatus(@Body RequestBody body);
    //提交刷脸认证结果
    @POST("Authentication/FaceAuthQuery")
    Observable<BooleanRet> faceQuery(@Body RequestBody body);

    //刷脸解绑手机
    @POST("Safety/MobileUnBindByFace")
    Observable<FaceRet> mobileUnBindByFace(@Body RequestBody body);
    //刷脸解绑手机,上传返回结果
    @POST("Safety/MobileUnBindByFaceQuery")
    Observable<BooleanRet> mobileUnBindByFaceSumbit(@Body RequestBody body);

    //刷脸认证找回密码
    @POST("SearchPwd/FaceSearch")
    Observable<FaceRet> searchPswFace(@Body RequestBody body);
    //刷脸认证找回密码,上传返回结果
    @POST("SearchPwd/FaceSearchQuery")
    Observable<BooleanRet> faceSearchQuery(@Body RequestBody body);

    //查询实名认证状态
    @POST("Authentication/RealName")
    Observable<RealNameStatusRet> realNameStatus(@Body RequestBody body);
    //提交实名认证
    @POST("Authentication/RealNameSubmit")
    Observable<BooleanRet> realNameSubmit(@Body RequestBody body);

    //卖家激活
    @POST("Seller/Activation")
    Observable<BooleanRet> activeSeller(@Body RequestBody body);
    //买家激活
    @POST("Buyer/Activation")
    Observable<BooleanRet> activeBuyer(@Body RequestBody body);

    //资金明细
    @POST("Finance/Record")
    Observable<CashDetailRet> financeRecord(@Body RequestBody body);
    //充值记录
    @POST("Finance/RechargeLog")
    Observable<RechargeRecordRet> financeRechargeLog(@Body RequestBody body);
    //提现记录
    @POST("Finance/WithdrawLog")
    Observable<CashRecordRet> financeWithdrawLog(@Body RequestBody body);

    //大厅订单
    @POST("Hall/Index")
    Observable<HallRet> hallIndex(@Body RequestBody body);
    //大厅订单详情
    @POST("Hall/ReceiveInfo")
    Observable<HallDetailRet> hallDetail(@Body RequestBody body);
    //大厅订单详情(未登录)
    @POST("Hall/NoLoginReceiveInfo ")
    Observable<HallDetailRet> hallDetailNoLogin(@Body RequestBody body);

    //大厅订单详情(已接单)
    @POST("Hall/Receive")
    Observable<HallDetailReceiveRet> hallDetailReceive(@Body RequestBody body);

    //完成充值后，提交审核
    @POST("Hall/ReceiveSubmit")
    Observable<BooleanRet> receiveSubmit(@Body RequestBody body);

    //充值数量是否可用
    @POST("Hall/UsableQBCountList")
    Observable<IsCountDoRet> usableQBCountList(@Body RequestBody body);

    //取消订单详情
    @POST("Hall/ReceiveCancel")
    Observable<CancelOrderInfoRet> receiveCancel(@Body RequestBody body);

    //取消订单
    @POST("Hall/ReceiveCancelSubmit")
    Observable<BooleanRet> receiveCancelSubmit(@Body RequestBody body);

    //获取版本号
    @POST("Tool/GetAppVersion")
    Observable<AppVersionRet> getAppVersion(@Body RequestBody body);

    //我要提现
    @POST("Finance/Withdraw")
    Observable<CashRet> cash(@Body RequestBody body);
    //提现渠道列表
    @POST("Finance/WithdrawWaysList")
    Observable<CashChannelRet> cashWays(@Body RequestBody body);
    //保存提现账号
    @POST("Finance/WithdrawWayAccountSave")
    Observable<BooleanRet> addCashAccount(@Body RequestBody body);
    //删除提现账号
    @POST("Finance/WithdrawWayAccountDelete")
    Observable<BooleanRet> delCashAccount(@Body RequestBody body);
    //提交提现申请
    @POST("Finance/WithdrawReq")
    Observable<BooleanRet> submitCash(@Body RequestBody body);

    //提交充值申请
    @POST("Finance/RechargeReq")
    Observable<RewardRet> submitRecharge(@Body RequestBody body);
    //获取充值费率
    @POST("Finance/Recharge")
    Observable<RechargeRateRet> getRechargeRate(@Body RequestBody body);

    //上传图片
    @Multipart
    @POST("Tool/UploadImg?biz=user")
    Observable<UploadIdentityRet> uploadIdentity(@Part MultipartBody.Part imgs);


    //游戏产品列表
    @POST("ProductData/Product")
    Observable<ProductListRet> getProductList(@Body RequestBody body);

    //产品属性一
    @POST("ProductData/ProductProperty1")
    Observable<ProductPropertyRet> getProductProperty1(@Body RequestBody body);

    //产品属性二
    @POST("ProductData/ProductProperty2")
    Observable<ProductPropertyRet> getProductProperty2(@Body RequestBody body);

    //生成订单号
    @POST("Buyer/GetOrderReady")
    Observable<OrderNumRet> getOrderNum(@Body RequestBody body);

    //检测QQ是否被限制使用
    @POST("Buyer/CheckLimitQQ")
    Observable<BooleanRet> checkLimitQQ(@Body RequestBody body);

    //发布预定订单接口
    @POST("Buyer/OrderPublish")
    Observable<BooleanRet> orderPublish(@Body RequestBody body);

    //预定订单管理
    @POST("Buyer/OrderManage")
    Observable<PreOrderManagerRet> orderManage(@Body RequestBody body);

    //订单详情
    @POST("Buyer/OrderInfo")
    Observable<BuyerOrderInfoRet> orderInfo(@Body RequestBody body);

    //取消订单
    @POST("Buyer/OrderCancel")
    Observable<BooleanRet> orderCancel(@Body RequestBody body);

    //结算订单信息
    @POST("Buyer/OrderSettlement")
    Observable<OrderSettlementRet> orderSettlement(@Body RequestBody body);

    //结算订单
    @POST("Buyer/OrderSettlementP")
    Observable<BooleanRet> orderSettlementP(@Body RequestBody body);

    //充值流水
    @POST("Buyer/OrderReceiveList")
    Observable<RechargeStreamRet> orderReceiveList(@Body RequestBody body);

    //订单日志
    @POST("Buyer/OrderLog")
    Observable<OrderLogRet> orderLog(@Body RequestBody body);

    //暂停订单信息
    @POST("Buyer/OrderStopInfo")
    Observable<OrderPauseRet> orderStopInfo(@Body RequestBody body);

    //暂停订单
    @POST("Buyer/OrderStopP")
    Observable<BooleanRet> orderStopP(@Body RequestBody body);

    //启动订单信息
    @POST("Buyer/OrderStartInfo")
    Observable<OrderPauseRet> orderStartInfo(@Body RequestBody body);

    //启动订单
    @POST("Buyer/OrderStartP")
    Observable<BooleanRet> orderStartP(@Body RequestBody body);

    //修改订单
    @POST("Buyer/OrderModify")
    Observable<BooleanRet> orderModify(@Body RequestBody body);

    //申请代理确认
    @POST("Buyer/AgentConfirmApply")
    Observable<BooleanRet> agentConfirmApply(@Body RequestBody body);

    //取消代理确认
    @POST("Buyer/AgentConfirmCancel")
    Observable<BooleanRet> agentConfirmCancel(@Body RequestBody body);

    //订单失败原因
    @POST("Buyer/OrderFailRason")
    Observable<OrderFailReasonRet> orderFailReason(@Body RequestBody body);

    //申请维权
    @POST("Buyer/StartAppeal")
    Observable<BooleanRet> startAppeal(@Body RequestBody body);

    //预定订单历史
    @POST("Buyer/OrderHistory")
    Observable<PreOrderHistoryRet> orderHistory(@Body RequestBody body);
    //充值订单管理
    @POST("Buyer/OrderReceiveManage")
    Observable<RechargeOrderManageRet> orderReceiveManage(@Body RequestBody body);

    //充值订单历史
    @POST("Buyer/OrderReceiveHistory")
    Observable<OrderReceiveHistoryRet> orderReceiveHistory(@Body RequestBody body);

    //充值订单信息
    @POST("Buyer/OrderReceiveInfo")
    Observable<RechargeOrderInfoRet> orderReceiveInfo(@Body RequestBody body);

    //确认收货
    @POST("Buyer/OrderReceiveConfirmP")
    Observable<BooleanRet> orderReceiveConfirmP(@Body RequestBody body);

    //确认收货信息
    @POST("Buyer/OrderReceiveConfirm")
    Observable<OrderReceiveConfirmRet> orderReceiveConfirm(@Body RequestBody body);

    //点券充值网页url
    @POST("Buyer/DQCZNews")
    Observable<BuyerWebListRet> dQCZNews(@Body RequestBody body);

    //待充值订单
    @POST("Seller/WaitRechargeOrder")
    Observable<WaitingRechargeOrderRet> waitRechargeOrder(@Body RequestBody body);

    //出售订单管理
    @POST("Seller/OrderReceiveManage")
    Observable<SellerOrderManagerRet> orderReceiveManageSeller(@Body RequestBody body);

    //卖家订单详情
    @POST("Seller/OrderReceiveInfo")
    Observable<SellerOrderReceiveInfoRet> orderReceiveInfoSeller(@Body RequestBody body);

    //出售订单历史
    @POST("Seller/OrderReceiveHistory")
    Observable<SellerOrderHistoryRet> orderReceiveHistorySeller(@Body RequestBody body);

    //接单点数流水
    @POST("Finance/ReceivePoint")
    Observable<ReceivePointRet> receivePoint(@Body RequestBody body);

    //QB寄售列表
    @POST("Seller/QBJSNews")
    Observable<QBListRet> qBJSNews(@Body RequestBody body);

    //申请验图确认
    @POST("Seller/ApplyPicConfirm")
    Observable<BooleanRet> applyPicConfirm(@Body RequestBody body);

    //重新传图
    @POST("Seller/OrderReceiveReSubmit")
    Observable<BooleanRet> orderReceiveReSubmit(@Body RequestBody body);

    //行情
    @POST("Home/GetStatistics")
    Observable<StatisticsListRet> statisticsList(@Body RequestBody body);

    //点数充值
    @POST("Seller/ReceivePoint")
    Observable<BooleanRet> rechargePoint(@Body RequestBody body);

    //点数充值历史
    @POST("Seller/ReceivePointHistory")
    Observable<RechargePointHistoryRet> receivePointHistory(@Body RequestBody body);

    //获取图片服务器
    @POST("Tool/GetImgProcess")
    Observable<GetImgProcessRet> getImgProcess(@Body RequestBody body);

//    @GET("orderAudit/history")
//    Observable<HistoryRet> historyOrder(@Query("page") String page
//            , @Query("pageSize") String pageSize);


}
