package com.saiyu.foreground.https.response;

public class OrderSettlementRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String Id;
        private int unConfirmCount;
        private int appealCount;
        private int rechargeCount;
        private String refundMoney;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public int getUnConfirmCount() {
            return unConfirmCount;
        }

        public void setUnConfirmCount(int unConfirmCount) {
            this.unConfirmCount = unConfirmCount;
        }

        public int getAppealCount() {
            return appealCount;
        }

        public void setAppealCount(int appealCount) {
            this.appealCount = appealCount;
        }

        public int getRechargeCount() {
            return rechargeCount;
        }

        public void setRechargeCount(int rechargeCount) {
            this.rechargeCount = rechargeCount;
        }

        public String getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(String refundMoney) {
            this.refundMoney = refundMoney;
        }
    }
}
