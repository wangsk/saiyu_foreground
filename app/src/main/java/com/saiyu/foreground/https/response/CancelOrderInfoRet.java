package com.saiyu.foreground.https.response;

public class CancelOrderInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
       private String ConsumeRPoint;
        private String TimeoutPunishRPoint;
        private String TotalPoiint;
        private String OfficialConsumeRPoint;
        private String OfficialPunishRPoint;
        private String OfficialPoiint;

        public String getConsumeRPoint() {
            return ConsumeRPoint;
        }

        public void setConsumeRPoint(String consumeRPoint) {
            ConsumeRPoint = consumeRPoint;
        }

        public String getTimeoutPunishRPoint() {
            return TimeoutPunishRPoint;
        }

        public void setTimeoutPunishRPoint(String timeoutPunishRPoint) {
            TimeoutPunishRPoint = timeoutPunishRPoint;
        }

        public String getTotalPoiint() {
            return TotalPoiint;
        }

        public void setTotalPoiint(String totalPoiint) {
            TotalPoiint = totalPoiint;
        }

        public String getOfficialConsumeRPoint() {
            return OfficialConsumeRPoint;
        }

        public void setOfficialConsumeRPoint(String officialConsumeRPoint) {
            OfficialConsumeRPoint = officialConsumeRPoint;
        }

        public String getOfficialPunishRPoint() {
            return OfficialPunishRPoint;
        }

        public void setOfficialPunishRPoint(String officialPunishRPoint) {
            OfficialPunishRPoint = officialPunishRPoint;
        }

        public String getOfficialPoiint() {
            return OfficialPoiint;
        }

        public void setOfficialPoiint(String officialPoiint) {
            OfficialPoiint = officialPoiint;
        }
    }
}
