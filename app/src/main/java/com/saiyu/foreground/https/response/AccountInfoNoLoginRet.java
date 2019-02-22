package com.saiyu.foreground.https.response;

public class AccountInfoNoLoginRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String mobile;
        private int mobileBindStatus;
        private int IsRealNameAuth;
        private int IsFaceAuth;
        private String QQOpenId;
        private int QQUnionid;
        private String WXOpenId;
        private int WXUnionid;
        private int RiskLevel;
        private int TotalMoeney;
        private String RegTime;
        private String UserId;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getRegTime() {
            return RegTime;
        }

        public void setRegTime(String regTime) {
            RegTime = regTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMobileBindStatus() {
            return mobileBindStatus;
        }

        public void setMobileBindStatus(int mobileBindStatus) {
            this.mobileBindStatus = mobileBindStatus;
        }

        public int getIsRealNameAuth() {
            return IsRealNameAuth;
        }

        public void setIsRealNameAuth(int isRealNameAuth) {
            IsRealNameAuth = isRealNameAuth;
        }

        public int getIsFaceAuth() {
            return IsFaceAuth;
        }

        public void setIsFaceAuth(int isFaceAuth) {
            IsFaceAuth = isFaceAuth;
        }

        public String getQQOpenId() {
            return QQOpenId;
        }

        public void setQQOpenId(String QQOpenId) {
            this.QQOpenId = QQOpenId;
        }

        public int getQQUnionid() {
            return QQUnionid;
        }

        public void setQQUnionid(int QQUnionid) {
            this.QQUnionid = QQUnionid;
        }

        public String getWXOpenId() {
            return WXOpenId;
        }

        public void setWXOpenId(String WXOpenId) {
            this.WXOpenId = WXOpenId;
        }

        public int getWXUnionid() {
            return WXUnionid;
        }

        public void setWXUnionid(int WXUnionid) {
            this.WXUnionid = WXUnionid;
        }

        public int getRiskLevel() {
            return RiskLevel;
        }

        public void setRiskLevel(int riskLevel) {
            RiskLevel = riskLevel;
        }

        public int getTotalMoeney() {
            return TotalMoeney;
        }

        public void setTotalMoeney(int totalMoeney) {
            TotalMoeney = totalMoeney;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "mobile='" + mobile + '\'' +
                    ", mobileBindStatus=" + mobileBindStatus +
                    ", IsRealNameAuth=" + IsRealNameAuth +
                    ", IsFaceAuth=" + IsFaceAuth +
                    ", QQOpenId='" + QQOpenId + '\'' +
                    ", QQUnionid=" + QQUnionid +
                    ", WXOpenId='" + WXOpenId + '\'' +
                    ", WXUnionid=" + WXUnionid +
                    ", RiskLevel=" + RiskLevel +
                    ", TotalMoeney=" + TotalMoeney +
                    '}';
        }
    }
}
