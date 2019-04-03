package com.saiyu.foreground.https.response;

public class RechargeOrderInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String Id;
        private String ReceiveOrderNum;
        private String ProductTypeName;
        private String OrderNum;
        private String ProductName;
        private String CreateTime;
        private String FinishTime;
        private String RoleName;
        private String ReserveAccount;
        private String RechargeTime;
        private String OnlineTime;
        private String AutoConfirmTime;
        private String OrderPwd;
        private String ReserveQBCount;
        private String SuccQBCount;
        private String OrderInterval;
        private String SuccMoney;
        private String IsPicConfirm;
        private String ConfirmType;
        private String IsAgentConfirm;
        private String ReceiveOrderStatus;
        private String Remarks;
        private String OnceMinCount;
        private String AverageConfirmTime;
        private String ContactType;
        private String ContactMobile;
        private String ContactQQ;
        private String IsAllowShowContact;
        private String IsAllowShowContactStr;

        public String getContactMobile() {
            return ContactMobile;
        }

        public void setContactMobile(String contactMobile) {
            ContactMobile = contactMobile;
        }

        public String getContactQQ() {
            return ContactQQ;
        }

        public void setContactQQ(String contactQQ) {
            ContactQQ = contactQQ;
        }

        public String getIsAllowShowContact() {
            return IsAllowShowContact;
        }

        public void setIsAllowShowContact(String isAllowShowContact) {
            IsAllowShowContact = isAllowShowContact;
        }

        public String getIsAllowShowContactStr() {
            return IsAllowShowContactStr;
        }

        public void setIsAllowShowContactStr(String isAllowShowContactStr) {
            IsAllowShowContactStr = isAllowShowContactStr;
        }

        public String getContactType() {
            return ContactType;
        }

        public void setContactType(String contactType) {
            ContactType = contactType;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public String getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(String onceMinCount) {
            OnceMinCount = onceMinCount;
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

        public String getProductTypeName() {
            return ProductTypeName;
        }

        public void setProductTypeName(String productTypeName) {
            ProductTypeName = productTypeName;
        }

        public String getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(String orderNum) {
            OrderNum = orderNum;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
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

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String roleName) {
            RoleName = roleName;
        }

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getRechargeTime() {
            return RechargeTime;
        }

        public void setRechargeTime(String rechargeTime) {
            RechargeTime = rechargeTime;
        }

        public String getOnlineTime() {
            return OnlineTime;
        }

        public void setOnlineTime(String onlineTime) {
            OnlineTime = onlineTime;
        }

        public String getAutoConfirmTime() {
            return AutoConfirmTime;
        }

        public void setAutoConfirmTime(String autoConfirmTime) {
            AutoConfirmTime = autoConfirmTime;
        }

        public String getOrderPwd() {
            return OrderPwd;
        }

        public void setOrderPwd(String orderPwd) {
            OrderPwd = orderPwd;
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

        public String getOrderInterval() {
            return OrderInterval;
        }

        public void setOrderInterval(String orderInterval) {
            OrderInterval = orderInterval;
        }

        public String getSuccMoney() {
            return SuccMoney;
        }

        public void setSuccMoney(String succMoney) {
            SuccMoney = succMoney;
        }

        public String getIsPicConfirm() {
            return IsPicConfirm;
        }

        public void setIsPicConfirm(String isPicConfirm) {
            IsPicConfirm = isPicConfirm;
        }

        public String getConfirmType() {
            return ConfirmType;
        }

        public void setConfirmType(String confirmType) {
            ConfirmType = confirmType;
        }

        public String getIsAgentConfirm() {
            return IsAgentConfirm;
        }

        public void setIsAgentConfirm(String isAgentConfirm) {
            IsAgentConfirm = isAgentConfirm;
        }

        public String getReceiveOrderStatus() {
            return ReceiveOrderStatus;
        }

        public void setReceiveOrderStatus(String receiveOrderStatus) {
            ReceiveOrderStatus = receiveOrderStatus;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }
    }
}
