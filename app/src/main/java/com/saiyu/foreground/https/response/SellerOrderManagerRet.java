package com.saiyu.foreground.https.response;

import java.util.List;

public class SellerOrderManagerRet extends BaseRet{
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
            this.OrderReceives = orderReceives;
        }

        public static class ItemsBean {
            private String Id;
            private String ReceiveOrderNum;
            private String CreateTime;
            private String FinishTime;
            private String ProductTypeName;
            private String UnitName;
            private String RechargeProduct;
            private String ReserveQBCount;
            private String SuccQBCount;
            private String SuccMoney;
            private String PenaltyMoney;
            private String ServiceMoney;
            private String TotalMoney;
            private String AutoConfirmTime;
            private int ConfirmType;
            private int IsPicConfirm;
            private String ConfirmTypeStr;
            private int ReceiveOrderStatus;
            private String ReceiveOrderStatusStr;

            public int getIsPicConfirm() {
                return IsPicConfirm;
            }

            public void setIsPicConfirm(int isPicConfirm) {
                IsPicConfirm = isPicConfirm;
            }

            public String getReceiveOrderStatusStr() {
                return ReceiveOrderStatusStr;
            }

            public void setReceiveOrderStatusStr(String receiveOrderStatusStr) {
                ReceiveOrderStatusStr = receiveOrderStatusStr;
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

            public String getFinishTime() {
                return FinishTime;
            }

            public void setFinishTime(String finishTime) {
                FinishTime = finishTime;
            }

            public String getProductTypeName() {
                return ProductTypeName;
            }

            public void setProductTypeName(String productTypeName) {
                ProductTypeName = productTypeName;
            }

            public String getUnitName() {
                return UnitName;
            }

            public void setUnitName(String unitName) {
                UnitName = unitName;
            }

            public String getRechargeProduct() {
                return RechargeProduct;
            }

            public void setRechargeProduct(String rechargeProduct) {
                RechargeProduct = rechargeProduct;
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

            public String getPenaltyMoney() {
                return PenaltyMoney;
            }

            public void setPenaltyMoney(String penaltyMoney) {
                PenaltyMoney = penaltyMoney;
            }

            public String getServiceMoney() {
                return ServiceMoney;
            }

            public void setServiceMoney(String serviceMoney) {
                ServiceMoney = serviceMoney;
            }

            public String getTotalMoney() {
                return TotalMoney;
            }

            public void setTotalMoney(String totalMoney) {
                TotalMoney = totalMoney;
            }

            public String getAutoConfirmTime() {
                return AutoConfirmTime;
            }

            public void setAutoConfirmTime(String autoConfirmTime) {
                AutoConfirmTime = autoConfirmTime;
            }

            public int getConfirmType() {
                return ConfirmType;
            }

            public void setConfirmType(int confirmType) {
                ConfirmType = confirmType;
            }

            public String getConfirmTypeStr() {
                return ConfirmTypeStr;
            }

            public void setConfirmTypeStr(String confirmTypeStr) {
                ConfirmTypeStr = confirmTypeStr;
            }

            public int getReceiveOrderStatus() {
                return ReceiveOrderStatus;
            }

            public void setReceiveOrderStatus(int receiveOrderStatus) {
                ReceiveOrderStatus = receiveOrderStatus;
            }
        }
    }
}
