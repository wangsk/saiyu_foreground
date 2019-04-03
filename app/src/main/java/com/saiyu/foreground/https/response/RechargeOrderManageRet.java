package com.saiyu.foreground.https.response;

import java.util.List;

public class RechargeOrderManageRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int dataCount;

        public int getDataCount() {
            return dataCount;
        }

        public void setDataCount(int dataCount) {
            this.dataCount = dataCount;
        }

        private List<ItemsBean> orderReceiveList;

        public List<ItemsBean> getOrderReceiveList() {
            return orderReceiveList;
        }

        public void setOrderReceiveList(List<ItemsBean> orderReceiveList) {
            this.orderReceiveList = orderReceiveList;
        }

        public static class ItemsBean {
            private String Id;
            private String ReceiveOrderNum;
            private String CreateTime;
            private String FinishTime;
            private String ProductName;
            private String RechargeProduct;
            private String ReserveAccount;
            private String ReserveQBCount;
            private String SuccQBCount;
            private String SuccMoney;
            private String AutoConfirmTime;
            private String ConfirmType;
            private int ReceiveOrderStatus;
            private String ReceiveOrderStatusStr;

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

            public String getFinishTime() {
                return FinishTime;
            }

            public void setFinishTime(String finishTime) {
                FinishTime = finishTime;
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

            public String getAutoConfirmTime() {
                return AutoConfirmTime;
            }

            public void setAutoConfirmTime(String autoConfirmTime) {
                AutoConfirmTime = autoConfirmTime;
            }

            public int getReceiveOrderStatus() {
                return ReceiveOrderStatus;
            }

            public void setReceiveOrderStatus(int receiveOrderStatus) {
                ReceiveOrderStatus = receiveOrderStatus;
            }

            public String getConfirmType() {
                return ConfirmType;
            }

            public void setConfirmType(String confirmType) {
                ConfirmType = confirmType;
            }

            public String getReceiveOrderStatusStr() {
                return ReceiveOrderStatusStr;
            }

            public void setReceiveOrderStatusStr(String receiveOrderStatusStr) {
                ReceiveOrderStatusStr = receiveOrderStatusStr;
            }
        }}
}
