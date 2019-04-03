package com.saiyu.foreground.https.response;

import java.util.List;

public class PreOrderHistoryRet extends BaseRet{
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

        private List<ItemsBean> orderList;

        public List<ItemsBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<ItemsBean> orderList) {
            this.orderList = orderList;
        }

        public static class ItemsBean {
            private String Id;
            private String OrderNum;
            private String CreateTime;
            private String OrderFinishTime;
            private String ProductTypeName;
            private String RechargeProduct;
            private String ReserveAccount;
            private String ReserveQBCount;
            private String SuccQBCount;
            private String ReservePrice;
            private String SettlementMoney;
            private String OrderReceivesCount;
            private String OrderSettleTime;
            private String OrderStatusType;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
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

            public String getOrderFinishTime() {
                return OrderFinishTime;
            }

            public void setOrderFinishTime(String orderFinishTime) {
                OrderFinishTime = orderFinishTime;
            }

            public String getProductTypeName() {
                return ProductTypeName;
            }

            public void setProductTypeName(String productTypeName) {
                ProductTypeName = productTypeName;
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

            public String getReservePrice() {
                return ReservePrice;
            }

            public void setReservePrice(String reservePrice) {
                ReservePrice = reservePrice;
            }

            public String getSettlementMoney() {
                return SettlementMoney;
            }

            public void setSettlementMoney(String settlementMoney) {
                SettlementMoney = settlementMoney;
            }

            public String getOrderReceivesCount() {
                return OrderReceivesCount;
            }

            public void setOrderReceivesCount(String orderReceivesCount) {
                OrderReceivesCount = orderReceivesCount;
            }

            public String getOrderSettleTime() {
                return OrderSettleTime;
            }

            public void setOrderSettleTime(String orderSettleTime) {
                OrderSettleTime = orderSettleTime;
            }

            public String getOrderStatusType() {
                return OrderStatusType;
            }

            public void setOrderStatusType(String orderStatusType) {
                OrderStatusType = orderStatusType;
            }
        }
    }
}
