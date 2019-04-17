package com.saiyu.foreground.https.response;

import java.util.List;

public class AccountInfoLoginRet extends BaseRet{

    private static final long serialVersionUID = -4677681767264315922L;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        String Account;
        int RealNameStatus;
        int FaceAuthStatus;
        String TotalMoney;
        String UserBuyerCount;
        String UserBuyerMoney;
        String UserSellerCount;
        String UserSellerMoney;
        String Mobile;
        String RealName;
        String IDNum;
        int MobileBindStatus;
        String QQOpenId;
        String QQUnionid;
        String WXOpenId;
        String WXUnionid;
        int RiskLevel;
        String RegTime;

        boolean UserBuyerStatus;
        boolean UserSellerStatus;

        String UserBuyerWaitConfirmOrdersCount;
        String UserBuyerWaitConfirmOrdersMoney;
        String UserBuyerOrderRSettleTotalCount;
        String UserBuyerOrderRSettleTotalMoney;
        String UserSellerWaitRechargeOrdersCount;
        String UserSellerWaitRechargeOrdersMoney;
        String UserSellerAuditOrdersCount;
        String UserSellerAuditOrdersMoney;
        String UserSellerWaitConfirmOrdersCount;
        String UserSellerWaitConfirmOrdersMoney;
        String UserSellerOrderRSettleTotalCount;
        String UserSellerOrderRSettleTotalMoney;
        String AverageConfirmTime;

        boolean IsUserLimit;
        String OrderMoneyLimitByDay;
        String OrderCountLimitByDay;

        boolean IsModify;

        public boolean isModify() {
            return IsModify;
        }

        public void setModify(boolean modify) {
            IsModify = modify;
        }

        public String getOrderMoneyLimitByDay() {
            return OrderMoneyLimitByDay;
        }

        public void setOrderMoneyLimitByDay(String orderMoneyLimitByDay) {
            OrderMoneyLimitByDay = orderMoneyLimitByDay;
        }

        public String getOrderCountLimitByDay() {
            return OrderCountLimitByDay;
        }

        public void setOrderCountLimitByDay(String orderCountLimitByDay) {
            OrderCountLimitByDay = orderCountLimitByDay;
        }

        public boolean isUserLimit() {
            return IsUserLimit;
        }

        public void setUserLimit(boolean userLimit) {
            IsUserLimit = userLimit;
        }

        private List<ItemsBean> Xttzlist;

        public List<ItemsBean> getXttzlist() {
            return Xttzlist;
        }

        public void setXttzlist(List<ItemsBean> xttzlist) {
            Xttzlist = xttzlist;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String account) {
            Account = account;
        }

        public int getRealNameStatus() {
            return RealNameStatus;
        }

        public void setRealNameStatus(int realNameStatus) {
            RealNameStatus = realNameStatus;
        }

        public int getFaceAuthStatus() {
            return FaceAuthStatus;
        }

        public void setFaceAuthStatus(int faceAuthStatus) {
            FaceAuthStatus = faceAuthStatus;
        }

        public String getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            TotalMoney = totalMoney;
        }

        public String getUserBuyerCount() {
            return UserBuyerCount;
        }

        public void setUserBuyerCount(String userBuyerCount) {
            UserBuyerCount = userBuyerCount;
        }

        public String getUserBuyerMoney() {
            return UserBuyerMoney;
        }

        public void setUserBuyerMoney(String userBuyerMoney) {
            UserBuyerMoney = userBuyerMoney;
        }

        public String getUserSellerCount() {
            return UserSellerCount;
        }

        public void setUserSellerCount(String userSellerCount) {
            UserSellerCount = userSellerCount;
        }

        public String getUserSellerMoney() {
            return UserSellerMoney;
        }

        public void setUserSellerMoney(String userSellerMoney) {
            UserSellerMoney = userSellerMoney;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public String getIDNum() {
            return IDNum;
        }

        public void setIDNum(String IDNum) {
            this.IDNum = IDNum;
        }

        public int getMobileBindStatus() {
            return MobileBindStatus;
        }

        public void setMobileBindStatus(int mobileBindStatus) {
            MobileBindStatus = mobileBindStatus;
        }

        public String getQQOpenId() {
            return QQOpenId;
        }

        public void setQQOpenId(String QQOpenId) {
            this.QQOpenId = QQOpenId;
        }

        public String getQQUnionid() {
            return QQUnionid;
        }

        public void setQQUnionid(String QQUnionid) {
            this.QQUnionid = QQUnionid;
        }

        public String getWXOpenId() {
            return WXOpenId;
        }

        public void setWXOpenId(String WXOpenId) {
            this.WXOpenId = WXOpenId;
        }

        public String getWXUnionid() {
            return WXUnionid;
        }

        public void setWXUnionid(String WXUnionid) {
            this.WXUnionid = WXUnionid;
        }

        public int getRiskLevel() {
            return RiskLevel;
        }

        public void setRiskLevel(int riskLevel) {
            RiskLevel = riskLevel;
        }

        public String getRegTime() {
            return RegTime;
        }

        public void setRegTime(String regTime) {
            RegTime = regTime;
        }

        public boolean isUserBuyerStatus() {
            return UserBuyerStatus;
        }

        public void setUserBuyerStatus(boolean userBuyerStatus) {
            UserBuyerStatus = userBuyerStatus;
        }

        public boolean isUserSellerStatus() {
            return UserSellerStatus;
        }

        public void setUserSellerStatus(boolean userSellerStatus) {
            UserSellerStatus = userSellerStatus;
        }

        public String getUserBuyerWaitConfirmOrdersCount() {
            return UserBuyerWaitConfirmOrdersCount;
        }

        public void setUserBuyerWaitConfirmOrdersCount(String userBuyerWaitConfirmOrdersCount) {
            UserBuyerWaitConfirmOrdersCount = userBuyerWaitConfirmOrdersCount;
        }

        public String getUserBuyerWaitConfirmOrdersMoney() {
            return UserBuyerWaitConfirmOrdersMoney;
        }

        public void setUserBuyerWaitConfirmOrdersMoney(String userBuyerWaitConfirmOrdersMoney) {
            UserBuyerWaitConfirmOrdersMoney = userBuyerWaitConfirmOrdersMoney;
        }

        public String getUserBuyerOrderRSettleTotalCount() {
            return UserBuyerOrderRSettleTotalCount;
        }

        public void setUserBuyerOrderRSettleTotalCount(String userBuyerOrderRSettleTotalCount) {
            UserBuyerOrderRSettleTotalCount = userBuyerOrderRSettleTotalCount;
        }

        public String getUserBuyerOrderRSettleTotalMoney() {
            return UserBuyerOrderRSettleTotalMoney;
        }

        public void setUserBuyerOrderRSettleTotalMoney(String userBuyerOrderRSettleTotalMoney) {
            UserBuyerOrderRSettleTotalMoney = userBuyerOrderRSettleTotalMoney;
        }

        public String getUserSellerWaitRechargeOrdersCount() {
            return UserSellerWaitRechargeOrdersCount;
        }

        public void setUserSellerWaitRechargeOrdersCount(String userSellerWaitRechargeOrdersCount) {
            UserSellerWaitRechargeOrdersCount = userSellerWaitRechargeOrdersCount;
        }

        public String getUserSellerWaitRechargeOrdersMoney() {
            return UserSellerWaitRechargeOrdersMoney;
        }

        public void setUserSellerWaitRechargeOrdersMoney(String userSellerWaitRechargeOrdersMoney) {
            UserSellerWaitRechargeOrdersMoney = userSellerWaitRechargeOrdersMoney;
        }

        public String getUserSellerAuditOrdersCount() {
            return UserSellerAuditOrdersCount;
        }

        public void setUserSellerAuditOrdersCount(String userSellerAuditOrdersCount) {
            UserSellerAuditOrdersCount = userSellerAuditOrdersCount;
        }

        public String getUserSellerAuditOrdersMoney() {
            return UserSellerAuditOrdersMoney;
        }

        public void setUserSellerAuditOrdersMoney(String userSellerAuditOrdersMoney) {
            UserSellerAuditOrdersMoney = userSellerAuditOrdersMoney;
        }

        public String getUserSellerWaitConfirmOrdersCount() {
            return UserSellerWaitConfirmOrdersCount;
        }

        public void setUserSellerWaitConfirmOrdersCount(String userSellerWaitConfirmOrdersCount) {
            UserSellerWaitConfirmOrdersCount = userSellerWaitConfirmOrdersCount;
        }

        public String getUserSellerWaitConfirmOrdersMoney() {
            return UserSellerWaitConfirmOrdersMoney;
        }

        public void setUserSellerWaitConfirmOrdersMoney(String userSellerWaitConfirmOrdersMoney) {
            UserSellerWaitConfirmOrdersMoney = userSellerWaitConfirmOrdersMoney;
        }

        public String getUserSellerOrderRSettleTotalCount() {
            return UserSellerOrderRSettleTotalCount;
        }

        public void setUserSellerOrderRSettleTotalCount(String userSellerOrderRSettleTotalCount) {
            UserSellerOrderRSettleTotalCount = userSellerOrderRSettleTotalCount;
        }

        public String getUserSellerOrderRSettleTotalMoney() {
            return UserSellerOrderRSettleTotalMoney;
        }

        public void setUserSellerOrderRSettleTotalMoney(String userSellerOrderRSettleTotalMoney) {
            UserSellerOrderRSettleTotalMoney = userSellerOrderRSettleTotalMoney;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public static class ItemsBean {
            String NewsDetailUrl;
            String NewsDetailTitle;
            String ReleaseTime;

            public String getNewsDetailUrl() {
                return NewsDetailUrl;
            }

            public void setNewsDetailUrl(String newsDetailUrl) {
                NewsDetailUrl = newsDetailUrl;
            }

            public String getNewsDetailTitle() {
                return NewsDetailTitle;
            }

            public void setNewsDetailTitle(String newsDetailTitle) {
                NewsDetailTitle = newsDetailTitle;
            }

            public String getReleaseTime() {
                return ReleaseTime;
            }

            public void setReleaseTime(String releaseTime) {
                ReleaseTime = releaseTime;
            }
        }

    }
}
