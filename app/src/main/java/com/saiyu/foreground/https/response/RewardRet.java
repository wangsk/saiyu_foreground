package com.saiyu.foreground.https.response;

/**
 * Created by jiushubu on 2017/7/4.
 */

public class RewardRet extends BaseRet {


    /**
     * appid : wx3e154e50aa6775d4
     * partnerid : 1514392241
     * package_1 : Sign=WXPay
     * noncestr : FxgCgVFpSpsKSwK
     * timestamp : 1538114782
     * sign : 62DC7827EBD7ABE49D537E2544650FE5
     * prepayid : 62DC7827EBD7ABE49D537E2544650FE5
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String appid;
        private String partnerid;
        private String noncestr;
        private String package_1;
        private String timestamp;
        private String sign;
        private String prepayid;

        private String orderInfo;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackage_1() {
            return package_1;
        }

        public void setPackage_1(String package_1) {
            this.package_1 = package_1;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
        }
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RewardRet{");
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
