package com.saiyu.foreground.https.response;

public class OrderPauseRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String orderReceiveCount;
        private String orderReceiveMoney;

        public String getOrderReceiveCount() {
            return orderReceiveCount;
        }

        public void setOrderReceiveCount(String orderReceiveCount) {
            this.orderReceiveCount = orderReceiveCount;
        }

        public String getOrderReceiveMoney() {
            return orderReceiveMoney;
        }

        public void setOrderReceiveMoney(String orderReceiveMoney) {
            this.orderReceiveMoney = orderReceiveMoney;
        }
    }
}
