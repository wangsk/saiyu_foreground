package com.saiyu.foreground.https.response;

public class SellerInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        String ReceivePoint;
        String UserSellerWaitRechargeOrdersCount;
        String UserSellerWaitRechargeOrdersMoney;
        String UserSellerAuditOrdersCount;
        String UserSellerAuditOrdersMoney;
        String UserSellerWaitConfirmOrdersCount;
        String UserSellerWaitConfirmOrdersMoney;
        String UserSellerOrderRSettleTotalCount;
        String UserSellerOrderRSettleTotalMoney;

        public String getReceivePoint() {
            return ReceivePoint;
        }

        public void setReceivePoint(String receivePoint) {
            ReceivePoint = receivePoint;
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
    }
}
