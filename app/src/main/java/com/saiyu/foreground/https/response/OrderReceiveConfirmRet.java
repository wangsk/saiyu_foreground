package com.saiyu.foreground.https.response;

public class OrderReceiveConfirmRet extends BaseRet{
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
        private String CreateTime;
        private String ProductName;
        private String FinishTime;
        private String ProductPropertyStr;
        private String RechargeTime;
        private String ReserveAccount;
        private String AutoConfirmTime;
        private String ReserveQBCount;
        private String ConfirmType;
        private String SuccQBCount;
        private String AverageConfirmTime;
        private String SuccMoney;
        private String OnceMinCount;
        private String Pic_TradeInfo;
        private String Pic_RechargeSucc;
        private String Pic_BillRecord;

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

        public String getFinishTime() {
            return FinishTime;
        }

        public void setFinishTime(String finishTime) {
            FinishTime = finishTime;
        }

        public String getProductPropertyStr() {
            return ProductPropertyStr;
        }

        public void setProductPropertyStr(String productPropertyStr) {
            ProductPropertyStr = productPropertyStr;
        }

        public String getRechargeTime() {
            return RechargeTime;
        }

        public void setRechargeTime(String rechargeTime) {
            RechargeTime = rechargeTime;
        }

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getAutoConfirmTime() {
            return AutoConfirmTime;
        }

        public void setAutoConfirmTime(String autoConfirmTime) {
            AutoConfirmTime = autoConfirmTime;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public String getConfirmType() {
            return ConfirmType;
        }

        public void setConfirmType(String confirmType) {
            ConfirmType = confirmType;
        }

        public String getSuccQBCount() {
            return SuccQBCount;
        }

        public void setSuccQBCount(String succQBCount) {
            SuccQBCount = succQBCount;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public String getSuccMoney() {
            return SuccMoney;
        }

        public void setSuccMoney(String succMoney) {
            SuccMoney = succMoney;
        }

        public String getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(String onceMinCount) {
            OnceMinCount = onceMinCount;
        }

        public String getPic_TradeInfo() {
            return Pic_TradeInfo;
        }

        public void setPic_TradeInfo(String pic_TradeInfo) {
            Pic_TradeInfo = pic_TradeInfo;
        }

        public String getPic_RechargeSucc() {
            return Pic_RechargeSucc;
        }

        public void setPic_RechargeSucc(String pic_RechargeSucc) {
            Pic_RechargeSucc = pic_RechargeSucc;
        }

        public String getPic_BillRecord() {
            return Pic_BillRecord;
        }

        public void setPic_BillRecord(String pic_BillRecord) {
            Pic_BillRecord = pic_BillRecord;
        }
    }
}
