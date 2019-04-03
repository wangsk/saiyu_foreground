package com.saiyu.foreground.https.response;

import java.util.List;

public class PreOrderManagerRet extends BaseRet{

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int OrderCount;

        private List<ItemsBean> OrderList;

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int orderCount) {
            OrderCount = orderCount;
        }

        public List<ItemsBean> getOrderList() {
            return OrderList;
        }

        public void setOrderList(List<ItemsBean> orderList) {
            OrderList = orderList;
        }

        public static class ItemsBean {
            private String Id;
            private String OrderNum;
            private String CreateTime;
            private String ProductTypeName;
            private String RechargeProduct;
            private String ReserveAccount;
            private String ReserveQBCount;
            private String ReservePrice;
            private String SuccQBCount;
            private String OnlineTime;
            private String ConfirmStr;
            private int IsAgentConfirm;
            private int AgentConfirmAuditStatus;
            private int AuditStatus;
            private String AuditStatusStr;
            private String OperatorType;
            private String PrepaidMoney;
            private String Title;
            private int OrderStatus;

            public int getOrderStatus() {
                return OrderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                OrderStatus = orderStatus;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

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

            public String getReservePrice() {
                return ReservePrice;
            }

            public void setReservePrice(String reservePrice) {
                ReservePrice = reservePrice;
            }

            public String getSuccQBCount() {
                return SuccQBCount;
            }

            public void setSuccQBCount(String succQBCount) {
                SuccQBCount = succQBCount;
            }

            public String getOnlineTime() {
                return OnlineTime;
            }

            public void setOnlineTime(String onlineTime) {
                OnlineTime = onlineTime;
            }

            public String getConfirmStr() {
                return ConfirmStr;
            }

            public void setConfirmStr(String confirmStr) {
                ConfirmStr = confirmStr;
            }

            public int getIsAgentConfirm() {
                return IsAgentConfirm;
            }

            public void setIsAgentConfirm(int isAgentConfirm) {
                IsAgentConfirm = isAgentConfirm;
            }

            public int getAgentConfirmAuditStatus() {
                return AgentConfirmAuditStatus;
            }

            public void setAgentConfirmAuditStatus(int agentConfirmAuditStatus) {
                AgentConfirmAuditStatus = agentConfirmAuditStatus;
            }

            public int getAuditStatus() {
                return AuditStatus;
            }

            public void setAuditStatus(int auditStatus) {
                AuditStatus = auditStatus;
            }

            public String getAuditStatusStr() {
                return AuditStatusStr;
            }

            public void setAuditStatusStr(String auditStatusStr) {
                AuditStatusStr = auditStatusStr;
            }

            public String getOperatorType() {
                return OperatorType;
            }

            public void setOperatorType(String operatorType) {
                OperatorType = operatorType;
            }

            public String getPrepaidMoney() {
                return PrepaidMoney;
            }

            public void setPrepaidMoney(String prepaidMoney) {
                PrepaidMoney = prepaidMoney;
            }
        }
    }
}
