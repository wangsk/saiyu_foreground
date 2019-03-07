package com.saiyu.foreground.https.response;

public class RealNameStatusRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int realNameStatus;//0申请1通过2失败3未申请

        public int getRealNameStatus() {
            return realNameStatus;
        }

        public void setRealNameStatus(int realNameStatus) {
            this.realNameStatus = realNameStatus;
        }
    }
}
