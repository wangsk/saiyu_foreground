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
        private String Mobile;
        private String RealName;
        private String IDNum;
        private int MobileBindStatus;
        private int IsRealNameAuth;
        private int IsFaceAuth;
        private String QQOpenId;
        private String QQUnionid;
        private String WXOpenId;
        private String WXUnionid;
        private int RiskLevel;
        private String TotalMoney;
        private String RegTime;

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public String getIDNum() {
            return IDNum;
        }

        public void setIDNum(String IDNum) {
            this.IDNum = IDNum;
        }

        public String getRegTime() {
            return RegTime;
        }

        public void setRegTime(String regTime) {
            RegTime = regTime;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public int getMobileBindStatus() {
            return MobileBindStatus;
        }

        public void setMobileBindStatus(int mobileBindStatus) {
            MobileBindStatus = mobileBindStatus;
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

        public String getWXOpenId() {
            return WXOpenId;
        }

        public void setWXOpenId(String WXOpenId) {
            this.WXOpenId = WXOpenId;
        }

        public int getRiskLevel() {
            return RiskLevel;
        }

        public void setRiskLevel(int riskLevel) {
            RiskLevel = riskLevel;
        }

        public String getQQUnionid() {
            return QQUnionid;
        }

        public void setQQUnionid(String QQUnionid) {
            this.QQUnionid = QQUnionid;
        }

        public String getWXUnionid() {
            return WXUnionid;
        }

        public void setWXUnionid(String WXUnionid) {
            this.WXUnionid = WXUnionid;
        }

        public String getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            TotalMoney = totalMoney;
        }
    }
}
