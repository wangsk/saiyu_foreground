package com.saiyu.foreground.https.response;

public class RechargeRateRet extends BaseRet{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private String WechatServiceRate;
        private String AliServiceRate;

        public String getWechatServiceRate() {
            return WechatServiceRate;
        }

        public void setWechatServiceRate(String wechatServiceRate) {
            WechatServiceRate = wechatServiceRate;
        }

        public String getAliServiceRate() {
            return AliServiceRate;
        }

        public void setAliServiceRate(String aliServiceRate) {
            AliServiceRate = aliServiceRate;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }
}
