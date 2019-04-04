package com.saiyu.foreground.https.response;

public class ActiveStatusRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean UserBuyerStatus;
        private boolean UserSellerStatus;
        private boolean UserInfoStatus;

        public boolean isUserInfoStatus() {
            return UserInfoStatus;
        }

        public void setUserInfoStatus(boolean userInfoStatus) {
            UserInfoStatus = userInfoStatus;
        }

        public boolean isUserBuyerStatus() {
            return UserBuyerStatus;
        }

        public void setUserBuyerStatus(boolean userBuyerStatus) {
            UserBuyerStatus = userBuyerStatus;
        }

        public boolean isUserSellerStatus() {
            return UserSellerStatus;
        }

        public void setUserSellerStatus(boolean userSellerStatus) {
            UserSellerStatus = userSellerStatus;
        }
    }
}
