package com.saiyu.foreground.https.response;

public class StartAppealInfoRet extends BaseRet{
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
        private String ReserveAccount;
        private String ProductTypeName;
        private String ReserveQBCount;
        private String ProductName;
        private String SuccQBCount;
        private String ProductPropertyStr;
        private String SuccMoney;

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

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getProductTypeName() {
            return ProductTypeName;
        }

        public void setProductTypeName(String productTypeName) {
            ProductTypeName = productTypeName;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getSuccQBCount() {
            return SuccQBCount;
        }

        public void setSuccQBCount(String succQBCount) {
            SuccQBCount = succQBCount;
        }

        public String getProductPropertyStr() {
            return ProductPropertyStr;
        }

        public void setProductPropertyStr(String productPropertyStr) {
            ProductPropertyStr = productPropertyStr;
        }

        public String getSuccMoney() {
            return SuccMoney;
        }

        public void setSuccMoney(String succMoney) {
            SuccMoney = succMoney;
        }
    }
}
