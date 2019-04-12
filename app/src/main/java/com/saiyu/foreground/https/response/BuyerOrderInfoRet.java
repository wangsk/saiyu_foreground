package com.saiyu.foreground.https.response;

import java.util.List;

public class BuyerOrderInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String Id;
        private String OrderNum;
        private String CreateTime;
        private String OrderExpiryTime;
        private String ProductType;
        private String ProductName;
        private String ConfirmStr;
        private int IsAgentConfirm;
        private int AgentConfirmAuditStatus;
        private int ProductIsAgentConfirm;
        private String AverageConfirmTime;
        private String ReserveAccount;
        private String ReservePwd;
        private String OftenLoginProvince;
        private String OftenLoginCity;
        private String OrderStatusType;
        private String ReserveQBCount;
        private String OrderAuditStatusType;
        private String ReservePrice;
        private String OrderFinishTime;
        private String ReserveTitle;
        private String SettlementTime;
        private String OnlineTime;
        private String SettlementMoney;
        private String OrderPwd;
        private String GetPenaltyMoney;
        private float OnceMinCount;
        private float LessChargeDiscount;
        private String OrderReceivesCount;
        private String OrderInterval;
        private String CancelRemarks;
        private int IsPicConfirm;
        private String OrderCancelTime;
        private String IsAgentConfirmStr;
        private String ContactType;
        private String Remarks;
        private String RefundMoney;
        private String SuccQBCount;
        private int IsAllowShowContact;
        private String IsAllowShowContactStr;
        private String ContactQQ;
        private String ContactMobile;
        private List<String> PostScriptList;

        public List<String> getPostScriptList() {
            return PostScriptList;
        }

        public void setPostScriptList(List<String> postScriptList) {
            PostScriptList = postScriptList;
        }

        public String getReservePwd() {
            return ReservePwd;
        }

        public void setReservePwd(String reservePwd) {
            ReservePwd = reservePwd;
        }

        public String getOftenLoginProvince() {
            return OftenLoginProvince;
        }

        public void setOftenLoginProvince(String oftenLoginProvince) {
            OftenLoginProvince = oftenLoginProvince;
        }

        public String getOftenLoginCity() {
            return OftenLoginCity;
        }

        public void setOftenLoginCity(String oftenLoginCity) {
            OftenLoginCity = oftenLoginCity;
        }

        public int getProductIsAgentConfirm() {
            return ProductIsAgentConfirm;
        }

        public void setProductIsAgentConfirm(int productIsAgentConfirm) {
            ProductIsAgentConfirm = productIsAgentConfirm;
        }

        public String getIsAllowShowContactStr() {
            return IsAllowShowContactStr;
        }

        public void setIsAllowShowContactStr(String isAllowShowContactStr) {
            IsAllowShowContactStr = isAllowShowContactStr;
        }

        public String getContactQQ() {
            return ContactQQ;
        }

        public void setContactQQ(String contactQQ) {
            ContactQQ = contactQQ;
        }

        public String getContactMobile() {
            return ContactMobile;
        }

        public void setContactMobile(String contactMobile) {
            ContactMobile = contactMobile;
        }

        public int getIsAllowShowContact() {
            return IsAllowShowContact;
        }

        public void setIsAllowShowContact(int isAllowShowContact) {
            IsAllowShowContact = isAllowShowContact;
        }

        public String getSuccQBCount() {
            return SuccQBCount;
        }

        public void setSuccQBCount(String succQBCount) {
            SuccQBCount = succQBCount;
        }

        public String getRefundMoney() {
            return RefundMoney;
        }

        public void setRefundMoney(String refundMoney) {
            RefundMoney = refundMoney;
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

        public String getOrderExpiryTime() {
            return OrderExpiryTime;
        }

        public void setOrderExpiryTime(String orderExpiryTime) {
            OrderExpiryTime = orderExpiryTime;
        }

        public String getProductType() {
            return ProductType;
        }

        public void setProductType(String productType) {
            ProductType = productType;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
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

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getOrderStatusType() {
            return OrderStatusType;
        }

        public void setOrderStatusType(String orderStatusType) {
            OrderStatusType = orderStatusType;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public String getOrderAuditStatusType() {
            return OrderAuditStatusType;
        }

        public void setOrderAuditStatusType(String orderAuditStatusType) {
            OrderAuditStatusType = orderAuditStatusType;
        }

        public String getReservePrice() {
            return ReservePrice;
        }

        public void setReservePrice(String reservePrice) {
            ReservePrice = reservePrice;
        }

        public String getOrderFinishTime() {
            return OrderFinishTime;
        }

        public void setOrderFinishTime(String orderFinishTime) {
            OrderFinishTime = orderFinishTime;
        }

        public String getReserveTitle() {
            return ReserveTitle;
        }

        public void setReserveTitle(String reserveTitle) {
            ReserveTitle = reserveTitle;
        }

        public String getSettlementTime() {
            return SettlementTime;
        }

        public void setSettlementTime(String settlementTime) {
            SettlementTime = settlementTime;
        }

        public String getOnlineTime() {
            return OnlineTime;
        }

        public void setOnlineTime(String onlineTime) {
            OnlineTime = onlineTime;
        }

        public String getSettlementMoney() {
            return SettlementMoney;
        }

        public void setSettlementMoney(String settlementMoney) {
            SettlementMoney = settlementMoney;
        }

        public String getOrderPwd() {
            return OrderPwd;
        }

        public void setOrderPwd(String orderPwd) {
            OrderPwd = orderPwd;
        }

        public String getGetPenaltyMoney() {
            return GetPenaltyMoney;
        }

        public void setGetPenaltyMoney(String getPenaltyMoney) {
            GetPenaltyMoney = getPenaltyMoney;
        }

        public float getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(float onceMinCount) {
            OnceMinCount = onceMinCount;
        }

        public float getLessChargeDiscount() {
            return LessChargeDiscount;
        }

        public void setLessChargeDiscount(float lessChargeDiscount) {
            LessChargeDiscount = lessChargeDiscount;
        }

        public String getOrderReceivesCount() {
            return OrderReceivesCount;
        }

        public void setOrderReceivesCount(String orderReceivesCount) {
            OrderReceivesCount = orderReceivesCount;
        }

        public String getOrderInterval() {
            return OrderInterval;
        }

        public void setOrderInterval(String orderInterval) {
            OrderInterval = orderInterval;
        }

        public String getCancelRemarks() {
            return CancelRemarks;
        }

        public void setCancelRemarks(String cancelRemarks) {
            CancelRemarks = cancelRemarks;
        }

        public int getIsPicConfirm() {
            return IsPicConfirm;
        }

        public void setIsPicConfirm(int isPicConfirm) {
            IsPicConfirm = isPicConfirm;
        }

        public String getOrderCancelTime() {
            return OrderCancelTime;
        }

        public void setOrderCancelTime(String orderCancelTime) {
            OrderCancelTime = orderCancelTime;
        }

        public String getIsAgentConfirmStr() {
            return IsAgentConfirmStr;
        }

        public void setIsAgentConfirmStr(String isAgentConfirmStr) {
            IsAgentConfirmStr = isAgentConfirmStr;
        }

        public String getContactType() {
            return ContactType;
        }

        public void setContactType(String contactType) {
            ContactType = contactType;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }
    }
}
