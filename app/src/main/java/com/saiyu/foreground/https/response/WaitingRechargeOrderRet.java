package com.saiyu.foreground.https.response;

import java.util.List;

public class WaitingRechargeOrderRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int OrderReceivesCount;
        private List<ItemsBean> OrderReceivesList;

        public int getOrderReceivesCount() {
            return OrderReceivesCount;
        }

        public void setOrderReceivesCount(int orderReceivesCount) {
            OrderReceivesCount = orderReceivesCount;
        }

        public List<ItemsBean> getOrderReceivesList() {
            return OrderReceivesList;
        }

        public void setOrderReceivesList(List<ItemsBean> orderReceivesList) {
            OrderReceivesList = orderReceivesList;
        }

        public static class ItemsBean {
            private String Id;
            private String ReceiveOrderNum;
            private String CreateTime;
            private String ProductTypeName;
            private String ProductName;
            private String RechargeProduct;
            private String ReserveAccount;
            private String ReserveQBCount;
            private String ReserveMoney;
            private String ConsumeRPoint;
            private String ConfirmationTime;
            private String SurplusTime;
            private String OrderID;

            public String getOrderID() {
                return OrderID;
            }

            public void setOrderID(String orderID) {
                OrderID = orderID;
            }

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getReceiveOrderNum() {
                return ReceiveOrderNum;
            }

            public void setReceiveOrderNum(String receiveOrderNum) {
                ReceiveOrderNum = receiveOrderNum;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getProductTypeName() {
                return ProductTypeName;
            }

            public void setProductTypeName(String productTypeName) {
                ProductTypeName = productTypeName;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
            }

            public String getRechargeProduct() {
                return RechargeProduct;
            }

            public void setRechargeProduct(String rechargeProduct) {
                RechargeProduct = rechargeProduct;
            }

            public String getReserveAccount() {
                return ReserveAccount;
            }

            public void setReserveAccount(String reserveAccount) {
                ReserveAccount = reserveAccount;
            }

            public String getReserveQBCount() {
                return ReserveQBCount;
            }

            public void setReserveQBCount(String reserveQBCount) {
                ReserveQBCount = reserveQBCount;
            }

            public String getReserveMoney() {
                return ReserveMoney;
            }

            public void setReserveMoney(String reserveMoney) {
                ReserveMoney = reserveMoney;
            }

            public String getConsumeRPoint() {
                return ConsumeRPoint;
            }

            public void setConsumeRPoint(String consumeRPoint) {
                ConsumeRPoint = consumeRPoint;
            }

            public String getConfirmationTime() {
                return ConfirmationTime;
            }

            public void setConfirmationTime(String confirmationTime) {
                ConfirmationTime = confirmationTime;
            }

            public String getSurplusTime() {
                return SurplusTime;
            }

            public void setSurplusTime(String surplusTime) {
                SurplusTime = surplusTime;
            }
        }}
}
