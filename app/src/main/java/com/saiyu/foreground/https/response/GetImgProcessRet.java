package com.saiyu.foreground.https.response;

public class GetImgProcessRet extends BaseRet{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String PublicServerProcess;
        private String OrderImgServerProcess;
        private String UserImgServerProcess;

        public String getPublicServerProcess() {
            return PublicServerProcess;
        }

        public void setPublicServerProcess(String publicServerProcess) {
            PublicServerProcess = publicServerProcess;
        }

        public String getOrderImgServerProcess() {
            return OrderImgServerProcess;
        }

        public void setOrderImgServerProcess(String orderImgServerProcess) {
            OrderImgServerProcess = orderImgServerProcess;
        }

        public String getUserImgServerProcess() {
            return UserImgServerProcess;
        }

        public void setUserImgServerProcess(String userImgServerProcess) {
            UserImgServerProcess = userImgServerProcess;
        }
    }
}
