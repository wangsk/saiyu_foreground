package com.saiyu.foreground.https.response;

import java.util.List;

public class QBListRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private float ReceivePoint;
        private String UserSellerWaitRechargeOrdersCount;
        private String UserSellerWaitRechargeOrdersMoney;
        private String UserSellerAuditOrdersCount;
        private String UserSellerAuditOrdersMoney;
        private String UserSellerWaitConfirmOrdersCount;
        private String UserSellerWaitConfirmOrdersMoney;
        private String UserSellerOrderRSettleTotalCount;
        private String UserSellerOrderRSettleTotalMoney;
        private String OrderFreePointsUrl;

        public String getOrderFreePointsUrl() {
            return OrderFreePointsUrl;
        }

        public void setOrderFreePointsUrl(String orderFreePointsUrl) {
            OrderFreePointsUrl = orderFreePointsUrl;
        }

        private List<ItemsBean> QBJSNews;

        public float getReceivePoint() {
            return ReceivePoint;
        }

        public void setReceivePoint(float receivePoint) {
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

        public List<ItemsBean> getQBJSNews() {
            return QBJSNews;
        }

        public void setQBJSNews(List<ItemsBean> QBJSNews) {
            this.QBJSNews = QBJSNews;
        }

        public static class ItemsBean {
            private String Title;
            private String NewsDetailUrl;

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

            public String getNewsDetailUrl() {
                return NewsDetailUrl;
            }

            public void setNewsDetailUrl(String newsDetailUrl) {
                NewsDetailUrl = newsDetailUrl;
            }
        }
    }
}
