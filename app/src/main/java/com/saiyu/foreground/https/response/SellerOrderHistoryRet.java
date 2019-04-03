package com.saiyu.foreground.https.response;

import java.util.List;

public class SellerOrderHistoryRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int OrderReceivesCount;

        private List<ItemsBean> OrderReceives;

        public int getOrderReceivesCount() {
            return OrderReceivesCount;
        }

        public void setOrderReceivesCount(int orderReceivesCount) {
            OrderReceivesCount = orderReceivesCount;
        }

        public List<ItemsBean> getOrderReceives() {
            return OrderReceives;
        }

        public void setOrderReceives(List<ItemsBean> orderReceives) {
            OrderReceives = orderReceives;
        }

        public static class ItemsBean {
            private String Id;
            private String ReceiveOrderNum;
            private String OrderNum;
            private String CreateTime;
            private String ProductName;
            private String ReserveAccount;
            private String ReserveQBCount;
            private String SuccQBCount;
            private String SuccMoney;
            private String TotalMoney;
            private String FinishTime;
            private String ConfirmTime;
            private String ConfirmType;
            private int ReceiveOrderStatus;
            private String ReceiveOrderStatusStr;
            private String OrderCancelRemarks;

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

            public String getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(String orderNum) {
                OrderNum = orderNum;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
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

            public String getSuccQBCount() {
                return SuccQBCount;
            }

            public void setSuccQBCount(String succQBCount) {
                SuccQBCount = succQBCount;
            }

            public String getSuccMoney() {
                return SuccMoney;
            }

            public void setSuccMoney(String succMoney) {
                SuccMoney = succMoney;
            }

            public String getTotalMoney() {
                return TotalMoney;
            }

            public void setTotalMoney(String totalMoney) {
                TotalMoney = totalMoney;
            }

            public String getFinishTime() {
                return FinishTime;
            }

            public void setFinishTime(String finishTime) {
                FinishTime = finishTime;
            }

            public String getConfirmTime() {
                return ConfirmTime;
            }

            public void setConfirmTime(String confirmTime) {
                ConfirmTime = confirmTime;
            }

            public String getConfirmType() {
                return ConfirmType;
            }

            public void setConfirmType(String confirmType) {
                ConfirmType = confirmType;
            }

            public int getReceiveOrderStatus() {
                return ReceiveOrderStatus;
            }

            public void setReceiveOrderStatus(int receiveOrderStatus) {
                ReceiveOrderStatus = receiveOrderStatus;
            }

            public String getReceiveOrderStatusStr() {
                return ReceiveOrderStatusStr;
            }

            public void setReceiveOrderStatusStr(String receiveOrderStatusStr) {
                ReceiveOrderStatusStr = receiveOrderStatusStr;
            }

            public String getOrderCancelRemarks() {
                return OrderCancelRemarks;
            }

            public void setOrderCancelRemarks(String orderCancelRemarks) {
                OrderCancelRemarks = orderCancelRemarks;
            }
        }
    }
}
