package com.saiyu.foreground.https.response;

public class BuyerInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        String VipLevel;
        String AverageConfirmTime;
        String UserBuyerCount;
        String UserBuyerMoney;
        String UserBuyerWaitConfirmOrdersCount;
        String UserBuyerWaitConfirmOrdersMoney;
        String UserBuyerOrderRSettleTotalCount;
        String UserBuyerOrderRSettleTotalMoney;

        public String getVipLevel() {
            return VipLevel;
        }

        public void setVipLevel(String vipLevel) {
            VipLevel = vipLevel;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
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
    }
}
