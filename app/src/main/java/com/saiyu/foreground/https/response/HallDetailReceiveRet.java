package com.saiyu.foreground.https.response;

public class HallDetailReceiveRet extends BaseRet {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private String OrderId;
        private String OrderNum;
        private String ReceiveId;
        private int Type;
        private String AverageConfirmTime;
        private boolean IsCustomerConfirmation;
        private String CreateTime;
        private String ReserveAccount;
        private String ProductName;
        private String OfficialRatio;
        private String ProductPropertyStr;
        private String RoleName;
        private String RechargeUrl;
        private String Remarks;
        private String TutorialUrl;
        private int IsAllowShowContact;
        private String ContactMobile;
        private String ContactQQ;
        private String ReserveQBCount;
        private String RechargeQBCount;
        private String ReceiveQBCount;
        private String RechargeNum;
        private boolean IsFriendLimit;
        private String ReserveDiscount;
        private String ServiceMoney;
        private String LiquidatedMoney;
        private String TotalMoney;
        private String ConsumeRPoint;
        private String SuccessDian;
        private int OnceMinCount;
        private float LessChargeDiscount;
        private String ServiceRate;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(String orderNum) {
            OrderNum = orderNum;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public String getServiceRate() {
            return ServiceRate;
        }

        public void setServiceRate(String serviceRate) {
            ServiceRate = serviceRate;
        }

        public String getReceiveQBCount() {
            return ReceiveQBCount;
        }

        public void setReceiveQBCount(String receiveQBCount) {
            ReceiveQBCount = receiveQBCount;
        }

        public float getLessChargeDiscount() {
            return LessChargeDiscount;
        }

        public void setLessChargeDiscount(float lessChargeDiscount) {
            LessChargeDiscount = lessChargeDiscount;
        }

        public String getRechargeQBCount() {
            return RechargeQBCount;
        }

        public void setRechargeQBCount(String rechargeQBCount) {
            RechargeQBCount = rechargeQBCount;
        }

        public int getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(int onceMinCount) {
            OnceMinCount = onceMinCount;
        }

        public String getReceiveId() {
            return ReceiveId;
        }

        public void setReceiveId(String receiveId) {
            ReceiveId = receiveId;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public boolean isCustomerConfirmation() {
            return IsCustomerConfirmation;
        }

        public void setCustomerConfirmation(boolean customerConfirmation) {
            IsCustomerConfirmation = customerConfirmation;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getOfficialRatio() {
            return OfficialRatio;
        }

        public void setOfficialRatio(String officialRatio) {
            OfficialRatio = officialRatio;
        }

        public String getProductPropertyStr() {
            return ProductPropertyStr;
        }

        public void setProductPropertyStr(String productPropertyStr) {
            ProductPropertyStr = productPropertyStr;
        }

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String roleName) {
            RoleName = roleName;
        }

        public String getRechargeUrl() {
            return RechargeUrl;
        }

        public void setRechargeUrl(String rechargeUrl) {
            RechargeUrl = rechargeUrl;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }

        public String getTutorialUrl() {
            return TutorialUrl;
        }

        public void setTutorialUrl(String tutorialUrl) {
            TutorialUrl = tutorialUrl;
        }

        public int getIsAllowShowContact() {
            return IsAllowShowContact;
        }

        public void setIsAllowShowContact(int isAllowShowContact) {
            IsAllowShowContact = isAllowShowContact;
        }

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

        public String getRechargeNum() {
            return RechargeNum;
        }

        public void setRechargeNum(String rechargeNum) {
            RechargeNum = rechargeNum;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public boolean isFriendLimit() {
            return IsFriendLimit;
        }

        public void setFriendLimit(boolean friendLimit) {
            IsFriendLimit = friendLimit;
        }

        public String getReserveDiscount() {
            return ReserveDiscount;
        }

        public void setReserveDiscount(String reserveDiscount) {
            ReserveDiscount = reserveDiscount;
        }

        public String getServiceMoney() {
            return ServiceMoney;
        }

        public void setServiceMoney(String serviceMoney) {
            ServiceMoney = serviceMoney;
        }

        public String getLiquidatedMoney() {
            return LiquidatedMoney;
        }

        public void setLiquidatedMoney(String liquidatedMoney) {
            LiquidatedMoney = liquidatedMoney;
        }

        public String getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            TotalMoney = totalMoney;
        }

        public String getConsumeRPoint() {
            return ConsumeRPoint;
        }

        public void setConsumeRPoint(String consumeRPoint) {
            ConsumeRPoint = consumeRPoint;
        }

        public String getSuccessDian() {
            return SuccessDian;
        }

        public void setSuccessDian(String successDian) {
            SuccessDian = successDian;
        }
    }
}
